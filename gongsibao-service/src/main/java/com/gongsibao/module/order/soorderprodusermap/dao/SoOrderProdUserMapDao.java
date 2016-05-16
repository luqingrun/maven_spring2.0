package com.gongsibao.module.order.soorderprodusermap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.soorder.entity.OrderBusiness;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("soOrderProdUserMapDao")
public class SoOrderProdUserMapDao extends BaseDao<SoOrderProdUserMap> {

    public static String INSERT_COLUMNS = " `user_id`, `order_prod_id`, `type_id`, `status_id`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdUserMap soOrderProdUserMap) {
        insertObject(soOrderProdUserMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdUserMap soOrderProdUserMap) {
        getJdbcTemplate().update("insert into `so_order_prod_user_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ? )",
                soOrderProdUserMap.getUserId(),
                soOrderProdUserMap.getOrderProdId(),
                soOrderProdUserMap.getTypeId(),
                soOrderProdUserMap.getStatusId(),
                soOrderProdUserMap.getAddTime()
        );
    }

    public int[] insertBatch(List<SoOrderProdUserMap> list) {
        String sql = "insert into `so_order_prod_user_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, NOW() )";
        return getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SoOrderProdUserMap map = list.get(i);
                ps.setInt(1, map.getUserId());
                ps.setInt(2, map.getOrderProdId());
                ps.setInt(3, map.getTypeId());
                ps.setInt(4, map.getStatusId());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public int update(SoOrderProdUserMap soOrderProdUserMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod_user_map` set pkid = pkid, `user_id` = :userId, `order_prod_id` = :orderProdId, `type_id` = :typeId, `status_id` = :statusId, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdUserMap),Map.class));
    }

    public int updateStatus(Integer pkid, int newStatus, int oldStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod_user_map ");
        sql.append("SET ");
        sql.append("status_id = ? ");
        sql.append("WHERE ");
        sql.append("pkid = ? ");
        sql.append("AND ");
        sql.append("status_id = ? ");

        return getJdbcTemplate().update(sql.toString(), newStatus, pkid, oldStatus);
    }

    public int updateStatusByOrderProdId(Integer orderProdId, Integer typeId, int newStatus, int oldStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod_user_map ");
        sql.append("SET ");
        sql.append("status_id = ? ");
        sql.append("WHERE ");
        sql.append("order_prod_id = ? ");
        sql.append("AND ");
        sql.append("type_id = ? ");
        sql.append("AND ");
        sql.append("status_id = ? ");

        return getJdbcTemplate().update(sql.toString(), newStatus, orderProdId, typeId, oldStatus);
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_user_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    public int deleteInfo(Integer userId, Integer orderProdId, Integer typeId) {
        return getJdbcTemplate().update("delete from `so_order_prod_user_map` where user_id = "+userId+" AND order_prod_id = "+orderProdId+" AND type_id = "+typeId);
    }

    @Override
    public SoOrderProdUserMap findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where pkid = " + pkid, getRowMapper()));
    }

    public SoOrderProdUserMap findByOrderId(Integer orderId) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where order_prod_id = " + orderId + " and status_id = 3141", getRowMapper()));
    }

    @Override
    public List<SoOrderProdUserMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    public List<SoOrderProdUserMap> findListByIds(List<Integer> pkidList, Integer typeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        map.put("typeId",typeId);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdUserMap> getRowMapper() {
        RowMapper<SoOrderProdUserMap> rowMapper = (rs, i) -> {
            SoOrderProdUserMap soOrderProdUserMap = new SoOrderProdUserMap();
            soOrderProdUserMap.setPkid(rs.getInt("pkid"));
            soOrderProdUserMap.setUserId(rs.getInt("user_id"));
            soOrderProdUserMap.setOrderProdId(rs.getInt("order_prod_id"));
            soOrderProdUserMap.setTypeId(rs.getInt("type_id"));
            soOrderProdUserMap.setStatusId(rs.getInt("status_id"));
            soOrderProdUserMap.setAddTime(rs.getTimestamp("add_time"));
            return soOrderProdUserMap;
        };
        return rowMapper;
    }

    public RowMapper<OrderBusiness> getBusinessNameRowMapper() {
        RowMapper<OrderBusiness> rowMapper = (rs, i) -> {
            OrderBusiness orderBusiness = new OrderBusiness();
            orderBusiness.setOrderId(rs.getInt("so_order_prod.order_id"));
            orderBusiness.setBusinessName(rs.getString("name"));
            return orderBusiness;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProdUserMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where 1=1 ");
        buildSQL(properties, sql);
        if(start >= 0 && pageSize > 0) {
            sql.append(" limit ").append(start).append(", ").append(pageSize);
        }
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    /***
     * 根据orderid集合得到所有相关业务员姓名
     *
     * @return
     */
    public Map<Integer, String> findBusinessNameByOrderId(List<Integer> orderIdList) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT so_order_prod.order_id , group_concat(uc_user.real_name) as name ");
        sql.append(" FROM `uc_user` ");
        sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.user_id = uc_user.pkid ");
        // 设置连表查询
        sql.append(" INNER JOIN `so_order_prod` ON so_order_prod.pkid = so_order_prod_user_map.order_prod_id ");
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        sql.append(" AND so_order_prod.order_id IN (:orderIdList)");
        sql.append(" GROUP BY so_order_prod.order_id");
        Map<String, Object> map = new HashMap<>();
        map.put("orderIdList", orderIdList);
        List<OrderBusiness> orderBusinesses = getNamedParameterJdbcTemplate().query(sql.toString(), map, getBusinessNameRowMapper());
        Map<Integer, String> businessMap = new HashMap<>();
        for (OrderBusiness o : orderBusinesses) {
            businessMap.put(o.getOrderId(), o.getBusinessName());
        }
        return businessMap;
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_user_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int deleteByOrderProdIdsAndType(Collection<Integer> orderProdIds, Integer type) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM `so_order_prod_user_map` ");
        sql.append("WHERE order_prod_id IN (:orderProdIds) AND type_id = :type");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("orderProdIds", orderProdIds);
        source.addValue("type", type);

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }
    public List<SoOrderProdUserMap> findListByProperties(Map<String, Object> properties, Collection<Integer> userIds, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod_user_map` where 1=1 ");
        buildSQL(properties, sql);
        if(CollectionUtils.isEmpty(userIds)) {
            sql.append(" ORDER BY add_time DESC ");
        } else {
            sql.append(" ORDER BY FIND_IN_SET(user_id,'"+ StringUtils.join(userIds, ",")+"') ");
        }
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    public List<Integer> queryUserIdsByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("user_id ");
        sql.append("FROM ");
        sql.append("so_order_prod_user_map ");
        sql.append("WHERE 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY add_time DESC ");

        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), properties, Integer.class);
    }

    public List<SoOrderProdUserMap> findByOderProdIdAndTypeId(Integer orderProdId, int typeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_prod_id", orderProdId);
        map.put("type_id",typeId);
        String sql = "select " + ALL_COLUMNS + " from `so_order_prod_user_map` where order_prod_id=:order_prod_id AND type_id=:type_id";
        return getNamedParameterJdbcTemplate().query(sql, map, getRowMapper());
    }

    public List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT order_prod_id ");
        sql.append("FROM ");
        sql.append("so_order_prod_user_map ");
        sql.append("WHERE 1=1 ");
        if(NumberUtils.toInt(properties.get("userId")) > 0) {
            sql.append("AND user_id = :userId ");
        }
        if(NumberUtils.toInt(properties.get("typeId")) > 0) {
            sql.append("AND type_id = :typeId ");
        }
        if(NumberUtils.toInt(properties.get("statusId")) > 0) {
            sql.append("AND status_id = :statusId ");
        }
        if(NumberUtils.toInt(properties.get("noStatusId")) > 0) {
            sql.append("AND status_id != :noStatusId ");
        }
        List<Integer> orderProdIds = (List<Integer>) properties.get("orderProdIds");
        if(CollectionUtils.isNotEmpty(orderProdIds)) {
            sql.append("AND order_prod_id IN ("+StringUtils.join(orderProdIds, ",")+") ");
        }
        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), properties, Integer.class);
    }

}