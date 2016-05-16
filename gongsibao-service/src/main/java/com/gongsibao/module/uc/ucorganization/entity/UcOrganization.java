package com.gongsibao.module.uc.ucorganization.entity;

import com.gongsibao.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class  UcOrganization extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 父序号，默认0
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 主管人
     */
    private Integer leaderId;
    private String leaderName;

    /**
     * 地区序号，type=101
     */
    private Integer cityId;
    private String cityName;

    /**
     * 排序
     */
    private Double sort;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 是否叶子节点
     */
    private Integer isLeaf = 0;

    /**
     * 是否启用
     */
    private Integer isEnabled = 1;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 说明
     */
    private String remark;

    /**
     * 父节点
     */
    private UcOrganization parent;

    /**
     * 子节点
     */
    private List<UcOrganization> childrenList = new ArrayList<>();

    /**
     * 是否可用
     */
    private Integer isUse = 1;

    private List<Integer> categoryIds;      // 服务产品分类ids
    private List<Integer> cityIds;          // 服务地区ids

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Double getSort() {
        return null == sort ? 0d : sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
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

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public String getRemark() {
        return StringUtils.trimToEmpty(remark);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UcOrganization getParent() {
        return parent;
    }

    public void setParent(UcOrganization parent) {
        this.parent = parent;
    }

    public List<UcOrganization> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<UcOrganization> childrenList) {
        this.childrenList = childrenList;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Integer> getCityIds() {
        return cityIds;
    }

    public void setCityIds(List<Integer> cityIds) {
        this.cityIds = cityIds;
    }
}