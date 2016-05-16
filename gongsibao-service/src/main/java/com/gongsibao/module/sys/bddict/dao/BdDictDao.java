package com.gongsibao.module.sys.bddict.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("bdDictDao")
public class BdDictDao extends BaseDao<BdDict>{

    public static String INSERT_COLUMNS = " `pid`, `type`, `name`, `code`, `sort`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(BdDict bdDict) {
        insertObject(bdDict);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdDict bdDict) {
        getJdbcTemplate().update("insert into `bd_dict`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ? )",
                bdDict.getPid(),
                bdDict.getType(),
                bdDict.getName(),
                bdDict.getCode(),
                bdDict.getSort(),
                bdDict.getIsEnabled(),
                bdDict.getAddTime(),
                bdDict.getAddUserId(),
                bdDict.getRemark()
                );
    }

    @Override
    public int update(BdDict bdDict) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `bd_dict` set pkid = pkid, `pid` = :pid, `type` = :type, `name` = :name, `code` = :code, `sort` = :sort, `is_enabled` = :isEnabled, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdDict),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `bd_dict` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public BdDict findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `bd_dict` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<BdDict> findByIds(List<Integer> pkidList) {
        if(CollectionUtils.isEmpty(pkidList)) {
            return  new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `bd_dict` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<BdDict> getRowMapper(){
        RowMapper<BdDict> rowMapper = (rs, i) -> {
            BdDict bdDict = new BdDict();
            bdDict.setPkid(rs.getInt("pkid"));
            bdDict.setPid(rs.getInt("pid"));
            bdDict.setType(rs.getInt("type"));
            bdDict.setName(rs.getString("name"));
            bdDict.setCode(rs.getInt("code"));
            bdDict.setSort(rs.getDouble("sort"));
            bdDict.setIsEnabled(rs.getInt("is_enabled"));
            bdDict.setAddTime(rs.getTimestamp("add_time"));
            bdDict.setAddUserId(rs.getInt("add_user_id"));
            bdDict.setRemark(rs.getString("remark"));
            return bdDict;
        };
        return rowMapper;
    }

    @Override
    public List<BdDict> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `bd_dict` where 1=1 ");
        buildSQL(properties, sql);
        if(start >= 0 && pageSize > 0) {
            sql.append(" limit ").append(start).append(", ").append(pageSize);
        }
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_dict` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<BdDict> findAll() {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `bd_dict` where 1=1 AND `is_enabled` = 1 order by `type`, `sort` ");
        return getNamedParameterJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public void insertBatch(final List<BdDict> itemList) {

        String sql = "insert into `bd_dict` (`pkid`,`pid`,`type`,`name`,`code`,`sort`) values (?, ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setObject(1, itemList.get(i).getPkid());
                ps.setObject(2, itemList.get(i).getPid());
                ps.setObject(3, itemList.get(i).getType());
                ps.setObject(4, itemList.get(i).getName());
                ps.setObject(5, 0);
                ps.setObject(6, itemList.get(i).getSort());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    public BdDict getMaxPkidByType(int type)
    {
        return getFirstObj(getJdbcTemplate().query("SELECT * FROM `bd_dict` WHERE `type` = " + type + " ORDER BY pkid DESC LIMIT 0,1", getRowMapper()));

    }

}