package com.gongsibao.module.product.prodproductcmstemplate.entity;

import com.gongsibao.common.db.BaseEntity;
import com.gongsibao.common.util.security.SecurityUtils;

import java.util.List;

/**
 * Created by a on 2016/5/4.
 */
public class ProductCmsTemplateCity {
    /*城市区域id*/
    private Integer cityId;
    /*城市区域id(加密)*/
    private String cityIdStr;
    /*城市区域名称*/
    private String cityName;
    /*该区域在该产品下是否已经设置了该模板（0:(没有:默认不选中),1:(有:默认选中)）*/
    private Integer isHasTemplate;
    /*子集区域*/
    private List<ProductCmsTemplateCity> children;

    public String getCityIdStr() {
        return getCityId() == null || getCityId() == 0 ? cityIdStr : SecurityUtils.rc4Encrypt(getCityId());
    }

    public void setCityIdStr(String cityIdStr) {
        this.cityIdStr = cityIdStr;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getIsHasTemplate() {
        return isHasTemplate;
    }

    public void setIsHasTemplate(Integer isHasTemplate) {
        this.isHasTemplate = isHasTemplate;
    }

    public List<ProductCmsTemplateCity> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCmsTemplateCity> children) {
        this.children = children;
    }
}
