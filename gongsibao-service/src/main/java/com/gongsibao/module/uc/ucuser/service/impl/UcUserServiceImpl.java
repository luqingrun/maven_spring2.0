package com.gongsibao.module.uc.ucuser.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import com.gongsibao.module.sys.bdsync.entity.BdSync;
import com.gongsibao.module.sys.bdsync.service.BdSyncService;
import com.gongsibao.module.uc.ucauth.dao.UcAuthDao;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucorganizationbddictmap.service.UcOrganizationBdDictMapService;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import com.gongsibao.module.uc.ucrole.service.UcRoleService;
import com.gongsibao.module.uc.ucuser.dao.UcUserDao;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.LoginUserUtil;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import com.gongsibao.module.uc.ucuserorganizationmap.service.UcUserOrganizationMapService;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;
import com.gongsibao.module.uc.ucuserrolemap.service.UcUserRoleMapService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("ucUserService")
public class UcUserServiceImpl implements UcUserService {

    Log log = LogFactory.getLog(UcUserService.class);

    @Autowired
    private UcUserDao ucUserDao;

    @Autowired
    private UcRoleService ucRoleService;

    @Autowired
    private UcAuthDao ucAuthDao;
    @Autowired
    private UcOrganizationService ucOrganizationService;
    @Autowired
    private UcOrganizationBdDictMapService ucOrganizationBdDictMapService;

    @Autowired
    private UcUserOrganizationMapService ucUserOrganizationMapService;

    @Autowired
    private UcUserRoleMapService ucUserRoleMapService;

    @Autowired
    private BdFileService bdFileService;

    @Autowired
    private SoOrderProdTraceService soOrderProdTraceService;

    @Autowired
    private BdSyncService bdSyncService;

    @Override
    public UcUser findById(Integer pkid) {
        return ucUserDao.findById(pkid);
    }

    @Override
    public UcUser findUserInfoById(Integer pkid) {
        UcUser user = findById(pkid);
        if (null == user) {
            return user;
        }

        // 取头像
        if (user.getHeadThumbFileId() > 0) {
            List<BdFile> bdFileList = bdFileService.getListByFormId(pkid, UcUser.HEAD_IMG_URL_TAB);
            if (CollectionUtils.isNotEmpty(bdFileList)) {
                user.setHeadThumbFileUrl(bdFileList.get(0).getUrl());
            }
        }

        // 取角色
        user.setRoleList(ucRoleService.findByUserPkid(pkid));
        setEmptyPasswd(user);
        return user;
    }

