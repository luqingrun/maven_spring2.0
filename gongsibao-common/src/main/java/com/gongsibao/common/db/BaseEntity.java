package com.gongsibao.common.db;


import com.gongsibao.common.util.security.SecurityUtils;

public abstract class BaseEntity implements java.io.Serializable {
    /** 主键ID */
    protected Integer pkid;

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public String getPkidStr(){
        return SecurityUtils.rc4Encrypt(getPkid());
    }

}
