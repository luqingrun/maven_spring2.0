package com.gongsibao.module.product.prodservice.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodservice.dao.ProdServiceDao;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodServiceService")
public class ProdServiceServiceImpl implements ProdServiceService {
    @Autowired
    private ProdServiceDao prodServiceDao;

    @Override
    public ProdService findById(Integer pkid) {
        return prodServiceDao.findById(pkid);
    }

    @Override
    public int update(ProdService prodService) {
        return prodServiceDao.update(prodService);
    }

    @Override
    public int delete(Integer pkid) {
        return prodServiceDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdService prodService) {
        return prodServiceDao.insert(prodService);
    }

    @Override
    public List<ProdService> findByIds(List<Integer> pkidList) {
        return prodServiceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdService> findMapByIds(List<Integer> pkidList) {
        List<ProdService> list = findByIds(pkidList);
        Map<Integer, ProdService> map = new HashMap<>();
        for(ProdService prodService : list){
            map.put(prodService.getPkid(), prodService);
        }
        return map;
    }

    @Override
    public List<ProdService> findByIds(Integer productId) {
        if(productId == null || productId <= 0) {

        }
        return prodServiceDao.findByIds(productId);
    }

    @Override
    public Pager<ProdService> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodServiceDao.countByProperties(map);
        Pager<ProdService> pager = new Pager<>(totalRows, page);
        List<ProdService> prodServiceList = prodServiceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodServiceList);
        return pager;
    }

    public void insertBatch(final List<ProdService> itemList) {
        prodServiceDao.insertBatch(itemList);
    }

    public int updateBatch(final List<ProdService> itemList) {
        return prodServiceDao.updateBatch(itemList);
    }

    public int deleteBatch(final List<ProdService> itemList) {
        return prodServiceDao.deleteBatch(itemList);
    }

    public List<ProdService> findByProperties(Map<String, Object> properties, int start, int pageSize){ return prodServiceDao.findByProperties(properties,start,pageSize);}

}