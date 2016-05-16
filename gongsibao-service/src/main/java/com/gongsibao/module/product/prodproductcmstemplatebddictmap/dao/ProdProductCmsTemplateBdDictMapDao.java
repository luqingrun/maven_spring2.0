package com.gongsibao.module.product.prodproductcmstemplatebddictmap.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.entity.ProdProductCmsTemplateBdDictMap;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodProductCmsTemplateBdDictMapDao")
public class ProdProductCmsTemplateBdDictMapDao extends BaseDao<ProdProductCmsTemplateBdDictMap> {

    public static String INSERT_COLUMNS = " `product_id`, `city_id`, `template_id`, `add_user_id`, `remark`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap) {
        insertObject(prodProductCmsTemplateBdDictMap);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap) {
        getJdbcTemplate().update("insert into `prod_product_cms_template_bd_dict_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ? )",
                prodProductCmsTemplateBdDictMap.getProductId(),
                prodProductCmsTemplateBdDictMap.getCityId(),
                prodProductCmsTemplateBdDictMap.getTemplateId(),
                prodProductCmsTemplateBdDictMap.getAddUserId(),
                prodProductCmsTemplateBdDictMap.getRemark(),
                prodProductCmsTemplateBdDictMap.getAddTime()
        );
    }

    @Override
    public int update(ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_product_cms_template_bd_dict_map` set pkid = pkid, `product_id` = :productId, `city_id` = :cityId, `template_id` = :templateId, `add_user_id` = :addUserId, `remark` = :remark, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProductCmsTemplateBdDictMap),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product_cms_template_bd_dict_map` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProductCmsTemplateBdDictMap findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_product_cms_template_bd_dict_map` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProductCmsTemplateBdDictMap> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_template_bd_dict_map` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<ProdProductCmsTemplateBdDictMap> getRowMapper() {
        RowMapper<ProdProductCmsTemplateBdDictMap> rowMapper = (rs, i) -> {
            ProdProductCmsTemplateBdDictMap prodProductCmsTemplateBdDictMap = new ProdProductCmsTemplateBdDictMap();
            prodProductCmsTemplateBdDictMap.setPkid(rs.getInt("pkid"));
            prodProductCmsTemplateBdDictMap.setProductId(rs.getInt("product_id"));
            prodProductCmsTemplateBdDictMap.setCityId(rs.getInt("city_id"));
            prodProductCmsTemplateBdDictMap.setTemplateId(rs.getInt("template_id"));
            prodProductCmsTemplateBdDictMap.setAddUserId(rs.getInt("add_user_id"));
            prodProductCmsTemplateBdDictMap.setRemark(rs.getString("remark"));
            prodProductCmsTemplateBdDictMap.setAddTime(rs.getTimestamp("add_time"));
            return prodProductCmsTemplateBdDictMap;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProductCmsTemplateBdDictMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product_cms_template_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms_template_bd_dict_map` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }


    /*根据产品id和模板id删除*/
    public int deletebyprodid(Integer prodid, Integer templateid) {
        return getJdbcTemplate().update("delete from `prod_product_cms_template_bd_dict_map` where product_id = " + prodid + " and template_id = " + templateid);
    }

    /*批量添加*/
    public void insertBatch(final List<ProdProductCmsTemplateBdDictMap> itemList) {
        String sql = "insert into `prod_product_cms_template_bd_dict_map`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getProductId());
                ps.setObject(2, itemList.get(i).getCityId());
                ps.setObject(3, itemList.get(i).getTemplateId());
                ps.setObject(4, itemList.get(i).getAddUserId());
                ps.setObject(5, itemList.get(i).getRemark());
                ps.setObject(6, itemList.get(i).getAddTime());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    public List<ProdProductCmsTemplateBdDictMap> findByProdId(Integer prodId) {
        Map<String, Object> map = new HashMap<>();
        return getJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_template_bd_dict_map` where product_id = "+prodId,  getRowMapper());
    }
}