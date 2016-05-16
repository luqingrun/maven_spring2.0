package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.module.sys.cms.base.entity.CMSBase;

import java.util.Date;


public class CmsSpecialActive extends CMSBase {

    private static final long serialVersionUID = -1L;

    /** 描述信息 */
    private String description;
    
    /** 活动时间 */
    private Date activeTime;
    
    /** 链接地址 */
    private String url;
    
    /** 图片url */
    private String img;
    private String imgBig;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
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

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }
}