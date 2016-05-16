package com.gongsibao.module.order.sorefunditemprice.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.sorefunditemprice.dao.SoRefundItemPriceDao;
import com.gongsibao.module.order.sorefunditemprice.entity.SoRefundItemPrice;
import com.gongsibao.module.order.sorefunditemprice.service.SoRefundItemPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soRefundItemPriceService")
public class SoRefundItemPriceServiceImpl implements SoRefundItemPriceService {
    @Autowired
    private SoRefundItemPriceDao soRefundItemPriceDao;

    @Override
    public SoRefundItemPrice findById(Integer pkid) {
        return soRefundItemPriceDao.findById(pkid);
    }

    @Override
    public int update(SoRefundItemPrice soRefundItemPrice) {
        return soRefundItemPriceDao.update(soRefundItemPrice);
    }

    @Override
    public int delete(Integer pkid) {
        return soRefundItemPriceDao.delete(pkid);
    }

    @Override
    public Integer insert(SoRefundItemPrice soRefundItemPrice) {
        return soRefundItemPriceDao.insert(soRefundItemPrice);
    }

    @Override
    public List<SoRefundItemPrice> findByIds(List<Integer> pkidList) {
        return soRefundItemPriceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoRefundItemPrice> findMapByIds(List<Integer> pkidList) {
        List<SoRefundItemPrice> list = findByIds(pkidList);
        Map<Integer, SoRefundItemPrice> map = new HashMap<>();
        for(SoRefundItemPrice soRefundItemPrice : list){
            map.put(soRefundItemPrice.getPkid(), soRefundItemPrice);
        }
        return map;
    }

    @Override
    public Pager<SoRefundItemPrice> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soRefundItemPriceDao.countByProperties(map);
        Pager<SoRefundItemPrice> pager = new Pager<>(totalRows, page);
        List<SoRefundItemPrice> soRefundItemPriceList = soRefundItemPriceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soRefundItemPriceList);
        return pager;
    }


    /**
     * 批量保存退单商品服务项记录
     *
     * @param list
     */
    @Override
    public void insertBatch(final List<SoRefundItemPrice> list) {
        soRefundItemPriceDao.insertBatch(list);
    }

}