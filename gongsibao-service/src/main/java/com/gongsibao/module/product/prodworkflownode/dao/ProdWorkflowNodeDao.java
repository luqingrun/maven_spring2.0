package com.gongsibao.module.product.prodworkflownode.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Repository("prodWorkflowNodeDao")
public class ProdWorkflowNodeDao extends BaseDao<ProdWorkflowNode>{

    public static String INSERT_COLUMNS = " `workflow_id`, `type_id`, `name`, `sort`, `weekday_count` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    public void insertBatch(final List<ProdWorkflowNode> itemList) {
        String sql = "insert into `prod_workflow_node`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getWorkflowId());
                ps.setObject(2, itemList.get(i).getTypeId());
                ps.setObject(3, itemList.get(i).getName());
                ps.setObject(4, itemList.get(i).getSort());
                ps.setObject(5, itemList.get(i).getWeekdayCount());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    @Override
    public Integer insert(ProdWorkflowNode prodWorkflowNode) {
        insertObject(prodWorkflowNode);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdWorkflowNode prodWorkflowNode) {
        getJdbcTemplate().update("insert into `prod_workflow_node`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ? )",
                prodWorkflowNode.getWorkflowId(),
                prodWorkflowNode.getTypeId(),
                prodWorkflowNode.getName(),
                prodWorkflowNode.getSort(),
                prodWorkflowNode.getWeekdayCount()
                );
    }

    @Override
    public int update(ProdWorkflowNode prodWorkflowNode) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_workflow_node` set pkid = pkid, `workflow_id` = :workflowId, `type_id` = :typeId, `name` = :name, `sort` = :sort, `weekday_count` = :weekdayCount where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodWorkflowNode),Map.class));
    }

    @Override
    public int delete(Integer workflowId) {
        return getJdbcTemplate().update("delete from `prod_workflow_node` where workflow_id = "+workflowId);
    }

    @Override
    public ProdWorkflowNode findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_node` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdWorkflowNode> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_node` where pkid IN (:pkidList) " , map , getRowMapper());
    }

    public List<ProdWorkflowNode> findByWorkflowIds(Collection<Integer> workflowIdList) {
        Map<String, Object> map = new HashMap<>();
        map.put("workflowIdList", workflowIdList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_node` where workflow_id IN (:workflowIdList) order by sort " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdWorkflowNode> getRowMapper(){
        RowMapper<ProdWorkflowNode> rowMapper = (rs, i) -> {
            ProdWorkflowNode prodWorkflowNode = new ProdWorkflowNode();
            prodWorkflowNode.setPkid(rs.getInt("pkid"));
            prodWorkflowNode.setWorkflowId(rs.getInt("workflow_id"));
            prodWorkflowNode.setTypeId(rs.getInt("type_id"));
            prodWorkflowNode.setName(rs.getString("name"));
            prodWorkflowNode.setSort(rs.getDouble("sort"));
            prodWorkflowNode.setWeekdayCount(rs.getInt("weekday_count"));
            return prodWorkflowNode;
        };
        return rowMapper;
    }

    @Override
    public List<ProdWorkflowNode> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_workflow_node` where 1=1 ");
        buildSQL(properties, sql);
        if(start >= 0 && pageSize > 0) {
            sql.append(" limit ").append(start).append(", ").append(pageSize);
        }
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_workflow_node` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}