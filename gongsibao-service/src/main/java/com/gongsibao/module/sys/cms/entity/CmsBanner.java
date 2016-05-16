package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.module.sys.cms.base.entity.CMSBase;

public class CmsBanner extends CMSBase {

    private static final long serialVersionUID = -1L;

    /** 描述信息 */
    private String description;

    /** 颜色方案 0深色, 1浅色 */
    private Integer color;

    /** 链接地址 */
    private String url;

    /** 图片url */
    private String img;

    /** 排序 */
    private Integer sort;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
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
}