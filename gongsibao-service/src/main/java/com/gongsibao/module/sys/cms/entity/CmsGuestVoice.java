package com.gongsibao.module.sys.cms.entity;

import java.util.Date;


public class CmsGuestVoice extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 名称 */
    private String name;
    
    /** 客户姓名 */
    private String guestName;
    
    /** 客户职位 */
    private String guestPort;
    
    /** 客户评价 */
    private String guestEvaluation;
    
    /** 图片url */
    private String img;
    
    /** 排序 */
    private Integer sort;
    
    /** 屏蔽百度蜘蛛 0否, 1是 */
    private Integer spider;
    
    /** 状态 0INIT, 1发布, 2:删除 */
    private Integer status;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 创建人 */
    private Integer addUser;
    
    /** 修改时间 */
    private Date updTime;
    
    /** 修改人 */
    private Integer updUser;
    

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    public String getGuestPort() {
        return guestPort;
    }

    public void setGuestPort(String guestPort) {
        this.guestPort = guestPort;
    }
    
    public String getGuestEvaluation() {
        return guestEvaluation;
    }

    public void setGuestEvaluation(String guestEvaluation) {
        this.guestEvaluation = guestEvaluation;
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