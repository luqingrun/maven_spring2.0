package com.gongsibao.module.order.sopay.entity;

/**
 * Created by xuminjglang on 2016/4/29.
 */
public class PayType {
    private int installmentTypeId;
    private Double amount;

    public PayType(int installmentTypeId, Double amount) {
        this.installmentTypeId = installmentTypeId;
        this.amount = amount;
    }

    public int getInstallmentTypeId() {
        return installmentTypeId;
    }

    public void setInstallmentTypeId(int installmentTypeId) {
        this.installmentTypeId = installmentTypeId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
