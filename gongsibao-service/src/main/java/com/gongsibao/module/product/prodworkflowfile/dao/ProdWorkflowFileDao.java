package com.gongsibao.module.product.prodworkflowfile.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodWorkflowFileDao")
public class ProdWorkflowFileDao extends BaseDao<ProdWorkflowFile>{

    public static String INSERT_COLUMNS = " `prod_workflow_id`, `name`, `is_must`, `sort` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    public void insertBatch(final List<ProdWorkflowFile> itemList) {
        String sql = "insert into `prod_workflow_file`("+INSERT_COLUMNS+") values (?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getProdWorkflowId());
                ps.setObject(2, itemList.get(i).getName());
                ps.setObject(3, itemList.get(i).getIsMust());
                ps.setObject(4, itemList.get(i).getSort());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    @Override
    public Integer insert(ProdWorkflowFile prodWorkflowFile) {
        insertObject(prodWorkflowFile);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdWorkflowFile prodWorkflowFile) {
        getJdbcTemplate().update("insert into `prod_workflow_file`("+INSERT_COLUMNS+") values (?, ?, ?, ? )",
                prodWorkflowFile.getProdWorkflowId(),
                prodWorkflowFile.getName(),
                prodWorkflowFile.getIsMust(),
                prodWorkflowFile.getSort()
                );
    }

    @Override
    public int update(ProdWorkflowFile prodWorkflowFile) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_workflow_file` set pkid = pkid, `prod_workflow_id` = :prodWorkflowId, `name` = :name, `is_must` = :isMust, `sort` = :sort where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodWorkflowFile),Map.class));
    }

    @Override
    public int delete(Integer workflowId) {
        return getJdbcTemplate().update("delete from `prod_workflow_file` where prod_workflow_id = "+workflowId);
    }

    @Override
    public ProdWorkflowFile findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_file` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdWorkflowFile> findByIds(List<Integer> workflowIdList) {
        Map<String, Object> map = new HashMap<>();
        map.put("workflowIdList", workflowIdList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_workflow_file` where prod_workflow_id IN (:workflowIdList) order by sort " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdWorkflowFile> getRowMapper(){
        RowMapper<ProdWorkflowFile> rowMapper = (rs, i) -> {
            ProdWorkflowFile prodWorkflowFile = new ProdWorkflowFile();
            prodWorkflowFile.setPkid(rs.getInt("pkid"));
            prodWorkflowFile.setProdWorkflowId(rs.getInt("prod_workflow_id"));
            prodWorkflowFile.setName(rs.getString("name"));
            prodWorkflowFile.setIsMust(rs.getInt("is_must"));
            prodWorkflowFile.setSort(rs.getDouble("sort"));
            return prodWorkflowFile;
        };
        return rowMapper;
    }

    @Override
    public List<ProdWorkflowFile> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_workflow_file` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_workflow_file` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}