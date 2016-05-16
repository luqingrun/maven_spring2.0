package com.gongsibao.module.order.soorderprodtracefile.entity;

import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdfile.entity.BdFile;

import java.util.Date;


public class SoOrderProdTraceFile extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项记录序号 */
    private Integer orderProdTraceId;
    
    /** 订单处理流程材料序号 */
    private Integer prodWorkflowFileId;
    
    /** 订单处理流程材料名称 */
    private String prodWorkflowFileName;

    /** 上传材料序号 */
    private Integer fileId;

    /** 上传材料是否最新(1:最新;2:历史) */
    private Integer isNew;
    
    /** 审核状态序号，type=105，1051待审核、1052通过、1053不通过 */
    private Integer auditStatusId;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;

    /** 上传材料是否最新名称(1:最新;2:历史) */
    private String isNewStr;

    /** 上传文件对象 */
    private BdFile file;

    /** 添加人名称 */
    private String addUserName;
    

    
    public Integer getOrderProdTraceId() {
        return orderProdTraceId;
    }

    public void setOrderProdTraceId(Integer orderProdTraceId) {
        this.orderProdTraceId = orderProdTraceId;
    }

    public String getOrderProdTraceIdStr(){
        return SecurityUtils.rc4Encrypt(getOrderProdTraceId());
    }
    
    public Integer getProdWorkflowFileId() {
        return prodWorkflowFileId;
    }

    public void setProdWorkflowFileId(Integer prodWorkflowFileId) {
        this.prodWorkflowFileId = prodWorkflowFileId;
    }

    public String getProdWorkflowFileIdStr(){
        return SecurityUtils.rc4Encrypt(getProdWorkflowFileId());
    }
    
    public String getProdWorkflowFileName() {
        return prodWorkflowFileName;
    }

    public void setProdWorkflowFileName(String prodWorkflowFileName) {
        this.prodWorkflowFileName = prodWorkflowFileName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileIdStr(){
        return SecurityUtils.rc4Encrypt(getFileId());
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public String getAuditStatusIdStr(){
        return SecurityUtils.rc4Encrypt(getAuditStatusId());
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

    public String getAddUserIdStr(){
        return SecurityUtils.rc4Encrypt(getAddUserId());
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsNewStr() {
        isNewStr = "历史";
        isNewStr = (this.isNew != null && this.isNew.compareTo(1) == 0) ? "最新" : isNewStr;
        return isNewStr;
    }

    public void setIsNewStr(String isNewStr) {
        this.isNewStr = isNewStr;
    }

    public BdFile getFile() {
        return file;
    }

    public void setFile(BdFile file) {
        this.file = file;
    }

    public String getAddUserName() {
        return addUserName;
    }

    public void setAddUserName(String addUserName) {
        this.addUserName = addUserName;
    }
}