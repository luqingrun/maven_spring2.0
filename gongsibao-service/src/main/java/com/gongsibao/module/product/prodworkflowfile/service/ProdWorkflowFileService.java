package com.gongsibao.module.product.prodworkflowfile.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;

import java.util.List;
import java.util.Map;

public interface ProdWorkflowFileService {

    ProdWorkflowFile findById(Integer pkid);

    int update(ProdWorkflowFile prodWorkflowFile);

    int delete(Integer workflowId);

    Integer insert(ProdWorkflowFile prodWorkflowFile);

    void insertBatch(final List<ProdWorkflowFile> itemList);

    List<ProdWorkflowFile> findByIds(List<Integer> workflowIdList);

    Map<Integer, List<ProdWorkflowFile>> findMapByIds(List<Integer> workflowIdList);

    Pager<ProdWorkflowFile> pageByProperties(Map<String, Object> properties, int page);

    List<ProdWorkflowFile> queryWorkflowFileListByOrderProdId(Integer orderProdId);
}