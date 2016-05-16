package com.gongsibao.module.sys.bdconfig.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdconfig.dao.BdConfigDao;
import com.gongsibao.module.sys.bdconfig.entity.BdConfig;
import com.gongsibao.module.sys.bdconfig.service.BdConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("bdConfigService")
public class BdConfigServiceImpl implements BdConfigService {
    @Autowired
    private BdConfigDao bdConfigDao;

    @Override
    public BdConfig findById(Integer pkid) {
        return bdConfigDao.findById(pkid);
    }

    @Override
    public int update(BdConfig bdConfig) {
        return bdConfigDao.update(bdConfig);
    }

    @Override
    public int delete(Integer pkid) {
        return bdConfigDao.delete(pkid);
    }

    @Override
    public Integer insert(BdConfig bdConfig) {
        return bdConfigDao.insert(bdConfig);
    }

    @Override
    public List<BdConfig> findByIds(List<Integer> pkidList) {
        return bdConfigDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, BdConfig> findMapByIds(List<Integer> pkidList) {
        List<BdConfig> list = findByIds(pkidList);
        Map<Integer, BdConfig> map = new HashMap<>();
        for(BdConfig bdConfig : list){
            map.put(bdConfig.getPkid(), bdConfig);
        }
        return map;
    }

    @Override
    public Pager<BdConfig> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = bdConfigDao.countByProperties(map);
        Pager<BdConfig> pager = new Pager<>(totalRows, page);
        List<BdConfig> bdConfigList = bdConfigDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(bdConfigList);
        return pager;
    }

}