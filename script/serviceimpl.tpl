package com.gongsibao.module.{{module_name}}.{{pack_name}}.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.{{module_name}}.{{pack_name}}.dao.{{class_name}}Dao;
import com.gongsibao.module.{{module_name}}.{{pack_name}}.entity.{{class_name}};
import com.gongsibao.module.{{module_name}}.{{pack_name}}.service.{{class_name}}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("{{java_name}}Service")
public class {{class_name}}ServiceImpl implements {{class_name}}Service {
    @Autowired
    private {{class_name}}Dao {{java_name}}Dao;

    @Override
    public {{class_name}} findById(Integer pkid) {
        return {{java_name}}Dao.findById(pkid);
    }

    @Override
    public int update({{class_name}} {{java_name}}) {
        return {{java_name}}Dao.update({{java_name}});
    }

    @Override
    public int delete(Integer pkid) {
        return {{java_name}}Dao.delete(pkid);
    }

    @Override
    public Integer insert({{class_name}} {{java_name}}) {
        return {{java_name}}Dao.insert({{java_name}});
    }

    @Override
    public List<{{class_name}}> findByIds(List<Integer> pkidList) {
        return {{java_name}}Dao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, {{class_name}}> findMapByIds(List<Integer> pkidList) {
        List<{{class_name}}> list = findByIds(pkidList);
        Map<Integer, {{class_name}}> map = new HashMap<>();
        for({{class_name}} {{java_name}} : list){
            map.put({{java_name}}.getPkid(), {{java_name}});
        }
        return map;
    }

    @Override
    public Pager<{{class_name}}> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = {{java_name}}Dao.countByProperties(map);
        Pager<{{class_name}}> pager = new Pager<>(totalRows, page);
        List<{{class_name}}> {{java_name}}List = {{java_name}}Dao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList({{java_name}}List);
        return pager;
    }

}
