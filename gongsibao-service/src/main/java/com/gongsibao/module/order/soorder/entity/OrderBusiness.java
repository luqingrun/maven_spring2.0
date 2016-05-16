package com.gongsibao.module.order.soorder.entity;

/**
 * Created by duan on 04-25.
 * 订单业务员姓名
 */
public class OrderBusiness extends com.gongsibao.common.db.BaseEntity {
    private Integer orderId;
    private String businessName;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
