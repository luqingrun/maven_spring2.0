package com.gongsibao.module.uc.ucroleauthmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucroleauthmap.entity.UcRoleAuthMap;

import java.util.List;
import java.util.Map;

public interface UcRoleAuthMapService {

    UcRoleAuthMap findById(Integer pkid);

    int update(UcRoleAuthMap ucRoleAuthMap);

    int delete(Integer pkid);

    Integer insert(UcRoleAuthMap ucRoleAuthMap);

    List<UcRoleAuthMap> findByIds(List<Integer> pkidList);

    Map<Integer, UcRoleAuthMap> findMapByIds(List<Integer> pkidList);

    Pager<UcRoleAuthMap> pageByProperties(Map<String, Object> properties, int page);
}