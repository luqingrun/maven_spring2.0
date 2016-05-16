package com.gongsibao.module.order.soorderitemusermap.entity;

import java.util.Date;


public class SoOrderItemUserMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 用户序号 */
    private Integer userId;
    
    /** 订单项序号 */
    private Integer orderItemId;
    
    /** 关系类型序号，type=306，3061业务、3062客服（关注）、3063操作 */
    private Integer typeId;
    
    /** 订单项和用户关系状态，type=314，3141正在负责、3142曾经负责 */
    private Integer statusId;
    
    /** 创建时间 */
    private Date addTime;
    

    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    

}