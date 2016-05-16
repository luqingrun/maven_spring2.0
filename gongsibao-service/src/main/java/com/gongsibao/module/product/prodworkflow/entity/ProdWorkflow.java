package com.gongsibao.module.product.prodworkflow.entity;

import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;

import java.util.Date;
import java.util.List;


public class ProdWorkflow extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品序号 */
    private Integer productId;
    
    /** 表单名称 */
    private String formName;
    
    /** 是否启用 */
    private Integer isEnabled;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;

    /** 产品方案关联的地区名称字符串 */
    private String regionStr;

    /** 产品 */
    private ProdProduct prodProduct;

    /** 产品方案关联地区 */
    private List<ProdWorkflowBdDictMap> regionList;

    /** 产品方案关联节点 */
    private List<ProdWorkflowNode> prodWorkflowNodeList;

    /** 产品方案关联材料 */
    private List<ProdWorkflowFile> workflowFileList;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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

    public ProdProduct getProdProduct() {
        return prodProduct;
    }

    public void setProdProduct(ProdProduct prodProduct) {
        this.prodProduct = prodProduct;
    }

    public List<ProdWorkflowBdDictMap> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<ProdWorkflowBdDictMap> regionList) {
        this.regionList = regionList;
    }

    public List<ProdWorkflowNode> getProdWorkflowNodeList() {
        return prodWorkflowNodeList;
    }

    public void setProdWorkflowNodeList(List<ProdWorkflowNode> prodWorkflowNodeList) {
        this.prodWorkflowNodeList = prodWorkflowNodeList;
    }

    public String getRegionStr() {
        return regionStr;
    }

    public void setRegionStr(String regionStr) {
        this.regionStr = regionStr;
    }

    public List<ProdWorkflowFile> getWorkflowFileList() {
        return workflowFileList;
    }

    public void setWorkflowFileList(List<ProdWorkflowFile> workflowFileList) {
        this.workflowFileList = workflowFileList;
    }
}