package com.gongsibao.module.sys.cms.base.entity;

import com.gongsibao.common.db.BaseEntity;

import java.util.Date;

/**
 * Created by wk on 2016/4/7.
 */
public class CMSBase extends BaseEntity {
    // 初始化
    public static final Integer STATUS_INIT = 0;
    // 发布
    public static final Integer STATUS_PUBLISH = 1;
    // 删除
    public static final Integer STATUS_DEL = 2;
    // 可见状态
    public static final Integer[] STATUS_SHOW = new Integer[]{STATUS_INIT, STATUS_PUBLISH};

    /** banner名称 */
    private String name;

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
