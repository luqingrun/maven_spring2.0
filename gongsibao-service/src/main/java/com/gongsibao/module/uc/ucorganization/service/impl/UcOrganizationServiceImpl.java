package com.gongsibao.module.uc.ucorganization.service.impl;

import com.gongsibao.common.constant.ConstantCache;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.uc.ucorganization.dao.UcOrganizationDao;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucorganizationbddictmap.entity.UcOrganizationBdDictMap;
import com.gongsibao.module.uc.ucorganizationbddictmap.service.UcOrganizationBdDictMapService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.LoginUserUtil;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import com.gongsibao.module.uc.ucuserorganizationmap.service.UcUserOrganizationMapService;
import com.gongsibao.util.cache.CacheService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("ucOrganizationService")
public class UcOrganizationServiceImpl implements UcOrganizationService {
    @Autowired
    private UcOrganizationDao ucOrganizationDao;

    @Autowired
    private UcUserOrganizationMapService ucUserOrganizationMapService;

    @Autowired
    private UcOrganizationBdDictMapService ucOrganizationBdDictMapService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UcUserService ucUserService;

    @Autowired
    private BdDictService bdDictService;

    @Override
    public UcOrganization findById(Integer pkid) {
        return ucOrganizationDao.findById(pkid);
    }

    @Override
    public List<UcOrganization> findByUserPkid(Integer userPkid) {
        return ucOrganizationDao.findByUserPkid(userPkid);
    }

    @Override
    public UcOrganization findTreeById(Integer pkid, Boolean autoCompleteParent) {
        Map<Integer, UcOrganization> allMap = findTreeMap();

        String df = "yyyy-MM-dd HH:mm:ss";

        String json = JsonUtils.objectToJson(allMap, df);
        // 复制一份map
        Map<Integer, UcOrganization> treeMap = JsonUtils.jsonToMap(json, Integer.class, UcOrganization.class, df);

        UcOrganization org = treeMap.get(pkid);
        if (null == org) {
            return null;
        }

        if (!autoCompleteParent) {
            return org;
        }

        Integer pid = org.getPid();
        while (pid > 0) {
            UcOrganization parentOrg = treeMap.get(pid);
            if (null == parentOrg) {
                break;
            }
            parentOrg.setIsUse(0);
            parentOrg.getChildrenList().clear();
            parentOrg.getChildrenList().add(org);

            org = parentOrg;

            pid = org.getPid();
        }

        return org;
    }

    @Override
    public UcOrganization findOrgDetail(Integer pkid) {
        UcOrganization org = findById(pkid);
        if (null == org) {
            return org;
        }

        // 查关联地区id和产品id
        org.setCategoryIds(ucOrganizationBdDictMapService.findDictIdsByOrganizationId(pkid, 1));
        org.setCityIds(ucOrganizationBdDictMapService.findDictIdsByOrganizationId(pkid, 2));

        if (org.getPid() > 0) {
            org.setParent(findById(org.getPid()));
        }

        return org;
    }

    @Override
    public int update(UcOrganization ucOrganization) {
        return ucOrganizationDao.update(ucOrganization);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        // 查询是否可删除
        // 查询下属组织机构
        List<Integer> userOrganizationIds = ucUserService.getUserOrganizationIds(userId);
        if (!userOrganizationIds.contains(pkid)) {
            return -1;
        }

        List<UcOrganization> ucOrganizationList = LoginUserUtil.getUcOrganizationList(new ArrayList<UcOrganization>() {{
            add(findTreeMap().get(pkid));
        }});

        if (CollectionUtils.isNotEmpty(ucOrganizationList)) {
            List<Integer> idList = new ArrayList<>();
            for (UcOrganization ucOrganization : ucOrganizationList) {
                idList.add(ucOrganization.getPkid());
            }

            List<Integer> userIds = ucUserService.findByOrganizationIdList(idList);
            if (CollectionUtils.isNotEmpty(userIds)) {
                return -2;
            }
        }

        return ucOrganizationDao.delete(pkid);
    }

