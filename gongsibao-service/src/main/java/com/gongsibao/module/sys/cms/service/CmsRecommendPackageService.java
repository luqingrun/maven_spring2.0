package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsRecommendPackage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsRecommendPackageService {

    CmsRecommendPackage findById(Integer pkid);

    int update(CmsRecommendPackage cmsRecommendPackage);

    int delete(Integer pkid, Integer userIdQ);

    Integer insert(CmsRecommendPackage cmsRecommendPackage);

    List<CmsRecommendPackage> findByIds(List<Integer> pkidList);

    Map<Integer, CmsRecommendPackage> findMapByIds(List<Integer> pkidList);

    Pager<CmsRecommendPackage> pageByProperties(Map<String, Object> properties, int page);

    List<CmsRecommendPackage> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    List<CmsRecommendPackage> getIndexList(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids, Integer userId);
}