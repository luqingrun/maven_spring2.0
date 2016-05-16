package com.gongsibao.module.product.prodworkflow.service.impl;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodworkflow.dao.ProdWorkflowDao;
import com.gongsibao.module.product.prodworkflow.entity.ProdWorkflow;
import com.gongsibao.module.product.prodworkflow.service.ProdWorkflowService;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;
import com.gongsibao.module.product.prodworkflowbddictmap.service.ProdWorkflowBdDictMapService;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflowfile.service.ProdWorkflowFileService;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("prodWorkflowService")
public class ProdWorkflowServiceImpl implements ProdWorkflowService {
    @Autowired
    private ProdWorkflowDao prodWorkflowDao;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private ProdWorkflowBdDictMapService prodWorkflowBdDictMapService;
    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;
    @Autowired
    private ProdWorkflowFileService prodWorkflowFileService;
    @Autowired
    private BdDictService bdDictService;

    @Override
    public ProdWorkflow findById(Integer pkid) {
        return prodWorkflowDao.findById(pkid);
    }

    @Override
    public ProdWorkflow findDetailById(Integer pkid) {
        if(pkid.compareTo(0) <= 0){
            return null;
        }
        ProdWorkflow prodWorkflow = findById(pkid);

        ProdProduct prodProduct = prodProductService.findById(prodWorkflow.getProductId());
        prodWorkflow.setProdProduct(prodProduct);

        List<Integer> workflowIds = new ArrayList<>();
        workflowIds.add(pkid);
        // 查询地区信息
        List<ProdWorkflowBdDictMap> regionList = prodWorkflowBdDictMapService.findByIds(workflowIds);
        Set<Integer> regionIds = new HashSet<>();
        for(ProdWorkflowBdDictMap item : regionList) {
            regionIds.add(item.getCityId());
        }
        // 根据地区ID集合查询对应地区名称映射
        Map<Integer, String> dictMap = bdDictService.queryDictNames(BdDict.TYPE_101, regionIds);
        for(ProdWorkflowBdDictMap item : regionList) {
            item.setRegionName(StringUtils.isBlank(dictMap.get(item.getCityId())) ? "" : dictMap.get(item.getCityId()));
        }
        prodWorkflow.setRegionList(regionList);
        // 查询节点信息
        List<ProdWorkflowNode> prodWorkflowNodeList = prodWorkflowNodeService.findByWorkflowIds(workflowIds);
        prodWorkflow.setProdWorkflowNodeList(prodWorkflowNodeList);
        // 查询材料信息
        List<ProdWorkflowFile> workflowFileList = prodWorkflowFileService.findByIds(workflowIds);
        prodWorkflow.setWorkflowFileList(workflowFileList);
        return prodWorkflow;
    }

    @Override
    public int update(ProdWorkflow prodWorkflow) {
        return prodWorkflowDao.update(prodWorkflow);
    }

    @Override
    public int updateEnabled(Integer pkid, int enabled) {
        if (pkid == null || pkid.compareTo(0) <= 0 || enabled < 0) {
            return -1;
        }
        return prodWorkflowDao.updateEnabled(pkid, enabled);
    }

    @Override
    public int delete(Integer pkid) {
        return prodWorkflowDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdWorkflow prodWorkflow) {
        return prodWorkflowDao.insert(prodWorkflow);
    }

    @Override
    public int saveItem(ProdWorkflow prodWorkflow) {
        int result = 0;
        Integer workflowId = prodWorkflow.getPkid();
        if(workflowId.compareTo(0) <= 0) {
            result = insert(prodWorkflow);
            workflowId = result;
        } else {
            result = update(prodWorkflow);
        }
        if(!CollectionUtils.isEmpty(prodWorkflow.getProdWorkflowNodeList())) {
            for(ProdWorkflowNode node : prodWorkflow.getProdWorkflowNodeList()) {
                node.setWorkflowId(workflowId);
            }

            prodWorkflowNodeService.delete(workflowId);
            prodWorkflowNodeService.insertBatch(prodWorkflow.getProdWorkflowNodeList());
        }
        if(!CollectionUtils.isEmpty(prodWorkflow.getRegionList())) {
            for(ProdWorkflowBdDictMap region : prodWorkflow.getRegionList()) {
                region.setWorkflowId(workflowId);
            }

            prodWorkflowBdDictMapService.delete(workflowId);
            prodWorkflowBdDictMapService.insertBatch(prodWorkflow.getRegionList());
        }
        if(!CollectionUtils.isEmpty(prodWorkflow.getWorkflowFileList())) {
            for(ProdWorkflowFile file : prodWorkflow.getWorkflowFileList()) {
                file.setProdWorkflowId(workflowId);
            }

            prodWorkflowFileService.delete(workflowId);
            prodWorkflowFileService.insertBatch(prodWorkflow.getWorkflowFileList());
        }
        return result;
    }

