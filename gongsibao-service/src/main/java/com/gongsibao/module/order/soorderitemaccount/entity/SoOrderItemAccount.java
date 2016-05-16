package com.gongsibao.module.order.soorderitemaccount.entity;

import java.util.Date;


public class SoOrderItemAccount extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项序号 */
    private Integer orderItemId;
    
    /** 帐号 */
    private String account;
    
    /** 密码 */
    private String passwd;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 备注 */
    private String remark;
    

    
    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    

}