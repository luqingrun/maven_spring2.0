package com.gongsibao.module.order.soorderinvoicemap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderinvoicemap.dao.SoOrderInvoiceMapDao;
import com.gongsibao.module.order.soorderinvoicemap.entity.SoOrderInvoiceMap;
import com.gongsibao.module.order.soorderinvoicemap.service.SoOrderInvoiceMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderInvoiceMapService")
public class SoOrderInvoiceMapServiceImpl implements SoOrderInvoiceMapService {
    @Autowired
    private SoOrderInvoiceMapDao soOrderInvoiceMapDao;

    @Override
    public SoOrderInvoiceMap findById(Integer pkid) {
        return soOrderInvoiceMapDao.findById(pkid);
    }

    @Override
    public int update(SoOrderInvoiceMap soOrderInvoiceMap) {
        return soOrderInvoiceMapDao.update(soOrderInvoiceMap);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderInvoiceMapDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderInvoiceMap soOrderInvoiceMap) {
        return soOrderInvoiceMapDao.insert(soOrderInvoiceMap);
    }

    @Override
    public List<SoOrderInvoiceMap> findByIds(List<Integer> pkidList) {
        return soOrderInvoiceMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderInvoiceMap> findMapByIds(List<Integer> pkidList) {
        List<SoOrderInvoiceMap> list = findByIds(pkidList);
        Map<Integer, SoOrderInvoiceMap> map = new HashMap<>();
        for(SoOrderInvoiceMap soOrderInvoiceMap : list){
            map.put(soOrderInvoiceMap.getPkid(), soOrderInvoiceMap);
        }
        return map;
    }

    @Override
    public Pager<SoOrderInvoiceMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderInvoiceMapDao.countByProperties(map);
        Pager<SoOrderInvoiceMap> pager = new Pager<>(totalRows, page);
        List<SoOrderInvoiceMap> soOrderInvoiceMapList = soOrderInvoiceMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderInvoiceMapList);
        return pager;
    }

}