package com.gongsibao.module.sys.cms.entity;

import java.util.Date;


public class CmsRecommendPackage extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 名称
     */
    private String name;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 图标url
     */
    private String img;

    /**
     * 图标url
     */
    private String imgFocus;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 0INIT, 1发布, 2:删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 创建人
     */
    private Integer addUser;

    /**
     * 修改时间
     */
    private Date updTime;

    /**
     * 修改人
     */
    private Integer updUser;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImgFocus() {
        return imgFocus;
    }

    public void setImgFocus(String imgFocus) {
        this.imgFocus = imgFocus;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public Integer getUpdUser() {
        return updUser;
    }

    public void setUpdUser(Integer updUser) {
        this.updUser = updUser;
    }


}