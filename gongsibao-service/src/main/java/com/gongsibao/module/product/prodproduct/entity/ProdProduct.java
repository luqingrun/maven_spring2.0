package com.gongsibao.module.product.prodproduct.entity;

import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodservice.entity.ProdService;

import java.util.Date;
import java.util.List;


public class ProdProduct extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 产品分类序号，字典表type=3
     */
    private Integer typeId;

    /**
     * 销售方类型序号，字典表type=8
     */
    private Integer dealerTypeId;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品编号
     */
    private String no;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 排序
     */
    private Double sort;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 说明
     */
    private String remark;

    /**
     * 产品分类序号，字典表type=3
     */
    private String typeName;
    /**
     * 销售方类型，字典表type=8
     */
    private String dealerTypeName;

    /**
     * 是否仅可后台购买（不会在网站展示）
     */
    private Integer isOrderedBackgroundOnly;
    /**
     * 是否可以加入购物车
     */
    private Integer isAllowedAddToCart;
    /**
     * 是否需要填写邮寄地址（此产品需要邮寄给客户）
     */
    private Integer isRequiredEmsAddress;
    /**
     * 是否可以复数购买 (多于一个)
     */
    private Integer isAllowedBuyOneMore;

    /**
     * 是否需要选择服务周期（适用于以时间为单位的产品）
     */
    private Integer isRequiredServiceLifecycle;

    /**
     * 是否需要填写公司注册基本信息（仅涉及PC端）
     */
    private Integer isRequiredCompanyRegisterInfo;
    /**
     * 是否需要填写核名信息（仅涉及APP）
     */
    private Integer isRequiredCheckNameInfo;

    /**
     * 是否需要公司注册地址(是否需要选择“是否自有地址”)
     */
    private Integer isRequiredCompanyRegisterAddress;

    private List<ProdService> prodServiceList;

    public Integer getIsOrderedBackgroundOnly() {
        return isOrderedBackgroundOnly;
    }

    public void setIsOrderedBackgroundOnly(Integer isOrderedBbackgroudOnly) {
        this.isOrderedBackgroundOnly = isOrderedBbackgroudOnly;
    }

    public Integer getIsAllowedAddToCart() {
        return isAllowedAddToCart;
    }

    public void setIsAllowedAddToCart(Integer isAllowedAddToCart) {
        this.isAllowedAddToCart = isAllowedAddToCart;
    }

    public Integer getIsRequiredEmsAddress() {
        return isRequiredEmsAddress;
    }

    public void setIsRequiredEmsAddress(Integer isRequiredEmsAddress) {
        this.isRequiredEmsAddress = isRequiredEmsAddress;
    }

    public Integer getIsAllowedBuyOneMore() {
        return isAllowedBuyOneMore;
    }

    public void setIsAllowedBuyOneMore(Integer isAllowedBuyOneMore) {
        this.isAllowedBuyOneMore = isAllowedBuyOneMore;
    }

    public Integer getIsRequiredServiceLifecycle() {
        return isRequiredServiceLifecycle;
    }

    public void setIsRequiredServiceLifecycle(Integer isRequiredServiceLifecycle) {
        this.isRequiredServiceLifecycle = isRequiredServiceLifecycle;
    }

    public Integer getIsRequiredCompanyRegisterInfo() {
        return isRequiredCompanyRegisterInfo;
    }

    public void setIsRequiredCompanyRegisterInfo(Integer isRequiredCompanyRegisterInfo) {
        this.isRequiredCompanyRegisterInfo = isRequiredCompanyRegisterInfo;
    }

    public Integer getIsRequiredCheckNameInfo() {
        return isRequiredCheckNameInfo;
    }

    public void setIsRequiredCheckNameInfo(Integer isRequiredCheckNameInfo) {
        this.isRequiredCheckNameInfo = isRequiredCheckNameInfo;
    }

    public Integer getIsRequiredCompanyRegisterAddress() {
        return isRequiredCompanyRegisterAddress;
    }

    public void setIsRequiredCompanyRegisterAddress(Integer isRequiredCompanyRegisterAddress) {
        this.isRequiredCompanyRegisterAddress = isRequiredCompanyRegisterAddress;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeIdStr() {
        return SecurityUtils.rc4Encrypt(getTypeId());
    }

    public Integer getDealerTypeId() {
        return dealerTypeId;
    }

    public void setDealerTypeId(Integer dealerTypeId) {
        this.dealerTypeId = dealerTypeId;
    }

    public String getDealerTypeIdStr() {
        return SecurityUtils.rc4Encrypt(getDealerTypeId());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
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

    public String getAddUserIdStr() {
        return SecurityUtils.rc4Encrypt(getAddUserId());
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ProdService> getProdServiceList() {
        return prodServiceList;
    }

    public void setProdServiceList(List<ProdService> prodServiceList) {
        this.prodServiceList = prodServiceList;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDealerTypeName() {
        return dealerTypeName;
    }

    public void setDealerTypeName(String dealerTypeName) {
        this.dealerTypeName = dealerTypeName;
    }


}