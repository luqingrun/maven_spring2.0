package com.gongsibao.module.order.soorderprodtracefile.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderProdTraceFileService {

    SoOrderProdTraceFile findById(Integer pkid);

    int update(SoOrderProdTraceFile soOrderProdTraceFile);

    int updateAuditStatus(Collection<Integer> pkids, int newStatus, int oldStatus);

    int delete(Integer pkid);

    Integer insert(SoOrderProdTraceFile soOrderProdTraceFile);

    List<SoOrderProdTraceFile> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdTraceFile> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdTraceFile> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    Map<Integer, Integer> queryTraceFileMap(Map<String, Object> properties);

    List<SoOrderProdTraceFile> findByProperties(Map<String, Object> properties);

    String getNeedUploadFileNames(Integer orderProdId);

    List<Integer> findProdWorkflowFileIdByOrderProdTraceId(List<Integer> pkidList);
}