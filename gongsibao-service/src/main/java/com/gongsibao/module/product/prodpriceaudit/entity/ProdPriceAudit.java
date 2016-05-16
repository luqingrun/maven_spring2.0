package com.gongsibao.module.product.prodpriceaudit.entity;

import java.util.Date;


public class ProdPriceAudit extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 产品序号
     */
    private Integer productId;

    /**
     * 产品定价审核状态序号，type=7
     */
    private Integer organizationId;

    /**
     * 审核内容
     */
    private Integer auditStatusId;


    /**
     * 审核类型
     */
    private Integer auditStatusType;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 创建人序号
     */
    private Integer addUserId;

    /**
     * 说明
     */
    private String remark;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public Integer getAuditStatusType() {
        return auditStatusType;
    }

    public void setAuditStatusType(Integer auditStatusType) {
        this.auditStatusType = auditStatusType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}