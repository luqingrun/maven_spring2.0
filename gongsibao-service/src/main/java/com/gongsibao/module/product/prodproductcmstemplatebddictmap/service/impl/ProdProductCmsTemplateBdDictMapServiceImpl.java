package com.gongsibao.module.product.prodproductcmstemplatebddictmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.dao.ProdProductCmsTemplateBdDictMapDao;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.entity.ProdProductCmsTemplateBdDictMap;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.service.ProdProductCmsTemplateBdDictMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodProductCmsTemplateBdDictMapService")
public class ProdProductCmsTemplateBdDictMapServiceImpl implements ProdProductCmsTemplateBdDictMapService {
    @Autowired
    private ProdProductCmsTemplateBdDictMapDao prodProductCmsTemplateBdDictMapDao;

    @Override
    public ProdProductCmsTemplateBdDictMap findById(Integer pkid) {
        return prodProductCmsTemplateBdDictMapDao.findById(pkid);
    }

    @Override
    public int update(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap) {
        return prodProductCmsTemplateBdDictMapDao.update(prodProductCmsTemplateBdDictMap);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductCmsTemplateBdDictMapDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap) {
        return prodProductCmsTemplateBdDictMapDao.insert(prodProductCmsTemplateBdDictMap);
    }

    @Override
    public List<ProdProductCmsTemplateBdDictMap> findByIds(List<Integer> pkidList) {
        return prodProductCmsTemplateBdDictMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProductCmsTemplateBdDictMap> findMapByIds(List<Integer> pkidList) {
        List<ProdProductCmsTemplateBdDictMap> list = findByIds(pkidList);
        Map<Integer, ProdProductCmsTemplateBdDictMap> map = new HashMap<>();
        for(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap : list){
            map.put(prodProductCmsTemplateBdDictMap.getPkid(), prodProductCmsTemplateBdDictMap);
        }
        return map;
    }

    @Override
    public Pager<ProdProductCmsTemplateBdDictMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductCmsTemplateBdDictMapDao.countByProperties(map);
        Pager<ProdProductCmsTemplateBdDictMap> pager = new Pager<>(totalRows, page);
        List<ProdProductCmsTemplateBdDictMap> prodProductCmsTemplateBdDictMapList = prodProductCmsTemplateBdDictMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductCmsTemplateBdDictMapList);
        return pager;
    }

}