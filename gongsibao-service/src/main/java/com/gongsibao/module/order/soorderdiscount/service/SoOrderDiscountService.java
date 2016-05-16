package com.gongsibao.module.order.soorderdiscount.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;

import java.util.List;
import java.util.Map;

public interface SoOrderDiscountService {

    SoOrderDiscount findById(Integer pkid);

    int update(SoOrderDiscount soOrderDiscount);

    int delete(Integer pkid);

    Integer insert(SoOrderDiscount soOrderDiscount);

    List<SoOrderDiscount> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderDiscount> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderDiscount> pageByProperties(Map<String, Object> properties, int page);

    List<Map<String, Object>> discountList(String discounts);

    List<Map<String, Object>> findByProductId(String productId,String discountId);

    List<Map<String, Object>> findByCityId(String cityId,String discountId);
}