package com.gongsibao.module.order.soorderitemusermap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemusermap.entity.SoOrderItemUserMap;

import java.util.List;
import java.util.Map;

public interface SoOrderItemUserMapService {

    SoOrderItemUserMap findById(Integer pkid);

    int update(SoOrderItemUserMap soOrderItemUserMap);

    int delete(Integer pkid);

    Integer insert(SoOrderItemUserMap soOrderItemUserMap);

    List<SoOrderItemUserMap> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderItemUserMap> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderItemUserMap> pageByProperties(Map<String, Object> properties, int page);
}