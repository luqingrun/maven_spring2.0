package com.gongsibao.module.product.prodpriceaudit.entity;

/**
 * Created by huoquanfu on 2016/5/9.
 */
public class ProdPriceAuditRequestBase {
    /**
     * 产品Id
     */
    private String prodIdStr;

    /**
     * 产品序号
     */
    private Integer prodId;

    /**
     * 组织Id
     */
    private Integer organizationIdStr;

    /**
     * 组织Id
     */
    private Integer organizationId;

    /**
     * 审核类型
     */
    private Integer auditType;

    public String getProdIdStr() {
        return prodIdStr;
    }

    public void setProdIdStr(String prodIdStr) {
        this.prodIdStr = prodIdStr;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getOrganizationIdStr() {
        return organizationIdStr;
    }

    public void setOrganizationIdStr(Integer organizationIdStr) {
        this.organizationIdStr = organizationIdStr;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getAuditType() {
        return auditType;
    }

    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
}
