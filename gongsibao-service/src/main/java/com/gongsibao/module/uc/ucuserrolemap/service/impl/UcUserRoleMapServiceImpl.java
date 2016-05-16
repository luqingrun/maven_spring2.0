package com.gongsibao.module.uc.ucuserrolemap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucuserrolemap.dao.UcUserRoleMapDao;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;
import com.gongsibao.module.uc.ucuserrolemap.service.UcUserRoleMapService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("ucUserRoleMapService")
public class UcUserRoleMapServiceImpl implements UcUserRoleMapService {
    @Autowired
    private UcUserRoleMapDao ucUserRoleMapDao;

    @Override
    public UcUserRoleMap findById(Integer pkid) {
        return ucUserRoleMapDao.findById(pkid);
    }

    @Override
    public int update(UcUserRoleMap ucUserRoleMap) {
        return ucUserRoleMapDao.update(ucUserRoleMap);
    }

    @Override
    public int delete(Integer pkid) {
        return ucUserRoleMapDao.delete(pkid);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return ucUserRoleMapDao.deleteByUserId(userId);
    }

    @Override
    public Integer insert(UcUserRoleMap ucUserRoleMap) {
        return ucUserRoleMapDao.insert(ucUserRoleMap);
    }

    @Override
    public int[] insert(List<UcUserRoleMap> ucUserRoleMapList) {
        return ucUserRoleMapDao.insert(ucUserRoleMapList);
    }

    @Override
    public List<UcUserRoleMap> findByIds(List<Integer> pkidList) {
        return ucUserRoleMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcUserRoleMap> findMapByIds(List<Integer> pkidList) {
        List<UcUserRoleMap> list = findByIds(pkidList);
        Map<Integer, UcUserRoleMap> map = new HashMap<>();
        for(UcUserRoleMap ucUserRoleMap : list){
            map.put(ucUserRoleMap.getPkid(), ucUserRoleMap);
        }
        return map;
    }

    @Override
    public Pager<UcUserRoleMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucUserRoleMapDao.countByProperties(map);
        Pager<UcUserRoleMap> pager = new Pager<>(totalRows, page);
        List<UcUserRoleMap> ucUserRoleMapList = ucUserRoleMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucUserRoleMapList);
        return pager;
    }

    @Override
    public Map<Integer, List<UcUserRoleMap>> findMapByUserIds(List<Integer> userIds) {
        Map<Integer, List<UcUserRoleMap>> result = new HashMap<>();
        if (CollectionUtils.isEmpty(userIds)) {
            return result;
        }

        List<UcUserRoleMap> roleMapList = ucUserRoleMapDao.findByUserIds(userIds);
        if (CollectionUtils.isEmpty(roleMapList)) {
            return result;
        }

        // 封装
        for (UcUserRoleMap roleMap : roleMapList) {
            Integer userId = roleMap.getUserId();

            List<UcUserRoleMap> roleList = result.get(userId);

            if (null == roleList) {
                roleList = new ArrayList<>();
                result.put(userId, roleList);
            }

            roleList.add(roleMap);
        }
        return result;
    }
}