package com.gongsibao.module.sys.bdsync.entity;

import java.util.Date;


public class BdSync extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    private Integer pkid;

    /** 表名，以mysql为主标识 */
    private String tableName;
    
    /** mysql主键 */
    private Integer mPkid;
    
    /** sqlserver 主键ID，GUID */
    private String sId;
    
    /** sqlserver SID，int */
    private Integer sSid;
    
    /** 创建时间 */
    private Date addTime;
    
    /** mysql ¨最后更新时间 */
    private Date mLastUpdateTime;
    
    /** sqlserver 最后创建时间 */
    private Date sLastUpdateTime;
    
    /** 是否需要同步 */
    private Integer syncStatus;

    @Override
    public Integer getPkid() {
        return pkid;
    }

    @Override
    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getmPkid() {
        return mPkid;
    }

    public void setmPkid(Integer mPkid) {
        this.mPkid = mPkid;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public Integer getsSid() {
        return sSid;
    }

    public void setsSid(Integer sSid) {
        this.sSid = sSid;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getmLastUpdateTime() {
        return mLastUpdateTime;
    }

    public void setmLastUpdateTime(Date mLastUpdateTime) {
        this.mLastUpdateTime = mLastUpdateTime;
    }

    public Date getsLastUpdateTime() {
        return sLastUpdateTime;
    }

    public void setsLastUpdateTime(Date sLastUpdateTime) {
        this.sLastUpdateTime = sLastUpdateTime;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }
}