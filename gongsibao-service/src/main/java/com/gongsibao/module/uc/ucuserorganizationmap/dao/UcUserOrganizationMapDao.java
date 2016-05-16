package com.gongsibao.module.uc.ucuserorganizationmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("ucUserOrganizationMapDao")
public class UcUserOrganizationMapDao extends BaseDao<UcUserOrganizationMap> {

    public static String INSERT_COLUMNS = " `user_id`, `organization_id`, `add_time`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcUserOrganizationMap ucUserOrganizationMap) {
        insertObject(ucUserOrganizationMap);
        return getLastInsertId();
    }

    public int[] insert(List<UcUserOrganizationMap> ucUserOrganizationMapList) {
        return getJdbcTemplate().batchUpdate("insert into `uc_user_organization_map`(" + INSERT_COLUMNS + ") values (?, ?, NOW(), ? )", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UcUserOrganizationMap map = ucUserOrganizationMapList.get(i);
                        ps.setInt(1, map.getUserId());
                        ps.setInt(2, map.getOrganizationId());
                        ps.setInt(3, map.getAddUserId());
                    }

                    @Override
                    public int getBatchSize() {
                        return ucUserOrganizationMapList.size();
                    }
                }
        );
    }

    @Override
    protected void insertObject(UcUserOrganizationMap ucUserOrganizationMap) {
        getJdbcTemplate().update("insert into `uc_user_organization_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ? )",
                ucUserOrganizationMap.getUserId(),
                ucUserOrganizationMap.getOrganizationId(),
                ucUserOrganizationMap.getAddTime(),
                ucUserOrganizationMap.getAddUserId()
        );
    }

    @Override
    public int update(UcUserOrganizationMap ucUserOrganizationMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_user_organization_map` set pkid = pkid, `user_id` = :userId, `organization_id` = :organizationId, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucUserOrganizationMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_user_organization_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    public int deleteByUserId(Integer userId) {
        //TODO,需要自己决定如何实现
        return getJdbcTemplate().update("delete from `uc_user_organization_map` where user_id = ? ", userId);
    }

    @Override
    public UcUserOrganizationMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `uc_user_organization_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcUserOrganizationMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_user_organization_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<UcUserOrganizationMap> getRowMapper() {
        RowMapper<UcUserOrganizationMap> rowMapper = (rs, i) -> {
            UcUserOrganizationMap ucUserOrganizationMap = new UcUserOrganizationMap();
            ucUserOrganizationMap.setPkid(rs.getInt("pkid"));
            ucUserOrganizationMap.setUserId(rs.getInt("user_id"));
            ucUserOrganizationMap.setOrganizationId(rs.getInt("organization_id"));
            ucUserOrganizationMap.setAddTime(rs.getTimestamp("add_time"));
            ucUserOrganizationMap.setAddUserId(rs.getInt("add_user_id"));
            return ucUserOrganizationMap;
        };
        return rowMapper;
    }

    @Override
    public List<UcUserOrganizationMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_user_organization_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_user_organization_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}