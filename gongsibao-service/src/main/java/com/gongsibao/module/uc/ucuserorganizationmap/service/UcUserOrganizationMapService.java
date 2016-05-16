package com.gongsibao.module.uc.ucuserorganizationmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;

import java.util.List;
import java.util.Map;

public interface UcUserOrganizationMapService {

    UcUserOrganizationMap findById(Integer pkid);

    int update(UcUserOrganizationMap ucUserOrganizationMap);

    int delete(Integer pkid);

    int deleteByUserId(Integer userId);

    Integer insert(UcUserOrganizationMap ucUserOrganizationMap);

    int[] insert(List<UcUserOrganizationMap> ucUserOrganizationMapList);

    List<UcUserOrganizationMap> findByIds(List<Integer> pkidList);

    Map<Integer, UcUserOrganizationMap> findMapByIds(List<Integer> pkidList);

    Pager<UcUserOrganizationMap> pageByProperties(Map<String, Object> properties, int page);

    List<UcUserOrganizationMap> listByProperties(Map<String, Object> properties);

    List<Integer> findUserIdsByProperties(Map<String, Object> properties);

}