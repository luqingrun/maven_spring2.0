package com.gongsibao.module.order.sorefunditemprice.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.sorefunditemprice.entity.SoRefundItemPrice;

import java.util.List;
import java.util.Map;

public interface SoRefundItemPriceService {

    SoRefundItemPrice findById(Integer pkid);

    int update(SoRefundItemPrice soRefundItemPrice);

    int delete(Integer pkid);

    Integer insert(SoRefundItemPrice soRefundItemPrice);

    List<SoRefundItemPrice> findByIds(List<Integer> pkidList);

    Map<Integer, SoRefundItemPrice> findMapByIds(List<Integer> pkidList);

    Pager<SoRefundItemPrice> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 批量保存退单商品服务项记录接口
     * @param list
     */
    void insertBatch(final List<SoRefundItemPrice> list);
}