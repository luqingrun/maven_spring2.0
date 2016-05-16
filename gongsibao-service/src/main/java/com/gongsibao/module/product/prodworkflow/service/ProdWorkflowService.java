package com.gongsibao.module.product.prodworkflow.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodworkflow.entity.ProdWorkflow;

import java.util.List;
import java.util.Map;

public interface ProdWorkflowService {

    ProdWorkflow findById(Integer pkid);

    ProdWorkflow findDetailById(Integer pkid);

    int update(ProdWorkflow prodWorkflow);

    int updateEnabled(Integer pkid, int enabled);

    int delete(Integer pkid);

    Integer insert(ProdWorkflow prodWorkflow);

    int saveItem(ProdWorkflow prodWorkflow);

    List<ProdWorkflow> findByIds(List<Integer> pkidList);

    Map<Integer, ProdWorkflow> findMapByIds(List<Integer> pkidList);

    Pager<ProdWorkflow> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    List<Integer> queryWorkflowIds(Map<String, Object> properties);
}