    @Override
    public List<ProdWorkflow> findByIds(List<Integer> pkidList) {
        return prodWorkflowDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdWorkflow> findMapByIds(List<Integer> pkidList) {
        List<ProdWorkflow> list = findByIds(pkidList);
        Map<Integer, ProdWorkflow> map = new HashMap<>();
        for(ProdWorkflow prodWorkflow : list){
            map.put(prodWorkflow.getPkid(), prodWorkflow);
        }
        return map;
    }

    @Override
    public Pager<ProdWorkflow> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        Pager<ProdWorkflow> pager = null;
        List<Integer> productIds = null;
        if(StringUtils.isNotBlank((String) map.get("name")) || StringUtils.isNotBlank((String) map.get("no")) || NumberUtils.toInt((String) map.get("prodTypeId")) > 0) {
            // 根据产品名称或者产品编码查询产品ID集合
            productIds = prodProductService.queryProductIds(map);
            if(CollectionUtils.isEmpty(productIds)) {
                return pager;
            }
            map.put("name", null);
            map.put("no", null);
            map.put("prodTypeId", null);
        }
        map.put("product_id", productIds);

        List<Integer> workflowIds = null;
        Set<Integer> regionIds = new HashSet<>();
        if(NumberUtils.toInt((String) map.get("cityId")) > 0) {
            // 根据地区ID查询方案ID集合
            regionIds.add(NumberUtils.toInt((String) map.get("cityId")));
            workflowIds = prodWorkflowBdDictMapService.queryWorkflowIds(regionIds);
            if(CollectionUtils.isEmpty(workflowIds)) {
                return pager;
            }
            map.put("cityId", null);
        }
        map.put("pkid", workflowIds);

        int totalRows = prodWorkflowDao.countByProperties(map);
        pager = new Pager<>(totalRows, page, pageSize);
        if(totalRows <= 0) {
            return pager;
        }
        List<ProdWorkflow> prodWorkflowList = prodWorkflowDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());

        if(CollectionUtils.isEmpty(productIds)) {
            productIds = new ArrayList<>();
        } else {
            productIds.clear();
        }
        if(CollectionUtils.isEmpty(workflowIds)) {
            workflowIds = new ArrayList<>();
        } else {
            workflowIds.clear();
        }

        for(ProdWorkflow prodWorkflow : prodWorkflowList) {
            productIds.add(prodWorkflow.getProductId());
            workflowIds.add(prodWorkflow.getPkid());
        }

        // 根据产品ID集合查询对应的产品信息
        Map<Integer, ProdProduct> prodMap = prodProductService.findMapByIds(productIds);
        // 根据方案ID集合查询地区ID集合映射
        Map<Integer, List<Integer>> regionMap = prodWorkflowBdDictMapService.findMapByIds(workflowIds);
        regionIds.clear();
        for(Integer key : regionMap.keySet()) {
            regionIds.addAll(regionMap.get(key));
        }
        // 根据地区ID集合查询对应地区名称映射
        Map<Integer, String> dictMap = bdDictService.queryDictNames(BdDict.TYPE_101, regionIds);

        List<String> regions = null;
        for(ProdWorkflow prodWorkflow : prodWorkflowList) {
            prodWorkflow.setProdProduct(prodMap.get(prodWorkflow.getProductId()));

            if(CollectionUtils.isEmpty(regionMap.get(prodWorkflow.getPkid()))) {
                continue;
            }
            regions = new ArrayList<>();
            for(Integer key : regionMap.get(prodWorkflow.getPkid())) {
                if(StringUtils.isNotBlank(dictMap.get(key))) {
                    regions.add(dictMap.get(key));
                }
            }
            prodWorkflow.setRegionStr(StringUtils.join(regions, "、"));
        }
        pager.setList(prodWorkflowList);
        return pager;
    }

    @Override
    public List<Integer> queryWorkflowIds(Map<String, Object> properties) {
        if(CollectionUtils.isEmpty(properties)) {
            return null;
        }
        return prodWorkflowDao.queryWorkflowIds(properties);
    }

}