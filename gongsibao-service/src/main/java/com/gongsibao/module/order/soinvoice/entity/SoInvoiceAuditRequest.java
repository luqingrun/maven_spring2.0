package com.gongsibao.module.order.soinvoice.entity;

import com.gongsibao.common.db.BaseEntity;

/**
 *  发票审核实体
 *
 * Created by lianghongpeng on 2016/4/23.
 */
public class SoInvoiceAuditRequest extends BaseEntity {
    private Boolean isAudit ;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isAudit() {
        return isAudit;
    }

    public void setIsAudit(boolean isAudit) {
        this.isAudit = isAudit;
    }
}
