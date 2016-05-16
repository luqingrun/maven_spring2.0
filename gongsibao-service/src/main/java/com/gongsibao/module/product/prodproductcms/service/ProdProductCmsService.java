package com.gongsibao.module.product.prodproductcms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.product.prodproductcms.entity.ProdProductCms;

import java.util.List;
import java.util.Map;

public interface ProdProductCmsService {

    ProdProductCms findById(Integer pkid);

    int update(ProdProductCms prodProductCms);

    int delete(Integer pkid);

    Integer insert(ProdProductCms prodProductCms);

    List<ProdProductCms> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProductCms> findMapByIds(List<Integer> pkidList);

    Pager<ProdProductCms> pageByProperties(Map<String, Object> properties, int page);

    ProdProductCms findByProdId(Integer pkid);

    ResponseData addorupdateprodcms(ProdProductCms prodProductCms);
}