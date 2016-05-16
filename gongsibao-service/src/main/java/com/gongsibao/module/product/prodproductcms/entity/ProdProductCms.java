package com.gongsibao.module.product.prodproductcms.entity;

import java.util.Date;
import java.util.List;
import com.gongsibao.module.product.prodproductcmsimage.entity.ProdProductCmsImage;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;


public class ProdProductCms extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** 产品序号 */
    private Integer productId;

    /** 标题（显示名称） */
    private String title;
    
    /** 关键字 */
    private String keyword;
    
    /** 摘要 */
    private String summary;
    /*显示价*/
    private String showprice;

    /** 价格描述 */
    private String priceDescription;
    
    /** 一句话卖点(推广文案) */
    private String promotionalCopy;
    
    /** 服务地区文案 */
    private String serviceAreaDescription;
    
    /** 注册地址文案 */
    private String registAddressDescription;
    
    /** 服务期文案 */
    private String servicePeriodDescription;
    
    /** 购买数量的文案 */
    private String buyCountDescription;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 备注 */
    private String remark;
    
    /** 说明 */
    private Date addTime;


    /*产品加密ID*/
    private String prodIdStr;
    /*产品图片的url地址(bd_file表的from_id是'产品id(prodId)')*/
    private String imgUrl;
    /*徽章图*/
    private String badgeImgUrl;

    public String getBadgeImgUrl() {
        return badgeImgUrl;
    }

    public void setBadgeImgUrl(String badgeImgUrl) {
        this.badgeImgUrl = badgeImgUrl;
    }

    public String getShowprice() {
        return showprice;
    }

    public void setShowprice(String showprice) {
        this.showprice = showprice;
    }

    public String getProdIdStr() {
        return prodIdStr;
    }

    public void setProdIdStr(String prodIdStr) {
        this.prodIdStr = prodIdStr;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public List<ProdProductCmsImage> getProductCmsImageList() {
        return productCmsImageList;
    }

    public void setProductCmsImageList(List<ProdProductCmsImage> productCmsImageList) {
        this.productCmsImageList = productCmsImageList;
    }

    public List<ProdProductCmsRelated> getProdProductCmsRelatedList() {
        return prodProductCmsRelatedList;
    }

    public void setProdProductCmsRelatedList(List<ProdProductCmsRelated> prodProductCmsRelatedList) {
        this.prodProductCmsRelatedList = prodProductCmsRelatedList;
    }

    /*排序编号*/
    private Double sort;
    /** 轮播图集合 */
    private List<ProdProductCmsImage> productCmsImageList;
    /** 相关产品集合(推荐产品) */
    private List<ProdProductCmsRelated> prodProductCmsRelatedList;


    
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }
    
    public String getPromotionalCopy() {
        return promotionalCopy;
    }

    public void setPromotionalCopy(String promotionalCopy) {
        this.promotionalCopy = promotionalCopy;
    }
    
    public String getServiceAreaDescription() {
        return serviceAreaDescription;
    }

    public void setServiceAreaDescription(String serviceAreaDescription) {
        this.serviceAreaDescription = serviceAreaDescription;
    }
    
    public String getRegistAddressDescription() {
        return registAddressDescription;
    }

    public void setRegistAddressDescription(String registAddressDescription) {
        this.registAddressDescription = registAddressDescription;
    }
    
    public String getServicePeriodDescription() {
        return servicePeriodDescription;
    }

    public void setServicePeriodDescription(String servicePeriodDescription) {
        this.servicePeriodDescription = servicePeriodDescription;
    }
    
    public String getBuyCountDescription() {
        return buyCountDescription;
    }

    public void setBuyCountDescription(String buyCountDescription) {
        this.buyCountDescription = buyCountDescription;
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