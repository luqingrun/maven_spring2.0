package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;

import java.util.List;


public class CmsTopnavCategory extends CMSBase {

    private static final long serialVersionUID = -1L;

    /**
     * 父id
     */
    private Integer pid;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父类别
     */
    private CmsTopnavCategory parentCategory;

    /**
     * 子类别
     */
    private List<CmsTopnavCategory> childCategories;

    private List<CmsTopnavLink> topnavLinks;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public CmsTopnavCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CmsTopnavCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<CmsTopnavCategory> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CmsTopnavCategory> childCategories) {
        this.childCategories = childCategories;
    }

    public List<CmsTopnavLink> getTopnavLinks() {
        return topnavLinks;
    }

    public void setTopnavLinks(List<CmsTopnavLink> topnavLinks) {
        this.topnavLinks = topnavLinks;
    }

    public String getPidStr() {
        return SecurityUtils.rc4Encrypt(getPid());
    }
}