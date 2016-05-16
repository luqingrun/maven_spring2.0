package com.gongsibao.module.uc.ucauth.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;

import java.util.List;
import java.util.Map;

public interface UcAuthService {

    UcAuth findById(Integer pkid);

    int update(UcAuth ucAuth);

    int delete(Integer pkid);

    Integer insert(UcAuth ucAuth);

    List<UcAuth> findByIds(List<Integer> pkidList);

    Map<Integer, UcAuth> findMapByIds(List<Integer> pkidList);

    Pager<UcAuth> pageByProperties(Map<String, Object> properties, int page);
}