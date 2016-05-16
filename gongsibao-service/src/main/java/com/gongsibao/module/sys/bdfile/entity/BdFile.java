package com.gongsibao.module.sys.bdfile.entity;

import com.gongsibao.common.util.StringUtils;

import java.util.Date;


public class BdFile extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /** 业务表名 */
    private String tabName;

    /** 表单序号 */
    private Integer formId;

    /** 名称 */
    private String name;
    
    /** 路径 */
    private String url;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getName() {
        return StringUtils.trimToEmpty(name);
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }
    

}