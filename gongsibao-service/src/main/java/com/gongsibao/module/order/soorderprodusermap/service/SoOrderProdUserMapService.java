package com.gongsibao.module.order.soorderprodusermap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.uc.ucuser.entity.UcUser;

import java.util.List;
import java.util.Map;

public interface SoOrderProdUserMapService {

    SoOrderProdUserMap findById(Integer pkid);

    SoOrderProdUserMap findByOrderId(Integer orderId);

    int update(SoOrderProdUserMap soOrderProdUserMap);

    int updateStatus(Integer pkid, int newStatus, int oldStatus);

    int updateStatusByOrderProdId(Integer orderProdId, Integer typeId, int newStatus, int oldStatus);

    int delete(Integer pkid);

    int deleteInfo(Integer userId, Integer orderProdId, Integer typeId);

    void insertBatch(final List<SoOrderProdUserMap> itemList);

    Integer insert(SoOrderProdUserMap soOrderProdUserMap);

    List<SoOrderProdUserMap> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderProdUserMap> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProdUserMap> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    List<SoOrderProdUserMap> findByProperties(Map<String, Object> properties, int start, int pageSize);

    List<SoOrderProdUserMap> findRefundByIds(List<Integer> pkidList, Integer type);

    List<Integer> queryUserIdsByProperties(Map<String, Object> properties);

    Map<Integer, List<Integer>> queryUserIdsMapByProperties(Map<String, Object> properties);

    Map<Integer, String> queryUserNameMapByProperties(Map<String, Object> properties);

    int countByProperties(Map<String, Object> properties);

    List<UcUser> findOperatorByOrderProdId(Integer orderProdId);

    List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties);

    Map<Integer, String> queryOrganizationNameMapByProperties(Map<String, Object> properties);
}