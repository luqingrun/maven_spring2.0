package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.module.sys.cms.base.entity.CMSBase;


public class CmsTopnavLink extends CMSBase {

    private static final long serialVersionUID = -1L;

    
    /** 分类id */
    private Integer categoryId;
    
    /** 名称 */
    private String name;
    
    /** 链接地址 */
    private String url;
    
    /** 是否推荐 0否, 1是 */
    private Integer recommend;

    /** 排序 */
    private Integer sort;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}