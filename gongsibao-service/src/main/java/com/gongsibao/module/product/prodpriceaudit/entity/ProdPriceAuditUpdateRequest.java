package com.gongsibao.module.product.prodpriceaudit.entity;

import com.gongsibao.module.product.prodprice.entity.ProdPriceRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by huoquanfu on 2016/5/9.
 */
public class ProdPriceAuditUpdateRequest extends ProdPriceAuditRequestBase {


    /**
     * 各地区产品服务的价格
     */
    private Map<String, List<ProdPriceRequest>> cityProdPrices;


    public Map<String, List<ProdPriceRequest>> getCityProdPrices() {
        return cityProdPrices;
    }

    public void setCityProdPrices(Map<String, List<ProdPriceRequest>> cityProdPrices) {
        this.cityProdPrices = cityProdPrices;
    }
}
