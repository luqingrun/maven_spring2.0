package com.gongsibao.module.order.sorefund.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.sorefund.entity.SoRefund;

import java.util.List;
import java.util.Map;

public interface SoRefundService {

    SoRefund findById(Integer pkid);

    int update(SoRefund soRefund);

    int delete(Integer pkid);

    Integer insert(SoRefund soRefund);

    List<SoRefund> findByIds(List<Integer> pkidList);

    Map<Integer, SoRefund> findMapByIds(List<Integer> pkidList);

    Pager<SoRefund> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    /**
     * 退单审核模块：退单审核列表
     *
     * @param properties
     * @param page
     * @param pageSize
     * @return
     */
    Pager<SoRefund> pageRefundAuditByProperties(Map<String, Object> properties, int page, int pageSize);

    /**
     * 退单查看模块：退单查看列表
     * @param typeId
     * @param statusId
     * @param addUserId
     * @return
     */
    List<SoOrder> getRefundViewList(Integer typeId,Integer statusId,Integer addUserId,Map<String,Object> map);

    /**
     * 退单查看模块：退单查看列表
     *
     * @param pkidList
     * @return SoRefund
     */
    List<SoRefund> findListByIds(List<Integer> pkidList);


    /**
     * 退单明细模块：展示列表
     *
     * @param orderId
     * @return List<SoRefund>
     */
    List<SoRefund> findDetailsListByIds(Integer orderId);


    /**
     * 退单申请模块：确认提交
     *
     * @param map
     * @return
     */
    Integer insertRefundApply(Map<String, Object> map,Integer currentUserId);


    /**
     * 审核通过
     *
     * @param pkid
     * @param currentUserId
     * @return -1 订单不存在, -2 审核任务不存在
     */
    int editRefundPass(Integer pkid, Integer currentUserId, String remark,Map<String,Object> map) throws Exception;

    /**
     * 审核驳回
     *
     * @param pkid
     * @param currentUserId
     * @return -1 订单不存在, -2 审核任务不存在
     */
    int editRefundReject(Integer pkid, Integer currentUserId, String remark,Map<String,Object> map) throws Exception;

    /**
     * 退单审核模块：更新状态
     * @param pkid
     * @param auditStatusId
     * @return int
     */
    int updateSoRefund(Integer pkid ,Integer auditStatusId );

}