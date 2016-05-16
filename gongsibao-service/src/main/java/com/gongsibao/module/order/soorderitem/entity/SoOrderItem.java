package com.gongsibao.module.order.soorderitem.entity;

import java.util.Date;


public class SoOrderItem extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /** 订单序号 */
    private Integer orderId;

    /** 产品序号 */
    private Integer productId;

    /** 城市序号 */
    private Integer cityId;

    /** 处理状态序号，type=305 */
    private Integer processStatusId;

    /** 审核状态序号，type=105 */
    private Integer auditStatusId;



    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }


}