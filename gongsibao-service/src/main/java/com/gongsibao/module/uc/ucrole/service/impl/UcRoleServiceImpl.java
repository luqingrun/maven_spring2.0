package com.gongsibao.module.uc.ucrole.service.impl;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucrole.dao.UcRoleDao;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import com.gongsibao.module.uc.ucrole.service.UcRoleService;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;
import com.gongsibao.module.uc.ucuserrolemap.service.UcUserRoleMapService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("ucRoleService")
public class UcRoleServiceImpl implements UcRoleService {
    @Autowired
    private UcRoleDao ucRoleDao;

    @Autowired
    private UcUserRoleMapService ucUserRoleMapService;

    @Override
    public UcRole findById(Integer pkid) {
        return ucRoleDao.findById(pkid);
    }

    @Override
    public int update(UcRole ucRole) {
        return ucRoleDao.update(ucRole);
    }

    @Override
    public int delete(Integer pkid) {
        return ucRoleDao.delete(pkid);
    }

    @Override
    public Integer insert(UcRole ucRole) {
        return ucRoleDao.insert(ucRole);
    }

    @Override
    public List<UcRole> findByIds(List<Integer> pkidList) {
        return ucRoleDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcRole> findMapByIds(List<Integer> pkidList) {
        List<UcRole> list = findByIds(pkidList);
        Map<Integer, UcRole> map = new HashMap<>();
        for(UcRole ucRole : list){
            map.put(ucRole.getPkid(), ucRole);
        }
        return map;
    }

    @Override
    public Pager<UcRole> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucRoleDao.countByProperties(map);
        Pager<UcRole> pager = new Pager<>(totalRows, page);
        List<UcRole> ucRoleList = ucRoleDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucRoleList);
        return pager;
    }

    @Override
    public List<UcRole> findByUserPkid(Integer userPkid) {
        return ucRoleDao.findByUserPkid(userPkid, null);
    }

    @Override
    public List<UcRole> findByUserPkid(Integer userPkid, Integer canPass) {
        return ucRoleDao.findByUserPkid(userPkid, canPass);
    }

    @Override
    public List<Integer> findUserIdsByRoleName(String roleName) {
        return ucRoleDao.findUserIdsByRoleName(roleName);
    }

    @Override
    public Map<Integer, List<UcRole>> findMapByUserIds(List<Integer> userIds) {
        Map<Integer, List<UcRole>> result = new HashMap<>();

        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }
        Map<Integer, List<UcUserRoleMap>> userRolesMap = ucUserRoleMapService.findMapByUserIds(userIds);

        if (MapUtils.isEmpty(userRolesMap)) {
            return result;
        }

        List<Integer> roleIds = new ArrayList<>();
        for (Map.Entry<Integer, List<UcUserRoleMap>> entry : userRolesMap.entrySet()) {
            for (UcUserRoleMap ucUserRoleMap : entry.getValue()) {
                roleIds.add(ucUserRoleMap.getRoleId());
            }
        }

        Map<Integer, UcRole> roleMap = findMapByIds(roleIds);
        for (Map.Entry<Integer, List<UcUserRoleMap>> entry : userRolesMap.entrySet()) {
            Integer userId = entry.getKey();

            List<UcRole> roleList = new ArrayList<>();
            for (UcUserRoleMap ucUserRoleMap : entry.getValue()) {
                Integer roleId = ucUserRoleMap.getRoleId();
                UcRole ucRole = roleMap.get(roleId);
                roleList.add(ucRole);
            }
            result.put(userId, roleList);
        }

        return result;
    }

    @Override
    public Map<Integer, String> findNameByUserIds(List<Integer> userIds) {
        Map<Integer, String> result = new HashMap<>();
        Map<Integer, List<UcRole>> map = findMapByUserIds(userIds);
        if (MapUtils.isEmpty(map)) {
            return result;
        }

        for (Map.Entry<Integer, List<UcRole>> entry : map.entrySet()) {
            Integer userId = entry.getKey();

            Set<String> roleNames = new HashSet<>();
            for (UcRole ucRole : entry.getValue()) {
                roleNames.add(ucRole.getName());
            }

            result.put(userId, StringUtils.join(roleNames, ","));
        }
        return result;
    }
}