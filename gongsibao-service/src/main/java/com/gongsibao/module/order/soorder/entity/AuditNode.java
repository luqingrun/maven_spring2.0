package com.gongsibao.module.order.soorder.entity;

import java.io.Serializable;

/**
 * Created by wk on 2016/4/26.
 */
public class AuditNode implements Serializable {

    private static final long serialVersionUID = -1L;

    private String name;
    // 1051待审核、1052通过、1053不通过、1054排队、1055关闭
    private Integer auditStatus;

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
