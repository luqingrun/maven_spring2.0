package com.gongsibao.module.{{module_name}}.{{pack_name}}.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.{{module_name}}.{{pack_name}}.entity.{{class_name}};

import java.util.List;
import java.util.Map;

public interface {{class_name}}Service {

    {{class_name}} findById(Integer pkid);

    int update({{class_name}} {{ java_name }});

    int delete(Integer pkid);

    Integer insert({{class_name}} {{ java_name }});

    List<{{class_name}}> findByIds(List<Integer> pkidList);

    Map<Integer, {{class_name}}> findMapByIds(List<Integer> pkidList);

    Pager<{{class_name}}> pageByProperties(Map<String, Object> properties, int page);
}
