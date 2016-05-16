package com.gongsibao.module.product.prodproduct.entity;


import com.gongsibao.module.product.prodservice.entity.ServiceList;

import java.util.List;

public class RequestProduct extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /** 产品id */
    private String pkidStr;

    /** 产品名称 */
    private String name;

    /** 产品分类序号，字典表type=3 */
    private String typeIdStr;
    
    /** 销售方类型序号，字典表type=8 */
    private String dealerTypeIdStr;
    
    /** 描述信息 */
    private String description;

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

    /** 服务项列表 */
    private List<ServiceList> prodServiceList;

    public Integer getIsOrderedBackgroundOnly() {
        return isOrderedBackgroundOnly;
    }

    public void setIsOrderedBackgroundOnly(Integer isOrderedBackgroundOnly) {
        this.isOrderedBackgroundOnly = isOrderedBackgroundOnly;
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



    public String getPkidStr() {
        return pkidStr;
    }

    public void setPkidStr(String pkidStr) {
        this.pkidStr = pkidStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getTypeIdStr() {
        return typeIdStr;
    }

    public void setTypeIdStr(String typeIdStr) {
        this.typeIdStr = typeIdStr;
    }
    
    public String getDealerTypeIdStr() {
        return dealerTypeIdStr;
    }

    public void setDealerTypeIdStr(String dealerTypeIdStr) {
        this.dealerTypeIdStr = dealerTypeIdStr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ServiceList> getProdServiceList() {
        return prodServiceList;
    }

    public void setProdServiceList(List<ServiceList> prodServiceList) {
        this.prodServiceList = prodServiceList;
    }

}