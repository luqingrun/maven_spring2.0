package com.gongsibao.module.order.soorderproditem.entity;

import java.io.Serializable;

/**
 * Created by jko on 2016/5/9.
 */
public class IteamServerInfo implements Serializable {

    private static final long serialVersionUID = -1268802638769774598L;

    private Integer pkId;
    private String IteamServerName;
    private String IteamServerUnit;
    private String IteamServerFeature;

    public String getIteamServerName() {
        return IteamServerName;
    }

    public void setIteamServerName(String iteamServerName) {
        IteamServerName = iteamServerName;
    }

    public String getIteamServerFeature() {
        return IteamServerFeature;
    }

    public void setIteamServerFeature(String iteamServerFeature) {
        IteamServerFeature = iteamServerFeature;
    }

    public String getIteamServerUnit() {
        return IteamServerUnit;
    }

    public void setIteamServerUnit(String iteamServerUnit) {
        IteamServerUnit = iteamServerUnit;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }
}
