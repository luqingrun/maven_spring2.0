package com.gongsibao.module.order.soorderproditem.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderProdItemService {

    SoOrderProdItem findById(Integer pkid);

    int update(SoOrderProdItem soOrderProdItem);

    int delete(Integer pkid);

    Integer insert(SoOrderProdItem soOrderProdItem);

    List<SoOrderProdItem> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdItem> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdItem> pageByProperties(Map<String, Object> properties, int page);

    Map<Integer, List<SoOrderProdItem>> getMapByProdIds(Collection<Integer> orderProdIds);

    Map<Integer, List<SoOrderProdItem>> getRefundMapByProdIds(Collection<Integer> orderProdIds);

}