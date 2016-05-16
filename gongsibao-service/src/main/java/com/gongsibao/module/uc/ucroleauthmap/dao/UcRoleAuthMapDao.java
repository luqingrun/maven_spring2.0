package com.gongsibao.module.uc.ucroleauthmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.uc.ucroleauthmap.entity.UcRoleAuthMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("ucRoleAuthMapDao")
public class UcRoleAuthMapDao extends BaseDao<UcRoleAuthMap>{

    public static String INSERT_COLUMNS = " `role_id`, `auth_id`, `add_time`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcRoleAuthMap ucRoleAuthMap) {
        insertObject(ucRoleAuthMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcRoleAuthMap ucRoleAuthMap) {
        getJdbcTemplate().update("insert into `uc_role_auth_map`("+INSERT_COLUMNS+") values (?, ?, ?, ? )",
                ucRoleAuthMap.getRoleId(),
                ucRoleAuthMap.getAuthId(),
                ucRoleAuthMap.getAddTime(),
                ucRoleAuthMap.getAddUserId()
                );
    }

    @Override
    public int update(UcRoleAuthMap ucRoleAuthMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_role_auth_map` set pkid = pkid, `role_id` = :roleId, `auth_id` = :authId, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucRoleAuthMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_role_auth_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public UcRoleAuthMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `uc_role_auth_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcRoleAuthMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `uc_role_auth_map` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<UcRoleAuthMap> getRowMapper(){
        RowMapper<UcRoleAuthMap> rowMapper = (rs, i) -> {
            UcRoleAuthMap ucRoleAuthMap = new UcRoleAuthMap();
            ucRoleAuthMap.setPkid(rs.getInt("pkid"));
            ucRoleAuthMap.setRoleId(rs.getInt("role_id"));
            ucRoleAuthMap.setAuthId(rs.getInt("auth_id"));
            ucRoleAuthMap.setAddTime(rs.getTimestamp("add_time"));
            ucRoleAuthMap.setAddUserId(rs.getInt("add_user_id"));
            return ucRoleAuthMap;
        };
        return rowMapper;
    }

    @Override
    public List<UcRoleAuthMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `uc_role_auth_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_role_auth_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}