package com.gongsibao.module.order.soorderitem.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderitem.entity.SoOrderItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderItemDao")
public class SoOrderItemDao extends BaseDao<SoOrderItem>{

    public static String INSERT_COLUMNS = " `order_id`, `product_id`, `city_id`, `process_status_id`, `audit_status_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderItem soOrderItem) {
        insertObject(soOrderItem);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderItem soOrderItem) {
        getJdbcTemplate().update("insert into `so_order_item`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ? )",
                soOrderItem.getOrderId(),
                soOrderItem.getProductId(),
                soOrderItem.getCityId(),
                soOrderItem.getProcessStatusId(),
                soOrderItem.getAuditStatusId()
                );
    }

    @Override
    public int update(SoOrderItem soOrderItem) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_item` set pkid = pkid, `order_id` = :orderId, `product_id` = :productId, `city_id` = :cityId, `process_status_id` = :processStatusId, `audit_status_id` = :auditStatusId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderItem),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_item` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderItem findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_item` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderItem> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_item` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderItem> getRowMapper(){
        RowMapper<SoOrderItem> rowMapper = (rs, i) -> {
            SoOrderItem soOrderItem = new SoOrderItem();
            soOrderItem.setPkid(rs.getInt("pkid"));
            soOrderItem.setOrderId(rs.getInt("order_id"));
            soOrderItem.setProductId(rs.getInt("product_id"));
            soOrderItem.setCityId(rs.getInt("city_id"));
            soOrderItem.setProcessStatusId(rs.getInt("process_status_id"));
            soOrderItem.setAuditStatusId(rs.getInt("audit_status_id"));
            return soOrderItem;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderItem> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_item` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_item` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

}