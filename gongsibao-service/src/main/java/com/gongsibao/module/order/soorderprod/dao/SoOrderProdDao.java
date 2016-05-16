package com.gongsibao.module.order.soorderprod.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.ParamType;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorderprod.entity.*;
import com.gongsibao.module.product.prodpriceaudit.entity.CityArea;
import com.gongsibao.module.sys.bdauditlog.entity.AuditStatuses;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("soOrderProdDao")
public class SoOrderProdDao extends BaseDao<SoOrderProd> {

    public static String INSERT_COLUMNS = " so_order_prod.no, so_order_prod.order_id, so_order_prod.product_id, so_order_prod.product_name, " +
            "so_order_prod.city_id, so_order_prod.process_status_id, so_order_prod.audit_status_id, so_order_prod.price, so_order_prod.price_original, " +
            "so_order_prod.is_refund, so_order_prod.processed_days, so_order_prod.is_complaint, so_order_prod.need_days, so_order_prod.timeout_days";


    public static String ALL_COLUMNS = "so_order_prod.pkid, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProd soOrderProd) {
        insertObject(soOrderProd);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProd soOrderProd) {
        getJdbcTemplate().update("insert into `so_order_prod`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soOrderProd.getNo(),
                soOrderProd.getOrderId(),
                soOrderProd.getProductId(),
                soOrderProd.getProductName(),
                soOrderProd.getCityId(),
                soOrderProd.getProcessStatusId(),
                soOrderProd.getAuditStatusId(),
                soOrderProd.getPrice(),
                soOrderProd.getPriceOriginal(),
                soOrderProd.getIsRefund(),
                soOrderProd.getProcessedDays(),
                soOrderProd.getIsComplaint(),
                soOrderProd.getNeedDays(),
                soOrderProd.getTimeoutDays()
        );
    }

    @Override
    public int update(SoOrderProd soOrderProd) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod` set pkid = pkid, `no` = :no, `order_id` = :orderId, `product_id` = :productId, `product_name` = :productName, `city_id` = :cityId, `process_status_id` = :processStatusId, `audit_status_id` = :auditStatusId, `price` = :price, `price_original` = :priceOriginal where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProd),Map.class));
    }

    public int updateAuditStatus(Integer pkid, int newStatus, int oldStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod ");
        sql.append("SET ");
        sql.append("audit_status_id = ? ");
        sql.append("WHERE ");
        sql.append("pkid = ? ");
        sql.append("AND ");
        sql.append("audit_status_id = ? ");

        return getJdbcTemplate().update(sql.toString(), newStatus, pkid, oldStatus);
    }

    public int updateJobDays(Integer pkid, int processedDays, int needDays, int timeoutDays) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append("so_order_prod ");
        sql.append("SET ");
        sql.append("processed_days = ?, ");
        sql.append("need_days = ?, ");
        sql.append("timeout_days = ? ");
        sql.append("WHERE ");
        sql.append("pkid = ? ");

        return getJdbcTemplate().update(sql.toString(), processedDays, needDays, timeoutDays, pkid);
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderProd findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<SoOrderProd> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order_prod` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProd> getRowMapper() {
        RowMapper<SoOrderProd> rowMapper = (rs, i) -> {
            SoOrderProd soOrderProd = new SoOrderProd();
            soOrderProd.setPkid(rs.getInt("so_order_prod.pkid"));
            soOrderProd.setNo(rs.getString("so_order_prod.no"));
            soOrderProd.setOrderId(rs.getInt("so_order_prod.order_id"));
            soOrderProd.setProductId(rs.getInt("so_order_prod.product_id"));
            soOrderProd.setProductName(rs.getString("so_order_prod.product_name"));
            soOrderProd.setCityId(rs.getInt("so_order_prod.city_id"));
            soOrderProd.setProcessStatusId(rs.getInt("so_order_prod.process_status_id"));
            soOrderProd.setAuditStatusId(rs.getInt("so_order_prod.audit_status_id"));
            soOrderProd.setPrice(rs.getInt("so_order_prod.price"));
            soOrderProd.setIsRefund(rs.getInt("so_order_prod.is_refund"));
            soOrderProd.setPriceOriginal(rs.getInt("so_order_prod.price_original"));
            soOrderProd.setIsRefund(rs.getInt("so_order_prod.is_refund"));
            soOrderProd.setProcessedDays(rs.getInt("so_order_prod.processed_days"));
            soOrderProd.setIsComplaint(rs.getInt("so_order_prod.is_complaint"));
            soOrderProd.setNeedDays(rs.getInt("so_order_prod.need_days"));
            soOrderProd.setTimeoutDays(rs.getInt("so_order_prod.timeout_days"));
            return soOrderProd;
        };
        return rowMapper;
    }

    public RowMapper<OrderProdList> getOrderProdListRowMapper() {
        RowMapper<OrderProdList> rowMapper = (rs, i) -> {
            OrderProdList orderProdList = new OrderProdList();
            orderProdList.setPkid(rs.getInt("so_order_prod.pkid"));
            orderProdList.setOrderNo(rs.getString("so_order.no"));
            orderProdList.setNo(rs.getString("so_order_prod.no"));
            orderProdList.setOrderId(rs.getInt("so_order_prod.order_id"));
            orderProdList.setProductId(rs.getInt("so_order_prod.product_id"));
            orderProdList.setProductName(rs.getString("so_order_prod.product_name"));
            orderProdList.setCityId(rs.getInt("so_order_prod.city_id"));
            orderProdList.setProcessStatusId(rs.getInt("so_order_prod.process_status_id"));
            orderProdList.setAuditStatusId(rs.getInt("so_order_prod.audit_status_id"));
            orderProdList.setPrice(rs.getInt("so_order_prod.price"));
            orderProdList.setPriceOriginal(rs.getInt("so_order_prod.price_original"));
            orderProdList.setAccountName(rs.getString("so_order.account_name"));
            orderProdList.setAccountMobile(rs.getString("so_order.account_mobile"));
            orderProdList.setIsRefund(rs.getInt("so_order_prod.is_refund"));
            orderProdList.setProcessedDays(rs.getInt("so_order_prod.processed_days"));
            orderProdList.setIsComplaint(rs.getInt("so_order_prod.is_complaint"));
            orderProdList.setNeedDays(rs.getInt("so_order_prod.need_days"));
            orderProdList.setTimeoutDays(rs.getInt("so_order_prod.timeout_days"));
            orderProdList.setAddTime(rs.getDate("so_order.add_time"));
            orderProdList.setBusinessName(rs.getString("uc_user.real_name"));
            return orderProdList;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProd> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    /**
     * 查询数量
     *
     * @param properties
     * @return
     */
    public int findOrderProdCountByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order_prod.`pkid`)) ");
        sql.append(" FROM `so_order_prod` ");
        sql.append(" INNER JOIN `so_order` ON so_order.pkid = so_order_prod.order_id ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append("WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /***
     * 订单明细列表
     *
     * @param properties
     * @param start
     * @param pageSize
     * @return
     */
    public List<OrderProdList> findOrderProdListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS).append(",so_order.no,so_order.account_name,so_order.account_mobile,so_order.add_time,uc_user.real_name ");
        sql.append(" FROM `so_order_prod` ");
        sql.append(" INNER JOIN `so_order` ON so_order.pkid = so_order_prod.order_id ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);
        sql.append(" GROUP BY so_order_prod.pkid ");
        sql.append(" ORDER BY so_order.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getOrderProdListRowMapper());
    }

    //表连接
    private void setJoinSql(StringBuffer sql, Map<String, Object> properties) {
        sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
        sql.append(" INNER JOIN `uc_user` ON so_order_prod_user_map.user_id = uc_user.pkid ");
    }

    //查询条件
    private void setCondition(StringBuffer sql, Map<String, Object> properties) {
        //订单编号
        if (null != properties.get("orderNo")) {
            sql.append(" AND so_order.no like :orderNo ");
            properties.put("orderNo", properties.get("orderNo") + "%");
            return;
        }
        //订单明细编号
        if (null != properties.get("orderProdNo")) {
            sql.append(" AND so_order_prod.no like :orderProdNo ");
            properties.put("orderProdNo", properties.get("orderProdNo") + "%");
            return;
        }
        //订单ID
        if (null != properties.get("orderId")) {
            sql.append(" AND so_order_prod.order_id = :orderId ");
        }
        //产品名称
        if (StringUtils.isNotBlank(StringUtils.trimToEmpty(properties.get("productName")))) {
            sql.append(" AND so_order_prod.product_name like :productName");
            properties.put("productName", properties.get("productName") + "%");
        }
        //订单状态
        int state = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("state")));
        if (state > 0) {
            //订单状态 1等待付款、2已付全款、3已付部分款、4办理完成、5失效订单
            if (state == 1) {
                sql.append(" AND so_order.paid_price = 0 AND TIMESTAMPDIFF(HOUR ,NOW() ,so_order.add_time) < 72 ");
            } else if (state == 2) {
                sql.append(" AND so_order.paid_price = so_order.payable_price ");
            } else if (state == 3) {
                sql.append(" AND so_order.paid_price != so_order.payable_price AND so_order.paid_price > 0 ");
            } else if (state == 4) {
                sql.append(" AND so_order.process_status_id = 3024 ");
            } else if (state == 5) {
                sql.append(" AND so_order.paid_price = 0 AND TIMESTAMPDIFF(HOUR ,NOW() ,so_order.add_time) >= 72 ");
            }
        }
        //城市id
        String cityId = StringUtils.trimToEmpty(properties.get("cityId"));
        if (StringUtils.isNotBlank(cityId)) {
            sql.append(" AND so_order_prod.city_id = :cityId ");
        }
        //业务员姓名
        String realName = StringUtils.trimToEmpty(properties.get("realName"));
        if (StringUtils.isNotBlank(realName)) {
            sql.append(" AND uc_user.real_name like :realName ");
            properties.put("realName", properties.get("realName") + "%");
        }
        //下单人姓名
        if (null != properties.get("accountName")) {
            sql.append(" AND so_order.account_name like :accountName ");
            properties.put("accountName", properties.get("accountName") + "%");
        }
        //下单人手机
        if (null != properties.get("accountMobile")) {
            sql.append(" AND so_order.account_mobile like :accountMobile ");
            properties.put("accountMobile", properties.get("accountMobile") + "%");
        }
        //下单时间
        if (null != properties.get("beginTime")) {
            sql.append(" AND so_order.add_time >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append(" AND so_order.add_time <= :endTime ");
        }
        //订单订单处理状态
        if (null != properties.get("processStatusId")) {
            sql.append(" AND so_order.process_status_id = :processStatusId ");
        }
        //是否退单
        if (null != properties.get("isRefund")) {
            sql.append(" AND so_order_prod.is_refund = :isRefund");
        }
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public SoOrderProd updateStatus(SoOrderProd soOrderProd) {
        String sql = "update `so_order_prod` set `process_status_id` = :processStatusId, `audit_status_id` = :auditStatusId where pkid = :pkid";
        getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProd), Map.class));
        return findById(soOrderProd.getPkid());
    }

    public SoOrderProd updateIsComplaint(SoOrderProd soOrderProd) {
        String sql = "update `so_order_prod` set `is_complaint` = 1 where pkid = :pkid";
        getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProd), Map.class));
        return findById(soOrderProd.getPkid());
    }

    public List<SoOrderProd> getByOrderId(Integer orderId) {
        String sql = "SELECT " + ALL_COLUMNS + " FROM `so_order_prod` WHERE order_id = ? ORDER BY pkid DESC ";
        return getJdbcTemplate().query(sql, getRowMapper(), orderId);
    }

    public List<SoOrderProd> findBySoOrderProds(List<ParamType> paramTypeList, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod` where 1=1 ");
        buildSQL(paramTypeList, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public int countBySoOrderProds(List<ParamType> paramTypeList) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod` where 1=1 ");
        buildSQL(paramTypeList, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), new HashMap(), Integer.class);
    }

    /**
     * 通过订单表的orderId，得到退款产品订单列表
     *
     * @param orderId
     * @param isRefund
     * @return List<SoOrderProd>
     */
    public List<SoOrderProd> getRefundByOrderId(Integer orderId, Integer isRefund) {
        StringBuffer sql = new StringBuffer("");
        if (isRefund == 0) {//不退单产品列表
            sql.append("SELECT " + ALL_COLUMNS + " FROM `so_order_prod` where is_refund = 0 and order_id = ? ORDER BY pkid DESC ");
        } else {//退单产品列表
            sql.append("SELECT " + ALL_COLUMNS + " FROM `so_order_prod` where is_refund = 1 and order_id = ? ORDER BY pkid DESC ");
        }
        return getJdbcTemplate().query(sql.toString(), getRowMapper(), orderId);
    }


    /**
     * 通过参数批量插入退单商品记录
     *
     * @param
     */
    public int updateRefund(List<Integer> orderProdIds, Integer isRefund) {
        String sql = "update `so_order_prod` set  `is_refund` = :isRefund where pkid IN (:pkids)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("pkids", orderProdIds);
        source.addValue("isRefund", isRefund);
        return getNamedParameterJdbcTemplate().update(sql, source);

    }

    /**
     * 退单审核后更新商品表中的字段
     * @param orderProdIds
     * @param auditStatusId
     * @return int
     */
    public int updateAuditRefund(List<Integer> orderProdIds ,Integer auditStatusId ) {
        String sql = "update `so_order_prod` set  `audit_status_id` = :auditStatusId where pkid IN (:pkids)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("pkids", orderProdIds);
        source.addValue("auditStatusId", auditStatusId);
        return   getNamedParameterJdbcTemplate().update(sql, source);
    }

    public int returnProduct(int productPkid, int userId) {
        //3142  曾经负责
        String sql = "update  so_order_prod_user_map  set status_id = 3142 where user_id = " + userId + " and order_prod_id = " + productPkid;
        return getJdbcTemplate().update(sql);
    }

    /**
     * 订单审核搜索
     *
     * @param properties
     * @return 返回查询的数量
     */
    public int findOrderAuditByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order_prod.`pkid`)) ");
        sql.append(" FROM `so_order_prod` ");
        // 设置连表查询
        setOrderAuditJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setOrderAuditCondition(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 订单审核搜索
     *
     * @param properties
     * @return 分页查询
     */
    public List<OrderProdRow> findOrderAuditByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS).append(",uc_user.real_name, uc_organization.pkid, uc_organization.name");
        sql.append(" FROM `so_order_prod` ");
        // 设置连表查询
        setOrderAuditJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setOrderAuditCondition(sql, properties);
        sql.append(" GROUP BY so_order_prod.pkid ");
        sql.append(" ORDER BY so_order_prod_user_map.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getAuditOrderProdRowRowMapper());
    }

    //订单审核表连接
    private void setOrderAuditJoinSql(StringBuffer sql, Map<String, Object> properties) {
        sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
        sql.append(" INNER JOIN `uc_user` ON uc_user.pkid = so_order_prod_user_map.user_id ");
        sql.append(" INNER JOIN `uc_user_organization_map` ON uc_user_organization_map.user_id = uc_user.pkid ");
        sql.append(" INNER JOIN `uc_organization` ON uc_organization.pkid = uc_user_organization_map.organization_id ");
    }

    //订单审核查询条件
    private void setOrderAuditCondition(StringBuffer sql, Map<String, Object> properties) {
        //审核状态
        String auditStatusId = StringUtils.trimToEmpty(properties.get("auditStatusId"));
        if (StringUtils.isNotBlank(auditStatusId)) {
            sql.append(" AND so_order_prod.audit_status_id = :auditStatusId ");
        }
        //订单号
        String orderId = StringUtils.trimToEmpty(properties.get("orderId"));
        if (StringUtils.isNotBlank(orderId)) {
            sql.append(" AND so_order_prod.order_id = :orderId ");
        }
        //产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        if (StringUtils.isNotBlank(productName)) {
            sql.append(" AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }
        //业务员
        String realName = StringUtils.trimToEmpty(properties.get("realName"));
        if (StringUtils.isNotBlank(realName)) {
            sql.append(" AND uc_user.real_name like :realName ");
            properties.put("realName", properties.get("realName") + "%");
        }
        //组织机构
        String orgnizationName = StringUtils.trimToEmpty(properties.get("orgnizationName"));
        if (StringUtils.isNotBlank(orgnizationName)) {
            sql.append(" AND uc_organization.name like :orgnizationName ");
            properties.put("orgnizationName", properties.get("orgnizationName") + "%");
        }
        //权限控制,操作人员查看同组织的所有订单
        if (properties.get("orgIds") != null) {
            sql.append(" AND uc_organization.pkid IN(:orgIds) ");
        }
    }

    public RowMapper<OrderProdRow> getAuditOrderProdRowRowMapper() {
        RowMapper<OrderProdRow> rowMapper = (rs, i) -> {
            OrderProdRow orderProdRow = new OrderProdRow();
            orderProdRow.setOrderId(rs.getInt("so_order_prod.order_id"));//产品订单Id
            orderProdRow.setOrderIdStr(SecurityUtils.rc4Encrypt(rs.getInt("so_order_prod.order_id")));
            orderProdRow.setOrderProdId(rs.getInt("so_order_prod.pkid"));
            orderProdRow.setOrderProdIdStr(SecurityUtils.rc4Encrypt(rs.getInt("so_order_prod.pkid")));
            orderProdRow.setOrderNo(rs.getString("so_order_prod.no"));//产品订单编号
            orderProdRow.setProductId(rs.getInt("so_order_prod.product_id"));//产品id
            orderProdRow.setProductName(rs.getString("so_order_prod.product_name"));//产品名称
            orderProdRow.setUserName(rs.getString("uc_user.real_name"));//业务员
            orderProdRow.setOrgnizationId(rs.getInt("uc_organization.pkid"));//组织Id
            orderProdRow.setOrgnizationName(rs.getString("uc_organization.name"));//组织名称
            orderProdRow.setAuditStatusId(rs.getInt("so_order_prod.audit_status_id"));//审核状态
            orderProdRow.setProcessedDays(rs.getInt("so_order_prod.processed_days"));//办理天数
            orderProdRow.setNeedDays(rs.getInt("so_order_prod.need_days"));//需要天数
            orderProdRow.setTimeoutDays(rs.getInt("so_order_prod.timeout_days"));//超时天数
            orderProdRow.setCompanyName("");//公司名
            orderProdRow.setBrandName("");//商标名称
            orderProdRow.setBrandTypeName("");//商标类型
            return orderProdRow;
        };
        return rowMapper;
    }

    /**
     * 我负责的订单审核搜索
     *
     * @param properties
     * @return 返回查询的数量
     */
    public int findMyOrderListByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order_prod.`pkid`)) ");
        sql.append(" FROM `so_order_prod` ");
        // 设置连表查询
        setMyOrderListJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setMyOrderListJoinSql(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 我负责的订单搜索
     *
     * @param properties
     * @return 分页查询
     */
    public List<OrderProdRow> findMyOrderListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS).append(",so_order.account_name, so_order.account_mobile, uc_user.real_name, uc_organization.pkid, uc_organization.name");
        sql.append(" FROM `so_order_prod` ");
        // 设置连表查询
        setMyOrderListJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setMyOrderListCondition(sql, properties);
        sql.append(" GROUP BY so_order_prod.pkid ");
        sql.append(" ORDER BY so_order.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getMyOrderListProdRowRowMapper());
    }

    //我负责的订单表连接
    private void setMyOrderListJoinSql(StringBuffer sql, Map<String, Object> properties) {
        sql.append(" INNER JOIN `so_order` ON so_order.pkid = so_order_prod.order_id ");
        sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
        sql.append(" INNER JOIN `uc_user` ON uc_user.pkid = so_order_prod_user_map.user_id ");
        sql.append(" INNER JOIN `uc_user_organization_map` ON uc_user_organization_map.user_id = uc_user.pkid ");
        sql.append(" INNER JOIN `uc_organization` ON uc_organization.pkid = uc_user_organization_map.organization_id ");
    }

    //我负责的订单查询条件
    private void setMyOrderListCondition(StringBuffer sql, Map<String, Object> properties) {
        //权限控制,操作人员查看同组织的所有订单
        String userId = StringUtils.trimToEmpty(properties.get("userId"));
        if (StringUtils.isNotBlank(userId)) {
            sql.append(" AND so_order.add_user_id like :userId ");
            properties.put("userId", properties.get("userId") + "%");
        }
        //审核状态
        String auditStatusId = StringUtils.trimToEmpty(properties.get("auditStatusId"));
        if (StringUtils.isNotBlank(auditStatusId)) {
            sql.append(" AND so_order_prod.audit_status_id = :auditStatusId ");
        }
        //业务人员对订单的状态  3141正在负责  3142曾经负责
        String statusId = StringUtils.trimToEmpty(properties.get("statusId"));
        if (StringUtils.isNotBlank(statusId)) {
            sql.append(" AND so_order_prod_user_map.status_id= :statusId ");
        } else {
            sql.append(" AND so_order_prod_user_map.status_id = 3141 ");
        }
        //公司名
        //联系人
        String accountName = StringUtils.trimToEmpty(properties.get("accountName"));
        if (StringUtils.isNotBlank(accountName)) {
            sql.append(" AND so_order.account_name like :accountName ");
        }
        //联系人电话
        String accountMobile = StringUtils.trimToEmpty(properties.get("accountMobile"));
        if (StringUtils.isNotBlank(accountMobile)) {
            sql.append(" AND so_order.account_mobile like :accountMobile ");
        }
        //订单号
        String orderId = StringUtils.trimToEmpty(properties.get("orderId"));
        if (StringUtils.isNotBlank(orderId)) {
            sql.append(" AND so_order_prod.order_id = :orderId ");
        }
        //产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        if (StringUtils.isNotBlank(productName)) {
            sql.append(" AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }
        //地区
        String cityId = StringUtils.trimToEmpty(properties.get("cityId"));
        if (StringUtils.isNotBlank(cityId)) {
            sql.append(" AND so_order_prod.city_id like :cityId ");
            properties.put("cityId", properties.get("cityId") + "%");
        }
        //时间
        if (null != properties.get("beginTime")) {
            sql.append(" AND so_order.add_time >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append(" AND so_order.add_time <= :endTime ");
        }
    }

    public RowMapper<OrderProdRow> getMyOrderListProdRowRowMapper() {
        RowMapper<OrderProdRow> rowMapper = (rs, i) -> {
            OrderProdRow orderProdRow = new OrderProdRow();
            orderProdRow.setOrderId(rs.getInt("so_order_prod.order_id"));//产品订单Id
            orderProdRow.setOrderIdStr(SecurityUtils.rc4Encrypt(rs.getInt("so_order_prod.order_id")));
            orderProdRow.setOrderProdId(rs.getInt("so_order_prod.pkid"));
            orderProdRow.setOrderProdIdStr(SecurityUtils.rc4Encrypt(rs.getInt("so_order_prod.pkid")));
            orderProdRow.setOrderNo(rs.getString("so_order_prod.no"));//产品订单编号
            orderProdRow.setProductId(rs.getInt("so_order_prod.product_id"));//产品id
            orderProdRow.setProductName(rs.getString("so_order_prod.product_name"));//产品名称
            orderProdRow.setAccountName(rs.getString("so_order.account_name")); //联系人姓名
            orderProdRow.setAccountMobile(rs.getString("so_order.account_mobile"));//联系人电话
            orderProdRow.setUserName(rs.getString("uc_user.real_name"));//业务员
            orderProdRow.setOrgnizationId(rs.getInt("uc_organization.pkid"));//组织Id
            orderProdRow.setOrgnizationName(rs.getString("uc_organization.name"));//组织名称
            orderProdRow.setAuditStatusId(rs.getInt("so_order_prod.audit_status_id"));//审核状态
            orderProdRow.setProcessStatusId(rs.getInt("so_order_prod.process_status_id"));//处理节点Id
            orderProdRow.setProcessedDays(rs.getInt("so_order_prod.processed_days"));//办理天数
            orderProdRow.setNeedDays(rs.getInt("so_order_prod.need_days"));//需要天数
            orderProdRow.setTimeoutDays(rs.getInt("so_order_prod.timeout_days"));//超时天数
            orderProdRow.setCompanyName("");//公司名
            orderProdRow.setBrandName("");//商标名称
            orderProdRow.setBrandTypeName("");//商标类型
            return orderProdRow;
        };
        return rowMapper;
    }


    public int countOrderProdRowsByProperties(Map<String, Object> map) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                "SELECT Count(1) " +
                        "FROM " +
                        "(SELECT  " +
                        "  op.`pkid`, " +
                        "  op.`city_id`, " +
                        "  op.`need_days`, " +
                        "  op.`processed_days`, " +
                        "  o.`pkid` AS orderId, " +
                        "  o.`no` orderNo, " +
                        "  o.`account_id`, " +
                        "  o.`account_name`, " +
                        "  p.`pkid` AS prodId, " +
                        "  p.`name` AS prodName, " +
                        "  o.`add_time`, " +
                        "  op.`audit_status_id`, " +
                        "  op.`is_refund`, " +
                        "  op.`process_status_id`  " +
                        "  FROM " +
                        "  so_order_prod op  " +
                        "  LEFT JOIN so_order o  " +
                        "    ON op.`order_id` = o.`pkid`  " +
                        "  LEFT JOIN prod_product p  " +
                        "    ON op.`product_id` = p.`pkid`) t Where 1=1 ");

        MakeQueryOrderProdRowListSql(map, sql);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, Integer.class);
    }

    /**
     * 状态编码（产品处理状态码+审核状态码，eg 00）
     * 产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
     * 审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
     */
    public List<OrderProdRow> findOrderProdRowsByProperties(Map<String, Object> map, int startRow, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                "SELECT * " +
                        "FROM " +
                        "(SELECT  " +
                        "  op.`pkid`, " +
                        "  op.`city_id`, " +
                        "  op.`need_days`, " +
                        "  op.`processed_days`, " +
                        "  o.`pkid` AS orderId, " +
                        "  o.`no` orderNo, " +
                        "  o.`account_id`, " +
                        "  o.`account_name`, " +
                        "  p.`pkid` AS prodId, " +
                        "  p.`name` AS prodName, " +
                        "  o.`add_time`, " +
                        "  op.`audit_status_id`, " +
                        "  op.`is_refund`, " +
                        "  op.`process_status_id`  " +
                        " FROM " +
                        "  so_order_prod op  " +
                        "  LEFT JOIN so_order o  " +
                        "    ON op.`order_id` = o.`pkid`  " +
                        "  LEFT JOIN prod_product p  " +
                        "    ON op.`product_id` = p.`pkid` ) t Where 1=1 ");

        MakeQueryOrderProdRowListSql(map, sql);


        //分页
        sql.append(" LIMIT :start, :pageSize ");
        map.put("start", startRow);
        map.put("pageSize", pageSize);

        return getNamedParameterJdbcTemplate().query(sql.toString(), map, getOrderProdRowMapper());
    }

    private void MakeQueryOrderProdRowListSql(Map<String, Object> map, StringBuffer sql) {
        //开始时间
        if (map.containsKey("beginTime")) {
            sql.append(" And t.`add_time`>= :beginTime");
        }
        //结束时间
        if (map.containsKey("endTime")) {
            sql.append(" And t.`add_time`<= :endTime");
        }
        //订单号
        if (map.containsKey("orderNo")) {
            sql.append(" And t.`orderNo` Like :orderNo");
        }
        //产品名称
        if (map.containsKey("prodName")) {
            sql.append(" And t.`prodName` like :prodName ");
        }
        if (map.containsKey("cityId")) {
            sql.append(" And t.`city_id` like :cityId ");
        }
        //审核状态
        if (map.containsKey("auditStatusId")) {

            //审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
            Integer statusId = Integer.valueOf(map.get("auditStatusId").toString());
            switch (statusId) {
                case 0:
                    sql.append(" And t.`audit_status_id` = " + AuditStatuses.Type_1051);
                    break;
                case 1:
                    sql.append(" And t.`audit_status_id` = " + AuditStatuses.Type_1052);
                    break;
                case 2:
                    sql.append(" And t.`audit_status_id` = " + AuditStatuses.Type_1054);
                    break;
                case 4:
                    sql.append(" And t.`audit_status_id` = " + AuditStatuses.Type_1053);
                    break;
                default:
                    //条件永远为false
                    sql.append(" And 1= 0 ");
                    break;
            }

        }
        //进程状态
        if (map.containsKey("processStatusId")) {
            //产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
            Integer statusId = Integer.valueOf(map.get("processStatusId").toString());
            switch (statusId) {
                case 0://
                    //type=306，3061业务、3062客服（关注）、3063操作
                    sql.append(" AND EXISTS(SELECT 1 FROM so_order_prod_user_map a  " +
                            " WHERE a.`user_id` IS NULL And a.type_id=3063 AND a.`order_prod_id`=t.`pkid`) ");
                    break;
                case 1://操作人员不为空，为正在处理
                    //type=306，3061业务、3062客服（关注）、3063操作
                    sql.append(" AND EXISTS(SELECT 1 FROM so_order_prod_user_map a  " +
                            " WHERE a.`user_id` IS Not NULL And a.type_id=3063 AND a.`order_prod_id`=t.`pkid`) ");
                    break;
                case 2://(进入审核状态的都为正常结束的)
                    break;

                case 8://异常结束的为，退款订单
                    //TODO 需要合适异常终止处理的订单 判断逻辑
                    sql.append(" And t.`is_refund`= 1");
                    break;
                default:
                    //条件永远为false
                    sql.append(" And 1= 0 ");
                    break;

            }

        }
        //业务人员
        if (map.containsKey("userName")) {
            //type=306，3061业务、3062客服（关注）、3063操作
            sql.append(" AND EXISTS(SELECT 1 FROM so_order_prod_user_map a  " +
                    " LEFT JOIN uc_user u ON a.`user_id`=u.`pkid` " +
                    " WHERE u.`real_name` like :userName And a.type_id=3061 AND a.`order_prod_id`=t.`pkid`) ");
        }

        //权限控制,操作人员查看同组织的所有订单
        if (map.containsKey("orgIds")) {
            sql.append(" AND EXISTS(SELECT 1 FROM so_order_prod_organization_map b " +
                    " WHERE b.organization_id  IN( :orgIds) AND b.`order_prod_id`=t.`pkid`) ");
        }

    }

    /**
     * 获取产品订单明细行
     */
    private RowMapper<OrderProdRow> getOrderProdRowMapper() {
        RowMapper<OrderProdRow> rowMapper = (rs, i) -> {
            OrderProdRow orderProdRow = new OrderProdRow();
            orderProdRow.setCityArea(new CityArea());
            orderProdRow.setOrderProdId(rs.getInt("pkid"));
            orderProdRow.getCityArea().setPkid(rs.getInt("city_id"));
            orderProdRow.setOrderId(rs.getInt("orderId"));
            orderProdRow.setOrderNo(rs.getString("orderNo"));
            orderProdRow.setAccountName(rs.getString("account_name"));
            orderProdRow.setProductId(rs.getInt("prodId"));
            orderProdRow.setProductName(rs.getString("prodName"));
            orderProdRow.setAddTime(rs.getTimestamp("add_time"));
            orderProdRow.setAuditStatusId(rs.getInt("audit_status_id"));
            orderProdRow.setProcessStatusId(rs.getInt("process_status_id"));
            orderProdRow.setNeedDays(rs.getInt("need_days"));
            orderProdRow.setProcessedDays(rs.getInt("processed_days"));
            return orderProdRow;
        };
        return rowMapper;
    }

    /**
     * 订单流程监控列表查询数量
     *
     * @param properties
     * @return
     */
    public int findOrderProdMonitorCountByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order_prod.`pkid`)) ");
        sql.append(" FROM `so_order_prod` ");
        sql.append(" INNER JOIN `so_order` ON so_order.pkid = so_order_prod.order_id ");
        // 设置连表查询
        setMonitorJoinSql(sql, properties);
        sql.append("WHERE 1 = 1 ");
        // 查询条件
        setMonitorCondition(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /***
     * 订单流程监控列表
     *
     * @param properties
     * @param start
     * @param pageSize
     * @return
     */
    public List<OrderProdMonitorList> findOrderProdMonitorListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS).append(",so_order.no,so_order.add_time ");
        sql.append(" FROM `so_order_prod` ");
        sql.append(" INNER JOIN `so_order` ON so_order.pkid = so_order_prod.order_id ");
        // 设置连表查询
        setMonitorJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setMonitorCondition(sql, properties);
        sql.append(" GROUP BY so_order_prod.pkid ");
        sql.append(" ORDER BY so_order.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getOrderProdMonitorListRowMapper());
    }

    //表连接
    private void setMonitorJoinSql(StringBuffer sql, Map<String, Object> properties) {
        String realName = StringUtils.trimToEmpty(properties.get("realName"));
        // 用户名称链表
        if (StringUtils.isNotBlank(realName)) {
            sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
            sql.append(" INNER JOIN `uc_user` ON so_order_prod_user_map.user_id = uc_user.pkid ");
        }
    }

    //查询条件
    private void setMonitorCondition(StringBuffer sql, Map<String, Object> properties) {
        //订单编号
        if (StringUtils.isNotBlank(StringUtils.trimToEmpty(properties.get("orderNo")))) {
            sql.append(" AND so_order.no like :orderNo ");
            properties.put("orderNo", properties.get("orderNo") + "%");
        }
        //产品名称
        if (StringUtils.isNotBlank(StringUtils.trimToEmpty(properties.get("productName")))) {
            sql.append(" AND so_order_prod.product_name like :productName");
            properties.put("productName", properties.get("productName") + "%");
        }
        //城市id
        String cityId = StringUtils.trimToEmpty(properties.get("cityId"));
        if (StringUtils.isNotBlank(cityId)) {
            sql.append(" AND so_order_prod.city_id = :cityId ");
        }
        //业务员姓名
        String realName = StringUtils.trimToEmpty(properties.get("realName"));
        if (StringUtils.isNotBlank(realName)) {
            sql.append(" AND uc_user.real_name like :realName ");
            properties.put("realName", properties.get("realName") + "%");
        }
        //下单时间
        if (null != properties.get("beginTime")) {
            sql.append(" AND so_order.add_time >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append(" AND so_order.add_time <= :endTime ");
        }
        //类型 0:全部 1: 重点关注 2:被投诉 3:无人负责 4:超时 5:办理终止
        int type = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("type")));
        if (type > 0) {
            if (type == 2) {
                sql.append(" AND so_order_prod.is_complaint = 1 ");
            } else if (type == 4) {
                sql.append(" AND so_order_prod.timeout_days > 0 ");
            } else if (type == 5) {
                sql.append(" AND so_order_prod.is_refund = 1 ");
            } else {
            }
        }
        // 产品订单ID集合
        if (properties.get("orderProdIds") != null) {
            sql.append(" AND so_order_prod.pkid IN (:orderProdIds) ");
        }

    }

    public RowMapper<OrderProdMonitorList> getOrderProdMonitorListRowMapper() {
        RowMapper<OrderProdMonitorList> rowMapper = (rs, i) -> {
            OrderProdMonitorList orderProdMonitorList = new OrderProdMonitorList();
            orderProdMonitorList.setPkid(rs.getInt("so_order_prod.pkid"));
            orderProdMonitorList.setOrderNo(rs.getString("so_order.no"));
            orderProdMonitorList.setNo(rs.getString("so_order_prod.no"));
            orderProdMonitorList.setOrderId(rs.getInt("so_order_prod.order_id"));
            orderProdMonitorList.setProductId(rs.getInt("so_order_prod.product_id"));
            orderProdMonitorList.setProductName(rs.getString("so_order_prod.product_name"));
            orderProdMonitorList.setCityId(rs.getInt("so_order_prod.city_id"));
            orderProdMonitorList.setProcessStatusId(rs.getInt("so_order_prod.process_status_id"));
            orderProdMonitorList.setAuditStatusId(rs.getInt("so_order_prod.audit_status_id"));
            orderProdMonitorList.setPrice(rs.getInt("so_order_prod.price"));
            orderProdMonitorList.setPriceOriginal(rs.getInt("so_order_prod.price_original"));
            orderProdMonitorList.setIsRefund(rs.getInt("so_order_prod.is_refund"));
            orderProdMonitorList.setProcessedDays(rs.getInt("so_order_prod.processed_days"));
            orderProdMonitorList.setIsComplaint(rs.getInt("so_order_prod.is_complaint"));
            orderProdMonitorList.setNeedDays(rs.getInt("so_order_prod.need_days"));
            orderProdMonitorList.setTimeoutDays(rs.getInt("so_order_prod.timeout_days"));
            orderProdMonitorList.setAddTime(rs.getDate("so_order.add_time"));
            return orderProdMonitorList;
        };
        return rowMapper;
    }

    /**
     * 统计
     *
     * @param statusStr 状态编码（产品处理状态码+审核状态码，eg 00）
     *                  产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
     *                  审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
     */
    public Integer countOrderProdRows(String statusStr, String orgIdsStr) {
        StringBuffer sql = new StringBuffer();

        //关系类型序号，type=306，3061业务、3062客服（关注）、3063操作
        //TODO 将人员类型
        sql.append("SELECT count(1) FROM so_order_prod op  LEFT JOIN so_order_prod_user_map opu ON op.`pkid`=opu.`order_prod_id` Where opu.type_id=3063 ");
        if (StringUtils.isNotBlank(statusStr)) {
            switch (statusStr) {
                //代办理(1051)(未审核，且有操作员)
                case "00":
                    sql.append(" And opu.`user_id` IS NULL AND op.`audit_status_id`=" + AuditStatuses.Type_1051);
                    break;
                //正在办理(未审核，但有操作员)
                case "10":
                    sql.append(" And opu.`user_id` IS Not NULL AND op.`audit_status_id`=" + AuditStatuses.Type_1051);
                    break;
                //审核中
                case "21":
                    sql.append(" And  op.`audit_status_id`=" + AuditStatuses.Type_1052);
                    break;
                //审核不通过
                case "24":
                    sql.append(" And  op.`audit_status_id`=" + AuditStatuses.Type_1053);
                    break;
            }
        }

        //权限控制,操作人员查看同组织的所有订单

        sql.append(" AND EXISTS(SELECT 1 FROM so_order_prod_organization_map b " +
                " WHERE b.organization_id  IN( " + orgIdsStr + ") AND b.`order_prod_id`=`order_prod_id`) ");


        return getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
    }



    public List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT pkid ");
        sql.append("FROM ");
        sql.append("so_order_prod ");
        sql.append("WHERE 1=1 ");
        if (NumberUtils.toInt(properties.get("noAuditStatusId")) > 0) {
            sql.append("AND audit_status_id != :noAuditStatusId ");
        }
        List<Integer> orderProdIds = (List<Integer>) properties.get("orderProdIds");
        if (CollectionUtils.isNotEmpty(orderProdIds)) {
            sql.append("AND pkid IN (" + StringUtils.join(orderProdIds, ",") + ") ");
        }
        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), properties, Integer.class);
    }

    public int countByJobProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(1) from `so_order_prod` where 1=1 ");
        if (NumberUtils.toInt(properties.get("noAuditStatusId")) > 0) {
            sql.append("AND audit_status_id != :noAuditStatusId ");
        }
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<SoOrderProd> findByJobProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order_prod` where 1=1 ");
        if (NumberUtils.toInt(properties.get("noAuditStatusId")) > 0) {
            sql.append("AND audit_status_id != :noAuditStatusId ");
        }
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

}