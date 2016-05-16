package com.gongsibao.module.order.soorderprodtrace.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderProdTraceService {

    SoOrderProdTrace findById(Integer pkid);

    int update(SoOrderProdTrace soOrderProdTrace);

    int updateJobDays(Integer orderprodid, int timeoutDays);

    int delete(Integer pkid);

    Integer insert(SoOrderProdTrace soOrderProdTrace);

    List<SoOrderProdTrace> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdTrace> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdTrace> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    SoOrderProdTrace updateStatus(SoOrderProd soOrderProd, SoOrderProdTrace soOrderProdTrace);

    SoOrderProdTrace addfile(SoOrderProdTrace soOrderProdTrace, SoOrderProdTraceFile soOrderProdTraceFile);

    SoOrderProdTrace saveAccount(SoOrderProdTrace soOrderProdTrace, List<SoOrderProdAccount> soOrderProdAccountList);

    List<Integer> queryOrderProdTraceIds(Integer orderProdId, Integer typeId);

    int queryTraceProcessdDays(Map<String, Object> properties);

    List<SoOrderProdTrace> findByProperties(Map<String, Object> properties, int start, int pageSize);

    /**
     * 根据产品订单Id ,查询最近订单状态
     */
    SoOrderProdTrace findLatestStatusByOrderProdId(Integer orderProdId);

    Map<Integer, List<SoOrderProdTrace>> findMapByUserIds(Collection<Integer> userIds);
}