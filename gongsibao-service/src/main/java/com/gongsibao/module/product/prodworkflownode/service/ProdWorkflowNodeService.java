package com.gongsibao.module.product.prodworkflownode.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;

import java.util.List;
import java.util.Map;

public interface ProdWorkflowNodeService {

    ProdWorkflowNode findById(Integer pkid);

    int update(ProdWorkflowNode prodWorkflowNode);

    int delete(Integer workflowId);

    Integer insert(ProdWorkflowNode prodWorkflowNode);

    void insertBatch(final List<ProdWorkflowNode> itemList);

    List<ProdWorkflowNode> findByIds(List<Integer> pkidList);

    Map<Integer, ProdWorkflowNode> findMapByIds(List<Integer> pkidList);

    List<ProdWorkflowNode> findByWorkflowIds(List<Integer> workflowIdList);

    Map<Integer, ProdWorkflowNode> findMapByWorkflowIds(List<Integer> workflowIdList);

    Map<Integer, List<ProdWorkflowNode>> findGroupMapByWorkflowIds(List<Integer> workflowIdList);

    Pager<ProdWorkflowNode> pageByProperties(Map<String, Object> properties, int page);

    List<ProdWorkflowNode> queryWorkflowNodeListByOrderProdId(Integer orderProdId);
}