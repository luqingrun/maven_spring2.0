package com.gongsibao.module.order.soorderprod.entity;

import com.gongsibao.common.util.security.SecurityUtils;

import com.gongsibao.module.order.soorderitem.entity.SoOrderItem;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.util.AuditTypeUtils;


import java.util.Date;
import java.util.List;

/**
 * Created by duan on 04-25.
 */
public class OrderProdList extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 编号
     */
    private String no;

    /**
     * 订单序号
     */
    private Integer orderId;
    /***
     * 订单编号
     */
    private String orderNo;

    /**
     * 产品序号
     */
    private Integer productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 城市序号
     */
    private Integer cityId;

    /**
     * 处理状态序号，type=206
     */
    private Integer processStatusId;
    /***
     * 处理状态名称
     */
    private String processStatusName;

    /**
     * 审核状态序号，type=105
     */
    private Integer auditStatusId;
    /***
     * 审核状态名称
     */
    private String auditStatusName;
    /**
     * 总价
     */
    private Integer price;

    /**
     * 原价
     */
    private Integer priceOriginal;

    /**
     * 产品服务
     */
    private String itemNames;

    /**
     * 产品地区
     */
    private String placeName;
    /**
     * 下单人帐号
     */
    private String accountName;
    /**
     * 下单人手机号
     */
    private String accountMobile;
    /**
     * 是否退单
     */
    private Integer isRefund;

    /**
     * 已办理天数
     */
    private Integer processedDays;

    /**
     * 是否投诉
     */
    private Integer isComplaint;

    /**
     * 需要天数
     */
    private Integer needDays;

    /**
     * 超时天数
     */
    private Integer timeoutDays;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 城市名称
     */
    private String cityName;

    /***
     * 业务员姓名
     */
    private String businessName;

    /***
     * 订单id加密后字符串
     */
    private String orderIdStr;

    /***
     * 是否加急
     */
    private Integer isUrgeney;
    /***
     * 服务项
     */
    private List<SoOrderProdItem> soOrderProdItems;

    private String isUrgeneyName;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(Integer priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public String getItemNames() {
        return itemNames;
    }

    public void setItemNames(String itemNames) {
        this.itemNames = itemNames;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(Integer isComplaint) {
        this.isComplaint = isComplaint;
    }

    public Integer getTimeoutDays() {
        return timeoutDays;
    }

    public void setTimeoutDays(Integer timeoutDays) {
        this.timeoutDays = timeoutDays;
    }

    public Integer getNeedDays() {
        return needDays;
    }

    public void setNeedDays(Integer needDays) {
        this.needDays = needDays;
    }

    public String getOrderIdStr() {
        return SecurityUtils.rc4Encrypt(getOrderId());
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getProcessStatusName() {
        return AuditTypeUtils.getName(processStatusId);
    }

    public void setProcessStatusName(String processStatusName) {
        this.processStatusName = processStatusName;
    }

    public String getAuditStatusName() {
        return AuditTypeUtils.getName(auditStatusId);
    }

    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }

    public Integer getIsUrgeney() {
        return isUrgeney;
    }

    public void setIsUrgeney(Integer isUrgeney) {
        this.isUrgeney = isUrgeney;
    }

    public String getIsUrgeneyName() {
        if (null == isUrgeney) {
            return "否";
        }
        if (isUrgeney == 1) {
            return "是";
        } else {
            return "否";
        }
    }

    public void setIsUrgeneyName(String isUrgeneyName) {
        this.isUrgeneyName = isUrgeneyName;
    }

    public List<SoOrderProdItem> getSoOrderProdItems() {
        return soOrderProdItems;
    }

    public void setSoOrderProdItems(List<SoOrderProdItem> soOrderProdItems) {
        this.soOrderProdItems = soOrderProdItems;
    }
}
