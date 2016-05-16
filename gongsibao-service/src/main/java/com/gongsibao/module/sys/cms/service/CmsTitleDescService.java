package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsTitleDesc;

import java.util.List;
import java.util.Map;

public interface CmsTitleDescService {

    CmsTitleDesc findById(Integer pkid);

    CmsTitleDesc get();

    int update(CmsTitleDesc cmsTitleDesc);

    int delete(Integer pkid);

    Integer insert(CmsTitleDesc cmsTitleDesc);

    List<CmsTitleDesc> findByIds(List<Integer> pkidList);

    Map<Integer, CmsTitleDesc> findMapByIds(List<Integer> pkidList);

    Pager<CmsTitleDesc> pageByProperties(Map<String, Object> properties, int page);

    CmsTitleDesc getIndex();
}