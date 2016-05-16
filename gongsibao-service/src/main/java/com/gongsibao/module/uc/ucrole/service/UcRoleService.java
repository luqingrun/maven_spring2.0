package com.gongsibao.module.uc.ucrole.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucrole.entity.UcRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UcRoleService {

    UcRole findById(Integer pkid);

    int update(UcRole ucRole);

    int delete(Integer pkid);

    Integer insert(UcRole ucRole);

    List<UcRole> findByIds(List<Integer> pkidList);

    Map<Integer, UcRole> findMapByIds(List<Integer> pkidList);

    Pager<UcRole> pageByProperties(Map<String, Object> properties, int page);

    List<UcRole> findByUserPkid(Integer userPkid);

    List<UcRole> findByUserPkid(Integer userPkid, Integer canPass);

    List<Integer> findUserIdsByRoleName(String roleName);

    Map<Integer, List<UcRole>> findMapByUserIds(List<Integer> userIds);

    Map<Integer, String> findNameByUserIds(List<Integer> userIds);
}