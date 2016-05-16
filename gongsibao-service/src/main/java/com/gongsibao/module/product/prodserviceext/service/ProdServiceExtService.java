package com.gongsibao.module.product.prodserviceext.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodserviceext.entity.ProdServiceExt;

import java.util.List;
import java.util.Map;

public interface ProdServiceExtService {

    ProdServiceExt findById(Integer pkid);

    int update(ProdServiceExt prodServiceExt);

    int delete(Integer pkid);

    Integer insert(ProdServiceExt prodServiceExt);

    List<ProdServiceExt> findByIds(List<Integer> pkidList);

    Map<Integer, ProdServiceExt> findMapByIds(List<Integer> pkidList);

    Pager<ProdServiceExt> pageByProperties(Map<String, Object> properties, int page);
}