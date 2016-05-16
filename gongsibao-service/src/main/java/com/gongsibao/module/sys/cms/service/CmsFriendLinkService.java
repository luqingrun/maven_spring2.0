package com.gongsibao.module.sys.cms.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.entity.CmsFriendLink;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CmsFriendLinkService {

    CmsFriendLink findById(Integer pkid);

    int update(CmsFriendLink cmsFriendLink);

    int updateInfo(CmsFriendLink cmsFriendLink);

    int delete(Integer pkid, Integer userId);

    Integer insert(CmsFriendLink cmsFriendLink);

    List<CmsFriendLink> findByIds(List<Integer> pkidList);

    Map<Integer, CmsFriendLink> findMapByIds(List<Integer> pkidList);

    Pager<CmsFriendLink> pageByProperties(Map<String, Object> properties, int page);

    List<CmsFriendLink> listByProperties(Map<String, Object> properties);

    Boolean editSort(Integer pkid, Integer type, Boolean isUp, Integer userId);

    List<CmsFriendLink> getIndexList(Map<String, Object> properties);

    void editPublish(Collection<Integer> pkid, Integer type, Integer userId);
}