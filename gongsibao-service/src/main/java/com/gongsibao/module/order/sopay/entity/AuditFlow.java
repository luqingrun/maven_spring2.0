package com.gongsibao.module.order.sopay.entity;

/**
 * Created by xuminglang on 2016/4/28.
 */
public class AuditFlow {
    public AuditFlow(String userPkidStr,String userRealName,int level,int auditStatusId){
        this.userPkidStr = userPkidStr;
        this.userRealName = userRealName;
        this.level = level;
        this.auditStatusId = auditStatusId;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserPkidStr() {
        return userPkidStr;
    }

    public void setUserPkidStr(String userPkidStr) {
        this.userPkidStr = userPkidStr;
    }

    public int getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(int auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    private String userPkidStr;
    private String userRealName;
    private int level;
    private int auditStatusId;


}
