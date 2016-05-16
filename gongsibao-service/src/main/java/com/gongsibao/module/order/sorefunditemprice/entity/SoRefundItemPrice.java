package com.gongsibao.module.order.sorefunditemprice.entity;

import java.util.Date;


public class SoRefundItemPrice extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 退款序号 */
    private Integer refundId;
    
    /** 订单项价格序号 */
    private Integer orderProdItemId;
    
    /** 退款金额 */
    private Integer amount;
    
    /** 成本 */
    private Integer cost;
    

    
    public Integer getRefundId() {
        return refundId;
    }

    public void setRefundId(Integer refundId) {
        this.refundId = refundId;
    }
    
    public Integer getOrderProdItemId() {
        return orderProdItemId;
    }

    public void setOrderProdItemId(Integer orderProdItemId) {
        this.orderProdItemId = orderProdItemId;
    }
    
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
    

}