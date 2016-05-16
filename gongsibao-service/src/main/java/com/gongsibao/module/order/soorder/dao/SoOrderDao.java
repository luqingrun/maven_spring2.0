package com.gongsibao.module.order.soorder.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ParamType;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("soOrderDao")
public class SoOrderDao extends BaseDao<SoOrder> {

    public static String INSERT_COLUMNS = " so_order.type, so_order.no, so_order.account_id, so_order.pay_status_id, " +
            "so_order.process_status_id, so_order.refund_status_id, so_order.total_price, so_order.payable_price, so_order.paid_price," +
            "so_order.source_type_id, so_order.is_installment, so_order.installment_mode, so_order.installment_audit_status_id, " +
            "so_order.is_change_price, so_order.change_price_audit_status_id, so_order.description, so_order.is_package, " +
            "so_order.package_id, so_order.add_time, so_order.add_user_id, so_order.is_invoice, so_order.account_name, so_order.account_mobile, so_order.prod_name ";


    public static String ALL_COLUMNS = "so_order.pkid, " + INSERT_COLUMNS;

    public static String CHANGPRICE_COLUMNS = "uc_user.real_name" + ALL_COLUMNS;

    @Override
    public Integer insert(SoOrder soOrder) {
        insertObject(soOrder);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrder soOrder) {
        getJdbcTemplate().update("insert into `so_order`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soOrder.getType(),
                soOrder.getNo(),
                soOrder.getAccountId(),
                soOrder.getPayStatusId(),
                soOrder.getProcessStatusId(),
                soOrder.getRefundStatusId(),
                soOrder.getTotalPrice(),
                soOrder.getPaidPrice(),
                soOrder.getPayablePrice(),
                soOrder.getSourceTypeId(),
                soOrder.getIsInstallment(),
                soOrder.getInstallmentMode(),
                soOrder.getInstallmentAuditStatusId(),
                soOrder.getIsChangePrice(),
                soOrder.getChangePriceAuditStatusId(),
                soOrder.getDescription(),
                soOrder.getIsPackage(),
                soOrder.getPackageId(),
                soOrder.getAddTime(),
                soOrder.getAddUserId()
        );
    }

