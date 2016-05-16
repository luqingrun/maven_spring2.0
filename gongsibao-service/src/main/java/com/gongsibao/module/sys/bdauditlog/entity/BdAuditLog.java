package com.gongsibao.module.sys.bdauditlog.entity;

import com.gongsibao.common.util.StringUtils;

import java.util.Date;


public class BdAuditLog extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 审核类型序号，type=104，1041定价申请审核、1042订单改价申请审核、1043合同申请审核、1044发票申请审核、1045收款申请审核、1046退单申请审核、1047分期申请审核、1048产品改价申请审核、1049产品订单审核 */
    private Integer typeId;
    
    /** 表单序号 */
    private Integer formId;
    
    /** 审核状态序号，type=105，1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过、1055排队、1056关闭 */
    private Integer statusId;
    
    /** 审批内容 */
    private String content;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;

    /** 说明 */
    private String remark;
    
    /**  */
    private Integer level;

    /** 添加人名称 */
    private String addUserName;



    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }
    
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    
    public String getContent() {
        if(content==null)return "";
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return StringUtils.trimToEmpty(remark);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }
}