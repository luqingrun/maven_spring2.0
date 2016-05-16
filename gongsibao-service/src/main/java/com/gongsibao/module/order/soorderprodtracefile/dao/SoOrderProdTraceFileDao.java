package com.gongsibao.module.order.soorderprodtracefile.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderProdTraceFileDao")
public class SoOrderProdTraceFileDao extends BaseDao<SoOrderProdTraceFile>{

    public static String INSERT_COLUMNS = " `order_prod_trace_id`, `prod_workflow_file_id`, `prod_workflow_file_name`, `file_id`, `is_new`, `audit_status_id`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdTraceFile soOrderProdTraceFile) {
        insertObject(soOrderProdTraceFile);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdTraceFile soOrderProdTraceFile) {
        getJdbcTemplate().update("insert into `so_order_prod_trace_file`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soOrderProdTraceFile.getOrderProdTraceId(),
                soOrderProdTraceFile.getProdWorkflowFileId(),
                soOrderProdTraceFile.getProdWorkflowFileName(),
                soOrderProdTraceFile.getFileId(),
                soOrderProdTraceFile.getIsNew(),
                soOrderProdTraceFile.getAuditStatusId(),
                soOrderProdTraceFile.getAddTime(),
                soOrderProdTraceFile.getAddUserId(),
                soOrderProdTraceFile.getRemark()
                );
    }

    @Override
    public int update(SoOrderProdTraceFile soOrderProdTraceFile) {
        //TODO,需要自己决定如何实现
        throw new UnsupportedOperationException();
        //String sql = "update `so_order_prod_trace_file` set pkid = pkid, `order_prod_trace_id` = :orderProdTraceId, `prod_workflow_file_id` = :prodWorkflowFileId, `prod_workflow_file_name` = :prodWorkflowFileName, `audit_status_id` = :auditStatusId, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdTraceFile),Map.class));
    }

    public int updateAuditStatus(Collection<Integer> pkids, int newStatus, int oldStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod_trace_file ");
        sql.append("SET ");
        sql.append("audit_status_id = ? ");
        sql.append("WHERE ");
        sql.append("pkid IN ("+ StringUtils.join(pkids, ",")+") ");
        sql.append("AND ");
        sql.append("audit_status_id = ? ");

        return getJdbcTemplate().update(sql.toString(), newStatus, oldStatus);
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_trace_file` where pkid = "+id);
        throw new UnsupportedOperationException();
    }

    @Override
    public SoOrderProdTraceFile findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace_file` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<SoOrderProdTraceFile> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace_file` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdTraceFile> getRowMapper(){
        RowMapper<SoOrderProdTraceFile> rowMapper = (rs, i) -> {
            SoOrderProdTraceFile soOrderProdTraceFile = new SoOrderProdTraceFile();
            soOrderProdTraceFile.setPkid(rs.getInt("pkid"));
            soOrderProdTraceFile.setOrderProdTraceId(rs.getInt("order_prod_trace_id"));
            soOrderProdTraceFile.setProdWorkflowFileId(rs.getInt("prod_workflow_file_id"));
            soOrderProdTraceFile.setProdWorkflowFileName(rs.getString("prod_workflow_file_name"));
            soOrderProdTraceFile.setFileId(rs.getInt("file_id"));
            soOrderProdTraceFile.setIsNew(rs.getInt("is_new"));
            soOrderProdTraceFile.setAuditStatusId(rs.getInt("audit_status_id"));
            soOrderProdTraceFile.setAddTime(rs.getTimestamp("add_time"));
            soOrderProdTraceFile.setAddUserId(rs.getInt("add_user_id"));
            soOrderProdTraceFile.setRemark(rs.getString("remark"));
            return soOrderProdTraceFile;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProdTraceFile> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_prod_trace_file` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY pkid DESC ");
        if(start >= 0 && pageSize > 0) {
            sql.append(" limit ").append(start).append(", ").append(pageSize);
        }
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_trace_file` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public Map<Integer, Integer> queryTraceFileMap(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("prod_workflow_file_id, ");
        sql.append("COUNT(1) AS num ");
        sql.append("FROM ");
        sql.append("so_order_prod_trace_file ");
        sql.append("WHERE 1=1 ");

        buildSQL(properties, sql);
        sql.append(" GROUP BY ");
        sql.append("prod_workflow_file_id ");

        List<Map<Integer, Integer>> list = getNamedParameterJdbcTemplate().query(sql.toString(), properties, new RowMapper<Map<Integer, Integer>>() {
            @Override
            public Map<Integer, Integer> mapRow (ResultSet rs, int rowNum) throws SQLException {
                Map<Integer, Integer> map = new HashMap<>();
                map.put(rs.getInt("prod_workflow_file_id"), rs.getInt("num"));
                return map;
            }
        });

        Map<Integer, Integer> result = new HashMap<>();
        if(!CollectionUtils.isEmpty(list)) {
            for(Map<Integer, Integer> map : list) {
                result.putAll(map);
            }
        }
        return result;
    }

    public List<SoOrderProdTraceFile> findByOrderProdTraceId(List<Integer> pkidList, SoOrderProdTraceFile soOrderProdTraceFile)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        map.put("prodWorkflowFileId", soOrderProdTraceFile.getProdWorkflowFileId());
        map.put("prodWorkflowFileName", soOrderProdTraceFile.getProdWorkflowFileName());
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace_file` where order_prod_trace_id IN (:pkidList) and prod_workflow_file_id = :prodWorkflowFileId and prod_workflow_file_name = :prodWorkflowFileName " , map , getRowMapper());
    }

    public List<Integer> findProdWorkflowFileIdByOrderProdTraceId(List<Integer> pkidList)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT ");
        sql.append("prod_workflow_file_id ");
        sql.append("FROM ");
        sql.append("`so_order_prod_trace_file` ");
        sql.append("WHERE ");
        sql.append("order_prod_trace_id IN (" + StringUtils.join(pkidList, ",") + ") ");

        return this.getJdbcTemplate().queryForList(sql.toString(), Integer.class);
    }

    public int updateIsNew(List<Integer> pkidList)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("pkid", pkidList);
        String sql = "update `so_order_prod_trace_file` set `is_new` = 2 where pkid in (:pkid) ";
        return getNamedParameterJdbcTemplate().update(sql, map);
    }
}