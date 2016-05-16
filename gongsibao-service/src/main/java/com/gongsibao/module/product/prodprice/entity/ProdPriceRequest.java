package com.gongsibao.module.product.prodprice.entity;

import com.gongsibao.common.db.BaseEntity;

/**
 * Created by huoquanfu on 2016/4/20.
 */
public class ProdPriceRequest extends BaseEntity {

    public Integer getProdServiceId() {
        return prodServiceId;
    }

    public void setProdServiceId(Integer prodServiceId) {
        this.prodServiceId = prodServiceId;
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

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getServiceIdStr() {
        return serviceIdStr;
    }

    public void setServiceIdStr(String serviceIdStr) {
        this.serviceIdStr = serviceIdStr;
    }

    //服务ID加密后的字符串
    private String serviceIdStr;

    //服务Id
    private Integer prodServiceId;

    //是否必须
    private Boolean isMust;

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    //库存
    private Integer stock;

    //价格
    private Integer price;

    //原始价格
    private Integer originalPrice;

    //成本价
    private Integer cost;

}
