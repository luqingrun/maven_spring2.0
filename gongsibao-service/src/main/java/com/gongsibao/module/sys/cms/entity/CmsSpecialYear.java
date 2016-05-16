package com.gongsibao.module.sys.cms.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wk on 2016/4/20.
 */
public class CmsSpecialYear implements Serializable {

    private String year;

    private Integer total;

    List<CmsSpecialMonth> monthList;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<CmsSpecialMonth> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<CmsSpecialMonth> monthList) {
        this.monthList = monthList;
    }
}
