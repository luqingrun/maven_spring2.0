package com.gongsibao.module.product.prodpriceaudit.entity;

import com.gongsibao.common.db.BaseEntity;
import com.gongsibao.module.product.prodprice.entity.ProdPriceRequest;

import java.util.List;

/**
 * Created by huoquanfu on 2016/4/20.
 */
public class ProdPriceAuditRequest extends ProdPriceAuditRequestBase {

    public List<ProdPriceRequest> getProdServices() {
        return prodServices;
    }

    public void setProdServices(List<ProdPriceRequest> prodServices) {
        this.prodServices = prodServices;
    }

    public List<String> getCityAreaIdStrs() {
        return cityAreaIdStrs;
    }

    public void setCityAreaIdStrs(List<String> cityAreaIdStrs) {
        this.cityAreaIdStrs = cityAreaIdStrs;
    }

    public List<Integer> getCityAreaIds() {
        return cityAreaIds;
    }

    public void setCityAreaIds(List<Integer> cityAreaIds) {
        this.cityAreaIds = cityAreaIds;
    }

    List<ProdPriceRequest> prodServices;

    List<String> cityAreaIdStrs;

    List<Integer> cityAreaIds;

}
