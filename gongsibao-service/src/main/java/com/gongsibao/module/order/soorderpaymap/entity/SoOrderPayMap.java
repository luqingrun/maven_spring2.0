package com.gongsibao.module.order.soorderpaymap.entity;

import java.util.Date;


public class SoOrderPayMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单序号 */
    private Integer orderId;
    
    /** 支付序号 */
    private Integer payId;
    

    
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }
    

}