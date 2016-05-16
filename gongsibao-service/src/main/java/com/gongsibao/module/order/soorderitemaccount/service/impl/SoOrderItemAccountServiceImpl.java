package com.gongsibao.module.order.soorderitemaccount.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemaccount.dao.SoOrderItemAccountDao;
import com.gongsibao.module.order.soorderitemaccount.entity.SoOrderItemAccount;
import com.gongsibao.module.order.soorderitemaccount.service.SoOrderItemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderItemAccountService")
public class SoOrderItemAccountServiceImpl implements SoOrderItemAccountService {
    @Autowired
    private SoOrderItemAccountDao soOrderItemAccountDao;

    @Override
    public SoOrderItemAccount findById(Integer pkid) {
        return soOrderItemAccountDao.findById(pkid);
    }

    @Override
    public int update(SoOrderItemAccount soOrderItemAccount) {
        return soOrderItemAccountDao.update(soOrderItemAccount);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderItemAccountDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderItemAccount soOrderItemAccount) {
        return soOrderItemAccountDao.insert(soOrderItemAccount);
    }

    @Override
    public List<SoOrderItemAccount> findByIds(List<Integer> pkidList) {
        return soOrderItemAccountDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderItemAccount> findMapByIds(List<Integer> pkidList) {
        List<SoOrderItemAccount> list = findByIds(pkidList);
        Map<Integer, SoOrderItemAccount> map = new HashMap<>();
        for(SoOrderItemAccount soOrderItemAccount : list){
            map.put(soOrderItemAccount.getPkid(), soOrderItemAccount);
        }
        return map;
    }

    @Override
    public Pager<SoOrderItemAccount> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderItemAccountDao.countByProperties(map);
        Pager<SoOrderItemAccount> pager = new Pager<>(totalRows, page);
        List<SoOrderItemAccount> soOrderItemAccountList = soOrderItemAccountDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderItemAccountList);
        return pager;
    }

}