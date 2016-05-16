package com.gongsibao.module.product.prodpriceaudit.entity;

import java.util.List;

/**
 * Created by huoquanfu on 2016/5/6.
 */
public class ProdPriceOnSaleRow {

    private Integer organizationId;
    /**
     * 组织Id 加密串
     */
    private String organizationIdStr;

    /**
     * （销售）组织结构名
     */
    private String organizationName;

    /**
     * 产品序号
     */
    private Integer productId;

    /**
     * 产品Id加密
     */
    private String productIdStr;


    /**
     * 审核类型(产品，套餐)
     */
    private Integer prodType;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 上架地区列表
     */
    private List<CityArea> cityAreas;

    /**
     * 城市全名字符串
     */
    private String cityFullNamesStr;


    public String getCityFullNamesStr() {
        return cityFullNamesStr;
    }

    public void setCityFullNamesStr(String cityFullNamesStr) {
        this.cityFullNamesStr = cityFullNamesStr;
    }

    public List<CityArea> getCityAreas() {
        return cityAreas;
    }

    public void setCityAreas(List<CityArea> cityAreas) {
        this.cityAreas = cityAreas;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProdType() {
        return prodType;
    }

    public void setProdType(Integer prodType) {
        this.prodType = prodType;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationIdStr() {
        return organizationIdStr;
    }

    public void setOrganizationIdStr(String organizationIdStr) {
        this.organizationIdStr = organizationIdStr;
    }

    public String getProductIdStr() {
        return productIdStr;
    }

    public void setProductIdStr(String productIdStr) {
        this.productIdStr = productIdStr;
    }
}
