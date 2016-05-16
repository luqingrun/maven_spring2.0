package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;


public class CmsTitleDesc extends CMSBase {

    private static final long serialVersionUID = -1L;

    /**
     * 描述
     */
    private String description;
    private String keyword;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return StringUtils.trimToEmpty(keyword);
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}