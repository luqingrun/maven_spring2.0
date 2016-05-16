package com.gongsibao.module.product.prodservice.entity;

import com.gongsibao.common.util.security.SecurityUtils;

import java.util.Date;


public class ProdService extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品序号 */
    private Integer productId;
    
    /** 单位序号，type=4 */
    private Integer unitId;
    
    /** 特性序号，type=10 */
    private Integer propertyId;
    
    /** 分类序号，type=5 */
    private Integer typeId;
    
    /** 描述信息 */
    private String description;
    
    /** 排序 */
    private Double sort;
    
    /** 是否有库存量，默认否 */
    private Integer hasStock;
    
    /** 是否启用 */
    private Integer isEnabled;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;

    /** 单位名称 */
    private String unitName;

    /** 特性名称 */
    private String propertyName;

    /** 分类名称 */
    private String typeName;
    
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductIdStr(){
        return SecurityUtils.rc4Encrypt(getProductId());
    }
    
    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitIdStr(){
        return SecurityUtils.rc4Encrypt(getUnitId());
    }
    
    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyIdStr(){
        return SecurityUtils.rc4Encrypt(getPropertyId());
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeIdStr(){
        return SecurityUtils.rc4Encrypt(getTypeId());
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
    
    public Integer getHasStock() {
        return hasStock;
    }

    public void setHasStock(Integer hasStock) {
        this.hasStock = hasStock;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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

    public String getAddUserIdStr(){
        return SecurityUtils.rc4Encrypt(getAddUserId());
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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