package com.gongsibao.module.order.soorderpaymap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderpaymap.entity.SoOrderPayMap;

import java.util.List;
import java.util.Map;

public interface SoOrderPayMapService {

    SoOrderPayMap findById(Integer pkid);

    int update(SoOrderPayMap soOrderPayMap);

    int delete(Integer pkid);

    Integer insert(SoOrderPayMap soOrderPayMap);

    List<SoOrderPayMap> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderPayMap> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderPayMap> pageByProperties(Map<String, Object> properties, int page);
}