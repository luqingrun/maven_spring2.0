package com.gongsibao.module.product.prodworkflownode.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.product.prodworkflow.service.ProdWorkflowService;
import com.gongsibao.module.product.prodworkflowbddictmap.service.ProdWorkflowBdDictMapService;
import com.gongsibao.module.product.prodworkflownode.dao.ProdWorkflowNodeDao;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodWorkflowNodeService")
public class ProdWorkflowNodeServiceImpl implements ProdWorkflowNodeService {
    @Autowired
    private ProdWorkflowNodeDao prodWorkflowNodeDao;
    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private ProdWorkflowService prodWorkflowService;
    @Autowired
    private ProdWorkflowBdDictMapService prodWorkflowBdDictMapService;

    @Override
    public ProdWorkflowNode findById(Integer pkid) {
        return prodWorkflowNodeDao.findById(pkid);
    }

    @Override
    public int update(ProdWorkflowNode prodWorkflowNode) {
        return prodWorkflowNodeDao.update(prodWorkflowNode);
    }

    @Override
    public int delete(Integer workflowId) {
        return prodWorkflowNodeDao.delete(workflowId);
    }

    @Override
    public Integer insert(ProdWorkflowNode prodWorkflowNode) {
        return prodWorkflowNodeDao.insert(prodWorkflowNode);
    }

    @Override
    public void insertBatch(final List<ProdWorkflowNode> itemList) {
        prodWorkflowNodeDao.insertBatch(itemList);
    }

    @Override
    public List<ProdWorkflowNode> findByIds(List<Integer> pkidList) {
        return prodWorkflowNodeDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdWorkflowNode> findMapByIds(List<Integer> pkidList) {
        Map<Integer, ProdWorkflowNode> map = new HashMap<>();
        if (CollectionUtils.isEmpty(pkidList)) {
            return map;
        }
        List<ProdWorkflowNode> list = findByIds(pkidList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for(ProdWorkflowNode prodWorkflowNode : list){
            map.put(prodWorkflowNode.getPkid(), prodWorkflowNode);
        }
        return map;
    }

    @Override
    public List<ProdWorkflowNode> findByWorkflowIds(List<Integer> workflowIdList) {
        return prodWorkflowNodeDao.findByWorkflowIds(workflowIdList);
    }

    @Override
    public Map<Integer, ProdWorkflowNode> findMapByWorkflowIds(List<Integer> workflowIdList) {
        Map<Integer, ProdWorkflowNode> map = new HashMap<>();
        if (CollectionUtils.isEmpty(workflowIdList)) {
            return map;
        }
        List<ProdWorkflowNode> list = findByWorkflowIds(workflowIdList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for(ProdWorkflowNode prodWorkflowNode : list){
            map.put(prodWorkflowNode.getPkid(), prodWorkflowNode);
        }
        return map;
    }

    @Override
    public Map<Integer, List<ProdWorkflowNode>> findGroupMapByWorkflowIds(List<Integer> workflowIdList) {
        Map<Integer, List<ProdWorkflowNode>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(workflowIdList)) {
            return map;
        }
        List<ProdWorkflowNode> list = findByWorkflowIds(workflowIdList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        List<ProdWorkflowNode> nodeList = null;
        for(ProdWorkflowNode prodWorkflowNode : list){
            if (CollectionUtils.isEmpty(map.get(prodWorkflowNode.getWorkflowId()))) {
                nodeList = new ArrayList<>();
                map.put(prodWorkflowNode.getWorkflowId(), nodeList);
            }
            nodeList.add(prodWorkflowNode);
        }
        return map;
    }

    @Override
    public Pager<ProdWorkflowNode> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodWorkflowNodeDao.countByProperties(map);
        Pager<ProdWorkflowNode> pager = new Pager<>(totalRows, page);
        List<ProdWorkflowNode> prodWorkflowNodeList = prodWorkflowNodeDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodWorkflowNodeList);
        return pager;
    }

    @Override
    public List<ProdWorkflowNode> queryWorkflowNodeListByOrderProdId(Integer orderProdId) {
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
        return findByWorkflowIds(workflowIds);
    }

}