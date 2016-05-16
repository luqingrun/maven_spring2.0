package com.gongsibao.module.product.prodprice.entity;

/**
 * Created by huoquanfu on 2016/4/20.
 */
public class ProdPriceDetail extends com.gongsibao.common.db.BaseEntity {

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(Integer original_price) {
        this.original_price = original_price;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean getIsMust() {
        return isMust;
    }

    public void setIsMust(Boolean must) {
        isMust = must;
    }

    public Boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(Boolean hasStock) {
        this.hasStock = hasStock;
    }

    //服务Id
    private Integer serviceId;
    //服务名称
    private String serviceName;
    //服务类型Id
    private String serviceTypeId;
    //服务类型名称
    private String serviceTypeName;
    //是否必须
    private Boolean isMust;
    //单位
    private String unit;

    //是否有库存
    private Boolean hasStock;
    //库存
    private Integer stock;
    //价格
    private Integer price;
    //原始价格
    private Integer original_price;
    //成本价
    private Integer cost;
    //顺序ID
    private Double sort;
}
