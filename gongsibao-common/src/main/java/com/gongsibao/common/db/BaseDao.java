package com.gongsibao.common.db;

import com.gongsibao.common.util.page.ParamType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by luqingrun on 16/3/21.
 */
public abstract class BaseDao<T> extends NamedParameterJdbcDaoSupport {
    @Resource(name = "dataSource")
    public final void setDataSource1(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    public Integer getLastInsertId() {
        return getJdbcTemplate().queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    protected abstract void insertObject(T t);

    public abstract int update(T t);

    public abstract int delete(Integer id);

    public abstract T findById(Integer id);

    public abstract List<T> findByIds(List<Integer> id);

    public abstract RowMapper<T> getRowMapper();

    public Integer insert(T t) {
        insertObject(t);
        return getLastInsertId();
    }

    public abstract List<T> findByProperties(Map<String, Object> properties, int start, int pageSize);

    public abstract int countByProperties(Map<String, Object> properties);

    protected void buildSQL(Map<String, Object> properties, StringBuffer sql) {
        if (null == properties) {
            return;
        }

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }

            if (value instanceof Collection) {
                if (CollectionUtils.isEmpty((Collection) value)) {
                    sql.append(" AND 1 <> 1 ");
                } else {
                    sql.append(" AND `").append(key).append("`").append(" IN (:" + key + ")");
                }
            } else if (value instanceof Object[]) {
                if (ArrayUtils.isEmpty((Object[]) value)) {
                    sql.append(" AND 1 <> 1 ");
                } else {
                    sql.append(" AND `").append(key).append("`").append(" IN (:" + key + ")");
                    Object[] objects = (Object[]) value;
                    properties.put(key, Arrays.asList(objects));
                }
            } else {
                sql.append(" AND `").append(key).append("`").append(" = :" + key);
            }
        }
    }

    protected void buildSQL(List<ParamType> paramTypeList, StringBuffer sql) {
        if (null == paramTypeList || paramTypeList.size() == 0) {
            return;
        }

        if (paramTypeList.size() > 0) {
            for (ParamType item : paramTypeList)
            {
                if(item.getSelectType() == "=")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" = " + item.getDbValue() + " ");
                }
                else if(item.getSelectType() == "!=")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" != " + item.getDbValue() + " ");
                }
                else if(item.getSelectType().toLowerCase() == "like")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" like '%" + item.getDbValue() + "%' ");
                }
                else if(item.getSelectType().toLowerCase() == "in")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" in (" + item.getDbValue() + ") ");
                }
                else if(item.getSelectType().toLowerCase() == "notin")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" not in (" + item.getDbValue() + ") ");
                }
                else if(item.getSelectType() == ">")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" > " + item.getDbValue() + " ");
                }
                else if(item.getSelectType() == "<")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" < " + item.getDbValue() + " ");
                }
                else if(item.getSelectType() == ">=")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" >= " + item.getDbValue() + " ");
                }
                else if(item.getSelectType() == "<=")
                {
                    sql.append(" ").append(item.getAndOr()).append(" `").append(item.getDbKey()).append("`").append(" <= " + item.getDbValue() + " ");
                }
            }
        }
    }

    protected T getFirstObj(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

}
