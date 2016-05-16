package com.gongsibao.module.order.soorderprod.service;

import com.gongsibao.common.util.page.Pager;

import com.gongsibao.common.util.page.ParamType;

import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderProdService {

    SoOrderProd findById(Integer pkid);

    Map<String, Object> getDetailById(Integer orderProdId);

    SoOrder getOrderProdAuditById(Integer orderProdId);

    int update(SoOrderProd soOrderProd);

    int updateAuditStatus(Integer pkid, int newStatus, int oldStatus);

    int updateOrderProdAudit(Integer orderProdId, Collection<Integer> passTraceFileIds, Collection<Integer> failTraceFileIds, String content, Integer userId);

    void updateJobDays(Integer orderprodid, int processedDays, int needDays, int timeoutDays, int traceTimeoutDays);

    int delete(Integer pkid);

    Integer insert(SoOrderProd soOrderProd);

    List<SoOrderProd> findByIds(List<Integer> pkidList);

    List<OrderProdList> findOrderProdListByOrderId(Integer orderId);

    Map<Integer, SoOrderProd> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderProd> pageByProperties(Map<String, Object> properties, int page);

    SoOrderProd updateStatus(SoOrderProd soOrderProd);

    SoOrderProd updateIsComplaint(SoOrderProd soOrderProd);

    Pager<OrderProdList> pageOrderProdListByProperties(Map<String, Object> properties, int page);

    List<SoOrderProd> getByOrderId(Integer orderId);

    void setProdItem(SoOrderProd prod);

    void setProdItem(List<SoOrderProd> prodList);


    Pager<SoOrderProd> findBySoOrderProds(List<ParamType> paramTypeList, int start, int pageSize);

    Pager<OrderProdRow> findMyOrderListByProperties(Map<String, Object> properties, Integer pageNum, Integer pageSize);

    Pager<OrderProdRow> findOrderAuditListByProperties(Map<String, Object> properties, Integer pageNum, Integer pageSize);

    Pager<OrderProdMonitorList> findOrderProdMonitorListByProperties(Map<String, Object> properties, int page, int pageSize);

    Pager<OrderProdRow> pageOrderProdRowsByProperties(Map<String, Object> map, Integer page, Integer pageSize);

    /**
     * 退单申请模块：得到退款产品列表
     *
     * @param orderId
     * @return List<SoOrderProd>
     */
    List<SoOrderProd> getApplyRefundByOrderId(Integer orderId, Integer isRefund);

    /**
     * 退单申请模块:更新状态
     *
     * @param pkidList
     * @return
     */
    int updateRefund(List<Integer> pkidList, Integer isRefund);


    int updateReturnProduct(int productPkid, int userId);

    /**
     * 退单审核模块:更新状态
     *
     * @param pkidList
     * @return
     */
    int updateAuditRefund(List<Integer> pkidList, Integer auditStatusId);


    Integer getOrderProdCount(String statusStr, String orgIdsStr);


    Integer updateBeginOperation(int productId, int OrganizationId);

    List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties);

    Pager<SoOrderProd> findOrderProdListByJobProperties(Map<String, Object> properties, int page, int pageSize);

}