    @Override
    public int save(UcOrganization ucOrganization) {
        Integer pkid = ucOrganization.getPkid();
        Integer userId = ucOrganization.getAddUserId();

        UcOrganization parent = findById(ucOrganization.getPid());

        List<Integer> organizationIds = ucUserService.getUserOrganizationIds(userId);
        if (CollectionUtils.isEmpty(organizationIds) || !organizationIds.contains(ucOrganization.getPid())) {
            // 当前用户无此权限
            return -1;
        }

        if (parent.getIsLeaf() == 1) {
            ucOrganizationDao.updateLeaf(ucOrganization.getPid(), 0);
        }

        ucOrganization.setLevel(parent.getLevel() + 1);


        if (pkid == 0) {
            pkid = insert(ucOrganization);
        } else {
            update(ucOrganization);
            ucOrganizationBdDictMapService.delByorganizationId(pkid);
        }

        List<UcOrganizationBdDictMap> mapList = new ArrayList<>();
        // 1服务产品分类、2服务地区
        mapList.addAll(packBdDictMap(ucOrganization.getCategoryIds(), pkid, 1));
        mapList.addAll(packBdDictMap(ucOrganization.getCityIds(), pkid, 2));

        if (CollectionUtils.isNotEmpty(mapList)) {
            ucOrganizationBdDictMapService.insert(mapList);
        }
        return 1;
    }

    List<UcOrganizationBdDictMap> packBdDictMap(List<Integer> dictIds, Integer organizationId, Integer type) {
        List<UcOrganizationBdDictMap> list = new ArrayList<>();

        if (CollectionUtils.isEmpty(dictIds)) {
            return list;
        }

        for (Integer dictId : dictIds) {
            list.add(new UcOrganizationBdDictMap() {{
                setDictId(dictId);
                setOrganizationId(organizationId);
                setType(type);
            }});
        }
        return list;
    }

    @Override
    public Integer insert(UcOrganization ucOrganization) {
        return ucOrganizationDao.insert(ucOrganization);
    }

