package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsTopnavLink;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsTopnavLinkService {

    CmsTopnavLink findById(Integer pkid);

    int update(CmsTopnavLink cmsTopnavLink);

    int updateInfo(CmsTopnavLink cmsTopnavLink);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsTopnavLink cmsTopnavLink);

    List<CmsTopnavLink> findByIds(List<Integer> pkidList);

    Map<Integer, CmsTopnavLink> findMapByIds(List<Integer> pkidList);

    Pager<CmsTopnavLink> pageByProperties(Map<String, Object> properties, int page);

    Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId);

    List<CmsTopnavLink> listByProperties(Map<String, Object> properties);

    Map<Integer, List<CmsTopnavLink>> mapByCategoryIds(Collection<Integer> categoryIds, Integer... statuses);

    /**
     * 发布
     *
     * @param pkids
     * @param categoryId
     * @param userId
     * @return
     */
    void editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId);

    String export(Map<String, Object> properties);
}