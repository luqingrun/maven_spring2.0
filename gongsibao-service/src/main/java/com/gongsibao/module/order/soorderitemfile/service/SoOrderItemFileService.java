package com.gongsibao.module.order.soorderitemfile.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemfile.entity.SoOrderItemFile;

import java.util.List;
import java.util.Map;

public interface SoOrderItemFileService {

    SoOrderItemFile findById(Integer pkid);

    int update(SoOrderItemFile soOrderItemFile);

    int delete(Integer pkid);

    Integer insert(SoOrderItemFile soOrderItemFile);

    List<SoOrderItemFile> findByIds(List<Integer> pkidList);

    Map<Integer, SoOrderItemFile> findMapByIds(List<Integer> pkidList);

    Pager<SoOrderItemFile> pageByProperties(Map<String, Object> properties, int page);
}