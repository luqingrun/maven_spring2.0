package com.gongsibao.module.order.sopay.entity;

import java.util.Date;


public class SoPay extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 编号 */
    private String no;
    
    /** 金额 */
    private Integer amount;
    
    /** 付款方式序号，type= */
    private Integer payWayTypeId;
    
    /** 支付成功状态，type= */
    private Integer successStatusId;
    
    /** 线下付款方式序号，type= */
    private Integer offlineWayTypeId;
    
    /** 线下分期类型序号，type= */
    private Integer offlineInstallmentTypeId;
    
    /** 线下付款方名称 */
    private String offlinePayerName;
    
    /** 线下付款银行帐号 */
    private String offlineBankNo;
    
    /** 线下付款说明 */
    private String offlineRemark;
    
    /** 线下付款审核状态，type= */
    private Integer offlineAuditStatusId;
    
    /** 线下付款提交人序号，0则为会员提交 */
    private Integer offlineAddUserId;
    
    /** 线上付款银行代码序号，type= */
    private String onlineBankCodeId;
    
    /** 线上回调时间 */
    private Integer onlineConfirmTime;
    
    /** 创建时间 */
    private Date addTime;
    

    
    public String getNo() {

        if(no==null)return "";
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Integer getPayWayTypeId() {
        return payWayTypeId;
    }

    public void setPayWayTypeId(Integer payWayTypeId) {
        this.payWayTypeId = payWayTypeId;
    }
    
    public Integer getSuccessStatusId() {

        if(successStatusId==null)return 3122;
            return successStatusId;
    }

    public void setSuccessStatusId(Integer successStatusId) {
        this.successStatusId = successStatusId;
    }
    
    public Integer getOfflineWayTypeId() {
        if(offlineWayTypeId==null)return 3111;
        return offlineWayTypeId;
    }

    public void setOfflineWayTypeId(Integer offlineWayTypeId) {
        this.offlineWayTypeId = offlineWayTypeId;
    }
    
    public Integer getOfflineInstallmentTypeId() {
        return offlineInstallmentTypeId;
    }

    public void setOfflineInstallmentTypeId(Integer offlineInstallmentTypeId) {
        this.offlineInstallmentTypeId = offlineInstallmentTypeId;
    }
    
    public String getOfflinePayerName() {
        return offlinePayerName;
    }

    public void setOfflinePayerName(String offlinePayerName) {
        this.offlinePayerName = offlinePayerName;
    }
    
    public String getOfflineBankNo() {
        return offlineBankNo;
    }

    public void setOfflineBankNo(String offlineBankNo) {
        this.offlineBankNo = offlineBankNo;
    }
    
    public String getOfflineRemark() {
        return offlineRemark;
    }

    public void setOfflineRemark(String offlineRemark) {
        this.offlineRemark = offlineRemark;
    }
    
    public Integer getOfflineAuditStatusId() {

        if(offlineAuditStatusId==null)return 1051;
            return offlineAuditStatusId;
    }

    public void setOfflineAuditStatusId(Integer offlineAuditStatusId) {
        this.offlineAuditStatusId = offlineAuditStatusId;
    }
    
    public Integer getOfflineAddUserId() {
        if(offlineAddUserId==null)return 0;
        return offlineAddUserId;
    }

    public void setOfflineAddUserId(Integer offlineAddUserId) {
        this.offlineAddUserId = offlineAddUserId;
    }
    
    public String getOnlineBankCodeId() {
        if(onlineBankCodeId==null)return "";
        return onlineBankCodeId;
    }

    public void setOnlineBankCodeId(String onlineBankCodeId) {
        this.onlineBankCodeId = onlineBankCodeId;
    }
    
    public Integer getOnlineConfirmTime() {
        return onlineConfirmTime;
    }

    public void setOnlineConfirmTime(Integer onlineConfirmTime) {
        this.onlineConfirmTime = onlineConfirmTime;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    

}