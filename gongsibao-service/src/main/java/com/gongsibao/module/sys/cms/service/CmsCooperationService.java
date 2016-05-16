package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsCooperation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsCooperationService {

    CmsCooperation findById(Integer pkid);

    int update(CmsCooperation cmsCooperation);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsCooperation cmsCooperation);

    List<CmsCooperation> findByIds(List<Integer> pkidList);

    Map<Integer, CmsCooperation> findMapByIds(List<Integer> pkidList);

    Pager<CmsCooperation> pageByProperties(Map<String, Object> properties, int page);

    List<CmsCooperation> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    List<CmsCooperation> getIndexList(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids, Integer userId);
}