package com.gongsibao.module.uc.ucuserrolemap.entity;

import java.util.Date;


public class UcUserRoleMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 用户id */
    private Integer userId;
    
    /** 角色id */
    private Integer roleId;
    
    /** 能否传递 */
    private Integer canPass;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    

    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    
    public Integer getCanPass() {
        return canPass;
    }

    public void setCanPass(Integer canPass) {
        this.canPass = canPass;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }
    

}