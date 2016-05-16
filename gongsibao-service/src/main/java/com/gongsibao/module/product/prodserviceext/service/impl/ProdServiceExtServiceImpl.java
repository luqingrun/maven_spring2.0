package com.gongsibao.module.product.prodserviceext.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodserviceext.dao.ProdServiceExtDao;
import com.gongsibao.module.product.prodserviceext.entity.ProdServiceExt;
import com.gongsibao.module.product.prodserviceext.service.ProdServiceExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodServiceExtService")
public class ProdServiceExtServiceImpl implements ProdServiceExtService {
    @Autowired
    private ProdServiceExtDao prodServiceExtDao;

    @Override
    public ProdServiceExt findById(Integer pkid) {
        return prodServiceExtDao.findById(pkid);
    }

    @Override
    public int update(ProdServiceExt prodServiceExt) {
        return prodServiceExtDao.update(prodServiceExt);
    }

    @Override
    public int delete(Integer pkid) {
        return prodServiceExtDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdServiceExt prodServiceExt) {
        return prodServiceExtDao.insert(prodServiceExt);
    }

    @Override
    public List<ProdServiceExt> findByIds(List<Integer> pkidList) {
        return prodServiceExtDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdServiceExt> findMapByIds(List<Integer> pkidList) {
        List<ProdServiceExt> list = findByIds(pkidList);
        Map<Integer, ProdServiceExt> map = new HashMap<>();
        for(ProdServiceExt prodServiceExt : list){
            map.put(prodServiceExt.getPkid(), prodServiceExt);
        }
        return map;
    }

    @Override
    public Pager<ProdServiceExt> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodServiceExtDao.countByProperties(map);
        Pager<ProdServiceExt> pager = new Pager<>(totalRows, page);
        List<ProdServiceExt> prodServiceExtList = prodServiceExtDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodServiceExtList);
        return pager;
    }

}