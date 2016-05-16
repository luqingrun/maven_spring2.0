package com.gongsibao.module.order.soorderprodaccount.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;

import java.util.List;
import java.util.Map;

public interface SoOrderProdAccountService {

    SoOrderProdAccount findById(Integer pkid);

    int update(SoOrderProdAccount soOrderProdAccount);

    int delete(Integer pkid);

    Integer insert(SoOrderProdAccount soOrderProdAccount);

    List<SoOrderProdAccount> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdAccount> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdAccount> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    List<SoOrderProdAccount> saveAccount(Integer orderProdId,List<SoOrderProdAccount> sopalist);
}