    @Override
    public List<UcOrganization> findByIds(List<Integer> pkidList) {
        return ucOrganizationDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcOrganization> findMapByIds(List<Integer> pkidList) {
        List<UcOrganization> list = findByIds(pkidList);
        Map<Integer, UcOrganization> map = new HashMap<>();
        for (UcOrganization ucOrganization : list) {
            map.put(ucOrganization.getPkid(), ucOrganization);
        }
        return map;
    }

    @Override
    public Pager<UcOrganization> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucOrganizationDao.countByProperties(map);
        Pager<UcOrganization> pager = new Pager<>(totalRows, page);
        List<UcOrganization> ucOrganizationList = ucOrganizationDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucOrganizationList);
        return pager;
    }

    public List<UcOrganization> findAll() {
        List<UcOrganization> ucOrganizationList = (List<UcOrganization>) cacheService.get(ConstantCache.ALL_UC_ORGANIZATION);
        if (ucOrganizationList == null || ucOrganizationList.size() == 0) {
            ucOrganizationList = ucOrganizationDao.findAll();
            cacheService.put(ConstantCache.ALL_UC_ORGANIZATION, ucOrganizationList, 60 * 60 * 10);
        }
        return ucOrganizationDao.findAll();
    }

    public Map<Integer, UcOrganization> findTreeMap() {
        List<UcOrganization> list = findAll();
        Map<Integer, UcOrganization> map = new HashMap<>();
        for (UcOrganization ucOrganization : list) {
            map.put(ucOrganization.getPkid(), ucOrganization);
        }
        for (UcOrganization ucOrganization : list) {
            if (ucOrganization.getPid() != null || ucOrganization.getPid() > 0) {
                UcOrganization parent = map.get(ucOrganization.getPid());
                if (parent != null) {
                    parent.getChildrenList().add(ucOrganization);
//                    ucOrganization.setParent(parent);
                }
            }
        }
        return map;
    }

    @Override
    public Pager<UcOrganization> findOrgList(Map<String, Object> paraMap, int currentPage, int pageSize) {
        int currentUserId = NumberUtils.toInt(paraMap.get("currentUserId"));

        int orgPkid = NumberUtils.toInt(paraMap.get("orgPkid"));

        // 查当前用户组织结构id范围
        List<Integer> orgIds = ucUserService.getUserOrganizationIds(currentUserId);

        // 用户选择的组织机构id
        if (orgPkid > 0) {
            UcOrganization ucOrganization = findTreeMap().get(orgPkid);
            List<UcOrganization> orgList = LoginUserUtil.getUcOrganizationList(new ArrayList<UcOrganization>() {{
                add(ucOrganization);
            }});

            List<Integer> chooseOrgIds = new ArrayList<>();

            for (UcOrganization organization : orgList) {
                chooseOrgIds.add(organization.getPkid());
            }

            orgIds.retainAll(chooseOrgIds);
        }

        if (CollectionUtils.isEmpty(orgIds)) {
            return new Pager<UcOrganization>(0, 0);
        }

        paraMap.put("pkids", orgIds);

        // 总条数
        int total = ucOrganizationDao.countByCondition(paraMap);
        if (total == 0) {
            return new Pager<UcOrganization>(0, 0);
        }

        // 列表数据
        Pager<UcOrganization> pager = new Pager<UcOrganization>(total, currentPage, pageSize);
        List<UcOrganization> list = ucOrganizationDao.findByCondition(paraMap, pager.getStartRow(), pager.getPageSize());

        List<Integer> pids = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<Integer> cityIds = new ArrayList<>();

        for (UcOrganization ucOrganization : list) {
            pids.add(ucOrganization.getPid());
            userIds.add(ucOrganization.getLeaderId());
            cityIds.add(ucOrganization.getCityId());
        }
        // 查上级组织数据
        Map<Integer, UcOrganization> orgMap = findMapByIds(pids);
        // 查主管人
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIds);

        // 查所在地
        Map<Integer, String> cityMap = bdDictService.queryDictNames(101, cityIds);

        for (UcOrganization ucOrganization : list) {
            ucOrganization.setParent(orgMap.get(ucOrganization.getPid()));

            UcUser leader = userMap.get(ucOrganization.getLeaderId());
            if (null != leader) {
                ucOrganization.setLeaderName(leader.getRealName());
            }

            ucOrganization.setCityName(StringUtils.trimToEmpty(cityMap.get(ucOrganization.getCityId())));
        }

        pager.setList(list);
        return pager;
    }

    @Override
    public List<UcOrganization> findByLevel(Integer level) {
        List<UcOrganization> list = new ArrayList<>();
        if (null == level || level == 0) {
            return list;
        }

        List<UcOrganization> all = findAll();

        for (UcOrganization ucOrganization : all) {
            if (ucOrganization.getLevel() == level) {
                list.add(ucOrganization);
            }
        }

        return list;
    }

    @Override
    public Map<Integer, List<UcOrganization>> findMapByUserIds(Collection<Integer> userIds) {
        Map<Integer, List<UcOrganization>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }


        Map<String, Object> param = new HashMap<>();
        param.put("user_id", userIds);
        List<UcUserOrganizationMap> userOrgMapList = ucUserOrganizationMapService.listByProperties(param);

        if (CollectionUtils.isEmpty(userOrgMapList)) {
            return result;
        }

        List<Integer> orgIds = new ArrayList<>();

        for (UcUserOrganizationMap ucUserOrganizationMap : userOrgMapList) {
            orgIds.add(ucUserOrganizationMap.getOrganizationId());
        }

        Map<Integer, UcOrganization> orgMap = findMapByIds(orgIds);

        for (UcUserOrganizationMap ucUserOrganizationMap : userOrgMapList) {

            Integer userId = ucUserOrganizationMap.getUserId();
            List<UcOrganization> list = result.get(userId);

            if (null == list) {
                list = new ArrayList<>();
                result.put(userId, list);
            }

            UcOrganization ucOrganization = orgMap.get(ucUserOrganizationMap.getOrganizationId());
            if (null != ucOrganization) {
                list.add(ucOrganization);
            }
        }

        return result;
    }

    @Override
    public Map<Integer, String> findNameMapByUserIds(Collection<Integer> userIds) {
        Map<Integer, List<UcOrganization>> map = findMapByUserIds(userIds);
        Map<Integer, String> result = new HashMap<>();

        for (Map.Entry<Integer, List<UcOrganization>> entry : map.entrySet()) {

            List<UcOrganization> list = entry.getValue();
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }

            StringBuilder sb = new StringBuilder();

            for (UcOrganization ucOrganization : list) {
                sb.append(ucOrganization.getShortName()).append(",");
            }

            result.put(entry.getKey(), sb.toString().replaceAll("\\,$", ""));
        }
        return result;
    }

    public List<Integer> queryIdsByProperties(Map<String, Object> properties) {
        return ucOrganizationDao.queryIdsByProperties(properties);
    }

    public List<UcOrganization> getUserOrgList(Integer userPkid) {
        List<UcOrganization> ucOrganizationList = findByUserPkid(userPkid);
        Map<Integer, UcOrganization> treeMap = findTreeMap();
        List<UcOrganization> treeList = ucOrganizationList.stream().map(ucOrg -> treeMap.get(ucOrg.getPkid())).collect(Collectors.toList());
        return treeList;
    }
}