package com.gongsibao.module.order.sorefund.entity;

import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;

import java.util.Date;


public class SoRefund extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 订单序号
     */
    private Integer orderId;

    /**
     * 审核状态序号，type=105，1051待审核、1052通过、1053不通过
     */
    private Integer auditStatusId;

    /**
     * 退款方式序号，type=313，3131线上、3132线下
     */
    private Integer wayTypeId;

    /**
     * 是否退全款
     */
    private Integer isFullRefund;

    /**
     * 编号
     */
    private String no;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 银行帐号
     */
    private String bankNo;

    /**
     * 退款金额
     */
    private Integer amount;

    /**
     * 成本
     */
    private Integer cost;

    /**
     * 说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 关联订单表
     */
    private SoOrder soOrder;

    /**
     * 关联审核日志表
     */
    private BdAuditLog bdAuditLog;

    private SoOrderProd soOrderProd;

    public SoOrderProd getSoOrderProd() {
        return soOrderProd;
    }

    public void setSoOrderProd(SoOrderProd soOrderProd) {
        this.soOrderProd = soOrderProd;
    }

    public BdAuditLog getBdAuditLog() {
        return bdAuditLog;
    }

    public void setBdAuditLog(BdAuditLog bdAuditLog) {
        this.bdAuditLog = bdAuditLog;
    }

    public SoOrder getSoOrder() {
        return soOrder;
    }

    public void setSoOrder(SoOrder soOrder) {
        this.soOrder = soOrder;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public Integer getWayTypeId() {
        return wayTypeId;
    }

    public void setWayTypeId(Integer wayTypeId) {
        this.wayTypeId = wayTypeId;
    }

    public Integer getIsFullRefund() {
        return isFullRefund;
    }

    public void setIsFullRefund(Integer isFullRefund) {
        this.isFullRefund = isFullRefund;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }


}