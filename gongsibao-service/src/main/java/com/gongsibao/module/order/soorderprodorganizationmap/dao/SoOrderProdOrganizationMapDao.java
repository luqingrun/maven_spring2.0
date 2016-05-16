package com.gongsibao.module.order.soorderprodorganizationmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.soorderprodorganizationmap.entity.SoOrderProdOrganizationMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderProdOrganizationMapDao")
public class SoOrderProdOrganizationMapDao extends BaseDao<SoOrderProdOrganizationMap>{

    public static String INSERT_COLUMNS = " `order_prod_id`, `organization_id`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        insertObject(soOrderProdOrganizationMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        getJdbcTemplate().update("insert into `so_order_prod_organization_map`("+INSERT_COLUMNS+") values (?, ?, ? )",
                soOrderProdOrganizationMap.getOrderProdId(),
                soOrderProdOrganizationMap.getOrganizationId(),
                soOrderProdOrganizationMap.getAddTime()
                );
    }

    @Override
    public int update(SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod_organization_map` set pkid = pkid, `order_prod_id` = :orderProdId, `organization_id` = :organizationId, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdOrganizationMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_organization_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderProdOrganizationMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_prod_organization_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderProdOrganizationMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_organization_map` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdOrganizationMap> getRowMapper(){
        RowMapper<SoOrderProdOrganizationMap> rowMapper = (rs, i) -> {
            SoOrderProdOrganizationMap soOrderProdOrganizationMap = new SoOrderProdOrganizationMap();
            soOrderProdOrganizationMap.setPkid(rs.getInt("pkid"));
            soOrderProdOrganizationMap.setOrderProdId(rs.getInt("order_prod_id"));
            soOrderProdOrganizationMap.setOrganizationId(rs.getInt("organization_id"));
            soOrderProdOrganizationMap.setAddTime(rs.getTimestamp("add_time"));
            return soOrderProdOrganizationMap;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProdOrganizationMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_prod_organization_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_organization_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }


    public int updateByProdId(int productId,int organizationId) {
        String sql = "update so_order_prod_organization_map set organization_id = "+organizationId + " where order_prod_id = "+productId;
        return getJdbcTemplate().update(sql);
    }

    public int deleteByProdId(int productId) {
        String sql = "delete from so_order_prod_organization_map where order_prod_id = " + productId;
        return getJdbcTemplate().update(sql);
    }

    public List<Integer> queryOrderProdIdsByOrganizationIds(Collection<Integer> organizationIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT order_prod_id ");
        sql.append("FROM ");
        sql.append("so_order_prod_organization_map ");
        sql.append("WHERE 1=1 ");
        if(CollectionUtils.isNotEmpty(organizationIds)) {
            sql.append("AND organization_id IN ("+ StringUtils.join(organizationIds, ",")+") ");
        }
        return getJdbcTemplate().queryForList(sql.toString(), Integer.class);

    }
}