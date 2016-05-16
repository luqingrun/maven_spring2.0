package com.gongsibao.module.uc.ucorganizationbddictmap.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucorganizationbddictmap.entity.UcOrganizationBdDictMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UcOrganizationBdDictMapService {

    UcOrganizationBdDictMap findById(Integer pkid);

    int update(UcOrganizationBdDictMap ucOrganizationBdDictMap);

    int delete(Integer pkid);

    Integer insert(UcOrganizationBdDictMap ucOrganizationBdDictMap);

    List<UcOrganizationBdDictMap> findByIds(List<Integer> pkidList);

    Map<Integer, UcOrganizationBdDictMap> findMapByIds(List<Integer> pkidList);

    Pager<UcOrganizationBdDictMap> pageByProperties(Map<String, Object> properties, int page);

    List<UcOrganizationBdDictMap> listByProperties(Map<String, Object> properties);

    List<Integer> findOrganizationIdsByProperties(Map<String, Object> properties);

    int[] insert(List<UcOrganizationBdDictMap> ucOrganizationBdDictMaps);

    void delByorganizationId(Integer organizationId);

    default List<Integer> findDictIdsByOrganizationId(Integer organizationId, Integer type) {
        return findDictIdsByOrganizationId(new ArrayList<Integer>() {{
            add(organizationId);
        }}, type);
    }

    List<Integer> findDictIdsByOrganizationId(Collection<Integer> organizationIds, Integer type);

    Map<Integer, List<Integer>> findMapByOrganizationIds(Collection<Integer> organizationIds, Integer type);
}