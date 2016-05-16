package com.gongsibao.module.order.soinvoice.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soinvoice.entity.InvoiceList;
import com.gongsibao.module.order.soinvoice.entity.SoInvoice;

import java.util.List;
import java.util.Map;

public interface SoInvoiceService {

    SoInvoice findById(Integer pkid);

    int update(SoInvoice soInvoice);

    int delete(Integer pkid);

    Integer insert(SoInvoice soInvoice);

    List<SoInvoice> findByIds(List<Integer> pkidList);

    Map<Integer, SoInvoice> findMapByIds(List<Integer> pkidList);

    Pager<SoInvoice> pageByProperties(Map<String, Object> properties, int page);

    List<SoInvoice> findByOrderId(Integer orderId);

    Pager<InvoiceList> pageInvoiceListByProperties(Map<String, Object> map, int page);
}