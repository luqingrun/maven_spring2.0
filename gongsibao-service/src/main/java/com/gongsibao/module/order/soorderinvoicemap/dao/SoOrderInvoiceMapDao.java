package com.gongsibao.module.order.soorderinvoicemap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderinvoicemap.entity.SoOrderInvoiceMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderInvoiceMapDao")
public class SoOrderInvoiceMapDao extends BaseDao<SoOrderInvoiceMap>{

    public static String INSERT_COLUMNS = " `order_id`, `invoice_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderInvoiceMap soOrderInvoiceMap) {
        insertObject(soOrderInvoiceMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderInvoiceMap soOrderInvoiceMap) {
        getJdbcTemplate().update("insert into `so_order_invoice_map`("+INSERT_COLUMNS+") values (?, ? )",
                soOrderInvoiceMap.getOrderId(),
                soOrderInvoiceMap.getInvoiceId()
                );
    }

    @Override
    public int update(SoOrderInvoiceMap soOrderInvoiceMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_invoice_map` set pkid = pkid, `order_id` = :orderId, `invoice_id` = :invoiceId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderInvoiceMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_invoice_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderInvoiceMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_invoice_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderInvoiceMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_invoice_map` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderInvoiceMap> getRowMapper(){
        RowMapper<SoOrderInvoiceMap> rowMapper = (rs, i) -> {
            SoOrderInvoiceMap soOrderInvoiceMap = new SoOrderInvoiceMap();
            soOrderInvoiceMap.setPkid(rs.getInt("pkid"));
            soOrderInvoiceMap.setOrderId(rs.getInt("order_id"));
            soOrderInvoiceMap.setInvoiceId(rs.getInt("invoice_id"));
            return soOrderInvoiceMap;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderInvoiceMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_invoice_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_invoice_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}