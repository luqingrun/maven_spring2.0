package com.gongsibao.module.uc.ucuserrolemap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;

import java.util.List;
import java.util.Map;

public interface UcUserRoleMapService {

    UcUserRoleMap findById(Integer pkid);

    int update(UcUserRoleMap ucUserRoleMap);

    int delete(Integer pkid);

    int deleteByUserId(Integer userId);

    Integer insert(UcUserRoleMap ucUserRoleMap);
    int[] insert(List<UcUserRoleMap> ucUserRoleMapList);

    List<UcUserRoleMap> findByIds(List<Integer> pkidList);

    Map<Integer, UcUserRoleMap> findMapByIds(List<Integer> pkidList);

    Pager<UcUserRoleMap> pageByProperties(Map<String, Object> properties, int page);

    Map<Integer, List<UcUserRoleMap>> findMapByUserIds(List<Integer> userIds);
}