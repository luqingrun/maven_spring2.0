package com.gongsibao.module.order.sorefund.entity;

import java.io.Serializable;

/**
 * Created by 000 on 2016/4/29.
 */
public class RefundProdItemReq implements Serializable {
    private static final long serialVersionUID = 3663907452100057961L;

    private String pkidStr;
    private Integer priceRefund;

    public String getPkidStr() {
        return pkidStr;
    }

    public void setPkidStr(String pkidStr) {
        this.pkidStr = pkidStr;
    }

    public Integer getPriceRefund() {
        return priceRefund;
    }

    public void setPriceRefund(Integer priceRefund) {
        this.priceRefund = priceRefund;
    }
}
