package com.gongsibao.module.product.prodproductcmsrelated.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;

import java.util.List;
import java.util.Map;

public interface ProdProductCmsRelatedService {

    ProdProductCmsRelated findById(Integer pkid);

    int update(ProdProductCmsRelated prodProductCmsRelated);

    int delete(Integer pkid);

    Integer insert(ProdProductCmsRelated prodProductCmsRelated);

    List<ProdProductCmsRelated> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProductCmsRelated> findMapByIds(List<Integer> pkidList);

    Pager<ProdProductCmsRelated> pageByProperties(Map<String, Object> properties, int page);

    Pager<ProdProduct> getProdNotRelated(Integer prodpkid, int page, int pageSize);
}