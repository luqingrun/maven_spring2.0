package com.gongsibao.module.product.prodworkflowbddictmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProdWorkflowBdDictMapService {

    ProdWorkflowBdDictMap findById(Integer pkid);

    int update(ProdWorkflowBdDictMap prodWorkflowBdDictMap);

    int delete(Integer workflowId);

    Integer insert(ProdWorkflowBdDictMap prodWorkflowBdDictMap);

    void insertBatch(final List<ProdWorkflowBdDictMap> itemList);

    List<ProdWorkflowBdDictMap> findByIds(List<Integer> workflowIdList);

    Map<Integer, List<Integer>> findMapByIds(List<Integer> workflowIdList);

    Pager<ProdWorkflowBdDictMap> pageByProperties(Map<String, Object> properties, int page);

    List<Integer> queryWorkflowIds(Collection<Integer> cityIds);
}