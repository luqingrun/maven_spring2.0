package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsSpecialActive;
import com.gongsibao.module.sys.cms.entity.CmsSpecialYear;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsSpecialActiveService {

    CmsSpecialActive findById(Integer pkid);

    int update(CmsSpecialActive cmsSpecialActive);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsSpecialActive cmsSpecialActive);

    List<CmsSpecialActive> findByIds(List<Integer> pkidList);

    Map<Integer, CmsSpecialActive> findMapByIds(List<Integer> pkidList);

    Pager<CmsSpecialActive> pageByProperties(Map<String, Object> properties, int page);

    List<CmsSpecialActive> listByProperties(Map<String, Object> properties);

    List<CmsSpecialYear> getIndexActive(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids, Integer userId);
}