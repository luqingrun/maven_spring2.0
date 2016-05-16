package com.gongsibao.module.order.soorderitemprice.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemprice.entity.SoOrderItemPrice;

import java.util.List;
import java.util.Map;

public interface SoOrderItemPriceService {

    SoOrderItemPrice findById(Integer pkid);

    int update(SoOrderItemPrice soOrderItemPrice);

    int delete(Integer pkid);

    Integer insert(SoOrderItemPrice soOrderItemPrice);

    List<SoOrderItemPrice> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderItemPrice> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderItemPrice> pageByProperties(Map<String, Object> properties, int page);
}