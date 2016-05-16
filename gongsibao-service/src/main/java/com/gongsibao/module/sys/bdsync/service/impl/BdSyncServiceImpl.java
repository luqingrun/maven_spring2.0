package com.gongsibao.module.sys.bdsync.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdsync.dao.BdSyncDao;
import com.gongsibao.module.sys.bdsync.entity.BdSync;
import com.gongsibao.module.sys.bdsync.service.BdSyncService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("bdSyncService")
public class BdSyncServiceImpl implements BdSyncService {
    @Autowired
    private BdSyncDao bdSyncDao;

    @Override
    public BdSync findById(Integer pkid) {
        return bdSyncDao.findById(pkid);
    }

    @Override
    public int update(BdSync bdSync) {
        return bdSyncDao.update(bdSync);
    }

    @Override
    public int delete(Integer pkid) {
        return bdSyncDao.delete(pkid);
    }

    @Override
    public Integer insert(BdSync bdSync) {
        return bdSyncDao.insert(bdSync);
    }

    @Override
    public List<BdSync> findByIds(List<Integer> pkidList) {
        return bdSyncDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, BdSync> findMapByIds(List<Integer> pkidList) {
        List<BdSync> list = findByIds(pkidList);
        Map<Integer, BdSync> map = new HashMap<>();
        for(BdSync bdSync : list){
            map.put(bdSync.getPkid(), bdSync);
        }
        return map;
    }

    @Override
    public Pager<BdSync> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = bdSyncDao.countByProperties(map);
        Pager<BdSync> pager = new Pager<>(totalRows, page);
        List<BdSync> bdSyncList = bdSyncDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(bdSyncList);
        return pager;
    }

    @Override
    public BdSync findByMPkidAndTableName(Integer mpkid, String tableName)  {
        Map<String, Object> map = new HashMap<>();
        map.put("m_pkid", 1);
        map.put("table_name", tableName);
//        map.put("m_is_deleted", 0);
//        map.put("s_is_deleted", 0);
//        map.put("all_is_deleted", 0);

        List<BdSync> list = bdSyncDao.findByProperties(map, 0, 1);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

}