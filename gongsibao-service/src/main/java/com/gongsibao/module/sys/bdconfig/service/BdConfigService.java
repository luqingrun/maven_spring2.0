package com.gongsibao.module.sys.bdconfig.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bdconfig.entity.BdConfig;

import java.util.List;
import java.util.Map;

public interface BdConfigService {

    BdConfig findById(Integer pkid);

    int update(BdConfig bdConfig);

    int delete(Integer pkid);

    Integer insert(BdConfig bdConfig);

    List<BdConfig> findByIds(List<Integer> pkidList);

    Map<Integer, BdConfig> findMapByIds(List<Integer> pkidList);

    Pager<BdConfig> pageByProperties(Map<String, Object> properties, int page);
}