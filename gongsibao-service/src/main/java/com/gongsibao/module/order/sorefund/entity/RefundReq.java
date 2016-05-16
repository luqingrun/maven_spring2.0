package com.gongsibao.module.order.sorefund.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 000 on 2016/4/29.
 */
public class RefundReq implements Serializable {
    private static final long serialVersionUID = -1268802638769774598L;

    private String pkidStr;
    private Integer wayTypeId;
    private Integer isFullRefund;
    private String payerName;
    private String bankNo;
    private Integer amount;
    private Integer cost;
    private String remark;

    private List<RefundProdReq> orderProdList;


    public String getPkidStr() {
        return pkidStr;
    }

    public void setPkidStr(String pkidStr) {
        this.pkidStr = pkidStr;
    }

    public Integer getWayTypeId() {
        return wayTypeId;
    }

    public void setWayTypeId(Integer wayTypeId) {
        this.wayTypeId = wayTypeId;
    }

    public Integer getIsFullRefund() {
        return isFullRefund;
    }

    public void setIsFullRefund(Integer isFullRefund) {
        this.isFullRefund = isFullRefund;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<RefundProdReq> getOrderProdList() {
        return orderProdList;
    }

    public void setOrderProdList(List<RefundProdReq> orderProdList) {
        this.orderProdList = orderProdList;
    }
}
