package com.gongsibao.module.uc.ucorganizationbddictmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucorganizationbddictmap.entity.UcOrganizationBdDictMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("ucOrganizationBdDictMapDao")
public class UcOrganizationBdDictMapDao extends BaseDao<UcOrganizationBdDictMap> {

    public static String INSERT_COLUMNS = " `organization_id`, `dict_id`, `type` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcOrganizationBdDictMap ucOrganizationBdDictMap) {
        insertObject(ucOrganizationBdDictMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcOrganizationBdDictMap ucOrganizationBdDictMap) {
        getJdbcTemplate().update("insert into `uc_organization_bd_dict_map`(" + INSERT_COLUMNS + ") values (?, ?, ? )",
                ucOrganizationBdDictMap.getOrganizationId(),
                ucOrganizationBdDictMap.getDictId(),
                ucOrganizationBdDictMap.getType()
        );
    }

    public int[] insertBatch(List<UcOrganizationBdDictMap> ucOrganizationBdDictMaps) {
        if (CollectionUtils.isEmpty(ucOrganizationBdDictMaps)) {
            return null;
        }

        return getJdbcTemplate().batchUpdate("insert into `uc_organization_bd_dict_map`(" + INSERT_COLUMNS + ") values (?, ?, ? )", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UcOrganizationBdDictMap map = ucOrganizationBdDictMaps.get(i);

                ps.setInt(1, map.getOrganizationId());
                ps.setInt(2, map.getDictId());
                ps.setInt(3, map.getType());
            }

            @Override
            public int getBatchSize() {
                return ucOrganizationBdDictMaps.size();
            }
        });
    }

    @Override
    public int update(UcOrganizationBdDictMap ucOrganizationBdDictMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_organization_bd_dict_map` set pkid = pkid, `organization_id` = :organizationId, `dict_id` = :dictId, `type` = :type where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucOrganizationBdDictMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_organization_bd_dict_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    public int deleteByOrganizationId(Integer origanizationId) {
        return getJdbcTemplate().update("delete from `uc_organization_bd_dict_map` where organization_id = ? ", origanizationId);
    }

    @Override
    public UcOrganizationBdDictMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `uc_organization_bd_dict_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcOrganizationBdDictMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_organization_bd_dict_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<UcOrganizationBdDictMap> getRowMapper() {
        RowMapper<UcOrganizationBdDictMap> rowMapper = (rs, i) -> {
            UcOrganizationBdDictMap ucOrganizationBdDictMap = new UcOrganizationBdDictMap();
            ucOrganizationBdDictMap.setPkid(rs.getInt("pkid"));
            ucOrganizationBdDictMap.setOrganizationId(rs.getInt("organization_id"));
            ucOrganizationBdDictMap.setDictId(rs.getInt("dict_id"));
            ucOrganizationBdDictMap.setType(rs.getInt("type"));
            return ucOrganizationBdDictMap;
        };
        return rowMapper;
    }

    @Override
    public List<UcOrganizationBdDictMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_organization_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_organization_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}