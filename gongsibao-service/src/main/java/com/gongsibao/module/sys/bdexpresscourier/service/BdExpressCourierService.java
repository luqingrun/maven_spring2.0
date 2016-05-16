package com.gongsibao.module.sys.bdexpresscourier.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdexpresscourier.entity.BdExpressCourier;

import java.util.List;
import java.util.Map;

public interface BdExpressCourierService {

    BdExpressCourier findById(Integer pkid);

    int update(BdExpressCourier bdExpressCourier);

    int delete(Integer pkid);

    Integer insert(BdExpressCourier bdExpressCourier);

    List<BdExpressCourier> findByIds(List<Integer> pkidList);

    List<BdExpressCourier> findAllBdExpressCourier();

    Map<Integer, BdExpressCourier> findMapByIds(List<Integer> pkidList);

    Pager<BdExpressCourier> pageByProperties(Map<String, Object> properties, int page);
}