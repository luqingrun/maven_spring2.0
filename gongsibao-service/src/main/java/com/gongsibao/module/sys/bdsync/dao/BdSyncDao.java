package com.gongsibao.module.sys.bdsync.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.bdsync.entity.BdSync;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("bdSyncDao")
public class BdSyncDao extends BaseDao<BdSync> {

    public static String INSERT_COLUMNS = " `table_name`, `m_pkid`, `s_id`, `s_sid`, `add_time`, `m_last_update_time`, `s_last_update_time`, `sync_status` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(BdSync bdSync) {
        insertObject(bdSync);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdSync bdSync) {
        getJdbcTemplate().update("insert into `bd_sync`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, NOW(), ?, ?, ?)",
                bdSync.getTableName(),
                bdSync.getmPkid(),
                bdSync.getsId(),
                bdSync.getsSid(),
                bdSync.getmLastUpdateTime(),
                bdSync.getsLastUpdateTime(),
                bdSync.getSyncStatus()
        );
    }

    @Override
    public int update(BdSync bdSync) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `bd_sync` set pkid = pkid, `table_name` = :tableName, `m_pkid` = :mPkid, `s_id` = :sId, `s_sid` = :sSid, `add_time` = :addTime, `m_last_update_time` = :mLastUpdateTime, `s_last_update_time` = :sLastUpdateTime, `is_need_sync` = :isNeedSync, `m_is_deleted` = :mIsDeleted, `s_is_deleted` = :sIsDeleted, `all_is_deleted` = :allIsDeleted where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdSync),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `bd_sync` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public BdSync findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `bd_sync` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<BdSync> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `bd_sync` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<BdSync> getRowMapper() {
        RowMapper<BdSync> rowMapper = (rs, i) -> {
            BdSync bdSync = new BdSync();
            bdSync.setPkid(rs.getInt("pkid"));
            bdSync.setTableName(rs.getString("table_name"));
            bdSync.setmPkid(rs.getInt("m_pkid"));
            bdSync.setsId(rs.getString("s_id"));
            bdSync.setsSid(rs.getInt("s_sid"));
            bdSync.setAddTime(rs.getTimestamp("add_time"));
            bdSync.setmLastUpdateTime(rs.getTimestamp("m_last_update_time"));
            bdSync.setsLastUpdateTime(rs.getTimestamp("s_last_update_time"));
            bdSync.setSyncStatus(rs.getInt("sync_status"));
            return bdSync;
        };
        return rowMapper;
    }

    @Override
    public List<BdSync> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `bd_sync` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_sync` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public BdSync getByMPkid(Integer mpkid) {
        String sql = "SELECT " + ALL_COLUMNS + " FROM bd_sync WHERE m_pkid = ? ";
        return getFirstObj(getJdbcTemplate().query(sql, getRowMapper(), mpkid));
    }
}