package com.gongsibao.module.product.prodprice.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;

import java.util.List;
import java.util.Map;

public interface ProdPriceService {

    ProdPrice findById(Integer pkid);

    int update(ProdPrice prodPrice);

    int delete(Integer pkid);

    Integer insert(ProdPrice prodPrice);

    List<ProdPrice> findByIds(List<Integer> pkidList);

    Map<Integer, ProdPrice> findMapByIds(List<Integer> pkidList);

    List<ProdPrice> findByAuditId(Integer auditId);

    List<ProdPrice> findByAuditIdAndSaleState(Integer auditId, Integer isOnSale);

    List<ProdPrice> findByAuditIdAndCityID(Integer auditId, List cityId, Integer isOnSale);

    int editProdPriceWithIsOnSale(List<Integer> allPkis, Integer isOnSale);

    Pager<ProdPrice> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 通过prod_price_id 获取销售区域*/
    List<Integer> findCityIdsByAuditId(Integer pkid);

    /**通过prod_price_id 获取产品服务*/
    List<Integer> findServiceIdsByAuditId(Integer pkid);

    Boolean findIsMustByServiceIdAndAuditId(Integer pkid, Integer serviceId);

    ProdPrice findOnSaleProdPriceBy(Integer serviceId, Integer cityId);

    List<ProdPrice> findByCityIdAndProductId(Integer cityId, Integer productId);
}