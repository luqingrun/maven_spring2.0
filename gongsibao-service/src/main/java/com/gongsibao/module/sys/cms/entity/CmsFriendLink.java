package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.module.sys.cms.base.entity.CMSBase;


public class CmsFriendLink extends CMSBase {

    private static final long serialVersionUID = -1L;

    public static final Integer TYPE_WORD = 0;
    public static final Integer TYPE_IMAGE = 1;

    /**
     * 类型 0文字, 1图片
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 图片url
     */
    private String img;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 屏蔽百度蜘蛛 0否, 1是
     */
    private Integer spider;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getSpider() {
        return spider;
    }

    public void setSpider(Integer spider) {
        this.spider = spider;
    }

}