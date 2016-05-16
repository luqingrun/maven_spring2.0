package com.gongsibao.module.uc.ucaccount.entity;

import java.util.Date;


public class UcAccount extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /** 登录名 */
    private String name;

    /** 密码 */
    private String passwd;

    /** 邮箱 */
    private String email;

    /** 手机号码 */
    private String mobilePhone;

    /** 头像图片文件序号 */
    private Integer headThumbFileId;

    /** 尊称 */
    private String realName;

    /** 注册客户端序号，type=6 */
    private Integer sourceClientId;

    /** 创建时间 */
    private Date addTime;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getHeadThumbFileId() {
        return headThumbFileId;
    }

    public void setHeadThumbFileId(Integer headThumbFileId) {
        this.headThumbFileId = headThumbFileId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSourceClientId() {
        return sourceClientId;
    }

    public void setSourceClientId(Integer sourceClientId) {
        this.sourceClientId = sourceClientId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }


}