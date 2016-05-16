package com.gongsibao.module.order.soinvoice.entity;

import java.util.Date;

/**
 * Created by duan on 04-28.
 */
public class InvoiceList extends com.gongsibao.common.db.BaseEntity {
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 编号
     */
    private String no;
    /***
     * 产品名称
     */
    private String prodName;
    /**
     * 订单类型，1订单，2合同
     */
    private Integer type;
    /**
     * 应支付价格
     */
    private Integer payablePrice;

    /***
     * 已支付价格
     */
    private Integer paidPrice;
    /**
     * 发票金额
     */
    private Integer amount;

    /**
     * 开票类型，type=308
     */
    private Integer typeId;

    /**
     * 开票公司，type=307
     */
    private Integer companyId;
    /***
     * 下单人姓名
     */
    private String realName;

    /***
     * 下单人电话
     */
    private String mobilePhone;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 审核状态序号，type=105
     */
    private Integer auditStatusId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(Integer payablePrice) {
        this.payablePrice = payablePrice;
    }

    public Integer getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(Integer paidPrice) {
        this.paidPrice = paidPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }
}
