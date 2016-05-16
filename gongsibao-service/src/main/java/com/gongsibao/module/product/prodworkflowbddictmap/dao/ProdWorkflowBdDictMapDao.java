package com.gongsibao.module.product.prodworkflowbddictmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodWorkflowBdDictMapDao")
public class ProdWorkflowBdDictMapDao extends BaseDao<ProdWorkflowBdDictMap>{

    public static String INSERT_COLUMNS = " `workflow_id`, `city_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    public void insertBatch(final List<ProdWorkflowBdDictMap> itemList) {
        String sql = "insert into `prod_workflow_bd_dict_map`("+INSERT_COLUMNS+") values (?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getWorkflowId());
                ps.setObject(2, itemList.get(i).getCityId());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    @Override
    public Integer insert(ProdWorkflowBdDictMap prodWorkflowBdDictMap) {
        insertObject(prodWorkflowBdDictMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdWorkflowBdDictMap prodWorkflowBdDictMap) {
        getJdbcTemplate().update("insert into `prod_workflow_bd_dict_map`("+INSERT_COLUMNS+") values (?, ? )",
                prodWorkflowBdDictMap.getWorkflowId(),
                prodWorkflowBdDictMap.getCityId()
                );
    }

    @Override
    public int update(ProdWorkflowBdDictMap prodWorkflowBdDictMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_workflow_bd_dict_map` set pkid = pkid, `workflow_id` = :workflowId, `city_id` = :cityId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodWorkflowBdDictMap),Map.class));
    }

    @Override
    public int delete(Integer workflowId) {
        return getJdbcTemplate().update("delete from `prod_workflow_bd_dict_map` where workflow_id = "+workflowId);
    }

    @Override
    public ProdWorkflowBdDictMap findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_bd_dict_map` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdWorkflowBdDictMap> findByIds(List<Integer> workflowIdList) {
        Map<String, Object> map = new HashMap<>();
        map.put("workflowIdList", workflowIdList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_bd_dict_map` where workflow_id IN (:workflowIdList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdWorkflowBdDictMap> getRowMapper(){
        RowMapper<ProdWorkflowBdDictMap> rowMapper = (rs, i) -> {
            ProdWorkflowBdDictMap prodWorkflowBdDictMap = new ProdWorkflowBdDictMap();
            prodWorkflowBdDictMap.setPkid(rs.getInt("pkid"));
            prodWorkflowBdDictMap.setWorkflowId(rs.getInt("workflow_id"));
            prodWorkflowBdDictMap.setCityId(rs.getInt("city_id"));
            return prodWorkflowBdDictMap;
        };
        return rowMapper;
    }

    @Override
    public List<ProdWorkflowBdDictMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_workflow_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_workflow_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<Integer> queryWorkflowIds(Collection<Integer> cityIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT ");
        sql.append("workflow_id ");
        sql.append("FROM ");
        sql.append("prod_workflow_bd_dict_map ");
        sql.append("WHERE ");
        sql.append("city_id IN (" + StringUtils.join(cityIds, ",") + ") ");

        return this.getJdbcTemplate().queryForList(sql.toString(), Integer.class);
    }
}