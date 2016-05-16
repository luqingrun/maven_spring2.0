package com.gongsibao.module.order.soorderinvoicemap.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SoOrderInvoiceMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 订单序号
     */
    private Integer orderId;

    /**
     * 发票序号
     */
    private Integer invoiceId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }


}