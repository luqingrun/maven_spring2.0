package com.gongsibao.module.sys.cms.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wk on 2016/4/20.
 */
public class CmsSpecialMonth implements Serializable {

    private String month;

    List<CmsSpecialActive> activeList;

    public List<CmsSpecialActive> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<CmsSpecialActive> activeList) {
        this.activeList = activeList;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
