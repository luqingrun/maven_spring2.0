package com.gongsibao.module.order.soorderprodtrace.entity;

import com.gongsibao.common.util.security.SecurityUtils;

import java.util.Date;


public class SoOrderProdTrace extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项序号 */
    private Integer orderProdId;
    
    /** 订单项状态 */
    private Integer orderProdStatusId;
    
    /** 订单项记录类型序号，type=315，3151更改状态、3152备注、3153上传材料、3154提示客户、3155快递、3156帐号密码、3157标记投诉 */
    private Integer typeId;
    
    /** 跟进内容 */
    private String info;
    
    /** 操作人序号 */
    private Integer operatorId;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 备注、上传材料备注、提示客户、快递补充说明、帐号密码备注、提醒内容、投诉内容 */
    private String remark;
    
    /** 是否发送短信 */
    private Integer isSendSms;
    
    /** 快递-清单 */
    private String expressContent;
    
    /** 收件人 */
    private String expressTo;
    
    /** 快递公司名称，type=106 */
    private String expressCompanyName;
    
    /** 快递单号 */
    private String expressNo;

    /** 订单项状态名称 */
    private String orderProdStatusName;

    /** 操作人名称 */
    private String operatorName;

    /** 已办理天数 */
    private Integer processdDays;

    /** 超时天数 */
    private Integer timeoutDays;
    

    
    public Integer getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(Integer orderProdId) {
        this.orderProdId = orderProdId;
    }

    public String getOrderProdIdStr(){
        return SecurityUtils.rc4Encrypt(getOrderProdId());
    }
    
    public Integer getOrderProdStatusId() {
        return orderProdStatusId;
    }

    public void setOrderProdStatusId(Integer orderProdStatusId) {
        this.orderProdStatusId = orderProdStatusId;
    }

    public String getOrderProdStatusIdStr(){
        return SecurityUtils.rc4Encrypt(getOrderProdStatusId());
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeIdStr(){
        return SecurityUtils.rc4Encrypt(getTypeId());
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorIdStr(){
        return SecurityUtils.rc4Encrypt(getOperatorId());
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Integer getIsSendSms() {
        return isSendSms;
    }

    public void setIsSendSms(Integer isSendSms) {
        this.isSendSms = isSendSms;
    }
    
    public String getExpressContent() {
        return expressContent;
    }

    public void setExpressContent(String expressContent) {
        this.expressContent = expressContent;
    }
    
    public String getExpressTo() {
        return expressTo;
    }

    public void setExpressTo(String expressTo) {
        this.expressTo = expressTo;
    }
    
    public String getExpressCompanyName() {
        return expressCompanyName;
    }

    public void setExpressCompanyName(String expressCompanyName) {
        this.expressCompanyName = expressCompanyName;
    }
    
    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getOrderProdStatusName() {
        return orderProdStatusName;
    }

    public void setOrderProdStatusName(String orderProdStatusName) {
        this.orderProdStatusName = orderProdStatusName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getProcessdDays() {
        return processdDays;
    }

    public void setProcessdDays(Integer processdDays) {
        this.processdDays = processdDays;
    }

    public Integer getTimeoutDays() {
        return timeoutDays;
    }

    public void setTimeoutDays(Integer timeoutDays) {
        this.timeoutDays = timeoutDays;
    }
}