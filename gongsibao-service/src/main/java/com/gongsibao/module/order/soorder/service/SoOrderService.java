package com.gongsibao.module.order.soorder.service;

import com.gongsibao.common.util.page.Pager;

import com.gongsibao.common.util.page.ParamType;

import com.gongsibao.module.order.soinvoice.entity.SoInvoice;
import com.gongsibao.module.order.soinvoice.entity.SoInvoiceAuditRequest;

import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;

import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SoOrderService {

    SoOrder findById(Integer pkid);

    OrderList findOrderListById(Integer pkid);

    SoOrder findChangePriceById(Integer pkid);

    int update(SoOrder soOrder);

    int delete(Integer pkid);

    Integer insert(SoOrder soOrder);

    List<SoOrder> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrder> findMapByIds(List<Integer> pkidList);

    Pager<SoOrder> pageByProperties(Map<String, Object> properties, int page);

    Pager<OrderList> pageOrderListByProperties(Map<String, Object> properties, int page);

    String findMaxNo();

    // ************* 分期审核begin ****************

    /**
     * 我的订单-分期订单列表
     *
     * @param properties
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pager<OrderList> listInstallment(Map<String, Object> properties, Integer currentPage, Integer pageSize);

    /**
     * 我的订单-导出
     *
     * @param properties
     * @return
     */
    String exportInstallment(Map<String, Object> properties);

    Pager<OrderList> listAuditInstallment(Map<String, Object> properties, Integer currentPage, Integer pageSize);

    String exportAuditInstallment(Map<String, Object> properties);

    /**
     * 申请分期
     *
     * @param soOrder
     * @return -1 不存在 -2 合同订单 0 失败
     */
    int addInstallment(SoOrder soOrder, Integer currentUserId);

    /***
     * 申请发票
     *
     * @return -1 不存在 -2 合同订单 0 失败
     */

    int addInvoice(SoOrder soOrder, SoInvoice soInvoice, Integer currentUserId);

    /**
     * 审核通过
     *
     * @param pkid
     * @param currentUserId
     * @return -1 订单不存在, -2 审核任务不存在
     */
    int editInstallmentPass(Integer pkid, Integer currentUserId, String remark) throws Exception;

    /**
     * 审核驳回
     *
     * @param pkid
     * @param currentUserId
     * @return -1 订单不存在, -2 审核任务不存在
     */
    int editInstallmentReject(Integer pkid, Integer currentUserId, String remark) throws Exception;

    // ************* 分期审核end  ****************
    Map<String, Integer> getAuditNums(Integer currentUserId, Integer type);

    /***
     * 导出全部订单excel
     */
    String exportOrderList(Map<String, Object> properties, Integer page);


    Pager<SoOrder> findBySoOrders(List<ParamType> paramTypeList, int page, int pagesize);

    /**
     * 审核改价订单列表
     *
     * @param properties
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pager<OrderList> listAuditChangePrice(Map<String, Object> properties, Integer currentPage, Integer pageSize);

    /**
     * 订单分配业务员
     *
     * @param orderId       订单id
     * @param applyUserId   业务员id
     * @param operateUserId 操作人id
     * @return
     */
    int editAssignApply(Integer orderId, Integer applyUserId, Integer operateUserId);

    /**
     * 批量分配
     *
     * @param orderIds
     * @param applyUserId
     * @param operateUserId
     */
    int editAssignApply(Collection<Integer> orderIds, Integer applyUserId, Integer operateUserId);


    int editInvoice(Integer orderId, Integer invoiceId, Integer userId, SoInvoiceAuditRequest invoiceAuditRequest) throws Exception;

    /**
     * 创建新订单
     *
     * @param soOrder
     * @param currentUserId
     * @return
     */
    int insertOrders(SoOrder soOrder, Integer currentUserId);

    /**
     * 改价订单-浮层
     *
     * @param orderPkId
     * @return
     */
    List<SoOrderProdItem> listChangePriceInfo(Integer orderPkId);

    /**
     * 改价订单-审核通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    int editChangePricePass(Integer pkid, Integer currentUserId, String remark) throws Exception;

    /**
     * 改价订单-审核不通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    int editChangePriceReject(Integer pkid, Integer currentUserId, String remark) throws Exception;

    /**
     * 通过AccountId查找订单
     * @param accountId
     * @return
     */
    List<SoOrder> findByAccountId(Integer accountId);
}