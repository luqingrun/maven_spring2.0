package com.gongsibao.module.product.prodproduct.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.sys.bddict.entity.BdDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ProdProductService {

    ProdProduct findById(Integer pkid);

    int update(ProdProduct prodProduct);

    int delete(Integer pkid);

    Integer insert(ProdProduct prodProduct);

    List<ProdProduct> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProduct> findMapByIds(List<Integer> pkidList);

    Pager<ProdProduct> pageByProperties(Map<String, Object> properties, int page);

    ProdProduct saveProduct(ProdProduct pp, List<ProdService> pslist);

    ProdProduct updateProduct(ProdProduct pp, List<ProdService> pslist);

    int updateEnabled(ProdProduct pp);

    Pager<ProdProduct> pageByProducts(Map<String, Object> map, int page, int pagesize);

    List<Integer> queryProductIds(Map<String, Object> condition);

    List<BdDict> findAllCity(Map<String, Object> properties);
}