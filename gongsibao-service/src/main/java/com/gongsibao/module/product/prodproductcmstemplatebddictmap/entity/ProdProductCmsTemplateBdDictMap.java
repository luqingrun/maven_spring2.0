package com.gongsibao.module.product.prodproductcmstemplatebddictmap.entity;

import java.util.Date;


public class ProdProductCmsTemplateBdDictMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品序号 */
    private Integer productId;
    
    /** 区域序号 */
    private Integer cityId;
    
    /** 模板序号 */
    private Integer templateId;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 备注 */
    private String remark;
    
    /** 添加时间 */
    private Date addTime;
    

    
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    
    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
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
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    

}