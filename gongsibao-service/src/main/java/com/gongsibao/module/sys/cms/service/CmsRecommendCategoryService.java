package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsRecommendCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsRecommendCategoryService {

    CmsRecommendCategory findById(Integer pkid);

    int update(CmsRecommendCategory cmsRecommendCategory);

    int updateInfo(CmsRecommendCategory cmsRecommendCategory);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsRecommendCategory cmsRecommendCategory);

    List<CmsRecommendCategory> findByIds(List<Integer> pkidList);

    Map<Integer, CmsRecommendCategory> findMapByIds(List<Integer> pkidList);

    Pager<CmsRecommendCategory> pageByProperties(Map<String, Object> properties, int page);

    List<CmsRecommendCategory> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    List<CmsRecommendCategory> getIndexList(Map<String, Object> properties);

    void editPublish(Collection<Integer> ids, Integer userId);
}