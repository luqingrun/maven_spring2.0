package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsBanner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsBannerService {

    CmsBanner findById(Integer pkid);

    int update(CmsBanner cmsBanner);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsBanner cmsBanner);

    List<CmsBanner> findByIds(List<Integer> pkidList);

    Map<Integer, CmsBanner> findMapByIds(List<Integer> pkidList);

    Pager<CmsBanner> pageByProperties(Map<String, Object> properties, int page);

    List<CmsBanner> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    List<CmsBanner> getIndexList(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids,Integer userId);
}