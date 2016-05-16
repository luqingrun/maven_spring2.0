package com.gongsibao.module.order.soorderprodtrace.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("soOrderProdTraceDao")
public class SoOrderProdTraceDao extends BaseDao<SoOrderProdTrace>{

    public static String INSERT_COLUMNS = " `order_prod_id`, `order_prod_status_id`, `type_id`, `info`, `operator_id`, `add_time`, `remark`, `is_send_sms`, `express_content`, `express_to`, `express_company_name`, `express_no`, `processd_days`, `timeout_days` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdTrace soOrderProdTrace) {
        insertObject(soOrderProdTrace);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdTrace soOrderProdTrace) {
        getJdbcTemplate().update("insert into `so_order_prod_trace`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soOrderProdTrace.getOrderProdId(),
                soOrderProdTrace.getOrderProdStatusId(),
                soOrderProdTrace.getTypeId(),
                soOrderProdTrace.getInfo(),
                soOrderProdTrace.getOperatorId(),
                soOrderProdTrace.getAddTime(),
                soOrderProdTrace.getRemark(),
                soOrderProdTrace.getIsSendSms(),
                soOrderProdTrace.getExpressContent(),
                soOrderProdTrace.getExpressTo(),
                soOrderProdTrace.getExpressCompanyName(),
                soOrderProdTrace.getExpressNo(),
                soOrderProdTrace.getProcessdDays(),
                soOrderProdTrace.getTimeoutDays()
                );
    }

    @Override
    public int update(SoOrderProdTrace soOrderProdTrace) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod_trace` set pkid = pkid, `order_prod_id` = :orderProdId, `order_prod_status_id` = :orderProdStatusId, `type_id` = :typeId, `info` = :info, `file_id` = :fileId, `operator_id` = :operatorId, `add_time` = :addTime, `remark` = :remark, `is_send_sms` = :isSendSms, `express_content` = :expressContent, `express_to` = :expressTo, `express_company_name` = :expressCompanyName, `express_no` = :expressNo where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdTrace),Map.class));
    }

    public int updateJobDays(Integer orderprodid, int timeoutDays) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod_trace ");
        sql.append("SET ");
        sql.append("processd_days = processd_days+1, ");
        sql.append("timeout_days = ? ");
        sql.append("WHERE ");
        sql.append("order_prod_id = ? ");
        sql.append("ORDER BY add_time DESC ");
        sql.append("LIMIT 1 ");

        return getJdbcTemplate().update(sql.toString(), timeoutDays, orderprodid);
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_trace` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderProdTrace findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<SoOrderProdTrace> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdTrace> getRowMapper(){
        RowMapper<SoOrderProdTrace> rowMapper = (rs, i) -> {
            SoOrderProdTrace soOrderProdTrace = new SoOrderProdTrace();
            soOrderProdTrace.setPkid(rs.getInt("pkid"));
            soOrderProdTrace.setOrderProdId(rs.getInt("order_prod_id"));
            soOrderProdTrace.setOrderProdStatusId(rs.getInt("order_prod_status_id"));
            soOrderProdTrace.setTypeId(rs.getInt("type_id"));
            soOrderProdTrace.setInfo(rs.getString("info"));
            soOrderProdTrace.setOperatorId(rs.getInt("operator_id"));
            soOrderProdTrace.setAddTime(rs.getTimestamp("add_time"));
            soOrderProdTrace.setRemark(rs.getString("remark"));
            soOrderProdTrace.setIsSendSms(rs.getInt("is_send_sms"));
            soOrderProdTrace.setExpressContent(rs.getString("express_content"));
            soOrderProdTrace.setExpressTo(rs.getString("express_to"));
            soOrderProdTrace.setExpressCompanyName(rs.getString("express_company_name"));
            soOrderProdTrace.setExpressNo(rs.getString("express_no"));
            soOrderProdTrace.setProcessdDays(rs.getInt("processd_days"));
            soOrderProdTrace.setTimeoutDays(rs.getInt("timeout_days"));
            return soOrderProdTrace;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProdTrace> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_prod_trace` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY pkid DESC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_trace` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<Integer> queryOrderProdTraceIds(Integer orderProdId, Integer typeId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("pkid ");
        sql.append("FROM ");
        sql.append("so_order_prod_trace ");
        sql.append("WHERE ");
        sql.append("order_prod_id = ? ");
        sql.append("AND ");
        sql.append("type_id = ? ");

        return this.getJdbcTemplate().queryForList(sql.toString(), Integer.class, orderProdId, typeId);
    }

    public int queryTraceProcessdDays(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("SUM(processd_days) AS sumDays ");
        sql.append("FROM ");
        sql.append("so_order_prod_trace ");
        sql.append("WHERE 1=1 ");
        buildSQL(properties, sql);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 根据产品订单Id ,查询最近订单状态
     */
    public SoOrderProdTrace findLatestStatusByOrderProdId(Integer orderProdId) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_trace` where order_prod_id = " + orderProdId +"  ORDER BY add_time DESC ", getRowMapper()));
    }
}