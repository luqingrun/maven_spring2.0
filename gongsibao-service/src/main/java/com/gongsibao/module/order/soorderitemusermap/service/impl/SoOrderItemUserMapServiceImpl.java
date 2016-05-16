package com.gongsibao.module.order.soorderitemusermap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemusermap.dao.SoOrderItemUserMapDao;
import com.gongsibao.module.order.soorderitemusermap.entity.SoOrderItemUserMap;
import com.gongsibao.module.order.soorderitemusermap.service.SoOrderItemUserMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderItemUserMapService")
public class SoOrderItemUserMapServiceImpl implements SoOrderItemUserMapService {
    @Autowired
    private SoOrderItemUserMapDao soOrderItemUserMapDao;

    @Override
    public SoOrderItemUserMap findById(Integer pkid) {
        return soOrderItemUserMapDao.findById(pkid);
    }

    @Override
    public int update(SoOrderItemUserMap soOrderItemUserMap) {
        return soOrderItemUserMapDao.update(soOrderItemUserMap);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderItemUserMapDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderItemUserMap soOrderItemUserMap) {
        return soOrderItemUserMapDao.insert(soOrderItemUserMap);
    }

    @Override
    public List<SoOrderItemUserMap> findByIds(List<Integer> pkidList) {
        return soOrderItemUserMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderItemUserMap> findMapByIds(List<Integer> pkidList) {
        List<SoOrderItemUserMap> list = findByIds(pkidList);
        Map<Integer, SoOrderItemUserMap> map = new HashMap<>();
        for(SoOrderItemUserMap soOrderItemUserMap : list){
            map.put(soOrderItemUserMap.getPkid(), soOrderItemUserMap);
        }
        return map;
    }

    @Override
    public Pager<SoOrderItemUserMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderItemUserMapDao.countByProperties(map);
        Pager<SoOrderItemUserMap> pager = new Pager<>(totalRows, page);
        List<SoOrderItemUserMap> soOrderItemUserMapList = soOrderItemUserMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderItemUserMapList);
        return pager;
    }

}