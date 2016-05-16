package com.gongsibao.module.order.soorderprodorganizationmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodorganizationmap.entity.SoOrderProdOrganizationMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderProdOrganizationMapService {

    SoOrderProdOrganizationMap findById(Integer pkid);

    int update(SoOrderProdOrganizationMap soOrderProdOrganizationMap);

    int delete(Integer pkid);

    Integer insert(SoOrderProdOrganizationMap soOrderProdOrganizationMap);

    List<SoOrderProdOrganizationMap> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdOrganizationMap> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdOrganizationMap> pageByProperties(Map<String, Object> properties, int page);

    List<Integer> queryOrderProdIdsByOrganizationIds(Collection<Integer> organizationIds);
}