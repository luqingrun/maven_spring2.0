package com.gongsibao.module.order.soorderprodorganizationmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodorganizationmap.dao.SoOrderProdOrganizationMapDao;
import com.gongsibao.module.order.soorderprodorganizationmap.entity.SoOrderProdOrganizationMap;
import com.gongsibao.module.order.soorderprodorganizationmap.service.SoOrderProdOrganizationMapService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderProdOrganizationMapService")
public class SoOrderProdOrganizationMapServiceImpl implements SoOrderProdOrganizationMapService {
    @Autowired
    private SoOrderProdOrganizationMapDao soOrderProdOrganizationMapDao;

    @Override
    public SoOrderProdOrganizationMap findById(Integer pkid) {
        return soOrderProdOrganizationMapDao.findById(pkid);
    }

    @Override
    public int update(SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        return soOrderProdOrganizationMapDao.update(soOrderProdOrganizationMap);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdOrganizationMapDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        return soOrderProdOrganizationMapDao.insert(soOrderProdOrganizationMap);
    }

    @Override
    public List<SoOrderProdOrganizationMap> findByIds(List<Integer> pkidList) {
        return soOrderProdOrganizationMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderProdOrganizationMap> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdOrganizationMap> list = findByIds(pkidList);
        Map<Integer, SoOrderProdOrganizationMap> map = new HashMap<>();
        for(SoOrderProdOrganizationMap soOrderProdOrganizationMap : list){
            map.put(soOrderProdOrganizationMap.getPkid(), soOrderProdOrganizationMap);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdOrganizationMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderProdOrganizationMapDao.countByProperties(map);
        Pager<SoOrderProdOrganizationMap> pager = new Pager<>(totalRows, page);
        List<SoOrderProdOrganizationMap> soOrderProdOrganizationMapList = soOrderProdOrganizationMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderProdOrganizationMapList);
        return pager;
    }

    @Override
    public List<Integer> queryOrderProdIdsByOrganizationIds(Collection<Integer> organizationIds) {
        if(CollectionUtils.isEmpty(organizationIds)) {
            return null;
        }
        return soOrderProdOrganizationMapDao.queryOrderProdIdsByOrganizationIds(organizationIds);
    }

}