    @Override
    public UcUser findByTicket(String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ticket", ticket);
        List<UcUser> list = ucUserDao.findByProperties(map, 0, 10);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public UcUser findByLoginName(String loginName) {
        return ucUserDao.findByLoginName(loginName);
    }

    @Override
    public LoginUser findLoginUser(Integer userPkid) {
        LoginUser loginUser = new LoginUser();
        UcUser ucUser = ucUserDao.findById(userPkid);
        if (ucUser == null) {
            return null;
        }
        loginUser.setUcUser(ucUser);
        //角色
        List<UcRole> ucRoleList = ucRoleService.findByUserPkid(userPkid);
        loginUser.setUcRoleList(ucRoleList);
        //岗位
        loginUser.setUcOrganizationList(ucOrganizationService.getUserOrgList(userPkid));
        //权限
        List<Integer> roleIdList = ucRoleList.stream().map(UcRole::getPkid).collect(Collectors.toList());
        List<UcAuth> ucAuthList = ucAuthDao.findByRoldPkidList(roleIdList);
        loginUser.setUcAuthList(ucAuthList);
        //菜单
        List<UcAuth> menuList = ucAuthList.stream().filter(ucAuth -> ucAuth.getIsMenu() == 1).collect(Collectors.toList());
        loginUser.setMenuList(menuList);

        // sync信息
        BdSync bdSync = bdSyncService.findByMPkidAndTableName(userPkid, "uc_user");
        loginUser.setBdSync(bdSync);

        return loginUser;
    }

    @Override
    public LoginUser findLoginUser(String ticket) {
        UcUser ucUser = findByTicket(ticket);
        if (ucUser == null) {
            return null;
        }
        return findLoginUser(ucUser.getPkid());
    }

    @Override
    public int saveUcUser(UcUser ucUser) {
        Integer currentUserId = ucUser.getAddUserId();

        // 组织机构是否合法
        List<Integer> myOrgIds = getUserOrganizationIds(currentUserId);

        List<UcUserOrganizationMap> organizationMapList = ucUser.getUserOrganizationMapList();
        for (UcUserOrganizationMap ucUserOrganizationMap : organizationMapList) {
            if (!myOrgIds.contains(ucUserOrganizationMap.getOrganizationId())) {
                return -1;
            }
        }

        Integer pkid = ucUser.getPkid();
        if (pkid == 0) {
            pkid = insert(ucUser);
        } else {
            update(ucUser);
            int rows = ucUserRoleMapService.deleteByUserId(pkid);
            rows = ucUserOrganizationMapService.deleteByUserId(pkid);
            rows = bdFileService.deleteByFormId(pkid, UcUser.HEAD_IMG_URL_TAB);
        }

        // 关联角色
        List<UcUserRoleMap> userRoleMapList = ucUser.getUserRoleMapList();
        if (CollectionUtils.isNotEmpty(userRoleMapList)) {
            for (UcUserRoleMap ucUserRoleMap : userRoleMapList) {
                ucUserRoleMap.setUserId(pkid);
            }

            ucUserRoleMapService.insert(userRoleMapList);
        }

        // 头像
        String url = ucUser.getHeadThumbFileUrl();
        if (StringUtils.isNotBlank(url)) {
            Integer fileId = bdFileService.insert(new BdFile() {{
                setFormId(ucUser.getPkid());
                setUrl(url);
                setTabName(UcUser.HEAD_IMG_URL_TAB);
                setAddUserId(currentUserId);
            }});

            if (fileId > 0) {
                ucUserDao.updateHeadFileId(pkid, fileId);
            }
        }

        // 组织机构
        if (CollectionUtils.isNotEmpty(organizationMapList)) {
            for (UcUserOrganizationMap ucUserOrganizationMap : organizationMapList) {
                ucUserOrganizationMap.setUserId(pkid);
            }
            ucUserOrganizationMapService.insert(organizationMapList);
        }

        return 1;
    }

    @Override
    public int update(UcUser ucUser) {
        return ucUserDao.update(ucUser);
    }

    @Override
    public int delete(Integer pkid) {
        return ucUserDao.delete(pkid);
    }

    @Override
    public Integer insert(UcUser ucUser) {
        return ucUserDao.insert(ucUser);
    }

    @Override
    public List<UcUser> findByIds(List<Integer> pkidList) {
        if (CollectionUtils.isEmpty(pkidList)) {
            return null;
        }
        return ucUserDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcUser> findMapByIds(List<Integer> pkidList) {
        Map<Integer, UcUser> map = new HashMap<>();
        if (CollectionUtils.isEmpty(pkidList)) {
            return map;
        }
        List<UcUser> list = findByIds(pkidList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (UcUser ucUser : list) {
            map.put(ucUser.getPkid(), ucUser);
        }
        return map;
    }

    @Override
    public Pager<UcUser> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucUserDao.countByProperties(map);
        Pager<UcUser> pager = new Pager<>(totalRows, page);
        List<UcUser> ucUserList = ucUserDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());

        setEmptyPasswd(ucUserList);
        pager.setList(ucUserList);
        return pager;
    }

    @Override
    public Pager<UcUser> findByCondition(Map<String, Object> properties, int currentPage, int pageSize) {
        int currentUserId = NumberUtils.toInt(properties.get("currentUserId"));

        List<Integer> userIds = getUserPkid(currentUserId);

        if (CollectionUtils.isEmpty(userIds)) {
            return new Pager<>(0, 0);
        }

        // 角色名称
        List<String> roleTagList = (List<String>) properties.get("roleTagList");
        if (CollectionUtils.isNotEmpty(roleTagList)) {
            List<Integer> roleUserIds = findByRoleTagList(roleTagList);
            // 取交集
            userIds.retainAll(roleUserIds);
            if (CollectionUtils.isEmpty(userIds)) {
                return new Pager<>(0, 0);
            }
        }

        int cityId = NumberUtils.toInt(properties.get("cityId"));

        if (cityId > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("dict_id", cityId);
            List<Integer> orgIds = ucOrganizationBdDictMapService.findOrganizationIdsByProperties(map);
            List<Integer> cityUserIds = findByOrganizationIdList(orgIds);

            // 取交集
            userIds.retainAll(cityUserIds);
            if (CollectionUtils.isEmpty(userIds)) {
                return new Pager<>(0, 0);
            }
        }

        properties.put("pkids", userIds);

        int total = ucUserDao.countByCondition(properties);
        if (total == 0) {
            return new Pager<>(0, 0);
        }

        Pager<UcUser> pager = new Pager<>(total, currentPage, pageSize);

        List<UcUser> list = ucUserDao.findByCondition(properties, pager.getStartRow(), pager.getPageSize());

        // 组织机构名称
        userIds.clear();

        for (UcUser ucUser : list) {
            userIds.add(ucUser.getPkid());
        }

        Map<Integer, String> orgNameMap = ucOrganizationService.findNameMapByUserIds(userIds);

        for (UcUser ucUser : list) {
            ucUser.setOrganizationName(StringUtils.trimToEmpty(orgNameMap.get(ucUser.getPkid())));
        }

        if (null != properties.get("needFollow")) {
            // 查询是否跟进人
            Map<Integer, List<SoOrderProdTrace>> map = soOrderProdTraceService.findMapByUserIds(userIds);
            for (UcUser ucUser : list) {
                ucUser.setIsFollow(CollectionUtils.isEmpty(map.get(ucUser.getPkid())) ? 0 : 1);
            }
        }
        setEmptyPasswd(list);
        pager.setList(list);

        return pager;
    }

    @Override
    public int updateTicket(Integer pkid, String ticket) {
        return ucUserDao.updateTicket(pkid, ticket);
    }

    @Override
    public List<Integer> findByOrganizationIdList(List<Integer> organizationIdList) {
        return ucUserDao.findByOrganizationIdList(organizationIdList);
    }

//    @Override
//    public List<Integer> findByRoleNameList(List<String> roleNameList) {
//        return ucUserDao.findByRoleNameList(roleNameList);
//    }

    @Override
    public List<Integer> findByRoleTagList(List<String> roleTagList) {
        return ucUserDao.findByRoleTagList(roleTagList);
    }

    @Override
    public List<Integer> findByRoleTagAndDictIdList(List<String> roleTagList, Integer dictType, Collection<Integer> dictIds) {
        List<Integer> organizationIds = null;

        if (CollectionUtils.isNotEmpty(dictIds)) {
            // 根据字典项查组织机构id
            Map<String, Object> param = new HashMap<>();
            param.put("dict_id", dictIds);
            param.put("type", dictType);
            organizationIds = ucOrganizationBdDictMapService.findOrganizationIdsByProperties(param);

            if (CollectionUtils.isEmpty(organizationIds)) {
                return new ArrayList<>();
            }
        }

        return findByRoleTagAndOrganizationId(roleTagList, organizationIds);
    }

    @Override
    public List<Integer> findByRoleTagAndOrganizationId(List<String> roleTagList, List<Integer> organizationIdList) {
        if (organizationIdList == null || organizationIdList.size() == 0) {
            return new ArrayList<>();
        }
        List<Integer> list = findByOrganizationIdList(organizationIdList);
        if (CollectionUtils.isEmpty(roleTagList)) {
            return list;
        } else {
            List<Integer> byRoleNameList = findByRoleTagList(roleTagList);
            list.retainAll(byRoleNameList);
        }
        return list;
    }

    @Override
    public List<Integer> getUserPkid(Integer userId, String... roleTags) {
        // 查组织机构
        List<UcOrganization> treeList = ucOrganizationService.getUserOrgList(userId);

        List<UcOrganization> list = LoginUserUtil.getUcOrganizationList(treeList);

        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<Integer> orgIdList = new ArrayList<>();
        for (UcOrganization ucOrganization : list) {
            orgIdList.add(ucOrganization.getPkid());
        }
        if (CollectionUtils.isEmpty(orgIdList)) {
            return new ArrayList<>();
        }

        if (ArrayUtils.isEmpty(roleTags)) {
            return findByOrganizationIdList(orgIdList);
        } else {
            return findByRoleTagAndOrganizationId(Arrays.asList(roleTags), orgIdList);
        }
    }

    @Override
    public List<Integer> getUserOrganizationIds(Integer userId) {
        List<UcOrganization> treeList = ucOrganizationService.getUserOrgList(userId);

        List<UcOrganization> orgList = LoginUserUtil.getUcOrganizationList(treeList);

        List<Integer> orgIds = new ArrayList<>();
        for (UcOrganization ucOrganization : orgList) {
            orgIds.add(ucOrganization.getPkid());
        }
        return orgIds;
    }

    @Override
    public List<Integer> getUserParentPkid(Integer userId, Integer level, String... roleTags) {
        LoginUser loginUser = findLoginUser(userId);

        // 用户所有组织机构id
        List<UcOrganization> list = LoginUserUtil.getUcOrganizationList(loginUser.getUcOrganizationList());
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        Map<Integer, UcOrganization> treeMap = ucOrganizationService.findTreeMap();

        List<Integer> orgIdList = new ArrayList<>();
        for (UcOrganization ucOrganization : list) {
            Integer pkid = ucOrganization.getPkid();
            if (null != level && level > 0) {
                if (level == ucOrganization.getLevel()) {
                    orgIdList.add(pkid);
                }
            } else {
                orgIdList.add(pkid);
            }

            Integer pid = ucOrganization.getPid();
            while (pid > 0) {
                UcOrganization org = treeMap.get(pid);
                Integer orgPkid = org.getPkid();
                if (null == org) {
                    break;
                }

                if (null != level && level > 0) {
                    if (level == ucOrganization.getLevel()) {
                        orgIdList.add(orgPkid);
                    }
                } else {
                    orgIdList.add(orgPkid);
                }

                pid = org.getPid();
                if (pid == 0) {
                    break;
                }
            }
        }

        if (CollectionUtils.isEmpty(orgIdList)) {
            return new ArrayList<>();
        }

        return findByRoleTagAndOrganizationId(Arrays.asList(roleTags), orgIdList);
    }

    @Override
    public List<Integer> getUserIdsByOrgLevel(Integer level, String... roleTags) {
        List<UcOrganization> levelOrgList = ucOrganizationService.findByLevel(level);

        if (CollectionUtils.isEmpty(levelOrgList)) {
            return new ArrayList<>();
        }

        List<Integer> orgIdList = new ArrayList<>();
        for (UcOrganization ucOrganization : levelOrgList) {
            orgIdList.add(ucOrganization.getPkid());
        }

        if (ArrayUtils.isEmpty(roleTags)) {
            return findByOrganizationIdList(orgIdList);
        } else {
            return findByRoleTagAndOrganizationId(Arrays.asList(roleTags), orgIdList);
        }
    }

    @Override
    public List<UcUser> findUsersByOrgIds(List<Integer> orgIds) {
        if (CollectionUtils.isEmpty(orgIds)) {
            return null;
        }

        List<UcUser> list = findByIds(findByOrganizationIdList(orgIds));
        setEmptyPasswd(list);

        return list;
    }

    @Override
    public List<UcUser> findUsersByOrgId(Integer orgId) {
        return findUsersByOrgIds(new ArrayList<Integer>() {{
            add(orgId);
        }});
    }

    @Override
    public Map<String, Integer> findUserNums(Integer userId) {
        Map<String, Integer> result = new HashMap<String, Integer>() {{
            put("onNums", 0);
            put("leaveNums", 0);
            put("allNums", 0);
        }};

        List<Map<String, Object>> numList = ucUserDao.findUserNums(userId);

        for (Map<String, Object> map : numList) {
            int is_enabled = NumberUtils.toInt(map.get("is_enabled"));
            int num = NumberUtils.toInt(map.get("num"));

            if (is_enabled == 0) {
                result.put("leaveNums", num);
            } else {
                result.put("onNums", num);
            }
        }

        result.put("allNums", NumberUtils.toInt(result.get("leaveNums")) + NumberUtils.toInt(result.get("onNums")));
        return result;
    }

    @Override
    public Pager<UcUser> findManagerUserList(Map<String, Object> paramMap, Integer currentPage, Integer pageSize) {

        int currentUserId = NumberUtils.toInt(paramMap.get("currentUserId"));
        int orgPkid = NumberUtils.toInt(paramMap.get("orgPkid"));
        // 当前用户组织机构下用户
        List<Integer> userIds = getUserPkid(currentUserId);

        if (CollectionUtils.isEmpty(userIds)) {
            return new Pager<>(0, 0);
        }

        // 角色名称
        String roleName = StringUtils.trimToEmpty(paramMap.get("roleName"));
        if (StringUtils.isNotBlank(roleName)) {
            List<Integer> roleUserIds = ucRoleService.findUserIdsByRoleName(roleName);

            // 取交集
            userIds.retainAll(roleUserIds);
            if (CollectionUtils.isEmpty(userIds)) {
                return new Pager<>(0, 0);
            }
        }

        // 组织机构
        if (orgPkid > 0) {
            List<Integer> orgUserIds = findByOrganizationIdList(new ArrayList<Integer>() {{
                add(orgPkid);
            }});

            // 取交集
            userIds.retainAll(orgUserIds);
            if (CollectionUtils.isEmpty(userIds)) {
                return new Pager<>(0, 0);
            }
        }

        paramMap.put("pkids", userIds);

        int total = ucUserDao.countByCondition(paramMap);
        if (total == 0) {
            return new Pager<>(0, 0);
        }

        Pager<UcUser> pager = new Pager<>(total, currentPage, pageSize);

        List<UcUser> list = ucUserDao.findByCondition(paramMap, pager.getStartRow(), pager.getPageSize());

        userIds.clear();
        for (UcUser ucUser : list) {
            userIds.add(ucUser.getPkid());
        }

        // 组织机构名称
        Map<Integer, String> orgNameMap = ucOrganizationService.findNameMapByUserIds(userIds);

        // 角色名称
        Map<Integer, String> roleMap = ucRoleService.findNameByUserIds(userIds);
        for (UcUser ucUser : list) {
            ucUser.setOrganizationName(StringUtils.trimToEmpty(orgNameMap.get(ucUser.getPkid())));
            ucUser.setRoleName(StringUtils.trimToEmpty(roleMap.get(ucUser.getPkid())));
        }

        setEmptyPasswd(list);
        pager.setList(list);
        return pager;
    }

    @Override
    public int editUserEnabled(Integer pkid, Integer isEnabled, Integer operateUserId) {
        List<Integer> userPkids = getUserPkid(operateUserId);
        if (!userPkids.contains(pkid)) {
            return -1;
        }

        int rows = ucUserDao.updateEnabled(pkid, isEnabled);
        if (rows > 0) {
            // 打个日志
            log.info("operateUserId[" + operateUserId + "] update user[" + pkid + "] is_enabled = " + isEnabled);
        }
        return rows;
    }

    private void setEmptyPasswd(UcUser ucUser) {
        if (null != ucUser) {
            ucUser.setPasswd("");
            ucUser.setTicket("");
        }
    }

    private void setEmptyPasswd(List<UcUser> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        for (UcUser ucUser : userList) {
            setEmptyPasswd(ucUser);
        }
    }
}