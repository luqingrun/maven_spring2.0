package com.gongsibao.module.product.prodprice.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodprice.dao.ProdPriceDao;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodprice.service.ProdPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodPriceService")
public class ProdPriceServiceImpl implements ProdPriceService {
    @Autowired
    private ProdPriceDao prodPriceDao;

    @Override
    public ProdPrice findById(Integer pkid) {
        return prodPriceDao.findById(pkid);
    }

    @Override
    public int update(ProdPrice prodPrice) {
        return prodPriceDao.update(prodPrice);
    }

    @Override
    public int delete(Integer pkid) {
        return prodPriceDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdPrice prodPrice) {
        return prodPriceDao.insert(prodPrice);
    }

    @Override
    public List<ProdPrice> findByIds(List<Integer> pkidList) {
        return prodPriceDao.findByIds(pkidList);
    }

    @Override
    public List<ProdPrice> findByAuditIdAndSaleState(Integer auditId, Integer isOnSale) {
        return prodPriceDao.findByAuditIdAndSaleState(auditId, isOnSale);
    }

    @Override
    public List<ProdPrice> findByAuditIdAndCityID(Integer auditId, List cityId, Integer isOnSale) {
        return prodPriceDao.findByAuditIdAndCityID(auditId, cityId, 1);
    }

    @Override
    public int editProdPriceWithIsOnSale(List<Integer> allPkis, Integer isOnSale) {
        return prodPriceDao.editProdPriceWithIsOnSale(allPkis, isOnSale);
    }

    @Override
    public Map<Integer, ProdPrice> findMapByIds(List<Integer> pkidList) {
        List<ProdPrice> list = findByIds(pkidList);
        Map<Integer, ProdPrice> map = new HashMap<>();
        for (ProdPrice prodPrice : list) {
            map.put(prodPrice.getPkid(), prodPrice);
        }
        return map;
    }

    @Override
    public List<ProdPrice> findByAuditId(Integer auditId) {
        return prodPriceDao.findByAuditId(auditId);
    }

    @Override
    public Pager<ProdPrice> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodPriceDao.countByProperties(map);
        Pager<ProdPrice> pager = new Pager<>(totalRows, page);
        List<ProdPrice> prodPriceList = prodPriceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodPriceList);
        return pager;
    }

    @Override
    public List<Integer> findCityIdsByAuditId(Integer pkid) {
        return prodPriceDao.findSaleCityIds(pkid);
    }

    @Override
    public List<Integer> findServiceIdsByAuditId(Integer pkid) {
        return prodPriceDao.findServiceIdsByAuditId(pkid);
    }

    @Override
    public Boolean findIsMustByServiceIdAndAuditId(Integer pkid, Integer serviceId) {
        return prodPriceDao.findIsMustByAuditIdAndServiceId(pkid,serviceId);
    }

    @Override
    public ProdPrice findOnSaleProdPriceBy(Integer serviceId, Integer cityId) {
        return prodPriceDao.findOnSaleProdPriceBy(serviceId,cityId);
    }

    @Override
    public List<ProdPrice> findByCityIdAndProductId(Integer cityId, Integer productId) {
        return prodPriceDao.findProdPriceInfoByCityIdAndProductId(cityId,productId);
    }
}