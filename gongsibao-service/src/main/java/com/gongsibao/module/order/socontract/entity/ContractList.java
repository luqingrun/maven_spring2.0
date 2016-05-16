package com.gongsibao.module.order.socontract.entity;

import java.util.Date;

/**
 * Created by duan on 04-25.
 */
public class ContractList extends com.gongsibao.common.db.BaseEntity {
    private static final long serialVersionUID = -1L;
    /**
     * 订单类型，1订单，2合同
     */
    private Integer type;

    /**
     * 编号
     */
    private String no;

    /**
     * 会员序号
     */
    private Integer accountId;

    /**
     * 会员名称
     */
    private String accountName;

    /**
     * 付款状态序号，type=301
     */
    private Integer payStatusId;

    /**
     * 处理状态序号，type=302
     */
    private Integer processStatusId;

    /**
     * 退款状态序号，type=303
     */
    private Integer refundStatusId;

    /**
     * 总价，原价
     */
    private Integer totalPrice;

    /**
     * 应支付价格
     */
    private Integer payablePrice;

    /***
     * 已支付价格
     */
    private Integer paidPrice;

    /**
     * 来源类型序号，type=304
     */
    private Integer sourceTypeId;

    /**
     * 是否分期付款，默认否
     */
    private Integer isInstallment;

    /**
     * 分期形式，存储每期金额
     */
    private String installmentMode;

    /**
     * 分期审核状态，type=105
     */
    private Integer installmentAuditStatusId;

    /**
     * 是否改价订单，默认否
     */
    private Integer isChangePrice;

    /**
     * 改价审核状态，type=105
     */
    private Integer changePriceAuditStatusId;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 是否套餐，默认否
     */
    private Integer isPackage;

    /**
     * 套餐序号
     */
    private Integer packageId;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;
    /***
     * 下单人姓名
     */
    private String realName;

    /***
     * 下单人电话
     */
    private String mobilePhone;

    /***
     * 是否开发票
     *
     * @return
     */
    private Integer isInvoice;
    /***
     * 产品名称
     */
    private String prodName;

    /***
     * 实际合同额
     */
    private Integer realAmount;

    /***
     * 违约金
     */
    private Integer liquidatedDamages;

    /***
     * 分期审核状态 1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过
     */
    private Integer auditStatusId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getPayStatusId() {
        return payStatusId;
    }

    public void setPayStatusId(Integer payStatusId) {
        this.payStatusId = payStatusId;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public Integer getRefundStatusId() {
        return refundStatusId;
    }

    public void setRefundStatusId(Integer refundStatusId) {
        this.refundStatusId = refundStatusId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
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

    public Integer getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Integer sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    public Integer getIsInstallment() {
        return isInstallment;
    }

    public void setIsInstallment(Integer isInstallment) {
        this.isInstallment = isInstallment;
    }

    public String getInstallmentMode() {
        return installmentMode;
    }

    public void setInstallmentMode(String installmentMode) {
        this.installmentMode = installmentMode;
    }

    public Integer getInstallmentAuditStatusId() {
        return installmentAuditStatusId;
    }

    public void setInstallmentAuditStatusId(Integer installmentAuditStatusId) {
        this.installmentAuditStatusId = installmentAuditStatusId;
    }

    public Integer getIsChangePrice() {
        return isChangePrice;
    }

    public void setIsChangePrice(Integer isChangePrice) {
        this.isChangePrice = isChangePrice;
    }

    public Integer getChangePriceAuditStatusId() {
        return changePriceAuditStatusId;
    }

    public void setChangePriceAuditStatusId(Integer changePriceAuditStatusId) {
        this.changePriceAuditStatusId = changePriceAuditStatusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(Integer isPackage) {
        this.isPackage = isPackage;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Integer realAmount) {
        this.realAmount = realAmount;
    }

    public Integer getLiquidatedDamages() {
        return liquidatedDamages;
    }

    public void setLiquidatedDamages(Integer liquidatedDamages) {
        this.liquidatedDamages = liquidatedDamages;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
