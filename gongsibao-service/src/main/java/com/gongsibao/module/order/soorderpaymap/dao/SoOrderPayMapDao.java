package com.gongsibao.module.order.soorderpaymap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderpaymap.entity.SoOrderPayMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderPayMapDao")
public class SoOrderPayMapDao extends BaseDao<SoOrderPayMap>{

    public static String INSERT_COLUMNS = " `order_id`, `pay_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderPayMap soOrderPayMap) {
        insertObject(soOrderPayMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderPayMap soOrderPayMap) {
        getJdbcTemplate().update("insert into `so_order_pay_map`("+INSERT_COLUMNS+") values (?, ? )",
                soOrderPayMap.getOrderId(),
                soOrderPayMap.getPayId()
                );
    }

    @Override
    public int update(SoOrderPayMap soOrderPayMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_pay_map` set pkid = pkid, `order_id` = :orderId, `pay_id` = :payId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderPayMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_pay_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderPayMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_pay_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderPayMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_pay_map` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderPayMap> getRowMapper(){
        RowMapper<SoOrderPayMap> rowMapper = (rs, i) -> {
            SoOrderPayMap soOrderPayMap = new SoOrderPayMap();
            soOrderPayMap.setPkid(rs.getInt("pkid"));
            soOrderPayMap.setOrderId(rs.getInt("order_id"));
            soOrderPayMap.setPayId(rs.getInt("pay_id"));
            return soOrderPayMap;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderPayMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_pay_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_pay_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<SoOrderPayMap> findByOrderId(int orderId) {
        Map<String,Object> map = new HashMap<>();
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_pay_map`");
        sql.append("where order_id = " + orderId);
        return getNamedParameterJdbcTemplate().query(sql.toString(), map, getRowMapper());
    }

    public List<Integer> findPayIds(int orderId) {
        List<SoOrderPayMap> orderPayMap = findByOrderId(orderId);
        List<Integer> payIdList = new ArrayList<>();
        if(orderPayMap!=null){
            for (SoOrderPayMap opm :orderPayMap) {
                payIdList.add(opm.getPayId());
            }
        }
        return payIdList;
    }
}