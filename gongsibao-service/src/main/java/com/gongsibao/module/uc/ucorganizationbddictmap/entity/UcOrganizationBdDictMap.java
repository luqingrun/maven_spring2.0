package com.gongsibao.module.uc.ucorganizationbddictmap.entity;

import java.util.Date;


public class UcOrganizationBdDictMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 组织机构序号 */
    private Integer organizationId;
    
    /** 字典id */
    private Integer dictId;
    
    /** 关系类型，1服务产品分类、2服务地区 */
    private Integer type;
    

    
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    

}