package com.gongsibao.module.order.soorderprodorganizationmap.entity;

import java.util.Date;


public class SoOrderProdOrganizationMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品订单序号 */
    private Integer orderProdId;
    
    /** 组织序号 */
    private Integer organizationId;
    
    /** 创建时间 */
    private Date addTime;
    

    
    public Integer getOrderProdId() {
        return orderProdId;
    }

    public void setOrderProdId(Integer orderProdId) {
        this.orderProdId = orderProdId;
    }
    
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    

}