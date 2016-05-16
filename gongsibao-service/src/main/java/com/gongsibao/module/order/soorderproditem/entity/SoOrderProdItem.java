package com.gongsibao.module.order.soorderproditem.entity;

import com.gongsibao.common.util.security.SecurityUtils;

import java.util.Date;


public class SoOrderProdItem extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 订单项序号
     */
    private Integer orderProdId;

    /**
     * 定价序号
     */
    private Integer priceId;

    /**
     * 定价序号(加密)
     */
    private String priceIdStr;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private Integer price;

    /**
     * 原价
     */
    private Integer priceOriginal;

    /**
     * 已退款价
     */
    private Integer priceRefund;

    /***
     * 支出成本
     */
    private Integer cost;

    private String productName;

    private int cityId;

    private String cityName;

    public Integer getPriceRefund() {
        return priceRefund;
    }

    public void setPriceRefund(Integer priceRefund) {
        this.priceRefund = priceRefund;
    }

    public Integer getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(Integer orderProdId) {
        this.orderProdId = orderProdId;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getPriceIdStr() {
        return SecurityUtils.rc4Encrypt(getPriceId());
    }

    public void setPriceIdStr(String priceIdStr) {
        this.priceIdStr = priceIdStr;
    }

    private String feature;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}