package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsTopnavCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsTopnavCategoryService {

    CmsTopnavCategory findById(Integer pkid);

    int update(CmsTopnavCategory cmsTopnavCategory);

    int updateNameAndUrl(CmsTopnavCategory cmsTopnavCategory);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsTopnavCategory cmsTopnavCategory);

    List<CmsTopnavCategory> findByIds(List<Integer> pkidList);

    Map<Integer, CmsTopnavCategory> findMapByIds(List<Integer> pkidList);

    Pager<CmsTopnavCategory> pageByProperties(Map<String, Object> properties, int page);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    /**
     * 更新排序
     *
     * @param pkid
     * @param isUp   true 向上 false 向下
     * @param isRoot true 一级分类 false 二级分类
     * @param userId
     * @return
     */
    Boolean editSort(Integer pkid, Boolean isUp, Boolean isRoot, Integer userId);

    List<CmsTopnavCategory> listByProperties(Map<String, Object> properties);

    /**
     * 查CMS首页
     *
     * @param paramMap
     * @return
     */
    List<CmsTopnavCategory> getIndexList(Map<String, Object> paramMap);

    void editPublish(Collection<Integer> pkids, Integer pid, Integer userId);
}