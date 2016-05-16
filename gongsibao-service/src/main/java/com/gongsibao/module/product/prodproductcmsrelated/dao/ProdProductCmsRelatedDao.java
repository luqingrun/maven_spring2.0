package com.gongsibao.module.product.prodproductcmsrelated.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodProductCmsRelatedDao")
public class ProdProductCmsRelatedDao extends BaseDao<ProdProductCmsRelated> {

    public static String INSERT_COLUMNS = " `product_id`, `recommend_product_id`, `product_name`, `recommend_product_name`, `add_user_id`, `sort`, `remark`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProductCmsRelated prodProductCmsRelated) {
        insertObject(prodProductCmsRelated);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProductCmsRelated prodProductCmsRelated) {
        getJdbcTemplate().update("insert into `prod_product_cms_related`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ? )",
                prodProductCmsRelated.getProductId(),
                prodProductCmsRelated.getRecommendProductId(),
                prodProductCmsRelated.getProductName(),
                prodProductCmsRelated.getRecommendProductName(),
                prodProductCmsRelated.getAddUserId(),
                prodProductCmsRelated.getSort(),
                prodProductCmsRelated.getRemark(),
                prodProductCmsRelated.getAddTime()
        );
    }

    @Override
    public int update(ProdProductCmsRelated prodProductCmsRelated) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_product_cms_related` set pkid = pkid, `product_id` = :productId, `recommend_product_id` = :recommendProductId, `product_name` = :productName, `recommend_product_name` = :recommendProductName, `add_user_id` = :addUserId, `sort` = :sort, `remark` = :remark, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProductCmsRelated),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product_cms_related` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProductCmsRelated findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_product_cms_related` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProductCmsRelated> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_related` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<ProdProductCmsRelated> getRowMapper() {
        RowMapper<ProdProductCmsRelated> rowMapper = (rs, i) -> {
            ProdProductCmsRelated prodProductCmsRelated = new ProdProductCmsRelated();
            prodProductCmsRelated.setPkid(rs.getInt("pkid"));
            prodProductCmsRelated.setProductId(rs.getInt("product_id"));
            prodProductCmsRelated.setRecommendProductId(rs.getInt("recommend_product_id"));
            prodProductCmsRelated.setProductName(rs.getString("product_name"));
            prodProductCmsRelated.setRecommendProductName(rs.getString("recommend_product_name"));
            prodProductCmsRelated.setAddUserId(rs.getInt("add_user_id"));
            prodProductCmsRelated.setSort(rs.getDouble("sort"));
            prodProductCmsRelated.setRemark(rs.getString("remark"));
            prodProductCmsRelated.setAddTime(rs.getTimestamp("add_time"));
            return prodProductCmsRelated;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProductCmsRelated> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product_cms_related` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms_related` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /*根据产品id获取该产品所有的推荐产品*/
    public List<ProdProductCmsRelated> findByProId(Integer proid) {
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_related` where product_id = " + proid, getRowMapper());
    }


    /*根据产品id删除相关产品*/
    public int deletebyprodid(Integer prodid) {
        return getJdbcTemplate().update("delete from `prod_product_cms_related` where product_id = " + prodid);
    }

    /*批量添加*/
    public void insertBatch(final List<ProdProductCmsRelated> itemList) {
        String sql = "insert into `prod_product_cms_related`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getProductId());
                ps.setObject(2, itemList.get(i).getRecommendProductId());
                ps.setObject(3, itemList.get(i).getProductName());
                ps.setObject(4, itemList.get(i).getRecommendProductName());
                ps.setObject(5, itemList.get(i).getAddUserId());
                ps.setObject(6, itemList.get(i).getSort());
                ps.setObject(7, itemList.get(i).getRemark());
                ps.setObject(8, itemList.get(i).getAddTime());
            }
            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }
}