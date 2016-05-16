package com.gongsibao.module.order.soorderitem.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitem.entity.SoOrderItem;

import java.util.List;
import java.util.Map;

public interface SoOrderItemService {

    SoOrderItem findById(Integer pkid);

    int update(SoOrderItem soOrderItem);

    int delete(Integer pkid);

    Integer insert(SoOrderItem soOrderItem);

    List<SoOrderItem> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderItem> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderItem> pageByProperties(Map<String, Object> properties, int page);
}