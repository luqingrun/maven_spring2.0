package com.gongsibao.module.uc.ucaccount.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucaccount.dao.UcAccountDao;
import com.gongsibao.module.uc.ucaccount.entity.UcAccount;
import com.gongsibao.module.uc.ucaccount.service.UcAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ucAccountService")
public class UcAccountServiceImpl implements UcAccountService {
    @Autowired
    private UcAccountDao ucAccountDao;

    @Override
    public UcAccount findById(Integer pkid) {
        return ucAccountDao.findById(pkid);
    }

    @Override
    public int update(UcAccount ucAccount) {
        return ucAccountDao.update(ucAccount);
    }

    @Override
    public int delete(Integer pkid) {
        return ucAccountDao.delete(pkid);
    }

    @Override
    public Integer insert(UcAccount ucAccount) {
        return ucAccountDao.insert(ucAccount);
    }

    @Override
    public List<UcAccount> findByIds(List<Integer> pkidList) {
        return ucAccountDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcAccount> findMapByIds(List<Integer> pkidList) {
        List<UcAccount> list = findByIds(pkidList);
        Map<Integer, UcAccount> map = new HashMap<>();
        for(UcAccount ucAccount : list){
            map.put(ucAccount.getPkid(), ucAccount);
        }
        return map;
    }

    @Override
    public Pager<UcAccount> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucAccountDao.countByProperties(map);
        Pager<UcAccount> pager = new Pager<>(totalRows, page);
        List<UcAccount> ucAccountList = ucAccountDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucAccountList);
        return pager;
    }

    @Override
    public UcAccount findByMobile(String mobilePhone) {
        return ucAccountDao.findByMobile(mobilePhone);
    }

}