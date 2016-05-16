package com.gongsibao.module.product.prodproductcmstemplatebddictmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.entity.ProdProductCmsTemplateBdDictMap;

import java.util.List;
import java.util.Map;

public interface ProdProductCmsTemplateBdDictMapService {

    ProdProductCmsTemplateBdDictMap findById(Integer pkid);

    int update(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap);

    int delete(Integer pkid);

    Integer insert(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap);

    List<ProdProductCmsTemplateBdDictMap> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProductCmsTemplateBdDictMap> findMapByIds(List<Integer> pkidList);

    Pager<ProdProductCmsTemplateBdDictMap> pageByProperties(Map<String, Object> properties, int page);
}