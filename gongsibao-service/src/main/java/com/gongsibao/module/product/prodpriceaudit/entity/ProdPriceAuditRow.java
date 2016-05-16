package com.gongsibao.module.product.prodpriceaudit.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by huoquanfu on 2016/4/18.
 */
public class ProdPriceAuditRow extends com.gongsibao.common.db.BaseEntity {
    private static final long serialVersionUID = -1L;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProdType() {
        return prodType;
    }

    public void setProdType(Integer prodType) {
        this.prodType = prodType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusType() {
        return statusType;
    }

    public void setStatusType(Integer statusType) {
        this.statusType = statusType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
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

    public List<CityArea> getCityAreas() {
        return cityAreas;
    }

    public void setCityAreas(List<CityArea> cityAreas) {
        this.cityAreas = cityAreas;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCityFullNamesStr() {
        return cityFullNamesStr;
    }

    public void setCityFullNamesStr(String cityFullNamesStr) {
        this.cityFullNamesStr = cityFullNamesStr;
    }

    private Integer organizationId;
    /**
     * （销售）组织结构名
     */
    private String organizationName;

    /**
     * 产品序号
     */
    private Integer productId;


    /**
     * 审核类型(产品，套餐)
     */
    private Integer prodType;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品定价审核状态序号，type=7
     */
    private Integer statusId;


    /**
     * 审核类型
     */
    private Integer statusType;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 审核内容
     */
    private String content;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 创建人序号
     */
    private Integer addUserId;

    /**
     * 说明
     */
    private String remark;


    private List<CityArea> cityAreas;

    /**
     * 城市全名字符串
     */
    private String cityFullNamesStr;
}
