package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsBottomnav;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsBottomnavService {

    CmsBottomnav findById(Integer pkid);

    int update(CmsBottomnav cmsBottomnav);

    int updateInfo(CmsBottomnav cmsBottomnav);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsBottomnav cmsBottomnav);

    List<CmsBottomnav> findByIds(List<Integer> pkidList);

    Map<Integer, CmsBottomnav> findMapByIds(List<Integer> pkidList);

    Pager<CmsBottomnav> pageByProperties(Map<String, Object> properties, int page);

    List<CmsBottomnav> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId);

    List<Map<String, Object>> getIndexList(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId);
}