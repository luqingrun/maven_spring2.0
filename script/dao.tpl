package com.gongsibao.module.{{module_name}}.{{pack_name}}.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.{{module_name}}.{{pack_name}}.entity.{{class_name}};
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("{{ java_name }}Dao")
public class {{class_name}}Dao extends BaseDao<{{class_name}}>{

    public static String INSERT_COLUMNS = "{{ insert_column }}";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert({{class_name}} {{ java_name }}) {
        insertObject({{ java_name }});
        return getLastInsertId();
    }

    @Override
    protected void insertObject({{class_name}} {{ java_name }}) {
        getJdbcTemplate().update("insert into `{{table_name}}`("+INSERT_COLUMNS+") values ({% for column in columns %}{% if 'PRI' not in column.COLUMN_KEY %}?{% if not loop.last %},{% endif %} {% endif %}{% endfor %})",
                {% for column in columns %}{% if 'PRI' not in column.COLUMN_KEY %}{{ java_name }}.get{{ column.f_setget_name }}(){% if not loop.last %},{% endif %}
                {% endif %}{% endfor %});
    }

    @Override
    public int update({{class_name}} {{ java_name }}) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `{{table_name}}` set pkid = pkid{% for column in columns %}{% if 'PRI' not in column.COLUMN_KEY %}, `{{ column.COLUMN_NAME }}` = :{{ column.f_name }}{% endif %}{% endfor %} where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson({{ java_name }}),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `{{table_name}}` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public {{class_name}} findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `{{table_name}}` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<{{class_name}}> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `{{table_name}}` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<{{class_name}}> getRowMapper(){
        RowMapper<{{class_name}}> rowMapper = (rs, i) -> {
            {{class_name}} {{ java_name }} = new {{class_name}}();
            {% for column in columns %}{{ java_name }}.set{{ column.f_setget_name }}(rs.get{{ column.f_sql_type }}("{{ column.COLUMN_NAME }}"));
            {% endfor %}return {{ java_name }};
        };
        return rowMapper;
    }

    @Override
    public List<{{class_name}}> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `{{table_name}}` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `{{table_name}}` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}
