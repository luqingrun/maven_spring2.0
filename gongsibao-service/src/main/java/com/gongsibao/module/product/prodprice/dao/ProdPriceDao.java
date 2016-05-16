package com.gongsibao.module.product.prodprice.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodPriceDao")
public class ProdPriceDao extends BaseDao<ProdPrice> {

    public static String INSERT_COLUMNS = "  `service_id`, `city_id`, `price_audit_id`, `original_price`,`price`, `cost`, `is_must`, `stock`, `is_on_sale`, `add_time`, `add_user_id`, `remark` ";

    public static String BATCH_INSERT_COLUMNS = "  `service_id`, `city_id`, `price_audit_id`, `original_price`,`price`, `cost`, `is_must`, `stock`, `is_on_sale`, `add_user_id` ";

    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdPrice prodPrice) {
        insertObject(prodPrice);
        return getLastInsertId();
    }

    public void insertBatch(final List<ProdPrice> itemList) {
        String sql = "insert into `prod_price`(" + BATCH_INSERT_COLUMNS + ") values (?,?,?,?,?,?,?,?,?,? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProdPrice price = itemList.get(i);

                ps.setObject(1, price.getServiceId());
                ps.setObject(2, price.getCityId());
                ps.setObject(3, price.getPriceAuditId());
                ps.setObject(4, price.getOriginalPrice());
                ps.setObject(5, price.getPrice());
                ps.setObject(6, price.getCost());
                ps.setObject(7, price.getIsMust());
                ps.setObject(8, price.getStock());
                ps.setObject(9, price.getIsOnSale());
                ps.setObject(10, price.getAddUserId());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    @Override
    protected void insertObject(ProdPrice prodPrice) {
        getJdbcTemplate().update("insert into `prod_price`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                prodPrice.getServiceId(),
                prodPrice.getCityId(),
                prodPrice.getPriceAuditId(),
                prodPrice.getOriginalPrice(),
                prodPrice.getPrice(),
                prodPrice.getCost(),
                prodPrice.getIsMust(),
                prodPrice.getStock(),
                prodPrice.getIsOnSale(),
                prodPrice.getAddTime(),
                prodPrice.getAddUserId(),
                prodPrice.getRemark()
        );
    }

    @Override
    public int update(ProdPrice prodPrice) {
        //TODO,需要自己决定如何实
        String sql = "update `prod_price` set pkid = pkid, `service_id` = :serviceId, `city_id` = :cityId, `price_audit_id` = :priceAuditId, `original_price`=:originalPrice,`price` = :price, `cost` = :cost, `is_must` = :isMust, `stock` = :stock, `is_on_sale` = :isOnSale, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodPrice), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实
        //return getJdbcTemplate().update("delete from `prod_price` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdPrice findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_price` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdPrice> findByIds(List<Integer> pkidList) {
        if (CollectionUtils.isEmpty(pkidList)) {
            return new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    public List<ProdPrice> findByAuditId(Integer auditId) {
        Map<String, Object> map = new HashMap<>();
        map.put("auditId", auditId);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price` where price_audit_id =:auditId ", map, getRowMapper());
    }

    public List<ProdPrice> findByAuditIdAndSaleState(Integer auditId, Integer isOnSale) {
        return getJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price` where price_audit_id = " + auditId + " and is_on_sale = " + isOnSale, getRowMapper());
    }

    public List<ProdPrice> findByAuditIdAndCityID(Integer auditId, List<Integer> cityIds, Integer isOnSale) {
        return getJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price` where price_audit_id = " + auditId + " and city_id IN (" + StringUtils.join(cityIds, ",") + ") and is_on_sale = " + isOnSale, getRowMapper());
    }

    public int editProdPriceWithIsOnSale(List<Integer> allPkis, Integer isOnSale) {
        String sql = "update `prod_price` set `is_on_sale` =  :isOnSale where pkid IN (:allPkis) ";
        Map<String, Object> map = new HashMap<>();
        map.put("allPkis", allPkis);
        map.put("isOnSale", isOnSale);
        return getNamedParameterJdbcTemplate().update(sql, map);
    }

    @Override
    public RowMapper<ProdPrice> getRowMapper() {
        RowMapper<ProdPrice> rowMapper = (rs, i) -> {
            ProdPrice prodPrice = new ProdPrice();
            prodPrice.setPkid(rs.getInt("pkid"));
            prodPrice.setServiceId(rs.getInt("service_id"));
            prodPrice.setCityId(rs.getInt("city_id"));
            prodPrice.setPriceAuditId(rs.getInt("price_audit_id"));
            prodPrice.setPrice(rs.getInt("price"));
            prodPrice.setOriginalPrice(rs.getInt("original_price"));
            prodPrice.setCost(rs.getInt("cost"));
            prodPrice.setIsMust(rs.getInt("is_must"));
            prodPrice.setStock(rs.getInt("stock"));
            prodPrice.setIsOnSale(rs.getInt("is_on_sale"));
            prodPrice.setAddTime(rs.getTimestamp("add_time"));
            prodPrice.setAddUserId(rs.getInt("add_user_id"));
            prodPrice.setRemark(rs.getString("remark"));
            return prodPrice;
        };
        return rowMapper;
    }

    @Override
    public List<ProdPrice> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_price` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_price` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 根据审核Id获取销售地区的ID列表
     */
    public List<Integer> findSaleCityIds(Integer product_price_audit_id) {

        StringBuffer sql = new StringBuffer("select distinct city_id from `prod_price` where 1=1 ");
        sql.append(" And price_audit_id=" + product_price_audit_id);
        //buildSQL(null, sql);
        List<Integer> cityIds = getJdbcTemplate().queryForList(sql.toString(), Integer.class);
        return cityIds;

    }

    /**
     * 根据审核ID获取所有定价的服务ID列表
     */
    public List<Integer> findServiceIdsByAuditId(Integer product_price_audit_id) {
        StringBuffer sql = new StringBuffer("select distinct service_id from `prod_price` where 1=1 ");
        sql.append(" And price_audit_id=" + product_price_audit_id);
        //buildSQL(null, sql);
        List<Integer> cityIds = getJdbcTemplate().queryForList(sql.toString(), Integer.class);
        return cityIds;
    }

    /**
     * 下架其他批次已经上架的产品服务价格，指定本次审批通过
     */
    public int updateSameProdPriceSaleStatusesBy(Integer pkid) {

        int result = 0;
        Map<String, Object> map = new HashMap<>();

        //查询已经将上架的产品价格列表
        List<ProdPrice> prodPrices = findByAuditIdAndSaleState(pkid, 0);
        //将之前相同的产品下架
        for (ProdPrice price : prodPrices) {
            map.clear();
            map.put("retainPkid", pkid);
            map.put("serviceId", price.getServiceId());
            map.put("cityId", price.getCityId());
            //下架旧产品
            result += getNamedParameterJdbcTemplate().update("update  `prod_price` SET is_on_sale= 0 where is_on_sale=1 AND price_audit_id <>:retainPkid AND service_id=:serviceId AND city_id=:cityId", map);
            //上架新产品
            result += getNamedParameterJdbcTemplate().update("update  `prod_price` SET is_on_sale=1 where is_on_sale=0 AND price_audit_id =:retainPkid AND service_id=:serviceId AND city_id=:cityId", map);

        }
        return result;
    }


    /**
     * 查询服务项是否必须,根据审核Id和服务Id
     */
    public Boolean findIsMustByAuditIdAndServiceId(Integer pkid, Integer serviceId) {
        StringBuffer sql = new StringBuffer("select is_must from `prod_price` where 1=1 And price_audit_id=:auditId And service_id=:serviceId limit 1");
        Map<String, Object> map = new HashMap<>();
        map.put("auditId", pkid);
        map.put("serviceId", serviceId);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, Integer.class) > 0;
    }

    /**
     * 获取产品价格,通过服务Id 和城市ID
     */
    public ProdPrice findOnSaleProdPriceBy(Integer serviceId, Integer cityId) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_price` where 1=1 and city_id=:cityId and service_id=:serviceId ");
        Map<String, Object> map = new HashMap<>();
        map.put("cityId", cityId);
        map.put("serviceId", serviceId);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, ProdPrice.class);
    }

    /**
     * 产品价格列表
     */
    public List<ProdPrice> getProdPricesListBy(Map<String, Object> condition) {

        StringBuilder sql = new StringBuilder("SELECT  " +
                "  p.*  " +
                "FROM " +
                "  prod_price p  " +
                "WHERE  " +
                "city_id=:city_id " +
                "AND EXISTS  " +
                "  (SELECT  " +
                "    1  " +
                "  FROM " +
                "    prod_price_audit a  " +
                "  WHERE a.`pkid` = p.`price_audit_id` And a.`product_id` = :product_id And a.`organization_id` = :organization_id And a.`audit_status_id` = :audit_status_id)");

        return getNamedParameterJdbcTemplate().query(sql.toString(), condition, getRowMapper());
    }
    /**
     * 通过产品ID,地区ID获取产品服务
     * @param cityId
     * @param productId
     * @return
     */
    public List<ProdPrice> findProdPriceInfoByCityIdAndProductId(Integer cityId,Integer productId){
        StringBuffer sql = new StringBuffer("SELECT `prod_price`.`pkid`,`dict_property`.`name` AS `property_name`, `dict_service`.`name` AS `type_name`,`dict_unit`.`name`," +
                "`prod_price`.`original_price` FROM `prod_price`" +
                "INNER JOIN `prod_service` ON `prod_price`.`service_id` = `prod_service`.`pkid`" +
                "INNER JOIN `bd_dict` AS `dict_service` ON `prod_service`.`type_id` = `dict_service`.`pkid`" +
                "INNER JOIN `bd_dict` AS `dict_unit` ON `prod_service`.`unit_id` = `dict_unit`.`pkid`" +
                "INNER JOIN `bd_dict` AS `dict_property` on `prod_service`.`property_id` = `dict_property`.`pkid`" +
                "WHERE 1=1" +
                "AND `prod_price`.`city_id` =:cityId" +
                "AND `prod_service`.`product_id =:serviceId`");
        Map<String, Object> map = new HashMap<>();
        map.put("cityId", cityId);
        map.put("productId", productId);

        return getNamedParameterJdbcTemplate().query(sql.toString(),getProdPriceListMapper());
    }

    public RowMapper<ProdPrice> getProdPriceListMapper() {
        RowMapper<ProdPrice> rowMapper = (rs, i) -> {
            ProdPrice prodPrice = new ProdPrice();
            prodPrice.setPkid(rs.getInt("pkid"));
            prodPrice.setServiceId(rs.getInt("service_id"));
            prodPrice.setPropertyName(rs.getString("property_name"));
            prodPrice.setTypeName(rs.getString("type_name"));
            prodPrice.setUnitName(rs.getString("dict_unit.name"));
            prodPrice.setCityId(rs.getInt("city_id"));
            prodPrice.setPriceAuditId(rs.getInt("price_audit_id"));
            prodPrice.setPrice(rs.getInt("price"));
            prodPrice.setOriginalPrice(rs.getInt("original_price"));
            prodPrice.setCost(rs.getInt("cost"));
            prodPrice.setIsMust(rs.getInt("is_must"));
            prodPrice.setStock(rs.getInt("stock"));
            prodPrice.setIsOnSale(rs.getInt("is_on_sale"));
            prodPrice.setAddTime(rs.getTimestamp("add_time"));
            prodPrice.setAddUserId(rs.getInt("add_user_id"));
            prodPrice.setRemark(rs.getString("remark"));
            return prodPrice;
        };
        return rowMapper;
    }
}