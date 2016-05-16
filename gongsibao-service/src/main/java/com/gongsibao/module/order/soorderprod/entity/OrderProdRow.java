package com.gongsibao.module.order.soorderprod.entity;

import com.gongsibao.module.product.prodpriceaudit.entity.CityArea;

import java.util.Date;

/**
 * 订单操作
 * Created by huoquanfu on 2016/4/29.
 */
public class OrderProdRow {

    /**
     * 订单序号
     */
    private Integer orderId;

    /**
     * 订单Id(加密后的)
     */
    private String orderIdStr;

    /**
     * 产品订单序号
     */
    private Integer orderProdId;

    /**
     * 产品订单Id(加密后的)
     */
    private String orderProdIdStr;

    /**
     * 组织Id
     */
    private Integer orgnizationId;

    /**
     * 组织
     */
    private String orgnizationName;


    /***
     * 订单编号·
     */
    private String orderNo;

    /**
     * 产品Id
     */
    private Integer productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 商标名称
     */
    private String brandName;

    /**
     * 商标类型
     */
    private String brandTypeName;

    /**
     * 地区信息
     */
    private CityArea cityArea;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 下单人帐号
     */
    private String accountName;

    /**
     * 下单人手机号
     */
    private String accountMobile;

    /**
     * 业务人员
     */
    private String UserName;

    /**
     * 是否加急
     */
    private Integer isUrgent;

    /**
     * 是否退单
     */
    private Integer isRefund;

    /**
     * 需要天数
     */
    private Integer needDays;

    /**
     * 办理天数
     */
    private Integer processedDays;

    /**
     * 超时天数
     */
    private Integer timeoutDays;

    /**
     * 剩余天数
     */
    private Integer remainDays;

    /**
     * 处理状态序号，type=305
     */
    private Integer processStatusId;

    /**
     * 处理状态
     */
    private String processStatus;

    /**
     * 审核状态序号，type=105
     */
    private Integer auditStatusId;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 是否已经认领
     */
    private Boolean isGot;

    /**
     * 是否关注
     */
    private Boolean isConcerned;

    /**
     * 是否被投诉
     */
    private Boolean isComplained;

    /**
     * 创建时间
     */
    private Date addTime;

    public Integer getOrgnizationId() {
        return orgnizationId;
    }

    public void setOrgnizationId(Integer orgnizationId) {
        this.orgnizationId = orgnizationId;
    }

    public String getOrgnizationName() {
        return orgnizationName;
    }

    public void setOrgnizationName(String orgnizationName) {
        this.orgnizationName = orgnizationName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderIdStr() {
        return orderIdStr;
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }


    public Integer getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(Integer orderProdId) {
        this.orderProdId = orderProdId;
    }

    public String getOrderProdIdStr() {
        return orderProdIdStr;
    }

    public void setOrderProdIdStr(String orderProdIdStr) {
        this.orderProdIdStr = orderProdIdStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandTypeName() {
        return brandTypeName;
    }

    public void setBrandTypeName(String brandTypeName) {
        this.brandTypeName = brandTypeName;
    }

    public CityArea getCityArea() {
        return cityArea;
    }

    public void setCityArea(CityArea cityArea) {
        this.cityArea = cityArea;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountMobile() {
        return accountMobile;
    }

    public void setAccountMobile(String accountMobile) {
        this.accountMobile = accountMobile;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Integer urgent) {
        isUrgent = urgent;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getProcessedDays() {
        return processedDays;
    }

    public void setProcessedDays(Integer processedDays) {
        this.processedDays = processedDays;
    }

    public Integer getNeedDays() {
        return needDays;
    }

    public void setNeedDays(Integer needDays) {
        this.needDays = needDays;
    }

    public Integer getTimeoutDays() {
        return timeoutDays;
    }

    public void setTimeoutDays(Integer timeoutDays) {
        this.timeoutDays = timeoutDays;
    }

    public Integer getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(Integer remainDays) {
        this.remainDays = remainDays;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Boolean getIsGot() {
        return isGot;
    }

    public void setIsGot(Boolean got) {
        isGot = got;
    }

    public Boolean getIsConcerned() {
        return isConcerned;
    }

    public void setIsConcerned(Boolean concerned) {
        isConcerned = concerned;
    }

    public Boolean getIsComplained() {
        return isComplained;
    }

    public void setIsComplained(Boolean complained) {
        isComplained = complained;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

}
