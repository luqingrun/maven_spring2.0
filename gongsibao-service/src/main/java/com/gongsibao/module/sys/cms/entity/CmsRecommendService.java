package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.module.sys.cms.base.entity.CMSBase;


public class CmsRecommendService extends CMSBase {

    public static final int PRICE_TYPE0 = 0;
    public static final int PRICE_TYPE1 = 0;
    public static final int PRICE_TYPE2 = 0;

    private static final long serialVersionUID = -1L;

    /** 推荐分类id */
    private Integer categoryId;

    /** 描述信息 */
    private String description;

    /** 价格 */
    private String price;
    /** 价格类型 0 面议，1 价格xx 2 价格xx起 */
    private Integer priceType;

    /** 链接地址 */
    private String url;

    /** 图片url */
    private String img;

    /** 排序 */
    private Integer sort;

    private CmsRecommendCategory recommendCategory;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public CmsRecommendCategory getRecommendCategory() {
        return recommendCategory;
    }

    public void setRecommendCategory(CmsRecommendCategory recommendCategory) {
        this.recommendCategory = recommendCategory;
    }
}