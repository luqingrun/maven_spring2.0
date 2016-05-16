package com.gongsibao.module.order.soorderprodaccount.entity;

import java.util.Date;


public class RequestOrderProdAccount extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单项序号 */
    private String id;
    
    /** 帐号 */
    private String account;
    
    /** 密码 */
    private String passwd;
    
    /** 备注 */
    private String remark;
    

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    

}