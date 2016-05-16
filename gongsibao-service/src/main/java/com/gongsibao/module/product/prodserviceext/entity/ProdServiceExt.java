package com.gongsibao.module.product.prodserviceext.entity;

import java.util.Date;


public class ProdServiceExt extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品服务序号 */
    private Integer serviceId;
    
    /** 前台显示1；后台定价显示2；结转显示4 */
    private Integer showTypy;
    

    
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    
    public Integer getShowTypy() {
        return showTypy;
    }

    public void setShowTypy(Integer showTypy) {
        this.showTypy = showTypy;
    }
    

}