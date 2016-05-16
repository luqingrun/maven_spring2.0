package com.gongsibao.module.order.soorderprodaccount.entity;

import java.util.Date;
import java.util.List;


public class RequestOrderProd extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项序号 */
    private String orderProdId;
    
    /** 帐号列表 */
    private List<RequestOrderProdAccount> recordInfoList;
    

    
    public String getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(String orderProdId) {
        this.orderProdId = orderProdId;
    }
    
    public List<RequestOrderProdAccount> getRecordInfoList() {
        return recordInfoList;
    }

    public void setRecordInfoList(List<RequestOrderProdAccount> recordInfoList) {
        this.recordInfoList = recordInfoList;
    }
    

}