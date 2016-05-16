package com.gongsibao.module.product.prodproductcmsrelated.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproduct.dao.ProdProductDao;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodproductcmsrelated.dao.ProdProductCmsRelatedDao;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;
import com.gongsibao.module.product.prodproductcmsrelated.service.ProdProductCmsRelatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodProductCmsRelatedService")
public class ProdProductCmsRelatedServiceImpl implements ProdProductCmsRelatedService {
    @Autowired
    private ProdProductCmsRelatedDao prodProductCmsRelatedDao;
    @Autowired
    private ProdProductDao prodProductDao;

    @Override
    public ProdProductCmsRelated findById(Integer pkid) {
        return prodProductCmsRelatedDao.findById(pkid);
    }

    @Override
    public int update(ProdProductCmsRelated prodProductCmsRelated) {
        return prodProductCmsRelatedDao.update(prodProductCmsRelated);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductCmsRelatedDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProductCmsRelated prodProductCmsRelated) {
        return prodProductCmsRelatedDao.insert(prodProductCmsRelated);
    }

    @Override
    public List<ProdProductCmsRelated> findByIds(List<Integer> pkidList) {
        return prodProductCmsRelatedDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProductCmsRelated> findMapByIds(List<Integer> pkidList) {
        List<ProdProductCmsRelated> list = findByIds(pkidList);
        Map<Integer, ProdProductCmsRelated> map = new HashMap<>();
        for (ProdProductCmsRelated prodProductCmsRelated : list) {
            map.put(prodProductCmsRelated.getPkid(), prodProductCmsRelated);
        }
        return map;
    }

    @Override
    public Pager<ProdProductCmsRelated> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductCmsRelatedDao.countByProperties(map);
        Pager<ProdProductCmsRelated> pager = new Pager<>(totalRows, page);
        List<ProdProductCmsRelated> prodProductCmsRelatedList = prodProductCmsRelatedDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductCmsRelatedList);
        return pager;
    }

    @Override
    public Pager<ProdProduct> getProdNotRelated(Integer prodpkid, int page, int pageSize) {
        int totalRows = prodProductDao.findByProdIdCount(prodpkid);
        Pager<ProdProduct> pager = new Pager<>(totalRows, page);
        List<ProdProduct> rows = prodProductDao.findByProdIdPager(prodpkid, pager.getStartRow(), pageSize);
        pager.setList(rows);
        return pager;
    }
}