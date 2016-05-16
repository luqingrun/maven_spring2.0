package com.gongsibao.module.order.soorderprod.entity;

import java.util.Date;

/**
 * Created by huoquanfu on 2016/4/29.
 */
public class OrderProdRequest {

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
     * 订单编号
     */
    private String orderNo;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 销售地区Id
     */
    private String cityIdStr;

    /**
     * 审核状态
     */
    private String auditStatusIdStr;
    /**
     * 产品订单处理状态
     */
    private String processStatusIdStr;

    /**
     * 是否超时
     */
    private Boolean isTimeOut;

    /**
     * 业务人员（null,无业务人员）
     */
    private String userId;

    /**
     * 是否被投诉
     */
    private Boolean isComplained;

    private Date beginTime;

    private Date endTime;

    private Integer currentPage;

    private Integer pageSize;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCityIdStr() {
        return cityIdStr;
    }

    public void setCityIdStr(String cityIdStr) {
        this.cityIdStr = cityIdStr;
    }

    public String getAuditStatusIdStr() {
        return auditStatusIdStr;
    }

    public void setAuditStatusIdStr(String auditStatusIdStr) {
        this.auditStatusIdStr = auditStatusIdStr;
    }

    public String getProcessStatusIdStr() {
        return processStatusIdStr;
    }

    public void setProcessStatusIdStr(String processStatusIdStr) {
        this.processStatusIdStr = processStatusIdStr;
    }

    public Boolean getTimeOut() {
        return isTimeOut;
    }

    public void setTimeOut(Boolean timeOut) {
        isTimeOut = timeOut;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getComplained() {
        return isComplained;
    }

    public void setComplained(Boolean complained) {
        isComplained = complained;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
