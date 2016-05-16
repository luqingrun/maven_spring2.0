package com.gongsibao.module.order.soorderitemusermap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderitemusermap.entity.SoOrderItemUserMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderItemUserMapDao")
public class SoOrderItemUserMapDao extends BaseDao<SoOrderItemUserMap>{

    public static String INSERT_COLUMNS = " `user_id`, `order_item_id`, `type_id`, `status_id`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderItemUserMap soOrderItemUserMap) {
        insertObject(soOrderItemUserMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderItemUserMap soOrderItemUserMap) {
        getJdbcTemplate().update("insert into `so_order_item_user_map`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ? )",
                soOrderItemUserMap.getUserId(),
                soOrderItemUserMap.getOrderItemId(),
                soOrderItemUserMap.getTypeId(),
                soOrderItemUserMap.getStatusId(),
                soOrderItemUserMap.getAddTime()
                );
    }

    @Override
    public int update(SoOrderItemUserMap soOrderItemUserMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_item_user_map` set pkid = pkid, `user_id` = :userId, `order_item_id` = :orderItemId, `type_id` = :typeId, `status_id` = :statusId, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderItemUserMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_item_user_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderItemUserMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_item_user_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderItemUserMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_item_user_map` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderItemUserMap> getRowMapper(){
        RowMapper<SoOrderItemUserMap> rowMapper = (rs, i) -> {
            SoOrderItemUserMap soOrderItemUserMap = new SoOrderItemUserMap();
            soOrderItemUserMap.setPkid(rs.getInt("pkid"));
            soOrderItemUserMap.setUserId(rs.getInt("user_id"));
            soOrderItemUserMap.setOrderItemId(rs.getInt("order_item_id"));
            soOrderItemUserMap.setTypeId(rs.getInt("type_id"));
            soOrderItemUserMap.setStatusId(rs.getInt("status_id"));
            soOrderItemUserMap.setAddTime(rs.getTimestamp("add_time"));
            return soOrderItemUserMap;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderItemUserMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_item_user_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_item_user_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}