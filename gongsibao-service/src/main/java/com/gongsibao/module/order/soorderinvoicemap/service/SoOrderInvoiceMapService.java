package com.gongsibao.module.order.soorderinvoicemap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderinvoicemap.entity.SoOrderInvoiceMap;

import java.util.List;
import java.util.Map;

public interface SoOrderInvoiceMapService {

    SoOrderInvoiceMap findById(Integer pkid);

    int update(SoOrderInvoiceMap soOrderInvoiceMap);

    int delete(Integer pkid);

    Integer insert(SoOrderInvoiceMap soOrderInvoiceMap);

    List<SoOrderInvoiceMap> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderInvoiceMap> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderInvoiceMap> pageByProperties(Map<String, Object> properties, int page);
}