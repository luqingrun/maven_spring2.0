package com.gongsibao.module.order.soorderitemprice.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderitemprice.entity.SoOrderItemPrice;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderItemPriceDao")
public class SoOrderItemPriceDao extends BaseDao<SoOrderItemPrice>{

    public static String INSERT_COLUMNS = " `order_item_id`, `price_id`, `unit_name`, `service_name`, `quantity`, `price` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderItemPrice soOrderItemPrice) {
        insertObject(soOrderItemPrice);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderItemPrice soOrderItemPrice) {
        getJdbcTemplate().update("insert into `so_order_item_price`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ? )",
                soOrderItemPrice.getOrderItemId(),
                soOrderItemPrice.getPriceId(),
                soOrderItemPrice.getUnitName(),
                soOrderItemPrice.getServiceName(),
                soOrderItemPrice.getQuantity(),
                soOrderItemPrice.getPrice()
                );
    }

    @Override
    public int update(SoOrderItemPrice soOrderItemPrice) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_item_price` set pkid = pkid, `order_item_id` = :orderItemId, `price_id` = :priceId, `unit_name` = :unitName, `service_name` = :serviceName, `quantity` = :quantity, `price` = :price where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderItemPrice),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_item_price` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderItemPrice findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_item_price` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderItemPrice> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_item_price` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderItemPrice> getRowMapper(){
        RowMapper<SoOrderItemPrice> rowMapper = (rs, i) -> {
            SoOrderItemPrice soOrderItemPrice = new SoOrderItemPrice();
            soOrderItemPrice.setPkid(rs.getInt("pkid"));
            soOrderItemPrice.setOrderItemId(rs.getInt("order_item_id"));
            soOrderItemPrice.setPriceId(rs.getInt("price_id"));
            soOrderItemPrice.setUnitName(rs.getString("unit_name"));
            soOrderItemPrice.setServiceName(rs.getString("service_name"));
            soOrderItemPrice.setQuantity(rs.getInt("quantity"));
            soOrderItemPrice.setPrice(rs.getInt("price"));
            return soOrderItemPrice;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderItemPrice> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_item_price` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_item_price` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}