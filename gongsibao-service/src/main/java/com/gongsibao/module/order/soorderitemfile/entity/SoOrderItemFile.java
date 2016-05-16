package com.gongsibao.module.order.soorderitemfile.entity;

import java.util.Date;


public class SoOrderItemFile extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项记录序号 */
    private Integer orderProdTraceId;
    
    /** 订单处理流程材料序号 */
    private Integer prodWorkflowFileId;
    
    /** 审核状态序号，type=105，1051待审核、1052通过、1053不通过 */
    private Integer auditStatusId;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;
    

    
    public Integer getOrderProdTraceId() {
        return orderProdTraceId;
    }

    public void setOrderProdTraceId(Integer orderProdTraceId) {
        this.orderProdTraceId = orderProdTraceId;
    }
    
    public Integer getProdWorkflowFileId() {
        return prodWorkflowFileId;
    }

    public void setProdWorkflowFileId(Integer prodWorkflowFileId) {
        this.prodWorkflowFileId = prodWorkflowFileId;
    }
    
    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
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