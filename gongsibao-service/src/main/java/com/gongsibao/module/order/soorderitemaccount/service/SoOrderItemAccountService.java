package com.gongsibao.module.order.soorderitemaccount.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemaccount.entity.SoOrderItemAccount;

import java.util.List;
import java.util.Map;

public interface SoOrderItemAccountService {

    SoOrderItemAccount findById(Integer pkid);

    int update(SoOrderItemAccount soOrderItemAccount);

    int delete(Integer pkid);

    Integer insert(SoOrderItemAccount soOrderItemAccount);

    List<SoOrderItemAccount> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderItemAccount> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderItemAccount> pageByProperties(Map<String, Object> properties, int page);
}