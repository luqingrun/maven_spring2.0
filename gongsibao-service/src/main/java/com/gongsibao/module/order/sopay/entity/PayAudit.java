package com.gongsibao.module.order.sopay.entity;

import java.util.Date;

/**
 * Created by a on 2016/5/3.
 */
public class PayAudit {


    public PayAudit(String orderPkidStr, String orderNo, String productNames, Integer payStatusId, Integer typeId, Integer totalPrice, Integer payPrice, Integer installmentTypeId, String addUserName, String adduserPhone, Date addTime, String auditStatusId) {
        this.orderPkidStr = orderPkidStr;
        this.orderNo = orderNo;
        this.productNames = productNames;
        this.payStatusId = payStatusId;
        this.typeId = typeId;
        this.totalPrice = totalPrice;
        this.payPrice = payPrice;
        this.installmentTypeId = installmentTypeId;
        this.addUserName = addUserName;
        this.adduserPhone = adduserPhone;
        this.addTime = addTime;
        this.auditStatusId = auditStatusId;
    }

    public String getOrderPkidStr() {
        return orderPkidStr;
    }

    public void setOrderPkidStr(String orderPkidStr) {
        this.orderPkidStr = orderPkidStr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public Integer getPayStatusId() {
        return payStatusId;
    }

    public void setPayStatusId(Integer payStatusId) {
        this.payStatusId = payStatusId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getInstallmentTypeId() {
        return installmentTypeId;
    }

    public void setInstallmentTypeId(Integer installmentTypeId) {
        this.installmentTypeId = installmentTypeId;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }

    public String getAdduserPhone() {
        return adduserPhone;
    }

    public void setAdduserPhone(String adduserPhone) {
        this.adduserPhone = adduserPhone;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(String auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public String getPayPkidStr() {
        return payPkidStr;
    }

    public void setPayPkidStr(String payPkidStr) {
        this.payPkidStr = payPkidStr;
    }

    public PayAudit() {
    }
    private String orderPkidStr;
    private String orderNo;
    private String payPkidStr;
    private String productNames;
    private Integer payStatusId;
    private Integer typeId;
    private Integer totalPrice;
    private Integer payPrice;
    private Integer installmentTypeId;
    private String addUserName;
    private String adduserPhone;
    private Date addTime;
    private String auditStatusId;
}
