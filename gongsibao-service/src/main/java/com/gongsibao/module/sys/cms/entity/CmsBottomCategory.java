package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.common.db.BaseEntity;

/**
 * Created by wk on 2016/4/20.
 */
public class CmsBottomCategory extends BaseEntity {

    private static final long serialVersionUID = -266654098973656757L;

    private Integer pkid;
    private String name;

    public CmsBottomCategory() {
    }

    public CmsBottomCategory(Integer pkid, String name) {
        this.name = name;
        this.pkid = pkid;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
