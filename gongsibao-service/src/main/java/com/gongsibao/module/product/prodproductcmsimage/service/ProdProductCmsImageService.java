package com.gongsibao.module.product.prodproductcmsimage.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproductcmsimage.entity.ProdProductCmsImage;

import java.util.List;
import java.util.Map;

public interface ProdProductCmsImageService {

    ProdProductCmsImage findById(Integer pkid);

    int update(ProdProductCmsImage prodProductCmsImage);

    int delete(Integer pkid);

    Integer insert(ProdProductCmsImage prodProductCmsImage);

    List<ProdProductCmsImage> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProductCmsImage> findMapByIds(List<Integer> pkidList);

    Pager<ProdProductCmsImage> pageByProperties(Map<String, Object> properties, int page);
}