package com.gongsibao.module.order.sorefunditemprice.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.sorefunditemprice.entity.SoRefundItemPrice;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soRefundItemPriceDao")
public class SoRefundItemPriceDao extends BaseDao<SoRefundItemPrice>{

    public static String INSERT_COLUMNS = " `refund_id`, `order_prod_item_id`, `amount`, `cost` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoRefundItemPrice soRefundItemPrice) {
        insertObject(soRefundItemPrice);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoRefundItemPrice soRefundItemPrice) {
        getJdbcTemplate().update("insert into `so_refund_item_price`("+INSERT_COLUMNS+") values (?, ?, ?, ? )",
                soRefundItemPrice.getRefundId(),
                soRefundItemPrice.getOrderProdItemId(),
                soRefundItemPrice.getAmount(),
                soRefundItemPrice.getCost()
                );
    }

    @Override
    public int update(SoRefundItemPrice soRefundItemPrice) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_refund_item_price` set pkid = pkid, `refund_id` = :refundId, `order_item_price_id` = :orderItemPriceId, `amount` = :amount, `cost` = :cost where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soRefundItemPrice),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_refund_item_price` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoRefundItemPrice findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_refund_item_price` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoRefundItemPrice> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_refund_item_price` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoRefundItemPrice> getRowMapper(){
        RowMapper<SoRefundItemPrice> rowMapper = (rs, i) -> {
            SoRefundItemPrice soRefundItemPrice = new SoRefundItemPrice();
            soRefundItemPrice.setPkid(rs.getInt("pkid"));
            soRefundItemPrice.setRefundId(rs.getInt("refund_id"));
            soRefundItemPrice.setOrderProdItemId(rs.getInt("order_prod_item_id"));
            soRefundItemPrice.setAmount(rs.getInt("amount"));
            soRefundItemPrice.setCost(rs.getInt("cost"));
            return soRefundItemPrice;
        };
        return rowMapper;
    }

    @Override
    public List<SoRefundItemPrice> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_refund_item_price` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_refund_item_price` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }


    /**
     * 通过参数批量插入退单商品记录
     *
     * @param list
     */
    public void insertBatch(final List<SoRefundItemPrice> list) {
        String sql = "insert into `so_refund_item_price`("+INSERT_COLUMNS+") values (?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, list.get(i).getRefundId());
                ps.setObject(2, list.get(i).getOrderProdItemId());
                ps.setObject(3, list.get(i).getAmount());
                ps.setObject(4, list.get(i).getCost());
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }



}