package com.gongsibao.module.order.sorefund.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 000 on 2016/4/29.
 */
public class RefundProdReq implements Serializable {

    private static final long serialVersionUID = 2798012761525797262L;
    private String pkidStr;

    private List<RefundProdItemReq> orderProdItemList;

    public String getPkidStr() {
        return pkidStr;
    }

    public void setPkidStr(String pkidStr) {
        this.pkidStr = pkidStr;
    }

    public List<RefundProdItemReq> getOrderProdItemList() {
        return orderProdItemList;
    }

    public void setOrderProdItemList(List<RefundProdItemReq> orderProdItemList) {
        this.orderProdItemList = orderProdItemList;
    }

}
