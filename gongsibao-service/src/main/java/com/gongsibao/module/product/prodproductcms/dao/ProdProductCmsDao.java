package com.gongsibao.module.product.prodproductcms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproductcms.entity.ProdProductCms;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodProductCmsDao")
public class ProdProductCmsDao extends BaseDao<ProdProductCms> {

    public static String INSERT_COLUMNS = " `product_id`, `title`, `keyword`,`showprice`, `summary`, `price_description`, `promotional_copy`, `service_area_description`, `regist_address_description`, `service_period_description`, `buy_count_description`, `add_user_id`, `remark`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProductCms prodProductCms) {
        insertObject(prodProductCms);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProductCms prodProductCms) {
        getJdbcTemplate().update("insert into `prod_product_cms`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? )",
                prodProductCms.getProductId(),
                prodProductCms.getTitle(),
                prodProductCms.getKeyword(),
                prodProductCms.getShowprice(),
                prodProductCms.getSummary(),
                prodProductCms.getPriceDescription(),
                prodProductCms.getPromotionalCopy(),
                prodProductCms.getServiceAreaDescription(),
                prodProductCms.getRegistAddressDescription(),
                prodProductCms.getServicePeriodDescription(),
                prodProductCms.getBuyCountDescription(),
                prodProductCms.getAddUserId(),
                prodProductCms.getRemark(),
                prodProductCms.getAddTime()
        );
    }

    @Override
    public int update(ProdProductCms prodProductCms) {
        //TODO,需要自己决定如何实现
        //throw new java.lang.UnsupportedOperationException();
        String sql = "UPDATE `prod_product_cms` SET `title` = :title, `keyword` = :keyword, `summary` = :summary, `price_description` = :priceDescription, `promotional_copy` = :promotionalCopy, `service_area_description` = :serviceAreaDescription, `regist_address_description` = :registAddressDescription, `service_period_description` = :servicePeriodDescription, `buy_count_description` = :buyCountDescription, `remark` = :remark WHERE pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProductCms),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product_cms` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProductCms findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_product_cms` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProductCms> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<ProdProductCms> getRowMapper() {
        RowMapper<ProdProductCms> rowMapper = (rs, i) -> {
            ProdProductCms prodProductCms = new ProdProductCms();
            prodProductCms.setPkid(rs.getInt("pkid"));
            prodProductCms.setProductId(rs.getInt("product_id"));
            prodProductCms.setTitle(rs.getString("title"));
            prodProductCms.setKeyword(rs.getString("keyword"));
            prodProductCms.setShowprice(rs.getString("showprice"));
            prodProductCms.setSummary(rs.getString("summary"));
            prodProductCms.setPriceDescription(rs.getString("price_description"));
            prodProductCms.setPromotionalCopy(rs.getString("promotional_copy"));
            prodProductCms.setServiceAreaDescription(rs.getString("service_area_description"));
            prodProductCms.setRegistAddressDescription(rs.getString("regist_address_description"));
            prodProductCms.setServicePeriodDescription(rs.getString("service_period_description"));
            prodProductCms.setBuyCountDescription(rs.getString("buy_count_description"));
            prodProductCms.setAddUserId(rs.getInt("add_user_id"));
            prodProductCms.setRemark(rs.getString("remark"));
            prodProductCms.setAddTime(rs.getTimestamp("add_time"));
            return prodProductCms;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProductCms> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product_cms` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /*根据产品id获取cms信息*/
    public ProdProductCms findByProId(Integer propkid) {
        List<ProdProductCms> list = getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms` where product_id = " + propkid, getRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }
}