package com.gongsibao.module.product.prodworkflowbddictmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodworkflowbddictmap.dao.ProdWorkflowBdDictMapDao;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;
import com.gongsibao.module.product.prodworkflowbddictmap.service.ProdWorkflowBdDictMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("prodWorkflowBdDictMapService")
public class ProdWorkflowBdDictMapServiceImpl implements ProdWorkflowBdDictMapService {
    @Autowired
    private ProdWorkflowBdDictMapDao prodWorkflowBdDictMapDao;

    @Override
    public ProdWorkflowBdDictMap findById(Integer pkid) {
        return prodWorkflowBdDictMapDao.findById(pkid);
    }

    @Override
    public int update(ProdWorkflowBdDictMap prodWorkflowBdDictMap) {
        return prodWorkflowBdDictMapDao.update(prodWorkflowBdDictMap);
    }

    @Override
    public int delete(Integer workflowId) {
        return prodWorkflowBdDictMapDao.delete(workflowId);
    }

    @Override
    public Integer insert(ProdWorkflowBdDictMap prodWorkflowBdDictMap) {
        return prodWorkflowBdDictMapDao.insert(prodWorkflowBdDictMap);
    }

    @Override
    public void insertBatch(final List<ProdWorkflowBdDictMap> itemList) {
        prodWorkflowBdDictMapDao.insertBatch(itemList);
    }

    @Override
    public List<ProdWorkflowBdDictMap> findByIds(List<Integer> workflowIdList) {
        return prodWorkflowBdDictMapDao.findByIds(workflowIdList);
    }

    @Override
    public Map<Integer, List<Integer>> findMapByIds(List<Integer> workflowIdList) {
        List<ProdWorkflowBdDictMap> list = findByIds(workflowIdList);
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        List<Integer> cityIds = null;
        for(ProdWorkflowBdDictMap prodWorkflowBdDictMap : list){
            if (CollectionUtils.isEmpty(map.get(prodWorkflowBdDictMap.getWorkflowId()))) {
                cityIds = new ArrayList<>();
                map.put(prodWorkflowBdDictMap.getWorkflowId(), cityIds);
            }
            cityIds.add(prodWorkflowBdDictMap.getCityId());
        }
        return map;
    }

    @Override
    public Pager<ProdWorkflowBdDictMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodWorkflowBdDictMapDao.countByProperties(map);
        Pager<ProdWorkflowBdDictMap> pager = new Pager<>(totalRows, page);
        List<ProdWorkflowBdDictMap> prodWorkflowBdDictMapList = prodWorkflowBdDictMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodWorkflowBdDictMapList);
        return pager;
    }

    @Override
    public List<Integer> queryWorkflowIds(Collection<Integer> cityIds) {
        if (CollectionUtils.isEmpty(cityIds)) {
            return null;
        }
        return prodWorkflowBdDictMapDao.queryWorkflowIds(cityIds);
    }

}