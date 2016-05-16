package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsGuestVoice;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsGuestVoiceService {

    CmsGuestVoice findById(Integer pkid);

    int update(CmsGuestVoice cmsGuestVoice);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsGuestVoice cmsGuestVoice);

    List<CmsGuestVoice> findByIds(List<Integer> pkidList);

    Map<Integer, CmsGuestVoice> findMapByIds(List<Integer> pkidList);

    Pager<CmsGuestVoice> pageByProperties(Map<String, Object> properties, int page);

    List<CmsGuestVoice> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Boolean isUp, Integer userId);

    List<CmsGuestVoice> getIndexList(Map<String, Object> properties);

    Boolean editPublish(Collection<Integer> pkids, Integer userId);
}