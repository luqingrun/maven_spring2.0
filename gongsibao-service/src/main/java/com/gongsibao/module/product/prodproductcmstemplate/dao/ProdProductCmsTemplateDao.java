package com.gongsibao.module.product.prodproductcmstemplate.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproductcmstemplate.entity.ProdProductCmsTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodProductCmsTemplateDao")
public class ProdProductCmsTemplateDao extends BaseDao<ProdProductCmsTemplate>{

    public static String INSERT_COLUMNS = " `name`, `is_default`, `content`, `add_user_id`, `product_id`, `product_name`, `remark`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProductCmsTemplate prodProductCmsTemplate) {
        insertObject(prodProductCmsTemplate);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProductCmsTemplate prodProductCmsTemplate) {
        getJdbcTemplate().update("insert into `prod_product_cms_template`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ? )",
                prodProductCmsTemplate.getName(),
                prodProductCmsTemplate.getIsDefault(),
                prodProductCmsTemplate.getContent(),
                prodProductCmsTemplate.getAddUserId(),
                prodProductCmsTemplate.getProductId(),
                prodProductCmsTemplate.getProductName(),
                prodProductCmsTemplate.getRemark(),
                prodProductCmsTemplate.getAddTime()
                );
    }

    @Override
    public int update(ProdProductCmsTemplate prodProductCmsTemplate) {
        String sql = "update `prod_product_cms_template` set `name` = :name, `content` = :content, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProductCmsTemplate),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product_cms_template` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProductCmsTemplate findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `prod_product_cms_template` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProductCmsTemplate> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_product_cms_template` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdProductCmsTemplate> getRowMapper(){
        RowMapper<ProdProductCmsTemplate> rowMapper = (rs, i) -> {
            ProdProductCmsTemplate prodProductCmsTemplate = new ProdProductCmsTemplate();
            prodProductCmsTemplate.setPkid(rs.getInt("pkid"));
            prodProductCmsTemplate.setName(rs.getString("name"));
            prodProductCmsTemplate.setIsDefault(rs.getInt("is_default"));
            prodProductCmsTemplate.setContent(rs.getString("content"));
            prodProductCmsTemplate.setAddUserId(rs.getInt("add_user_id"));
            prodProductCmsTemplate.setProductId(rs.getInt("product_id"));
            prodProductCmsTemplate.setProductName(rs.getString("product_name"));
            prodProductCmsTemplate.setRemark(rs.getString("remark"));
            prodProductCmsTemplate.setAddTime(rs.getTimestamp("add_time"));
            return prodProductCmsTemplate;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProductCmsTemplate> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_product_cms_template` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms_template` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int countByProdId(Integer prodid) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms_template` where product_id = "+prodid);
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
    }
}