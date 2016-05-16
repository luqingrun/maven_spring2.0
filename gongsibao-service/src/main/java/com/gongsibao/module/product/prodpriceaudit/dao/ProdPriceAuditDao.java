package com.gongsibao.module.product.prodpriceaudit.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.product.prodprice.entity.ProdPriceDetail;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceAudit;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceAuditRow;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceOnSaleRow;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodPriceAuditDao")
public class ProdPriceAuditDao extends BaseDao<ProdPriceAudit> {

    public static String INSERT_COLUMNS = " `product_id`, `organization_id`, `audit_status_id`, `audit_status_type`, `add_time`, `add_user_id`, `remark` ";
    public static String INSERT_COLUMNS_AUDIT = " `product_id`, `organization_id`, `audit_status_id`,`audit_status_type`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdPriceAudit prodPriceAudit) {
        insertObject(prodPriceAudit);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdPriceAudit prodPriceAudit) {
        getJdbcTemplate().update("insert into `prod_price_audit`(" + INSERT_COLUMNS_AUDIT + ") values (?, ?, ?, ?,? )",

                prodPriceAudit.getProductId(),
                prodPriceAudit.getOrganizationId(),
                prodPriceAudit.getAuditStatusId(),
                prodPriceAudit.getAuditStatusType(),
                prodPriceAudit.getAddUserId()
        );
    }

    @Override
    public int update(ProdPriceAudit prodPriceAudit) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_price_audit` set pkid = pkid, `product_id` = :productId, `status_id` = :statusId, `content` = :content, `audit_time` = :auditTime, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodPriceAudit),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_price_audit` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdPriceAudit findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price_audit` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<ProdPriceAudit> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price_audit` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public RowMapper<ProdPriceAudit> getRowMapper() {
        return (rs, i) -> {
            ProdPriceAudit prodPriceAudit = new ProdPriceAudit();
            prodPriceAudit.setPkid(rs.getInt("pkid"));
            prodPriceAudit.setProductId(rs.getInt("product_id"));
            prodPriceAudit.setAuditStatusId(rs.getInt("audit_status_id"));
            prodPriceAudit.setAuditStatusType(rs.getInt("audit_status_type"));
            prodPriceAudit.setOrganizationId(rs.getInt("organization_id"));
            prodPriceAudit.setAddTime(rs.getTimestamp("add_time"));
            prodPriceAudit.setAddUserId(rs.getInt("add_user_id"));
            prodPriceAudit.setRemark(rs.getString("remark"));
            return prodPriceAudit;
        };
    }

    /**
     * map to ProdPriceMap
     */
    private RowMapper<ProdPriceAuditRow> getProdPriceAuditRowMapper() {
        return (rs, i) -> {
            ProdPriceAuditRow prodPriceAuditRow = new ProdPriceAuditRow();
            prodPriceAuditRow.setPkid(rs.getInt("pkid"));
            prodPriceAuditRow.setProductId(rs.getInt("product_id"));
            prodPriceAuditRow.setProductName(rs.getString("product_name"));
            prodPriceAuditRow.setOrganizationId(rs.getInt("organization_id"));
            prodPriceAuditRow.setStatusId(rs.getInt("audit_status_id"));
            prodPriceAuditRow.setStatusType(rs.getInt("audit_status_type"));

            // prodPriceAuditRow.setAddTime(rs.getTimestamp("add_time"));
            //prodPriceAuditRow.setAddUserId(rs.getInt("add_user_id"));
            //prodPriceAuditRow.setRemark(rs.getString("remark"));
            return prodPriceAuditRow;
        };
    }

    private RowMapper<ProdPriceOnSaleRow> getProdPriceOnSaleRowMapper() {
        return (rs, i) -> {
            ProdPriceOnSaleRow prodPriceOnSaleRow = new ProdPriceOnSaleRow();

            prodPriceOnSaleRow.setProductId(rs.getInt("product_id"));
            prodPriceOnSaleRow.setProductName(rs.getString("product_name"));
            prodPriceOnSaleRow.setOrganizationId(rs.getInt("organization_id"));

            return prodPriceOnSaleRow;
        };
    }

    @Override
    public List<ProdPriceAudit> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_price_audit` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_price_audit` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<ProdPriceAuditRow> findAuditRowByProperties(Map<String, Object> properties, int startRow, int pageSize) {
        StringBuffer sql = new StringBuffer("SELECT distinct b.pkid, b.organization_id, b.product_id, p.name product_name, b.audit_status_id,b.audit_status_type FROM gsb.prod_price_audit b JOIN gsb.prod_price a ON a.price_audit_id = b.pkid JOIN gsb.prod_product p ON p.pkid = b.product_id  where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(startRow).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getProdPriceAuditRowMapper());
    }

    public int countAuditRowByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("SELECT count(distinct b.pkid) FROM gsb.prod_price_audit b JOIN gsb.prod_price a ON a.price_audit_id = b.pkid JOIN gsb.prod_product p ON p.pkid = b.product_id  where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public boolean updateProdPriceAuditStatus(ProdPriceAudit prodPriceAudit) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkid", prodPriceAudit.getPkid());
        map.put("audit_status_id", prodPriceAudit.getAuditStatusId());

        String sql = "update `prod_price_audit` set `audit_status_id` = :audit_status_id where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, map) > 0;
    }

    /**
     * 通过产品ID，组织ID,状态来获取审核记录
     */
    public List<ProdPriceAudit> findProdPriceAuditRowsBy(Integer prodId, Integer organizationId, int auditStatusId) {

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", prodId);
        map.put("organization_id", organizationId);
        map.put("audit_status_id", auditStatusId);

        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_price_audit` where product_id=:product_id And organization_id=:organization_id AND audit_status_id=:audit_status_id", map, getRowMapper());

    }

    public int countOnSaleRowsByProperties(Map<String, Object> condition) {
        StringBuffer sql = new StringBuffer("SELECT  COUNT(DISTINCT b.organization_id, b.product_id)FROM prod_price_audit b JOIN prod_price a ON a. price_audit_id = b.pkid JOIN prod_product p ON p.pkid = b.product_id WHERE 1=1 ");
        buildSQL(condition, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), condition, Integer.class);
    }

    public List<ProdPriceOnSaleRow> findOnSaleRowsByProperties(Map<String, Object> condition, int startRow, int pageSize) {
        StringBuffer sql = new StringBuffer("SELECT DISTINCT b.organization_id, b.product_id, p.name product_name FROM prod_price_audit b JOIN prod_price a ON a.price_audit_id = b.pkid JOIN prod_product p ON p.pkid = b.product_id WHERE 1=1 ");
        //TODO 修改实现方式
        if (condition.containsKey("product_name")) {
            sql.append(" And product_name like " + condition.get("product_name") + " ");
            condition.remove("product_name");
        }

        buildSQL(condition, sql);
        sql.append(" limit ").append(startRow).append(", ").append(pageSize);

        return getNamedParameterJdbcTemplate().query(sql.toString(), condition, getProdPriceOnSaleRowMapper());
    }

    /**
     * 通过组织Id 和产品Id 以及审核状态来获取上架的确Id 列表
     */
    public List<Integer> getCityIdsByOrgIdAndProdId(Integer organizationId, Integer productId, Integer auditStatusId) {
        StringBuffer sql = new StringBuffer("SELECT  DISTINCT a.`city_id`FROM prod_price_audit b JOIN prod_price a ON a.price_audit_id = b.pkid WHERE b.organization_id=:organizationId And    b.product_id=:productId   AND b.`audit_status_id`=:auditStatusId ");
        Map<String, Object> map = new HashMap<>();
        map.put("organizationId", organizationId);
        map.put("productId", productId);
        map.put("auditStatusId", auditStatusId);

        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), map, Integer.class);
    }

}