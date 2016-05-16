package com.gongsibao.module.sys.bdexpresscourier.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdexpresscourier.dao.BdExpressCourierDao;
import com.gongsibao.module.sys.bdexpresscourier.entity.BdExpressCourier;
import com.gongsibao.module.sys.bdexpresscourier.service.BdExpressCourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("bdExpressCourierService")
public class BdExpressCourierServiceImpl implements BdExpressCourierService {
    @Autowired
    private BdExpressCourierDao bdExpressCourierDao;

    @Override
    public BdExpressCourier findById(Integer pkid) {
        return bdExpressCourierDao.findById(pkid);
    }

    @Override
    public int update(BdExpressCourier bdExpressCourier) {
        return bdExpressCourierDao.update(bdExpressCourier);
    }

    @Override
    public int delete(Integer pkid) {
        return bdExpressCourierDao.delete(pkid);
    }

    @Override
    public Integer insert(BdExpressCourier bdExpressCourier) {
        return bdExpressCourierDao.insert(bdExpressCourier);
    }

    @Override
    public List<BdExpressCourier> findByIds(List<Integer> pkidList) {
        return bdExpressCourierDao.findByIds(pkidList);
    }

    @Override
    public  List<BdExpressCourier> findAllBdExpressCourier() {return  bdExpressCourierDao.findAllBdExpressCourier(); }

    @Override
    public Map<Integer, BdExpressCourier> findMapByIds(List<Integer> pkidList) {
        List<BdExpressCourier> list = findByIds(pkidList);
        Map<Integer, BdExpressCourier> map = new HashMap<>();
        for(BdExpressCourier bdExpressCourier : list){
            map.put(bdExpressCourier.getPkid(), bdExpressCourier);
        }
        return map;
    }

    @Override
    public Pager<BdExpressCourier> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = bdExpressCourierDao.countByProperties(map);
        Pager<BdExpressCourier> pager = new Pager<>(totalRows, page);
        List<BdExpressCourier> bdExpressCourierList = bdExpressCourierDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(bdExpressCourierList);
        return pager;
    }

}