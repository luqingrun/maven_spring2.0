package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsRecommendService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsRecommendServiceService {

    CmsRecommendService findById(Integer pkid);

    int update(CmsRecommendService cmsRecommendService);

    int updateInfo(CmsRecommendService cmsRecommendService);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsRecommendService cmsRecommendService);

    List<CmsRecommendService> findByIds(List<Integer> pkidList);

    Map<Integer, CmsRecommendService> findMapByIds(List<Integer> pkidList);

    Pager<CmsRecommendService> pageByProperties(Map<String, Object> properties, int page);

    Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId);

    void editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId);

    List<CmsRecommendService> listByProperties(Map<String, Object> properties);

    Map<Integer, List<CmsRecommendService>> getMapByCategoryIds(Collection<Integer> categoryIds, Integer... status);
}