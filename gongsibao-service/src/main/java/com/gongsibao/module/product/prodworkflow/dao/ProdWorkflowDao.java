package com.gongsibao.module.product.prodworkflow.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodworkflow.entity.ProdWorkflow;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodWorkflowDao")
public class ProdWorkflowDao extends BaseDao<ProdWorkflow>{

    public static String INSERT_COLUMNS = " `product_id`, `form_name`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdWorkflow prodWorkflow) {
        insertObject(prodWorkflow);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdWorkflow prodWorkflow) {
        getJdbcTemplate().update("insert into `prod_workflow`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ? )",
                prodWorkflow.getProductId(),
                prodWorkflow.getFormName(),
                prodWorkflow.getIsEnabled(),
                prodWorkflow.getAddTime(),
                prodWorkflow.getAddUserId(),
                prodWorkflow.getRemark()
                );
    }

    @Override
    public int update(ProdWorkflow prodWorkflow) {
        String sql = "update `prod_workflow` set `product_id` = :productId, `form_name` = :formName where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodWorkflow),Map.class));
    }

    public int updateEnabled(Integer pkid, int enabled) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append("prod_workflow ");
        sql.append("SET ");
        sql.append("is_enabled = ? ");
        sql.append("WHERE ");
        sql.append("pkid = ? ");

        return getJdbcTemplate().update(sql.toString(), enabled, pkid);
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_workflow` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdWorkflow findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdWorkflow> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdWorkflow> getRowMapper(){
        RowMapper<ProdWorkflow> rowMapper = (rs, i) -> {
            ProdWorkflow prodWorkflow = new ProdWorkflow();
            prodWorkflow.setPkid(rs.getInt("pkid"));
            prodWorkflow.setProductId(rs.getInt("product_id"));
            prodWorkflow.setFormName(rs.getString("form_name"));
            prodWorkflow.setIsEnabled(rs.getInt("is_enabled"));
            prodWorkflow.setAddTime(rs.getTimestamp("add_time"));
            prodWorkflow.setAddUserId(rs.getInt("add_user_id"));
            prodWorkflow.setRemark(rs.getString("remark"));
            return prodWorkflow;
        };
        return rowMapper;
    }

    @Override
    public List<ProdWorkflow> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_workflow` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_workflow` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<Integer> queryWorkflowIds(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("pkid ");
        sql.append("FROM ");
        sql.append("prod_workflow ");
        sql.append("WHERE 1=1 ");
        buildSQL(properties, sql);

        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), properties, Integer.class);
    }
}