package com.gongsibao.module.order.soorderproditem.entity;

/**
 * Created by duan on 04-25.
 */
public class ProdItemName extends com.gongsibao.common.db.BaseEntity {
    private Integer orderProdId;
    private String itemName;

    public Integer getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(Integer orderProdId) {
        this.orderProdId = orderProdId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
