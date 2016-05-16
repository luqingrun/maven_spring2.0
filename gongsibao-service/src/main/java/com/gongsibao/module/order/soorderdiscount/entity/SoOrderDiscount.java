package com.gongsibao.module.order.soorderdiscount.entity;

import java.util.Date;


public class SoOrderDiscount extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单序号 */
    private Integer orderId;
    
    /** 优惠类型序号，type= */
    private Integer typeId;
    
    /** 优惠金额，优惠前-优惠后 */
    private Integer amount;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 说明 */
    private String remark;
    

    
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    

}