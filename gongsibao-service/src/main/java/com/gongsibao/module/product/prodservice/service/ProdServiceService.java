package com.gongsibao.module.product.prodservice.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodservice.entity.ProdService;

import java.util.List;
import java.util.Map;

public interface ProdServiceService {

    ProdService findById(Integer pkid);

    int update(ProdService prodService);

    int delete(Integer pkid);

    Integer insert(ProdService prodService);

    List<ProdService> findByIds(List<Integer> pkidList);

    Map<Integer, ProdService> findMapByIds(List<Integer> pkidList);

    List<ProdService> findByIds(Integer productId);

    Pager<ProdService> pageByProperties(Map<String, Object> properties, int page);

    void insertBatch(final List<ProdService> itemList);

    int updateBatch(final List<ProdService> itemList);

    int deleteBatch(final List<ProdService> itemList);

    List<ProdService> findByProperties(Map<String, Object> properties, int start, int pageSize);
}