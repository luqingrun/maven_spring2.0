package com.gongsibao.module.order.soorderitemprice.entity;

import java.util.Date;


public class SoOrderItemPrice extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项序号 */
    private Integer orderItemId;
    
    /** 定价序号 */
    private Integer priceId;
    
    /** 单位名称 */
    private String unitName;
    
    /** 服务名称 */
    private String serviceName;
    
    /** 数量 */
    private Integer quantity;
    
    /** 单价 */
    private Integer price;
    

    
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
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
    

}