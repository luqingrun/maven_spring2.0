package com.gongsibao.module.product.prodproductcmsrelated.entity;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.security.SecurityUtils;

import java.util.Date;


public class ProdProductCmsRelated extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 产品序号
     */
    private Integer productId;

    /**
     * 推荐产品序号
     */
    private Integer recommendProductId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 推荐产品名称
     */
    private String recommendProductName;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 排序编号
     */
    private Double sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加时间
     */
    private Date addTime;


    /**
     * 推荐产品id的加密值
     */
    private String recommendProductIdStr;

    public String getRecommendProductIdStr() {
        return getRecommendProductId() == null || getRecommendProductId() == 0 ? recommendProductIdStr : SecurityUtils.rc4Encrypt(getRecommendProductId());
    }

    public void setRecommendProductIdStr(String recommendProductIdStr) {
        this.recommendProductIdStr = recommendProductIdStr;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getRecommendProductId() {
        return recommendProductId;
    }

    public void setRecommendProductId(Integer recommendProductId) {
        this.recommendProductId = recommendProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRecommendProductName() {
        return recommendProductName;
    }

    public void setRecommendProductName(String recommendProductName) {
        this.recommendProductName = recommendProductName;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
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