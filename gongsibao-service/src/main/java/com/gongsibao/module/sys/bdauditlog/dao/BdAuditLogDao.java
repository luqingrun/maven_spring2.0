package com.gongsibao.module.sys.bdauditlog.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Repository("bdAuditLogDao")
public class BdAuditLogDao extends BaseDao<BdAuditLog> {

    public static String INSERT_COLUMNS = " `type_id`, `form_id`, `status_id`, `content`, `add_time`, `add_user_id`, `remark`, `level` ";

    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    /**
     * 通过参数查找审核日志记录
     * @param pkid
     * @return
     */
    @Override
    public BdAuditLog findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `bd_audit_log` where pkid = " + pkid, getRowMapper()));
    }

    /**
     * 通过参数查找审核日志记录列表
     * @param properties
     * @param start
     * @param pageSize
     * @return List
     */
    @Override
    public List<BdAuditLog> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `bd_audit_log` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `add_time` DESC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    /**
     * 插入单条审核日志记录
     * @param bdAuditLog
     * @return Integer
     */
    @Override
    public Integer insert(BdAuditLog bdAuditLog) {
        insertObject(bdAuditLog);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdAuditLog bdAuditLog) {
        getJdbcTemplate().update("insert into `bd_audit_log`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, NOW(), ?, ?, ? )",
                bdAuditLog.getTypeId(),
                bdAuditLog.getFormId(),
                bdAuditLog.getStatusId(),
                bdAuditLog.getContent(),
                bdAuditLog.getAddUserId(),
                bdAuditLog.getRemark(),
                bdAuditLog.getLevel()
        );
    }

    /**
     * 通过参数批量插入审核日志记录
     * @param list
     */
    public void insertBatch(final List<BdAuditLog> list) {
        String sql = "insert into `bd_audit_log`("+INSERT_COLUMNS+") values (?, ?, ?, ?, NOW(), ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, list.get(i).getTypeId());
                ps.setObject(2, list.get(i).getFormId());
                ps.setObject(3, list.get(i).getStatusId());
                ps.setObject(4, list.get(i).getContent());
                ps.setObject(5, list.get(i).getAddUserId());
                ps.setObject(6, list.get(i).getRemark());
                ps.setObject(7, list.get(i).getLevel());
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 更新审核日志记录
     * @param bdAuditLog
     * @return int
     */
    @Override
    public int update(BdAuditLog bdAuditLog) {
        String sql = "update `bd_audit_log` set `type_id` = :typeId, `form_id` = :formId, `status_id` = :statusId, `content` = :content, `add_time`= NOW(), `add_user_id` = :addUserId, `remark` = :remark, `level` = :level where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdAuditLog), Map.class));
    }

    @Override
    public RowMapper<BdAuditLog> getRowMapper() {
        RowMapper<BdAuditLog> rowMapper = (rs, i) -> {
            BdAuditLog bdAuditLog = new BdAuditLog();
            bdAuditLog.setPkid(rs.getInt("pkid"));
            bdAuditLog.setTypeId(rs.getInt("type_id"));
            bdAuditLog.setFormId(rs.getInt("form_id"));
            bdAuditLog.setStatusId(rs.getInt("status_id"));
            bdAuditLog.setContent(rs.getString("content"));
            bdAuditLog.setAddTime(rs.getTimestamp("add_time"));
            bdAuditLog.setAddUserId(rs.getInt("add_user_id"));
            bdAuditLog.setRemark(rs.getString("remark"));
            bdAuditLog.setLevel(rs.getInt("level"));
            return bdAuditLog;
        };
        return rowMapper;
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `bd_audit_log` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public List<BdAuditLog> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `bd_audit_log` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_audit_log` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public BdAuditLog getUserToauditLog(Integer auditUserId, Integer type, Integer formId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `bd_audit_log` ");
        sql.append("WHERE `form_id` = ? AND `type_id` = ? AND `status_id` = ? AND `add_user_id` = ? ");

        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), formId, type, status, auditUserId));
    }

    public int updateStatus(Integer pkid, Integer status, Integer oldStatus, String remark) {
        String sql = "UPDATE bd_audit_log SET status_id = ?, add_time = NOW(),`remark` = ? WHERE pkid = ? AND status_id = ? ";
        return getJdbcTemplate().update(sql, status, remark, pkid, oldStatus);
    }

    /**
     * 更新审核节点状态
     *
     * @param formId
     * @param typeId
     * @param exceptId
     * @param level
     * @param status
     * @param logic level比较运算符 >、>=、=、<、<=
     * @return
     */
    public int updateLevelStatus(Integer formId, Integer typeId, Integer exceptId, Integer level, Integer status, String logic) {
        String sql = "UPDATE bd_audit_log SET status_id = ? WHERE form_id = ? AND type_id = ? AND `level` " + logic + " ? ";

        List<Object> list = new ArrayList<>();
        list.add(status);
        list.add(formId);
        list.add(typeId);
        list.add(level);
        if (null != exceptId) {
            sql = sql + " AND pkid <> ? ";
            list.add(exceptId);
        }
        return getJdbcTemplate().update(sql, list.toArray());
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer oldStatus) {
        if (CollectionUtils.isEmpty(pkids)) {
            return 0;
        }

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE bd_audit_log ");
        sql.append("SET status_id = :status ");
        sql.append("WHERE pkid IN (:pkids) ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("pkids", pkids);

        if (null != oldStatus) {
            sql.append("AND status_id = :oldStatus ");
            source.addValue("oldStatus", oldStatus);
        }

        return getJdbcTemplate().update(sql.toString(), source);
    }

    public List<Integer> findFormIdsByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `bd_audit_log` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `add_time` DESC ");
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getFormIdsRowMapper());
    }

    public RowMapper<Integer> getFormIdsRowMapper() {
        RowMapper<Integer> rowMapper = (rs, i) -> rs.getInt("form_id");
        return rowMapper;
    }

    public int updateAuditStatus(int payId,int auditStatusId,String remark,int userId){
        Map<String,Object> map = new HashMap<>();
        StringBuilder update =new StringBuilder();
        update.append("update bd_audit_log");
        update.append(" set status_id = "+auditStatusId);
        update.append(" , remark = \""+remark+"\"");
        update.append(" where form_id = "+payId);
        update.append(" and add_user_id = "+userId);
        getJdbcTemplate().update(update.toString());

        String select = "select level from bd_audit_log where  form_id = "+payId+" and add_user_id = "+userId + " LIMIT 1";
        Integer level = getNamedParameterJdbcTemplate().queryForObject(select, map, Integer.class);

        String str ="update bd_audit_log set status_id = 1052 where form_id = "+payId+" and level = "+(level+1);
        int num = getJdbcTemplate().update(str);
        return num;
    }


}