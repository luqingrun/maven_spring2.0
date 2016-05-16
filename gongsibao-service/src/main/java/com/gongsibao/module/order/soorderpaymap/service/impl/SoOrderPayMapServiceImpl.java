package com.gongsibao.module.order.soorderpaymap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderpaymap.dao.SoOrderPayMapDao;
import com.gongsibao.module.order.soorderpaymap.entity.SoOrderPayMap;
import com.gongsibao.module.order.soorderpaymap.service.SoOrderPayMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderPayMapService")
public class SoOrderPayMapServiceImpl implements SoOrderPayMapService {
    @Autowired
    private SoOrderPayMapDao soOrderPayMapDao;

    @Override
    public SoOrderPayMap findById(Integer pkid) {
        return soOrderPayMapDao.findById(pkid);
    }

    @Override
    public int update(SoOrderPayMap soOrderPayMap) {
        return soOrderPayMapDao.update(soOrderPayMap);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderPayMapDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderPayMap soOrderPayMap) {
        return soOrderPayMapDao.insert(soOrderPayMap);
    }

    @Override
    public List<SoOrderPayMap> findByIds(List<Integer> pkidList) {
        return soOrderPayMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderPayMap> findMapByIds(List<Integer> pkidList) {
        List<SoOrderPayMap> list = findByIds(pkidList);
        Map<Integer, SoOrderPayMap> map = new HashMap<>();
        for(SoOrderPayMap soOrderPayMap : list){
            map.put(soOrderPayMap.getPkid(), soOrderPayMap);
        }
        return map;
    }

    @Override
    public Pager<SoOrderPayMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderPayMapDao.countByProperties(map);
        Pager<SoOrderPayMap> pager = new Pager<>(totalRows, page);
        List<SoOrderPayMap> soOrderPayMapList = soOrderPayMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderPayMapList);
        return pager;
    }

}