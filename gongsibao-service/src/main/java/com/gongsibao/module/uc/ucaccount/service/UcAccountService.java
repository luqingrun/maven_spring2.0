package com.gongsibao.module.uc.ucaccount.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucaccount.entity.UcAccount;

import java.util.List;
import java.util.Map;

public interface UcAccountService {

    UcAccount findById(Integer pkid);

    int update(UcAccount ucAccount);

    int delete(Integer pkid);

    Integer insert(UcAccount ucAccount);

    List<UcAccount> findByIds(List<Integer> pkidList);

    Map<Integer, UcAccount> findMapByIds(List<Integer> pkidList);

    Pager<UcAccount> pageByProperties(Map<String, Object> properties, int page);

    UcAccount findByMobile(String mobilePhone);
}