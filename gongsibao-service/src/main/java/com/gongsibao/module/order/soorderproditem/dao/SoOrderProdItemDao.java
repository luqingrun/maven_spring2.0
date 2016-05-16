package com.gongsibao.module.order.soorderproditem.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorder.entity.OrderBusiness;
import com.gongsibao.module.order.soorderproditem.entity.IteamServerInfo;
import com.gongsibao.module.order.soorderproditem.entity.ProdItemName;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("soOrderProdItemDao")
public class SoOrderProdItemDao extends BaseDao<SoOrderProdItem> {

    public static String INSERT_COLUMNS = " so_order_prod_item.order_prod_id, so_order_prod_item.`price_id`, so_order_prod_item.`unit_name`, " +
            "so_order_prod_item.`service_name`, so_order_prod_item.`quantity`, so_order_prod_item.`price`, " +
            "so_order_prod_item.`price_original`, so_order_prod_item.`price_refund` ";


    public static String ALL_COLUMNS = "so_order_prod_item.`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdItem soOrderProdItem) {
        insertObject(soOrderProdItem);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdItem soOrderProdItem) {
        getJdbcTemplate().update("insert into `so_order_prod_item`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ? ,?)",
                soOrderProdItem.getOrderProdId(),
                soOrderProdItem.getPriceId(),
                soOrderProdItem.getUnitName(),
                soOrderProdItem.getServiceName(),
                soOrderProdItem.getQuantity(),
                soOrderProdItem.getPrice(),
                soOrderProdItem.getPriceOriginal(),
                soOrderProdItem.getPriceRefund()
        );
    }

    @Override
    public int update(SoOrderProdItem soOrderProdItem) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod_item` set pkid = pkid, `order_prod_id` = :orderProdId, `price_id` = :priceId, `unit_name` = :unitName, `service_name` = :serviceName, `quantity` = :quantity, `price` = :price, `price_original` = :priceOriginal where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdItem),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_item` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderProdItem findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `so_order_prod_item` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderProdItem> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod_item` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdItem> getRowMapper() {
        RowMapper<SoOrderProdItem> rowMapper = (rs, i) -> {
            SoOrderProdItem soOrderProdItem = new SoOrderProdItem();
            soOrderProdItem.setPkid(rs.getInt("so_order_prod_item.pkid"));
            soOrderProdItem.setOrderProdId(rs.getInt("so_order_prod_item.order_prod_id"));
            soOrderProdItem.setPriceId(rs.getInt("so_order_prod_item.price_id"));
            soOrderProdItem.setUnitName(rs.getString("so_order_prod_item.unit_name"));
            soOrderProdItem.setServiceName(rs.getString("so_order_prod_item.service_name"));
            soOrderProdItem.setQuantity(rs.getInt("so_order_prod_item.quantity"));
            soOrderProdItem.setPrice(rs.getInt("so_order_prod_item.price"));
            soOrderProdItem.setPriceOriginal(rs.getInt("so_order_prod_item.price_original"));
            soOrderProdItem.setPriceRefund(rs.getInt("so_order_prod_item.price_refund"));
            return soOrderProdItem;
        };
        return rowMapper;
    }

    public RowMapper<SoOrderProdItem> getDetailRowMapper() {
        RowMapper<SoOrderProdItem> rowMapper = (rs, i) -> {
            SoOrderProdItem soOrderProdItem = new SoOrderProdItem();
            soOrderProdItem.setPkid(rs.getInt("so_order_prod_item.pkid"));
            soOrderProdItem.setOrderProdId(rs.getInt("so_order_prod_item.order_prod_id"));
            soOrderProdItem.setPriceId(rs.getInt("so_order_prod_item.price_id"));
            soOrderProdItem.setUnitName(rs.getString("so_order_prod_item.unit_name"));
            soOrderProdItem.setServiceName(rs.getString("so_order_prod_item.service_name"));
            soOrderProdItem.setQuantity(rs.getInt("so_order_prod_item.quantity"));
            soOrderProdItem.setPrice(rs.getInt("so_order_prod_item.price"));
            soOrderProdItem.setPriceOriginal(rs.getInt("so_order_prod_item.price_original"));
            soOrderProdItem.setPriceRefund(rs.getInt("so_order_prod_item.price_refund"));
            soOrderProdItem.setCost(rs.getInt("prod_price.cost"));
            soOrderProdItem.setCityId(rs.getInt("so_order_prod.city_id"));
            return soOrderProdItem;
        };
        return rowMapper;
    }

    public RowMapper<ProdItemName> getProdItemNameRowMapper() {
        RowMapper<ProdItemName> rowMapper = (rs, i) -> {
            ProdItemName prodItemName = new ProdItemName();
            prodItemName.setOrderProdId(rs.getInt("so_order_prod.pkid"));
            prodItemName.setItemName(rs.getString("name"));
            return prodItemName;
        };
        return rowMapper;
    }


    @Override
    public List<SoOrderProdItem> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod_item` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_item` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public Map<Integer, String> findItemNamesByOrderProdIds(List<Integer> orderProdIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT so_order_prod.pkid , group_concat(so_order_prod_item.service_name) as name ");
        sql.append(" FROM `so_order_prod` ");
        // 设置连表查询
        sql.append(" INNER JOIN `so_order_prod_item` ON so_order_prod.pkid = so_order_prod_item.order_prod_id ");
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        sql.append(" AND so_order_prod.pkid IN (:orderProdIds)");
        sql.append(" GROUP BY so_order_prod.pkid");
        Map<String, Object> map = new HashMap<>();
        map.put("orderProdIds", orderProdIds);
        List<ProdItemName> prodItemNames = getNamedParameterJdbcTemplate().query(sql.toString(), map, getProdItemNameRowMapper());
        Map<Integer, String> prodItemNameMap = new HashMap<>();
        for (ProdItemName p : prodItemNames) {
            prodItemNameMap.put(p.getOrderProdId(), p.getItemName());
        }
        return prodItemNameMap;
    }

    public List<SoOrderProdItem> findItemsByOrderProdIds(List<Integer> orderProdIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(ALL_COLUMNS).append(" ,prod_price.cost,so_order_prod.city_id ");
        sql.append(" FROM `so_order_prod_item` ");
        // 设置连表查询
        sql.append(" INNER JOIN `so_order_prod` ON so_order_prod.pkid = so_order_prod_item.order_prod_id ");
        sql.append(" INNER JOIN `prod_price` ON prod_price.service_id = so_order_prod_item.price_id ");
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        sql.append(" AND so_order_prod_item.order_prod_id IN (:orderProdIds)");
        Map<String, Object> map = new HashMap<>();
        map.put("orderProdIds", orderProdIds);
        List<SoOrderProdItem> List = getNamedParameterJdbcTemplate().query(sql.toString(), map, getDetailRowMapper());
        return List;
    }

    public List<SoOrderProdItem> getByProdIds(Collection<Integer> orderProdIds) {
        String sql = "SELECT " + ALL_COLUMNS + " FROM `so_order_prod_item` WHERE `order_prod_id` IN (:orderProdIds)";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("orderProdIds", orderProdIds);

        return getNamedParameterJdbcTemplate().query(sql, source, getRowMapper());
    }


    public List<IteamServerInfo> getByIteamIds(Collection<Integer> priceIds) {
        StringBuffer sql = new StringBuffer("SELECT  pd.name,td.name,sopi.unit_name,sopi.pkid FROM `so_order_prod_item` AS `sopi`");
        sql.append(" INNER JOIN `prod_price` AS `pp` ON `sopi`.`price_id` = `pp`.`pkid`");
        sql.append(" INNER JOIN `prod_service` AS ps ON `pp`.`service_id` = `ps`.`pkid`");
        sql.append(" INNER JOIN `bd_dict` AS `pd` ON `pd`.`pkid` = `ps`.`property_id`");
        sql.append(" INNER JOIN `bd_dict` AS `td` ON `td`.`pkid` = `ps`.`type_id`");
        sql.append(" WHERE `sopi`.`price_id` IN (:priceIds)");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("priceIds", priceIds);
        return getNamedParameterJdbcTemplate().query(sql.toString(), source, getIteamRowMapper());
    }

    public RowMapper<IteamServerInfo> getIteamRowMapper() {
        RowMapper<IteamServerInfo> rowMapper = (rs, i) -> {
            IteamServerInfo iteamServerInfo = new IteamServerInfo();
            iteamServerInfo.setIteamServerName(rs.getString("td.name"));
            iteamServerInfo.setIteamServerUnit(rs.getString("sopi.unit_name"));
            iteamServerInfo.setPkId(rs.getInt("sopi.pkid"));
            iteamServerInfo.setIteamServerFeature(rs.getString("pd.name"));
            return iteamServerInfo;
        };
        return rowMapper;
    }


}