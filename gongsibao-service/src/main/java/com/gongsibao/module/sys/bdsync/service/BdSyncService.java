package com.gongsibao.module.sys.bdsync.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdsync.entity.BdSync;

import java.util.List;
import java.util.Map;

public interface BdSyncService {

    BdSync findById(Integer pkid);

    int update(BdSync bdSync);

    int delete(Integer pkid);

    Integer insert(BdSync bdSync);

    List<BdSync> findByIds(List<Integer> pkidList);

    Map<Integer, BdSync> findMapByIds(List<Integer> pkidList);

    Pager<BdSync> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 根据mysql 用户主键获取sqlserver 用户主键
     * @param mpkid
     * @param tableName
     * @return
     */
    BdSync findByMPkidAndTableName(Integer mpkid, String tableName);
}