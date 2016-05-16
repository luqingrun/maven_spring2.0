package com.gongsibao.module.order.soorderitemprice.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemprice.dao.SoOrderItemPriceDao;
import com.gongsibao.module.order.soorderitemprice.entity.SoOrderItemPrice;
import com.gongsibao.module.order.soorderitemprice.service.SoOrderItemPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderItemPriceService")
public class SoOrderItemPriceServiceImpl implements SoOrderItemPriceService {
    @Autowired
    private SoOrderItemPriceDao soOrderItemPriceDao;

    @Override
    public SoOrderItemPrice findById(Integer pkid) {
        return soOrderItemPriceDao.findById(pkid);
    }

    @Override
    public int update(SoOrderItemPrice soOrderItemPrice) {
        return soOrderItemPriceDao.update(soOrderItemPrice);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderItemPriceDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderItemPrice soOrderItemPrice) {
        return soOrderItemPriceDao.insert(soOrderItemPrice);
    }

    @Override
    public List<SoOrderItemPrice> findByIds(List<Integer> pkidList) {
        return soOrderItemPriceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderItemPrice> findMapByIds(List<Integer> pkidList) {
        List<SoOrderItemPrice> list = findByIds(pkidList);
        Map<Integer, SoOrderItemPrice> map = new HashMap<>();
        for(SoOrderItemPrice soOrderItemPrice : list){
            map.put(soOrderItemPrice.getPkid(), soOrderItemPrice);
        }
        return map;
    }

    @Override
    public Pager<SoOrderItemPrice> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderItemPriceDao.countByProperties(map);
        Pager<SoOrderItemPrice> pager = new Pager<>(totalRows, page);
        List<SoOrderItemPrice> soOrderItemPriceList = soOrderItemPriceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderItemPriceList);
        return pager;
    }

}