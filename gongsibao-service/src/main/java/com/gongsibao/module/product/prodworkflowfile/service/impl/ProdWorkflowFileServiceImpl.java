package com.gongsibao.module.product.prodworkflowfile.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.product.prodworkflow.service.ProdWorkflowService;
import com.gongsibao.module.product.prodworkflowbddictmap.service.ProdWorkflowBdDictMapService;
import com.gongsibao.module.product.prodworkflowfile.dao.ProdWorkflowFileDao;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflowfile.service.ProdWorkflowFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodWorkflowFileService")
public class ProdWorkflowFileServiceImpl implements ProdWorkflowFileService {
    @Autowired
    private ProdWorkflowFileDao prodWorkflowFileDao;
    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private ProdWorkflowService prodWorkflowService;
    @Autowired
    private ProdWorkflowBdDictMapService prodWorkflowBdDictMapService;

    @Override
    public ProdWorkflowFile findById(Integer pkid) {
        return prodWorkflowFileDao.findById(pkid);
    }

    @Override
    public int update(ProdWorkflowFile prodWorkflowFile) {
        return prodWorkflowFileDao.update(prodWorkflowFile);
    }

    @Override
    public int delete(Integer workflowId) {
        return prodWorkflowFileDao.delete(workflowId);
    }

    @Override
    public Integer insert(ProdWorkflowFile prodWorkflowFile) {
        return prodWorkflowFileDao.insert(prodWorkflowFile);
    }

    @Override
    public void insertBatch(final List<ProdWorkflowFile> itemList) {
        prodWorkflowFileDao.insertBatch(itemList);
    }

    @Override
    public List<ProdWorkflowFile> findByIds(List<Integer> workflowIdList) {
        return prodWorkflowFileDao.findByIds(workflowIdList);
    }

    @Override
    public Map<Integer, List<ProdWorkflowFile>> findMapByIds(List<Integer> workflowIdList) {
        Map<Integer, List<ProdWorkflowFile>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(workflowIdList)) {
            return map;
        }
        List<ProdWorkflowFile> list = findByIds(workflowIdList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        List<ProdWorkflowFile> fileList = null;
        for(ProdWorkflowFile workflowFile : list){
            if (CollectionUtils.isEmpty(map.get(workflowFile.getProdWorkflowId()))) {
                fileList = new ArrayList<>();
                map.put(workflowFile.getProdWorkflowId(), fileList);
            }
            fileList.add(workflowFile);
        }
        return map;
    }

    @Override
    public Pager<ProdWorkflowFile> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodWorkflowFileDao.countByProperties(map);
        Pager<ProdWorkflowFile> pager = new Pager<>(totalRows, page);
        List<ProdWorkflowFile> prodWorkflowFileList = prodWorkflowFileDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodWorkflowFileList);
        return pager;
    }

    @Override
    public List<ProdWorkflowFile> queryWorkflowFileListByOrderProdId(Integer orderProdId) {
        // 查询产品订单
        SoOrderProd orderProd = soOrderProdService.findById(orderProdId);
        if(orderProd == null) {
            return null;
        }
        // 查询地区关联的方案ID集合
        List<Integer> cityIds = new ArrayList<>();
        cityIds.add(orderProd.getCityId());
        List<Integer> workflowIds = prodWorkflowBdDictMapService.queryWorkflowIds(cityIds);

        Map<String, Object> condition = new HashMap<>();
        condition.put("is_enabled", 1);
        condition.put("product_id", orderProd.getProductId());
        if(!CollectionUtils.isEmpty(workflowIds)) {
            condition.put("pkid", workflowIds);
        }
        // 查询产品关联的方案ID集合
        workflowIds = prodWorkflowService.queryWorkflowIds(condition);
        if(CollectionUtils.isEmpty(workflowIds)) {
            return null;
        }
        return findByIds(workflowIds);
    }

}