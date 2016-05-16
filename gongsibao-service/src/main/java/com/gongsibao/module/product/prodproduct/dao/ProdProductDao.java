package com.gongsibao.module.product.prodproduct.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodProductDao")
public class ProdProductDao extends BaseDao<ProdProduct>{

    public static String INSERT_COLUMNS = " `type_id`, `dealer_type_id`, `name`, `no`, `description`, `sort`, `is_enabled`, `add_time`, `add_user_id`, `remark` ,`is_ordered_background_only`,`is_allowed_add_to_cart`,`is_required_ems_address`,`is_allowed_buy_one_more`,`is_required_service_lifecycle`,`is_required_company_register_info`,`is_required_check_name_info`,`is_required_company_register_address` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProduct prodProduct) {
        insertObject(prodProduct);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProduct prodProduct) {
        getJdbcTemplate().update("insert into `prod_product`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                prodProduct.getTypeId(),
                prodProduct.getDealerTypeId(),
                prodProduct.getName(),
                prodProduct.getNo(),
                prodProduct.getDescription(),
                prodProduct.getSort(),
                prodProduct.getIsEnabled(),
                prodProduct.getAddTime(),
                prodProduct.getAddUserId(),
                prodProduct.getRemark(),
                prodProduct.getIsOrderedBackgroundOnly(),
                prodProduct.getIsAllowedAddToCart(),
                prodProduct.getIsRequiredEmsAddress(),
                prodProduct.getIsAllowedBuyOneMore(),
                prodProduct.getIsRequiredServiceLifecycle(),
                prodProduct.getIsRequiredCompanyRegisterInfo(),
                prodProduct.getIsRequiredCheckNameInfo(),
                prodProduct.getIsRequiredCompanyRegisterAddress()
        );
    }

    @Override
    public int update(ProdProduct prodProduct) {
        //TODO,需要自己决定如何实现
        String sql = "update `prod_product` set `type_id` = :typeId, `dealer_type_id` = :dealerTypeId, `name` = :name, `description` = :description, `sort` = :sort, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProduct), Map.class));
    }

    public int updateEnabled(ProdProduct prodProduct) {
        String sql = "update `prod_product` set `is_enabled` = :isEnabled where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProduct), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProduct findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_product` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProduct> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<ProdProduct> getRowMapper() {
        RowMapper<ProdProduct> rowMapper = (rs, i) -> {
            ProdProduct prodProduct = new ProdProduct();
            prodProduct.setPkid(rs.getInt("pkid"));
            prodProduct.setTypeId(rs.getInt("type_id"));
            prodProduct.setDealerTypeId(rs.getInt("dealer_type_id"));
            prodProduct.setName(rs.getString("name"));
            prodProduct.setNo(rs.getString("no"));
            prodProduct.setDescription(rs.getString("description"));
            prodProduct.setSort(rs.getDouble("sort"));
            prodProduct.setIsEnabled(rs.getInt("is_enabled"));
            prodProduct.setAddTime(rs.getTimestamp("add_time"));
            prodProduct.setAddUserId(rs.getInt("add_user_id"));
            prodProduct.setRemark(rs.getString("remark"));
            prodProduct.setIsOrderedBackgroundOnly(rs.getInt("is_ordered_background_only"));
            prodProduct.setIsAllowedAddToCart(rs.getInt("is_allowed_add_to_cart"));
            prodProduct.setIsRequiredEmsAddress(rs.getInt("is_required_ems_address"));
            prodProduct.setIsAllowedBuyOneMore(rs.getInt("is_allowed_buy_one_more"));
            prodProduct.setIsRequiredServiceLifecycle(rs.getInt("is_required_service_lifecycle"));
            prodProduct.setIsRequiredCompanyRegisterInfo(rs.getInt("is_required_company_register_info"));
            prodProduct.setIsRequiredCheckNameInfo(rs.getInt("is_required_check_name_info"));
            prodProduct.setIsRequiredCompanyRegisterAddress(rs.getInt("is_required_company_register_address"));
            return prodProduct;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProduct> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<ProdProduct> findByProducts(Map<String, Object> products, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product` where 1=1 ");
        if (products.size() > 0) {
            if (products.containsKey("name")) {
                sql.append(" and `name` like '%" + products.get("name").toString() + "%' ");
            }
            if(products.containsKey("dealerTypeId")) {
                sql.append(" and `dealer_type_id` = " + Integer.valueOf(products.get("dealerTypeId").toString()) + " ");
            }
            if(products.containsKey("typeId")) {
                sql.append(" and `type_id` = " + Integer.valueOf(products.get("typeId").toString()) + " ");
            }
            if(products.containsKey("isEnabled")) {
                sql.append(" and `is_enabled` = " + Integer.valueOf(products.get("isEnabled").toString()) + " ");
            }
        }
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public int countByProducts(Map<String, Object> products) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product` where 1=1 ");
        if (products.size() > 0) {
            if (products.containsKey("name")) {
                sql.append(" and `name` like '%" + products.get("name").toString() + "%' ");
            }
            if (products.containsKey("dealerTypeId")) {
                sql.append(" and `dealer_type_id` = " + Integer.valueOf(products.get("dealerTypeId").toString()) + " ");
            }
            if (products.containsKey("typeId")) {
                sql.append(" and `type_id` = " + Integer.valueOf(products.get("typeId").toString()) + " ");
            }
            if (products.containsKey("isEnabled")) {
                sql.append(" and `is_enabled` = " + Integer.valueOf(products.get("isEnabled").toString()) + " ");
            }
        }
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), products, Integer.class);
    }

    public List<Integer> queryProductIds(Map<String, Object> condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("DISTINCT ");
        sql.append("pkid ");
        sql.append("FROM ");
        sql.append("prod_product ");
        sql.append("WHERE ");
        sql.append("is_enabled = 1 ");

        if (!CollectionUtils.isEmpty(condition)) {
            if (NumberUtils.toInt((String) condition.get("prodTypeId")) > 0) {
                sql.append("AND ");
                sql.append("type_id = " + condition.get("prodTypeId") + " ");
            }
            if (StringUtils.isNotBlank((String) condition.get("name"))) {
                sql.append("AND ");
                sql.append("name LIKE '%" + condition.get("name") + "%' ");
            }
            if (StringUtils.isNotBlank((String) condition.get("no"))) {
                sql.append("AND ");
                sql.append("no = '" + condition.get("no") + "' ");
            }
        }

        return getJdbcTemplate().queryForList(sql.toString(), Integer.class);
    }




    /**/
    public List<ProdProduct> findByProdIdPager(Integer prodid, int start, int pageSize) {
        //StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product_cms_related` where product_id =" + prodid);
        StringBuffer sql = new StringBuffer("SELECT " + ALL_COLUMNS + " FROM prod_product WHERE pkid NOT IN(SELECT recommend_product_id FROM prod_product_cms_related WHERE product_id="+prodid+") ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public int findByProdIdCount(Integer prodid) {
        StringBuffer sql = new StringBuffer("SELECT count(*) FROM prod_product WHERE pkid NOT IN(SELECT recommend_product_id FROM prod_product_cms_related WHERE product_id=1) ");
        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
    }
}