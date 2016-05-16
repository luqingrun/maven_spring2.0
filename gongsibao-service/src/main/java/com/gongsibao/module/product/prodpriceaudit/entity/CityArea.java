package com.gongsibao.module.product.prodpriceaudit.entity;

/**
 * Created by huoquanfu on 2016/4/18.
 */
public class CityArea extends   com.gongsibao.common.db.BaseEntity  {

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private Integer parentId;
    private String name;
    private String fullName;

}
