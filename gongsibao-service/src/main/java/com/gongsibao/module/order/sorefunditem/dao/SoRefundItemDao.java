package com.gongsibao.module.order.sorefunditem.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.order.sorefunditem.entity.SoRefundItem;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("soRefundItemDao")
public class SoRefundItemDao extends BaseDao<SoRefundItem> {

    public static String INSERT_COLUMNS = " `refund_id`, `order_id`, `order_prod_id`, `order_prod_item_id`, `amount`, `cost` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoRefundItem soRefundItem) {
        insertObject(soRefundItem);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoRefundItem soRefundItem) {
        getJdbcTemplate().update("insert into `so_refund_item`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ? )",
                soRefundItem.getRefundId(),
                soRefundItem.getOrderId(),
                soRefundItem.getOrderProdId(),
                soRefundItem.getOrderProdItemId(),
                soRefundItem.getAmount(),
                soRefundItem.getCost()
        );
    }

    @Override
    public int update(SoRefundItem soRefundItem) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_refund_item` set pkid = pkid, `refund_id` = :refundId, `order_id` = :orderId, `order_prod_id` = :orderProdId, `order_prod_item_id` = :orderProdItemId, `amount` = :amount, `cost` = :cost where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soRefundItem),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_refund_item` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoRefundItem findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `so_refund_item` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoRefundItem> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_refund_item` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoRefundItem> getRowMapper() {
        RowMapper<SoRefundItem> rowMapper = (rs, i) -> {
            SoRefundItem soRefundItem = new SoRefundItem();
            soRefundItem.setPkid(rs.getInt("pkid"));
            soRefundItem.setRefundId(rs.getInt("refund_id"));
            soRefundItem.setOrderId(rs.getInt("order_id"));
            soRefundItem.setOrderProdId(rs.getInt("order_prod_id"));
            soRefundItem.setOrderProdItemId(rs.getInt("order_prod_item_id"));
            soRefundItem.setAmount(rs.getInt("amount"));
            soRefundItem.setCost(rs.getInt("cost"));
            return soRefundItem;
        };
        return rowMapper;
    }

    @Override
    public List<SoRefundItem> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_refund_item` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_refund_item` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 通过参数批量插入退单商品记录
     *
     * @param list
     */
    public void insertBatch(final List<SoRefundItem> list) {
        String sql = "insert into `so_refund_item`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, list.get(i).getRefundId());
                ps.setObject(2, list.get(i).getOrderId());
                ps.setObject(3, list.get(i).getOrderProdId());
                ps.setObject(4, list.get(i).getOrderProdItemId());
                ps.setObject(5, list.get(i).getAmount());
                ps.setObject(6, list.get(i).getCost());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    public List<SoRefundItem> getListByRefundId(Integer refundId) {
        String sql = "select " + ALL_COLUMNS + " from `so_refund_item` where refund_id = ? ";
        return getJdbcTemplate().query(sql, getRowMapper(), refundId);
    }
}