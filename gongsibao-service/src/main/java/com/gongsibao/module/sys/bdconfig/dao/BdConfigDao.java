package com.gongsibao.module.sys.bdconfig.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.bdconfig.entity.BdConfig;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("bdConfigDao")
public class BdConfigDao extends BaseDao<BdConfig>{

    public static String INSERT_COLUMNS = " `type`, `name`, `value`, `sort`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(BdConfig bdConfig) {
        insertObject(bdConfig);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdConfig bdConfig) {
        getJdbcTemplate().update("insert into `bd_config`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ? )",
                bdConfig.getType(),
                bdConfig.getName(),
                bdConfig.getValue(),
                bdConfig.getSort(),
                bdConfig.getIsEnabled(),
                bdConfig.getAddTime(),
                bdConfig.getAddUserId(),
                bdConfig.getRemark()
                );
    }

    @Override
    public int update(BdConfig bdConfig) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `bd_config` set pkid = pkid, `type` = :type, `name` = :name, `value` = :value, `sort` = :sort, `is_enabled` = :isEnabled, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdConfig),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `bd_config` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public BdConfig findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `bd_config` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<BdConfig> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `bd_config` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<BdConfig> getRowMapper(){
        RowMapper<BdConfig> rowMapper = (rs, i) -> {
            BdConfig bdConfig = new BdConfig();
            bdConfig.setPkid(rs.getInt("pkid"));
            bdConfig.setType(rs.getInt("type"));
            bdConfig.setName(rs.getString("name"));
            bdConfig.setValue(rs.getString("value"));
            bdConfig.setSort(rs.getDouble("sort"));
            bdConfig.setIsEnabled(rs.getInt("is_enabled"));
            bdConfig.setAddTime(rs.getTimestamp("add_time"));
            bdConfig.setAddUserId(rs.getInt("add_user_id"));
            bdConfig.setRemark(rs.getString("remark"));
            return bdConfig;
        };
        return rowMapper;
    }

    @Override
    public List<BdConfig> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `bd_config` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_config` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}