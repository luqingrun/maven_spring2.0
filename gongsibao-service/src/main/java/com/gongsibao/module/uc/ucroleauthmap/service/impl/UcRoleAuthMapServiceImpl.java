package com.gongsibao.module.uc.ucroleauthmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucroleauthmap.dao.UcRoleAuthMapDao;
import com.gongsibao.module.uc.ucroleauthmap.entity.UcRoleAuthMap;
import com.gongsibao.module.uc.ucroleauthmap.service.UcRoleAuthMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ucRoleAuthMapService")
public class UcRoleAuthMapServiceImpl implements UcRoleAuthMapService {
    @Autowired
    private UcRoleAuthMapDao ucRoleAuthMapDao;

    @Override
    public UcRoleAuthMap findById(Integer pkid) {
        return ucRoleAuthMapDao.findById(pkid);
    }

    @Override
    public int update(UcRoleAuthMap ucRoleAuthMap) {
        return ucRoleAuthMapDao.update(ucRoleAuthMap);
    }

    @Override
    public int delete(Integer pkid) {
        return ucRoleAuthMapDao.delete(pkid);
    }

    @Override
    public Integer insert(UcRoleAuthMap ucRoleAuthMap) {
        return ucRoleAuthMapDao.insert(ucRoleAuthMap);
    }

    @Override
    public List<UcRoleAuthMap> findByIds(List<Integer> pkidList) {
        return ucRoleAuthMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcRoleAuthMap> findMapByIds(List<Integer> pkidList) {
        List<UcRoleAuthMap> list = findByIds(pkidList);
        Map<Integer, UcRoleAuthMap> map = new HashMap<>();
        for(UcRoleAuthMap ucRoleAuthMap : list){
            map.put(ucRoleAuthMap.getPkid(), ucRoleAuthMap);
        }
        return map;
    }

    @Override
    public Pager<UcRoleAuthMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucRoleAuthMapDao.countByProperties(map);
        Pager<UcRoleAuthMap> pager = new Pager<>(totalRows, page);
        List<UcRoleAuthMap> ucRoleAuthMapList = ucRoleAuthMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucRoleAuthMapList);
        return pager;
    }

}