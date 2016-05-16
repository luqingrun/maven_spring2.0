package com.gongsibao.module.order.soinvoice.entity;

import java.util.Date;


public class SoInvoice extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /** 抬头 */
    private String title;
    
    /** 开票公司，type=307 */
    private Integer companyId;
    
    /** 开票类型，type=308 */
    private Integer typeId;
    
    /** 发票金额 */
    private Integer amount;
    
    /** 发票内容 */
    private String content;
    
    /** 审核状态序号，type=105 */
    private Integer auditStatusId;
    
    /** 接收人姓名 */
    private String receiverName;
    
    /** 接收人手机 */
    private String receiverMobilePhone;
    
    /** 接收人地址 */
    private String receiverAddress;
    
    /** 增值税公司税号 */
    private String vatTaxNo;
    
    /** 增值税公司注册地址 */
    private String vatAddress;
    
    /** 增值税公司注册电话 */
    private String vatPhone;
    
    /** 增值税公司开户行名称 */
    private String vatBankName;
    
    /** 增值税公司开户行帐号 */
    private String vatBankNo;
    
    /** 附件序号 */
    private Integer fileId;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;
    

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }
    
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    public String getReceiverMobilePhone() {
        return receiverMobilePhone;
    }

    public void setReceiverMobilePhone(String receiverMobilePhone) {
        this.receiverMobilePhone = receiverMobilePhone;
    }
    
    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
    
    public String getVatTaxNo() {
        return vatTaxNo;
    }

    public void setVatTaxNo(String vatTaxNo) {
        this.vatTaxNo = vatTaxNo;
    }
    
    public String getVatAddress() {
        return vatAddress;
    }

    public void setVatAddress(String vatAddress) {
        this.vatAddress = vatAddress;
    }
    
    public String getVatPhone() {
        return vatPhone;
    }

    public void setVatPhone(String vatPhone) {
        this.vatPhone = vatPhone;
    }
    
    public String getVatBankName() {
        return vatBankName;
    }

    public void setVatBankName(String vatBankName) {
        this.vatBankName = vatBankName;
    }
    
    public String getVatBankNo() {
        return vatBankNo;
    }

    public void setVatBankNo(String vatBankNo) {
        this.vatBankNo = vatBankNo;
    }
    
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
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