    @Override
    public int update(SoOrder soOrder) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order` set pkid = pkid, `type` = :type, `no` = :no, `account_id` = :accountId, `pay_status_id` = :payStatusId, `process_status_id` = :processStatusId, `refund_status_id` = :refundStatusId, `total_price` = :totalPrice, `pay_price` = :payPrice, `source_type_id` = :sourceTypeId, `is_installment` = :isInstallment, `installment_mode` = :installmentMode, `installment_audit_status_id` = :installmentAuditStatusId, `is_change_price` = :isChangePrice, `change_price_audit_status_id` = :changePriceAuditStatusId, `description` = :description, `is_package` = :isPackage, `package_id` = :packageId, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrder),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrder findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order` where pkid = " + pkid, getRowMapper()));
    }

    public SoOrder findChangePriceById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("SELECT" + ALL_COLUMNS + "FROM `so_order` WHERE pkid =" + pkid + "AND `so_order`.`total_price` <> `so_order`.`payable_price`", getRowMapper()));
    }

    @Override
    public List<SoOrder> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public RowMapper<SoOrder> getRowMapper() {
        RowMapper<SoOrder> rowMapper = (rs, i) -> {
            SoOrder soOrder = new SoOrder();
            soOrder.setPkid(rs.getInt("so_order.pkid"));
            soOrder.setType(rs.getInt("so_order.type"));
            soOrder.setNo(rs.getString("so_order.no"));
            soOrder.setAccountId(rs.getInt("so_order.account_id"));
            soOrder.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            soOrder.setProcessStatusId(rs.getInt("so_order.process_status_id"));
            soOrder.setRefundStatusId(rs.getInt("so_order.refund_status_id"));
            soOrder.setTotalPrice(rs.getInt("so_order.total_price"));
            soOrder.setPaidPrice(rs.getInt("so_order.paid_price"));
            soOrder.setPayablePrice(rs.getInt("so_order.payable_price"));
            soOrder.setSourceTypeId(rs.getInt("so_order.source_type_id"));
            soOrder.setIsInstallment(rs.getInt("so_order.is_installment"));
            soOrder.setInstallmentMode(rs.getString("so_order.installment_mode"));
            soOrder.setInstallmentAuditStatusId(rs.getInt("so_order.installment_audit_status_id"));
            soOrder.setIsChangePrice(rs.getInt("so_order.is_change_price"));
            soOrder.setChangePriceAuditStatusId(rs.getInt("so_order.change_price_audit_status_id"));
            soOrder.setDescription(rs.getString("so_order.description"));
            soOrder.setIsPackage(rs.getInt("so_order.is_package"));
            soOrder.setPackageId(rs.getInt("so_order.package_id"));
            soOrder.setAddTime(rs.getTimestamp("so_order.add_time"));
            soOrder.setAddUserId(rs.getInt("so_order.add_user_id"));
            return soOrder;
        };
        return rowMapper;
    }


    public RowMapper<OrderList> getOrderRowMapper() {
        RowMapper<OrderList> rowMapper = (rs, i) -> {
            OrderList orderList = new OrderList();
            orderList.setPkid(rs.getInt("so_order.pkid"));
            orderList.setType(rs.getInt("so_order.type"));
            orderList.setNo(rs.getString("so_order.no"));
            orderList.setAccountId(rs.getInt("so_order.account_id"));
            orderList.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            orderList.setProcessStatusId(rs.getInt("so_order.process_status_id"));
            orderList.setRefundStatusId(rs.getInt("so_order.refund_status_id"));
            orderList.setTotalPrice(rs.getInt("so_order.total_price"));
            orderList.setPayablePrice(rs.getInt("so_order.payable_price"));
            orderList.setPaidPrice(rs.getInt("so_order.paid_price"));
            orderList.setSourceTypeId(rs.getInt("so_order.source_type_id"));
            orderList.setIsInstallment(rs.getInt("so_order.is_installment"));
            orderList.setInstallmentMode(rs.getString("so_order.installment_mode"));
            orderList.setInstallmentAuditStatusId(rs.getInt("so_order.installment_audit_status_id"));
            orderList.setIsChangePrice(rs.getInt("so_order.is_change_price"));
            orderList.setChangePriceAuditStatusId(rs.getInt("so_order.change_price_audit_status_id"));
            orderList.setDescription(rs.getString("so_order.description"));
            orderList.setIsPackage(rs.getInt("so_order.is_package"));
            orderList.setPackageId(rs.getInt("so_order.package_id"));
            orderList.setAddTime(rs.getTimestamp("so_order.add_time"));
            orderList.setAddUserId(rs.getInt("so_order.add_user_id"));
            orderList.setIsInvoice(rs.getInt("so_order.is_invoice"));
            orderList.setAccountName(rs.getString("so_order.account_name"));
            orderList.setAccountMoblie(rs.getString("so_order.account_mobile"));
            orderList.setProdName(rs.getString("so_order.prod_name"));
            return orderList;
        };
        return rowMapper;
    }


    @Override
    public List<SoOrder> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order` where 1=1 ");
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
    public int findOrderCountByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order.`pkid`)) ");
        sql.append("FROM `so_order` ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append("WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 查询列表
     *
     * @param properties
     * @param start
     * @param pageSize
     * @return
     */
    public List<OrderList> findOrderListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append(" FROM `so_order` ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);
        sql.append(" GROUP BY so_order.pkid ");
        sql.append(" ORDER BY so_order.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getOrderRowMapper());
    }

    //表连接
    private void setJoinSql(StringBuffer sql, Map<String, Object> properties) {
        // 产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        String realName = StringUtils.trimToEmpty(properties.get("realName"));
        Object userId = properties.get("userId");
        // 产品名称连表
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }
        sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
        sql.append("INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
        //权限
        if (null != properties.get("ids")) {
            sql.append(" AND so_order_prod_user_map.user_id IN (:ids) ");
        }
        sql.append("INNER JOIN `uc_user` ON so_order_prod_user_map.user_id = uc_user.pkid ");
        //业务员姓名
        if (StringUtils.isNotBlank(realName)) {
            sql.append(" AND uc_user.real_name like :realName ");
            properties.put("realName", properties.get("realName") + "%");
        }
        //业务员ID
        if (null != userId) {
            sql.append(" AND uc_user.pkid = :userId ");
        }

    }

    //查询条件
    private void setCondition(StringBuffer sql, Map<String, Object> properties) {
        if (null != properties.get("pkid")) {
            sql.append(" AND so_order.pkid = pkid");
        }
        if (null != properties.get("no")) {
            sql.append(" AND so_order.no like :no ");
            properties.put("no", properties.get("no") + "%");
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
        //订单类型
        if (null != properties.get("type")) {
            sql.append(" AND so_order.type = :type ");
        }
        //是否分期付款
        if (null != properties.get("isInstallment")) {
            sql.append(" AND so_order.is_installment = :isInstallment ");
        }

        // 分期状态
        if (null != properties.get("installmentAuditStatusId")) {
            sql.append("AND so_order.installment_audit_status_id = :installmentAuditStatusId ");
        }

        //是否开发票
        if (null != properties.get("isInvoice")) {
            sql.append(" AND so_order.is_invoice = :isInvoice ");
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
        //下单方式
        if (null != properties.get("sourceType")) {
            sql.append(" AND so_order.source_type_id = :sourceType ");
        }
        //是否改价订单
        if (null != properties.get("isChangePrice")) {
            sql.append(" AND so_order.is_change_price = :isChangePrice");
        }
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    //获取最大订单号
    public String findMaxNo() {
        String sql = "SELECT `no` FROM so_order ORDER BY pkid DESC, add_time DESC FOR UPDATE ";

        List<String> list = getJdbcTemplate().query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("no");
            }
        });

        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    public int addInstallment(SoOrder order) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE  `so_order` ");
        sql.append("SET ");
        sql.append("is_installment = ?, installment_mode = ?, installment_audit_status_id = ? ");
        sql.append("WHERE pkid = ? AND is_installment = 0 ");

        List<Object> list = new ArrayList<>();
        list.add(order.getIsInstallment());
        list.add(order.getInstallmentMode());
        list.add(order.getInstallmentAuditStatusId());
        list.add(order.getPkid());

        return getJdbcTemplate().update(sql.toString(), list.toArray());
    }

    /**
     * #########################################################################################
     */

    public static String INSERT_COLUMNS_List = " `type`,`no`,`account_id`,`account_name`,`account_mobile`,`pay_status_id`,`process_status_id`,`refund_status_id`,`total_price`,`payable_price`,`paid_price`,`source_type_id`,`is_installment`," +
            "`installment_mode`,`installment_audit_status_id`,`is_change_price`,`change_price_audit_status_id`,`is_invoice`,`description`,`is_package`,`package_id`,`add_time`,`add_user_id`,`prod_name`,";


    public static String ALL_COLUMNS_List = "pkid, " + INSERT_COLUMNS_List;


    /**
     * 退单查看模块
     *
     * @param pkidList
     * @return List<SoOrder>
     */
    public List<SoOrder> findListByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS_List + " from `so_order` where pkid IN (:pkidList) ", map, getListRowMapper());
    }


    public RowMapper<SoOrder> getListRowMapper() {
        RowMapper<SoOrder> rowMapper = (rs, i) -> {
            SoOrder soOrder = new SoOrder();
            soOrder.setPkid(rs.getInt("pkid"));
            soOrder.setType(rs.getInt("type"));
            soOrder.setNo(rs.getString("no"));
            soOrder.setAccountId(rs.getInt("account_id"));
            soOrder.setPayStatusId(rs.getInt("pay_status_id"));
            soOrder.setProcessStatusId(rs.getInt("process_status_id"));
            soOrder.setRefundStatusId(rs.getInt("refund_status_id"));
            soOrder.setTotalPrice(rs.getInt("total_price"));
            soOrder.setPaidPrice(rs.getInt("paid_price"));
            soOrder.setPayablePrice(rs.getInt("payable_price"));
            soOrder.setSourceTypeId(rs.getInt("source_type_id"));
            soOrder.setIsInstallment(rs.getInt("is_installment"));
            soOrder.setInstallmentMode(rs.getString("installment_mode"));
            soOrder.setInstallmentAuditStatusId(rs.getInt("installment_audit_status_id"));
            soOrder.setIsChangePrice(rs.getInt("is_change_price"));
            soOrder.setChangePriceAuditStatusId(rs.getInt("change_price_audit_status_id"));
            soOrder.setDescription(rs.getString("description"));
            soOrder.setIsPackage(rs.getInt("is_package"));
            soOrder.setPackageId(rs.getInt("package_id"));
            soOrder.setAddTime(rs.getTimestamp("add_time"));
            soOrder.setAddUserId(rs.getInt("add_user_id"));
            return soOrder;
        };
        return rowMapper;
    }

    public int addInvoice(SoOrder order) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE  `so_order` ");
        sql.append("SET ");
        sql.append("is_invoice = ? ");
        sql.append("WHERE pkid = ? AND is_invoice = 0 ");

        List<Object> list = new ArrayList<>();
        list.add(order.getIsInvoice());
        list.add(order.getPkid());

        return getJdbcTemplate().update(sql.toString(), list.toArray());
    }

    public int getInstallmentAuditCount(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT bd_audit_log.type_id, bd_audit_log.form_id) ");
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`form_id` = so_order.`pkid` ");

        // 产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        // 产品名称连表
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }

        sql.append("WHERE 1 = 1 ");
        setInstallmentAuditCondition(sql, properties);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<OrderList> getInstallmentAuditList(Map<String, Object> properties, Integer start, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`form_id` = so_order.`pkid` ");

        // 产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        // 产品名称连表
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }

        sql.append("WHERE 1 = 1 ");
        setInstallmentAuditCondition(sql, properties);

        sql.append(" GROUP BY bd_audit_log.type_id, bd_audit_log.form_id ");
        sql.append(" ORDER BY bd_audit_log.pkid ASC ");
        sql.append(" LIMIT :start, :pageSize ");

        properties.put("start", start);
        properties.put("pageSize", pageSize);

        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getOrderRowMapper());
    }

    /**
     * 分期审核列表查询条件
     *
     * @param sql
     * @param properties
     */
    private void setInstallmentAuditCondition(StringBuffer sql, Map<String, Object> properties) {
        if (null != properties.get("no")) {
            sql.append(" AND so_order.no like :no ");
            properties.put("no", properties.get("no") + "%");
        }

        if (null != properties.get("typeId")) {
            sql.append(" AND bd_audit_log.type_id = :typeId ");
        }
        if (null != properties.get("formId")) {
            sql.append(" AND bd_audit_log.form_id = :formId ");
        }
        if (null != properties.get("auditUserId")) {
            sql.append(" AND bd_audit_log.add_user_id = :auditUserId ");
        }
        if (null != properties.get("statusIds")) {
            sql.append(" AND bd_audit_log.status_id IN (:statusIds) ");
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
    }


    public List<Map<String, Object>> getAuditNums(Integer currentUserId, Integer type) {
        String sql = "SELECT `status_id`, COUNT(1) AS num FROM `bd_audit_log` WHERE add_user_id = ? AND type_id = ? GROUP BY status_id";

        return getJdbcTemplate().queryForList(sql, currentUserId, type);
    }

    public List<SoOrder> findByOrders(List<ParamType> paramTypeList, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_order` where 1=1 ");
        buildSQL(paramTypeList, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public int countByOrders(List<ParamType> paramTypeList) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order` where 1=1 ");
        buildSQL(paramTypeList, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), new HashMap(), Integer.class);
    }

    /**
     * 改价审核查询条件
     *
     * @param sql
     * @param properties
     */
    private void setChangePriceAuditCondition(StringBuffer sql, Map<String, Object> properties) {
        if (null != properties.get("no")) {
            sql.append("AND so_order.no like :no ");
            properties.put("no", properties.get("no") + "%");
        }
        //审核订单类型  改价,合同,发票...
        if (null != properties.get("typeId")) {
            sql.append(" AND bd_audit_log.type_id = :typeId");
        }
        //审核状态
        if (null != properties.get("statusIds")) {
            sql.append(" AND bd_audit_log.status_id IN (:statusIds)");
        }
        //审核人
        if (null != properties.get("auditUserId")) {
            sql.append(" AND bd_audit_log.add_user_id = :auditUserId ");
        }
        //下单人姓名
        if (null != properties.get("accountName")) {
            sql.append(" AND so_order.account_name like :accountName");
            properties.put("accountName", properties.get("accountName") + "%");
        }
        if (null != properties.get("accountMobile")) {
            sql.append(" AND so_order.account_name like :accountMobile ");
            properties.put("accountMobile", properties.get("accountMobile") + "%");
        }

        if (null != properties.get("beginTime")) {
            sql.append("AND so_order.add_time >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append("AND so_order.add_time <= :endTime");
        }


    }

    public int getChangePriceAuditCount(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(CHANGPRICE_COLUMNS);
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`from_id` = so_order.`pkid`");
        sql.append("INNER JOIN uc_user ON so_order.`add_user_id` = uc_user.`pkid`");

        //订单编号
        String orderNo = StringUtils.trimToEmpty(properties.get("no"));
        //产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }

        sql.append("WHERE 1=1");
        setChangePriceAuditCondition(sql, properties);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 改价审核订单查询
     *
     * @param properties
     * @param start
     * @param pageSize
     * @return
     */
    public List<OrderList> getChangePriceAuditList(Map<String, Object> properties, Integer start, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(CHANGPRICE_COLUMNS);
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`from_id` = so_order.`pkid`");
        sql.append("INNER JOIN uc_user ON so_order.`add_user_id` = uc_user.`pkid`");

        //订单编号
        String orderNo = StringUtils.trimToEmpty(properties.get("no"));
        //产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }

        sql.append("WHERE 1=1");
        setChangePriceAuditCondition(sql, properties);

        sql.append(" GROUP BY bd_audit_log.pkid");
        sql.append(" ORDER BY bd_audit_log.pkid ASC");
        sql.append(" LIMIT :start, :pageSize ");

        properties.put("start", start);
        properties.put("pageSize", pageSize);

        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getChangPriceRowMapper());

    }

    public int updateInstallmentStatus(Integer orderPkid, Integer status, Integer oldStatus) {
        String sql = "UPDATE `so_order` SET installment_audit_status_id = ? WHERE `pkid` = ? AND installment_audit_status_id = ?";
        return getJdbcTemplate().update(sql, status, orderPkid, oldStatus);
    }

    public RowMapper<OrderList> getChangPriceRowMapper() {
        RowMapper<OrderList> rowMapper = (rs, i) -> {
            OrderList orderList = new OrderList();
            orderList.setPkid(rs.getInt("so_order.pkid"));
            orderList.setType(rs.getInt("so_order.type"));
            orderList.setNo(rs.getString("so_order.no"));
            orderList.setAccountId(rs.getInt("so_order.account_id"));
            orderList.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            orderList.setProcessStatusId(rs.getInt("so_order.process_status_id"));
            orderList.setRefundStatusId(rs.getInt("so_order.refund_status_id"));
            orderList.setTotalPrice(rs.getInt("so_order.total_price"));
            orderList.setPayablePrice(rs.getInt("so_order.payable_price"));
            orderList.setPaidPrice(rs.getInt("so_order.paid_price"));
            orderList.setSourceTypeId(rs.getInt("so_order.source_type_id"));
            orderList.setIsInstallment(rs.getInt("so_order.is_installment"));
            orderList.setInstallmentMode(rs.getString("so_order.installment_mode"));
            orderList.setInstallmentAuditStatusId(rs.getInt("so_order.installment_audit_status_id"));
            orderList.setIsChangePrice(rs.getInt("so_order.is_change_price"));
            orderList.setChangePriceAuditStatusId(rs.getInt("so_order.change_price_audit_status_id"));
            orderList.setDescription(rs.getString("so_order.description"));
            orderList.setIsPackage(rs.getInt("so_order.is_package"));
            orderList.setPackageId(rs.getInt("so_order.package_id"));
            orderList.setAddTime(rs.getTimestamp("so_order.add_time"));
            orderList.setAddUserId(rs.getInt("so_order.add_user_id"));
            orderList.setIsInvoice(rs.getInt("so_order.is_invoice"));
            orderList.setAccountName(rs.getString("so_order.account_name"));
            orderList.setAccountMoblie(rs.getString("so_order.account_mobile"));
            orderList.setProdName(rs.getString("so_order.prod_name"));
            orderList.setBusinessName(rs.getString("uc_user.real_name"));
            return orderList;
        };
        return rowMapper;
    }

    /**
     * 改价订单-浮层
     *
     * @return
     */
    public RowMapper<SoOrderProdItem> getSoOrderProdItemRowMapper() {
        RowMapper<SoOrderProdItem> rowMapper = (rs, i) -> {
            SoOrderProdItem soOrderProdItem = new SoOrderProdItem();
            soOrderProdItem.setProductName(rs.getString("so_order_prod.product_name"));
            soOrderProdItem.setServiceName(rs.getString("so_order_prod_item.service_name"));
            soOrderProdItem.setCityId(rs.getInt("so_order_prod.city_id"));
            soOrderProdItem.setQuantity(rs.getInt("so_order_prod_item.quantity"));
            soOrderProdItem.setUnitName(rs.getString("so_order_prod_item.unit_name"));
            soOrderProdItem.setPriceOriginal(rs.getInt("so_order_prod_item.price_original"));
            soOrderProdItem.setPrice(rs.getInt("so_order_prod_item.price"));

            return soOrderProdItem;
        };
        return rowMapper;
    }

    /**
     * 改价订单-浮层
     *
     * @return
     */
    public List<SoOrderProdItem> getSoOrderProdItem(Integer orderId) {
        return getNamedParameterJdbcTemplate().query("select `so_order_prod`.`product_name`,`so_order_prod_item`.`service_name`,`so_order_prod`.`city_id`," +
                "`so_order_prod_item`.`quantity`,`so_order_prod_item`.`unit_name`,`so_order_prod_item`.`price_original`," +
                "`so_order_prod_item`.`price` from `so_order_prod`" +
                "inner join `so_order_prod_item` on `so_order_prod`.`pkid` = `so_order_prod_item`.`order_prod_id`" +

                "where `so_order_prod`.`order_id=`"+ orderId + "and `so_order_prod`.`price` <> `so_order_prod`.`price_original`",getSoOrderProdItemRowMapper() );
    }

    public int updateChangePriceStatus(Integer orderPkid, Integer status, Integer oldStatus) {
        String sql = "UPDATE `so_order` SET change_price_audit_status_id = ? WHERE `pkid` = ? AND change_price_audit_status_id = ?";
        return getJdbcTemplate().update(sql, status, orderPkid, oldStatus);
    }

    public List<SoOrder> findByAccountId(Integer accountId){
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_order` where account_id = " + accountId+
                "and pay_status_id in (3012,3013)", getRowMapper());

    }

}