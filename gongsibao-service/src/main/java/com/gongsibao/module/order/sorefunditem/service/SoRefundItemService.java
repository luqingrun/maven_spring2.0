package com.gongsibao.module.order.sorefunditem.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.sorefunditem.entity.SoRefundItem;

import java.util.List;
import java.util.Map;

public interface SoRefundItemService {

    SoRefundItem findById(Integer pkid);

    int update(SoRefundItem soRefundItem);

    int delete(Integer pkid);

    Integer insert(SoRefundItem soRefundItem);

    List<SoRefundItem> findByIds(List<Integer> pkidList);

    Map<Integer, SoRefundItem> findMapByIds(List<Integer> pkidList);

    Pager<SoRefundItem> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 批量保存退单商品记录接口
     *
     * @param list
     */
    void insertBatch(final List<SoRefundItem> list);

    List<SoRefundItem> getListByRefundId(Integer refundId);
}