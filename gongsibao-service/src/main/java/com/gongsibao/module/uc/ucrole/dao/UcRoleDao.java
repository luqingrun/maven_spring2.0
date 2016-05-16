package com.gongsibao.module.uc.ucrole.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository("ucRoleDao")
public class UcRoleDao extends BaseDao<UcRole> {

    public static String INSERT_COLUMNS = " `name`, `tag`, `description`, `sort`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcRole ucRole) {
        insertObject(ucRole);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcRole ucRole) {
        getJdbcTemplate().update("insert into `uc_role`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ? )",
                ucRole.getName(),
                ucRole.getTag(),
                ucRole.getDescription(),
                ucRole.getSort(),
                ucRole.getIsEnabled(),
                ucRole.getAddTime(),
                ucRole.getAddUserId(),
                ucRole.getRemark()
        );
    }

    @Override
    public int update(UcRole ucRole) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_role` set pkid = pkid, `name` = :name, `description` = :description, `sort` = :sort, `is_enabled` = :isEnabled, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucRole),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_role` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public UcRole findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `uc_role` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcRole> findByIds(List<Integer> pkidList) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_role` where pkid IN (:pkidList) ", source, getRowMapper());
    }


    @Override
    public RowMapper<UcRole> getRowMapper() {
        RowMapper<UcRole> rowMapper = (rs, i) -> {
            UcRole ucRole = new UcRole();
            ucRole.setPkid(rs.getInt("pkid"));
            ucRole.setName(rs.getString("name"));
            ucRole.setTag(rs.getString("tag"));
            ucRole.setDescription(rs.getString("description"));
            ucRole.setSort(rs.getDouble("sort"));
            ucRole.setIsEnabled(rs.getInt("is_enabled"));
            ucRole.setAddTime(rs.getTimestamp("add_time"));
            ucRole.setAddUserId(rs.getInt("add_user_id"));
            ucRole.setRemark(rs.getString("remark"));
            return ucRole;
        };
        return rowMapper;
    }

    @Override
    public List<UcRole> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_role` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    public List<UcRole> findByUserPkid(Integer userPkid, Integer canPass) {
//        String sql = " " + userPkid;
        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        sql.append("t2.`pkid`, t2.`tag`, t2.`name`, t2.`description`, t2.`sort`, t2.`is_enabled`, t2.`add_time`, t2.`add_user_id`, t2.`remark` ");
        sql.append("from uc_user_role_map t1 ");
        sql.append("left join uc_role t2 on t1.`role_id`=t2.`pkid` ");
        sql.append("where t1.`user_id` = ? AND t2.`is_enabled` = 1 ");
        List<Object> list = new ArrayList<>();
        list.add(userPkid);
        if (null != canPass) {
            sql.append("AND t1.`can_pass` = ? ");
            list.add(canPass);
        }
        sql.append("GROUP BY t2.`pkid` ");
        return getJdbcTemplate().query(sql.toString(), getRowMapper(), list.toArray());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_role` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<Integer> findUserIdsByRoleName(String roleName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("DISTINCT t1.`user_id` ");
        sql.append("FROM `uc_user_role_map` t1 ");
        sql.append("INNER JOIN `uc_role` t2 ");
        sql.append("ON t1.`role_id` = t2.`pkid` AND t2.`name` LIKE :roleName ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("roleName", "%" + roleName + "%");
        return getNamedParameterJdbcTemplate().query(sql.toString(), source, (rs, rowNum) -> {
            return rs.getInt("user_id");
        });
    }
}