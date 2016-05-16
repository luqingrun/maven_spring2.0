package com.gongsibao.module.uc.ucuserorganizationmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucuserorganizationmap.dao.UcUserOrganizationMapDao;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import com.gongsibao.module.uc.ucuserorganizationmap.service.UcUserOrganizationMapService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ucUserOrganizationMapService")
public class UcUserOrganizationMapServiceImpl implements UcUserOrganizationMapService {
    @Autowired
    private UcUserOrganizationMapDao ucUserOrganizationMapDao;

    @Override
    public UcUserOrganizationMap findById(Integer pkid) {
        return ucUserOrganizationMapDao.findById(pkid);
    }

    @Override
    public int update(UcUserOrganizationMap ucUserOrganizationMap) {
        return ucUserOrganizationMapDao.update(ucUserOrganizationMap);
    }

    @Override
    public int delete(Integer pkid) {
        return ucUserOrganizationMapDao.delete(pkid);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return ucUserOrganizationMapDao.deleteByUserId(userId);
    }

    @Override
    public Integer insert(UcUserOrganizationMap ucUserOrganizationMap) {
        return ucUserOrganizationMapDao.insert(ucUserOrganizationMap);
    }

    @Override
    public int[] insert(List<UcUserOrganizationMap> ucUserOrganizationMapList) {
        return ucUserOrganizationMapDao.insert(ucUserOrganizationMapList);
    }

    @Override
    public List<UcUserOrganizationMap> findByIds(List<Integer> pkidList) {
        return ucUserOrganizationMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcUserOrganizationMap> findMapByIds(List<Integer> pkidList) {
        List<UcUserOrganizationMap> list = findByIds(pkidList);
        Map<Integer, UcUserOrganizationMap> map = new HashMap<>();
        for(UcUserOrganizationMap ucUserOrganizationMap : list){
            map.put(ucUserOrganizationMap.getPkid(), ucUserOrganizationMap);
        }
        return map;
    }

    @Override
    public Pager<UcUserOrganizationMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucUserOrganizationMapDao.countByProperties(map);
        Pager<UcUserOrganizationMap> pager = new Pager<>(totalRows, page);
        List<UcUserOrganizationMap> ucUserOrganizationMapList = ucUserOrganizationMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucUserOrganizationMapList);
        return pager;
    }

    @Override
    public List<UcUserOrganizationMap> listByProperties(Map<String, Object> properties) {
        return ucUserOrganizationMapDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Integer> findUserIdsByProperties(Map<String, Object> properties) {
        List<UcUserOrganizationMap> list = listByProperties(properties);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<Integer> userIds = new ArrayList<>();
        for (UcUserOrganizationMap ucUserOrganizationMap : list) {
            userIds.add(ucUserOrganizationMap.getUserId());
        }

        return userIds;
    }

}