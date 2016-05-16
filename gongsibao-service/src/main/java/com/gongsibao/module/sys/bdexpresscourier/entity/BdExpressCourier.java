package com.gongsibao.module.sys.bdexpresscourier.entity;

import java.util.Date;


public class BdExpressCourier extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 名称 */
    private String name;
    
    /** 编码 */
    private String code;
    
    /** 首字母 */
    private String firstLetter;
    
    /** 是否热门 */
    private Integer isHot;
    
    /** 排序 */
    private Double sort;
    
    /** 是否启用 */
    private Integer isEnabled;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 说明 */
    private String remark;
    

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
    
    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }
    
    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    

}