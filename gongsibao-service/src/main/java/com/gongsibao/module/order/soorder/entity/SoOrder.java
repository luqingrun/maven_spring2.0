package com.gongsibao.module.order.soorder.entity;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.sorefund.entity.SoRefund;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SoOrder extends com.gongsibao.common.db.BaseEntity {

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
     * 会员手机
     */
    private String accountMobile;

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

    /**
     * 已付价格
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
     * 是否开发票
     */
    private Integer isInvoice;

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

    private String prodName;

    private List<SoOrderProd> prodList;

    private String orderDiscount;

    private List<SoOrderDiscount> soOrderDiscountList;

    /**
     * 改价差额
     */
    private Integer margin;
    /**
     * 订单表关联退单表【一对多】
     */
    private SoRefund soRefund;

    public SoRefund getSoRefund() {
        return soRefund;
    }

    public void setSoRefund(SoRefund soRefund) {
        this.soRefund = soRefund;
    }

    private SoOrderProd soOrderProd;

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

    public List<SoOrderProd> getProdList() {
        return prodList;
    }

    public void setProdList(List<SoOrderProd> prodList) {
        this.prodList = prodList;
    }

    // 分期次数
    public Integer getInstallmentNum() {
        return getInstallmentList().size();
    }

    // 分期金额列表
    public List<Integer> getInstallmentList() {
        List<Integer> list = new ArrayList<>();
        if (null == isInstallment || isInstallment == 0) {
            return list; // 默认1期
        }

        String[] arr = StringUtils.trimToEmpty(installmentMode).split("\\|");
        for (String str : arr) {
            int money = NumberUtils.toInt(str);
            if (money > 0) {
                list.add(money);
            }

        }

        return list;
    }

    public SoOrderProd getSoOrderProd() {
        return soOrderProd;
    }

    public void setSoOrderProd(SoOrderProd soOrderProd) {
        this.soOrderProd = soOrderProd;
    }

    public String getAccountMobile() {
        return accountMobile;
    }

    public void setAccountMobile(String accountMobile) {
        this.accountMobile = accountMobile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    public List<SoOrderDiscount> getSoOrderDiscountList() {
        return soOrderDiscountList;
    }

    public void setSoOrderDiscountList(List<SoOrderDiscount> soOrderDiscountList) {
        this.soOrderDiscountList = soOrderDiscountList;
    }
}