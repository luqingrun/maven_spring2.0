package com.gongsibao.module.product.prodservice.entity;

import java.util.Date;


public class ServiceList extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /** 产品服务项id */
    private String pkidStr;
    
    /** 产品服务项类型id */
    private String typeIdStr;
    
    /** 产品服务项单位id */
    private String unitIdStr;
    
    /** 产品服务项特性id */
    private String propertyIdStr;
    
    /** 是否无限库存 */
    private Integer hasStock;


    public String getPkidStr() {
        return pkidStr;
    }

    public void setPkidStr(String pkidStr) {
        this.pkidStr = pkidStr;
    }
    
    public String getTypeIdStr() {
        return typeIdStr;
    }

    public void setTypeIdStr(String typeIdStr) {
        this.typeIdStr = typeIdStr;
    }
    
    public String getUnitIdStr() {
        return unitIdStr;
    }

    public void setUnitIdStr(String unitIdStr) {
        this.unitIdStr = unitIdStr;
    }
    
    public String getPropertyIdStr() {
        return propertyIdStr;
    }

    public void setPropertyIdStr(String propertyIdStr) {
        this.propertyIdStr = propertyIdStr;
    }
    
    public Integer getHasStock() {
        return hasStock;
    }

    public void setHasStock(Integer hasStock) {
        this.hasStock = hasStock;
    }
    

}