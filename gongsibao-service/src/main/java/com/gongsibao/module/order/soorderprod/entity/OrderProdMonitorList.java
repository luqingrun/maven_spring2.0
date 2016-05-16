package com.gongsibao.module.order.soorderprod.entity;

import java.util.Date;

/**
 * Created by a on 2016/5/4.
 */
public class OrderProdMonitorList extends SoOrderProd {

    /***
     * 订单编号
     */
    private String orderNo;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 是否加急
     */
    private Integer isUrgeney;

    /***
     * 业务员所在组织机构名称字符串
     */
    private String organizationNames;

    /***
     * 是否关注0:没关注1:已关注
     */
    private int isFollow = 0;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getIsUrgeney() {
        return isUrgeney;
    }

    public void setIsUrgeney(Integer isUrgeney) {
        this.isUrgeney = isUrgeney;
    }

    public String getOrganizationNames() {
        return organizationNames;
    }

    public void setOrganizationNames(String organizationNames) {
        this.organizationNames = organizationNames;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }
}
