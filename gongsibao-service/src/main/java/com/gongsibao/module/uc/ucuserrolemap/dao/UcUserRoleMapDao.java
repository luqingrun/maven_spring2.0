package com.gongsibao.module.uc.ucuserrolemap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("ucUserRoleMapDao")
public class UcUserRoleMapDao extends BaseDao<UcUserRoleMap> {

    public static String INSERT_COLUMNS = " `user_id`, `role_id`, `can_pass`, `add_time`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcUserRoleMap ucUserRoleMap) {
        insertObject(ucUserRoleMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcUserRoleMap ucUserRoleMap) {
        getJdbcTemplate().update("insert into `uc_user_role_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ? )",
                ucUserRoleMap.getUserId(),
                ucUserRoleMap.getRoleId(),
                ucUserRoleMap.getCanPass(),
                ucUserRoleMap.getAddTime(),
                ucUserRoleMap.getAddUserId()
        );
    }

    public int[] insert(List<UcUserRoleMap> ucUserRoleMapList) {
        return getJdbcTemplate().batchUpdate("insert into `uc_user_role_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, NOW(), ? )",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UcUserRoleMap ucUserRoleMap = ucUserRoleMapList.get(i);
                        ps.setInt(1, ucUserRoleMap.getUserId());
                        ps.setInt(2, ucUserRoleMap.getRoleId());
                        ps.setInt(3, ucUserRoleMap.getCanPass());
                        ps.setInt(4, ucUserRoleMap.getAddUserId());
                    }

                    @Override
                    public int getBatchSize() {
                        return ucUserRoleMapList.size();
                    }
                }
        );
    }


    @Override
    public int update(UcUserRoleMap ucUserRoleMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_user_role_map` set pkid = pkid, `user_id` = :userId, `role_id` = :roleId, `can_pass` = :canPass, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucUserRoleMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_user_role_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    public int deleteByUserId(Integer userId) {
        return getJdbcTemplate().update("delete from `uc_user_role_map` where user_id = ? ", userId);
    }

    @Override
    public UcUserRoleMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `uc_user_role_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcUserRoleMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_user_role_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<UcUserRoleMap> getRowMapper() {
        RowMapper<UcUserRoleMap> rowMapper = (rs, i) -> {
            UcUserRoleMap ucUserRoleMap = new UcUserRoleMap();
            ucUserRoleMap.setPkid(rs.getInt("pkid"));
            ucUserRoleMap.setUserId(rs.getInt("user_id"));
            ucUserRoleMap.setRoleId(rs.getInt("role_id"));
            ucUserRoleMap.setCanPass(rs.getInt("can_pass"));
            ucUserRoleMap.setAddTime(rs.getTimestamp("add_time"));
            ucUserRoleMap.setAddUserId(rs.getInt("add_user_id"));
            return ucUserRoleMap;
        };
        return rowMapper;
    }

    @Override
    public List<UcUserRoleMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_user_role_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_user_role_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<UcUserRoleMap> findByUserIds(List<Integer> userIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(ALL_COLUMNS);
        sql.append("FROM `uc_user_role_map` ");
        sql.append("WHERE user_id IN (:userIds)");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("userIds", userIds);

        return getNamedParameterJdbcTemplate().query(sql.toString(), source, getRowMapper());
    }
}