package com.gongsibao.module.product.prodproductcmstemplate.entity;

import java.util.Date;
import java.util.List;


public class ProdProductCmsTemplate extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /*修改时，前端传过来的加密id*/
    private String id;

    /** 名称 */
    private String name;

    /** 是否是默认模板 */
    private Integer isDefault;

    /** 模板详情 */
    private String content;

    /** 添加人序号 */
    private Integer addUserId;

    /** 产品序号 */
    private Integer productId;

    /** 产品名称 */
    private String productName;

    /** 备注 */
    private String remark;

    /** 添加时间 */
    private Date addTime;
    /*产品加密ID*/
    private String prodIdStr;
    /*服务地区集合*/
    private List<ProductCmsTemplateCity> serviceCityList;

    public String getProdIdStr() {
        return prodIdStr;
    }

    public void setProdIdStr(String prodIdStr) {
        this.prodIdStr = prodIdStr;
    }

    public List<ProductCmsTemplateCity> getServiceCityList() {
        return serviceCityList;
    }

    public void setServiceCityList(List<ProductCmsTemplateCity> serviceCityList) {
        this.serviceCityList = serviceCityList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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