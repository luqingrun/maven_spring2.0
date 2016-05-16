package com.gongsibao.module.uc.ucauth.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucauth.dao.UcAuthDao;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;
import com.gongsibao.module.uc.ucauth.service.UcAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ucAuthService")
public class UcAuthServiceImpl implements UcAuthService {
    @Autowired
    private UcAuthDao ucAuthDao;

    @Override
    public UcAuth findById(Integer pkid) {
        return ucAuthDao.findById(pkid);
    }

    @Override
    public int update(UcAuth ucAuth) {
        return ucAuthDao.update(ucAuth);
    }

    @Override
    public int delete(Integer pkid) {
        return ucAuthDao.delete(pkid);
    }

    @Override
    public Integer insert(UcAuth ucAuth) {
        return ucAuthDao.insert(ucAuth);
    }

    @Override
    public List<UcAuth> findByIds(List<Integer> pkidList) {
        return ucAuthDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcAuth> findMapByIds(List<Integer> pkidList) {
        List<UcAuth> list = findByIds(pkidList);
        Map<Integer, UcAuth> map = new HashMap<>();
        for(UcAuth ucAuth : list){
            map.put(ucAuth.getPkid(), ucAuth);
        }
        return map;
    }

    @Override
    public Pager<UcAuth> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucAuthDao.countByProperties(map);
        Pager<UcAuth> pager = new Pager<>(totalRows, page);
        List<UcAuth> ucAuthList = ucAuthDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucAuthList);
        return pager;
    }

}