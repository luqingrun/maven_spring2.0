package com.gongsibao.module.product.prodprice.entity;

import java.util.Date;


public class ProdPrice extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 服务序号
     */
    private Integer serviceId;

    /**
     * 地区序号，dict=1
     */
    private Integer cityId;

    /**
     * 定价审核序号
     */
    private Integer priceAuditId;


    /**
     * 原价，单位“分
     */
    private int originalPrice;

    /**
     * 单价，单位“分”
     */
    private Integer price;

    /**
     * 成本价，单位“分”
     */
    private Integer cost;

    /**
     * 是否必选
     */
    private Integer isMust;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 是否上架
     */
    private Integer isOnSale;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 说明
     */
    private String remark;

    private String UnitName;

    /**
     * 特性名称
     */
    private String propertyName;

    /**
     * 服务名称
     */
    private String typeName;


    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getPriceAuditId() {
        return priceAuditId;
    }

    public void setPriceAuditId(Integer priceAuditId) {
        this.priceAuditId = priceAuditId;
    }

    public Integer getPrice() {
        return price;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getIsMust() {
        return isMust;
    }

    public void setIsMust(Integer isMust) {
        this.isMust = isMust;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Integer isOnSale) {
        this.isOnSale = isOnSale;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}