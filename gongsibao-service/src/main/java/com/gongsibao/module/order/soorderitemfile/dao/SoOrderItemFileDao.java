package com.gongsibao.module.order.soorderitemfile.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderitemfile.entity.SoOrderItemFile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderItemFileDao")
public class SoOrderItemFileDao extends BaseDao<SoOrderItemFile>{

    public static String INSERT_COLUMNS = " `order_prod_trace_id`, `prod_workflow_file_id`, `audit_status_id`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderItemFile soOrderItemFile) {
        insertObject(soOrderItemFile);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderItemFile soOrderItemFile) {
        getJdbcTemplate().update("insert into `so_order_item_file`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ? )",
                soOrderItemFile.getOrderProdTraceId(),
                soOrderItemFile.getProdWorkflowFileId(),
                soOrderItemFile.getAuditStatusId(),
                soOrderItemFile.getAddTime(),
                soOrderItemFile.getAddUserId(),
                soOrderItemFile.getRemark()
                );
    }

    @Override
    public int update(SoOrderItemFile soOrderItemFile) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_item_file` set pkid = pkid, `order_prod_trace_id` = :orderProdTraceId, `prod_workflow_file_id` = :prodWorkflowFileId, `audit_status_id` = :auditStatusId, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderItemFile),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_item_file` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderItemFile findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_item_file` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderItemFile> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_item_file` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderItemFile> getRowMapper(){
        RowMapper<SoOrderItemFile> rowMapper = (rs, i) -> {
            SoOrderItemFile soOrderItemFile = new SoOrderItemFile();
            soOrderItemFile.setPkid(rs.getInt("pkid"));
            soOrderItemFile.setOrderProdTraceId(rs.getInt("order_prod_trace_id"));
            soOrderItemFile.setProdWorkflowFileId(rs.getInt("prod_workflow_file_id"));
            soOrderItemFile.setAuditStatusId(rs.getInt("audit_status_id"));
            soOrderItemFile.setAddTime(rs.getTimestamp("add_time"));
            soOrderItemFile.setAddUserId(rs.getInt("add_user_id"));
            soOrderItemFile.setRemark(rs.getString("remark"));
            return soOrderItemFile;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderItemFile> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_item_file` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_item_file` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}