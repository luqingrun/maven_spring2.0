package com.gongsibao.module.product.prodservice.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodServiceDao")
public class ProdServiceDao extends BaseDao<ProdService>{

    public static String INSERT_COLUMNS = " `product_id`, `unit_id`, `property_id`, `type_id`, `description`, `sort`, `has_stock`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdService prodService) {
        insertObject(prodService);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdService prodService) {
        getJdbcTemplate().update("insert into `prod_service`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                prodService.getProductId(),
                prodService.getUnitId(),
                prodService.getPropertyId(),
                prodService.getTypeId(),
                prodService.getDescription(),
                prodService.getSort(),
                prodService.getHasStock(),
                prodService.getIsEnabled(),
                prodService.getAddTime(),
                prodService.getAddUserId(),
                prodService.getRemark()
                );
    }

    @Override
    public int update(ProdService prodService) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_service` set pkid = pkid, `product_id` = :productId, `unit_id` = :unitId, `property_id` = :propertyId, `type_id` = :typeId, `description` = :description, `sort` = :sort, `has_stock` = :hasStock, `is_enabled` = :isEnabled, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodService),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_service` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdService findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_service` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdService> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_service` where pkid IN (:pkidList) " , map , getRowMapper());
    }

    public List<ProdService> findByIds(Integer productId) {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_service` where product_id = :productId " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdService> getRowMapper(){
        RowMapper<ProdService> rowMapper = (rs, i) -> {
            ProdService prodService = new ProdService();
            prodService.setPkid(rs.getInt("pkid"));
            prodService.setProductId(rs.getInt("product_id"));
            prodService.setUnitId(rs.getInt("unit_id"));
            prodService.setPropertyId(rs.getInt("property_id"));
            prodService.setTypeId(rs.getInt("type_id"));
            prodService.setDescription(rs.getString("description"));
            prodService.setSort(rs.getDouble("sort"));
            prodService.setHasStock(rs.getInt("has_stock"));
            prodService.setIsEnabled(rs.getInt("is_enabled"));
            prodService.setAddTime(rs.getTimestamp("add_time"));
            prodService.setAddUserId(rs.getInt("add_user_id"));
            prodService.setRemark(rs.getString("remark"));
            return prodService;
        };
        return rowMapper;
    }

    @Override
    public List<ProdService> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_service` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_service` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public void insertBatch(final List<ProdService> itemList) {
//         `product_id`, `unit_id`, `property_id`, `type_id`, `description`, `sort`, `has_stock`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";

        String sql = "insert into `prod_service` ("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setObject(1, itemList.get(i).getProductId());
                ps.setObject(2, itemList.get(i).getUnitId());
                ps.setObject(3, itemList.get(i).getPropertyId());
                ps.setObject(4, itemList.get(i).getTypeId());
                ps.setObject(5, itemList.get(i).getDescription());
                ps.setObject(6, itemList.get(i).getSort());
                ps.setObject(7, itemList.get(i).getHasStock());
                ps.setObject(8, itemList.get(i).getIsEnabled());
                ps.setObject(9, itemList.get(i).getAddTime());
                ps.setObject(10, itemList.get(i).getAddUserId());
                ps.setObject(11, itemList.get(i).getRemark());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    public int updateBatch(final List<ProdService> itemList) {
        String sql = "update `prod_service` set `unit_id` = ?, `property_id` = ?, `type_id` = ?, `has_stock` = ? where pkid = ?";
        int[] res = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setObject(1, itemList.get(i).getUnitId());
                ps.setObject(2, itemList.get(i).getPropertyId());
                ps.setObject(3, itemList.get(i).getTypeId());
                ps.setObject(4, itemList.get(i).getHasStock());
                ps.setObject(5, itemList.get(i).getPkid());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
        Integer resCount = 0;
        for(int item : res)
        {
            resCount += item;
        }
        return resCount;
    }

    public int deleteBatch(final List<ProdService> itemList) {
        String idList = "";
        if(itemList.size() > 0)
        {
            for(ProdService ps : itemList)
            {
                idList += ps.getPkid() + ",";
            }
            idList = idList.substring(0,(idList.length()-1));
            return getJdbcTemplate().update("delete from `prod_service` where pkid in (" + idList + ")");
        }
        else
        {
            return 0;
        }

    }

}