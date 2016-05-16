package com.gongsibao.module.order.sorefund.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.sorefund.entity.SoRefund;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("soRefundDao")
public class SoRefundDao extends BaseDao<SoRefund> {

    public static String INSERT_COLUMNS = " `order_id`, `audit_status_id`, `way_type_id`, `is_full_refund`, `no`, `payer_name`, `bank_no`, `amount`, `cost`, `remark`, `add_time`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoRefund soRefund) {
        insertObject(soRefund);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoRefund soRefund) {
        getJdbcTemplate().update("insert into `so_refund`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ? )",
                soRefund.getOrderId(),
                soRefund.getAuditStatusId(),
                soRefund.getWayTypeId(),
                soRefund.getIsFullRefund(),
                soRefund.getNo(),
                soRefund.getPayerName(),
                soRefund.getBankNo(),
                soRefund.getAmount(),
                soRefund.getCost(),
                soRefund.getRemark(),
                soRefund.getAddUserId()
        );
    }

    /**
     * 插入一条新的记录后，更新该记录的no字段值为pkid
     *
     * @param pkid
     * @param no
     * @param userId
     * @return
     */
    public Integer updateNo(Integer pkid, Integer no, Integer userId) {
        return getJdbcTemplate().update("UPDATE `so_refund` SET `no` = ?, add_time = NOW(), add_user_id = ? WHERE pkid = ?", no, userId, pkid);
    }

    @Override
    public int update(SoRefund soRefund) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_refund` set pkid = pkid, `order_id` = :orderId, `audit_status_id` = :auditStatusId, `way_type_id` = :wayTypeId, `is_full_refund` = :isFullRefund, `no` = :no, `payer_name` = :payerName, `bank_no` = :bankNo, `amount` = :amount, `cost` = :cost, `remark` = :remark, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soRefund),Map.class));
    }

    /**
     * 退单审核模块：退单审核后更新退单表中的状态
     * @param pkid
     * @param auditStatusId
     * @return int
     */
    public int updateSoRefund(Integer pkid ,Integer auditStatusId ) {
         return getJdbcTemplate().update("UPDATE `so_refund` SET `audit_status_id` = ?, add_time = NOW() WHERE pkid = ?", auditStatusId,pkid);
    }
    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_refund` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoRefund findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `so_refund` where pkid = " + pkid, getListRowMapper());
    }

    @Override
    public List<SoRefund> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_refund` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    public List<SoRefund> findListByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_refund` where pkid IN (:pkidList) ", map, getListRowMapper());
    }


    /**
     * 退单查看模块：退单列表展示
     *
     * @return
     */
    public RowMapper<SoRefund> getListRowMapper() {
        RowMapper<SoRefund> rowMapper = (rs, i) -> {
            SoRefund soRefund = new SoRefund();
            soRefund.setPkid(rs.getInt("pkid"));
            soRefund.setOrderId(rs.getInt("order_id"));
            soRefund.setAuditStatusId(rs.getInt("audit_status_id"));
            soRefund.setWayTypeId(rs.getInt("way_type_id"));
            soRefund.setIsFullRefund(rs.getInt("is_full_refund"));
            soRefund.setNo(rs.getString("no"));
            soRefund.setPayerName(rs.getString("payer_name"));
            soRefund.setBankNo(rs.getString("bank_no"));
            soRefund.setAmount(rs.getInt("amount"));
            soRefund.setCost(rs.getInt("cost"));
            soRefund.setRemark(rs.getString("remark"));
            soRefund.setAddTime(rs.getTimestamp("add_time"));
            soRefund.setAddUserId(rs.getInt("add_user_id"));
            return soRefund;
        };
        return rowMapper;
    }

    @Override
    public RowMapper<SoRefund> getRowMapper() {
        RowMapper<SoRefund> rowMapper = (rs, i) -> {
            SoRefund soRefund = new SoRefund();
            SoOrder soOrder = new SoOrder();
            BdAuditLog bdAuditLog = new BdAuditLog();

            soRefund.setPkid(rs.getInt("sr.pkid"));
            soRefund.setOrderId(rs.getInt("sr.order_id"));
            soRefund.setAuditStatusId(rs.getInt("sr.audit_status_id"));
            soRefund.setWayTypeId(rs.getInt("sr.way_type_id"));
            soRefund.setIsFullRefund(rs.getInt("sr.is_full_refund"));
            soRefund.setNo(rs.getString("sr.no"));
            soRefund.setPayerName(rs.getString("sr.payer_name"));
            soRefund.setBankNo(rs.getString("sr.bank_no"));
            soRefund.setAmount(rs.getInt("sr.amount"));
            soRefund.setCost(rs.getInt("sr.cost"));
            soRefund.setRemark(rs.getString("sr.remark"));
            soRefund.setAddTime(rs.getTimestamp("sr.add_time"));
            soRefund.setAddUserId(rs.getInt("sr.add_user_id"));

            soOrder.setPkid(rs.getInt("so.pkid"));
            soOrder.setType(rs.getInt("so.type"));
            soOrder.setNo(rs.getString("so.no"));
            soOrder.setAccountId(rs.getInt("so.account_id"));
            soOrder.setPayStatusId(rs.getInt("so.pay_status_id"));
            soOrder.setProcessStatusId(rs.getInt("so.process_status_id"));
            soOrder.setRefundStatusId(rs.getInt("so.refund_status_id"));
            soOrder.setTotalPrice(rs.getInt("so.total_price"));
            soOrder.setPayablePrice(rs.getInt("so.payable_price"));
            soOrder.setPaidPrice(rs.getInt("so.paid_price"));
            soOrder.setSourceTypeId(rs.getInt("so.source_type_id"));
            soOrder.setIsInstallment(rs.getInt("so.is_installment"));
            soOrder.setInstallmentMode(rs.getString("so.installment_mode"));
            soOrder.setInstallmentAuditStatusId(rs.getInt("so.installment_audit_status_id"));
            soOrder.setIsChangePrice(rs.getInt("so.is_change_price"));
            soOrder.setChangePriceAuditStatusId(rs.getInt("so.change_price_audit_status_id"));
            soOrder.setDescription(rs.getString("so.description"));
            soOrder.setIsPackage(rs.getInt("so.is_package"));
            soOrder.setPackageId(rs.getInt("so.package_id"));
            soOrder.setAddTime(rs.getTimestamp("so.add_time"));
            soOrder.setAddUserId(rs.getInt("so.add_user_id"));
            soOrder.setIsInvoice(rs.getInt("so.is_invoice"));

            bdAuditLog.setPkid(rs.getInt("bal.pkid"));
            bdAuditLog.setTypeId(rs.getInt("bal.type_id"));
            bdAuditLog.setFormId(rs.getInt("bal.form_id"));
            bdAuditLog.setStatusId(rs.getInt("bal.status_id"));
            bdAuditLog.setContent(rs.getString("bal.content"));
            bdAuditLog.setAddTime(rs.getTimestamp("bal.add_time"));
            bdAuditLog.setAddUserId(rs.getInt("bal.add_user_id"));
            bdAuditLog.setRemark(rs.getString("bal.remark"));
            bdAuditLog.setLevel(rs.getInt("bal.level"));

            soRefund.setSoOrder(soOrder);
            soRefund.setBdAuditLog(bdAuditLog);
            return soRefund;
        };
        return rowMapper;
    }

    @Override
    public List<SoRefund> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_refund` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `add_time` DESC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_refund` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }


    public int countRefundAuditByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(bal.`pkid`))  FROM `bd_audit_log`  bal");
        sql.append(" INNER JOIN `so_refund` sr ON bal.`form_id` = sr.`order_id` AND bal.`type_id` = 1046 ");
        sql.append(" INNER JOIN `so_order` so ON bal.`form_id` = so.`pkid`");

        //关联表查询
        String productName = StringUtils.trimToEmpty(properties.get("productName"));//产品名称
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN so_order_prod sop ON sor.`order_id` = so.`pkid`");
            sql.append("AND sop.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }

        sql.append(" WHERE 1 = 1 ");
        //设置查询条件
        setQueryCondition(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<SoRefund> findRefundAuditByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM `bd_audit_log`  bal");
        sql.append(" INNER JOIN `so_refund` sr ON bal.`form_id` = sr.`order_id` AND bal.`type_id` = 1046 ");
        sql.append(" INNER JOIN `so_order` so ON bal.`form_id` = so.`pkid`");
        sql.append(" GROUP BY bal.`pkid`");
        //关联表查询
        String productName = StringUtils.trimToEmpty(properties.get("productName"));//产品名称
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN so_order_prod sop ON sor.`order_id` = so.`pkid`");
            sql.append("AND sop.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }
        sql.append(" WHERE 1 = 1 ");
        //设置查询条件
        setQueryCondition(sql, properties);
        sql.append(" ORDER BY `add_time` DESC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }


    //查询条件
    private static void setQueryCondition(StringBuffer sql, Map<String, Object> properties) {

        //订单编号
        if (null != properties.get("no")) {
            sql.append(" AND so.no like :no ");
            properties.put("no", properties.get("no") + "%");
        }
        //订单状态 (拆分订单状态)

        //退单审核状态
        int auditStatusId = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("auditStatusId")));
        if (auditStatusId > 0) {
            sql.append(" AND sr.audit_status_id = " + auditStatusId);
        }

        //审核状态
        if (null != properties.get("statusIds")) {
            sql.append(" AND bal.status_id IN (:statusIds)");
        }

        //订单付款状态
        int payStatusId = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("payStatusId")));
        if (payStatusId > 0) {
            sql.append(" AND so.pay_status_id =" + payStatusId);
        }

        //订单处理状态
        int processStatusId = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("processStatusId")));
        if (processStatusId > 0) {
            sql.append(" AND so.process_status_id =" + processStatusId);
        }

        //订单类型
        if (null != properties.get("type")) {
            sql.append(" AND so.type = :type ");
        }

        //是否分期付款
        if (null != properties.get("isInstallment")) {
            sql.append(" AND so.is_installment = :isInstallment ");
        }

        //下单人姓名
        if (null != properties.get("accountName")) {
            sql.append(" AND so.account_name like :accountName ");
            properties.put("accountName", properties.get("accountName") + "%");
        }

        //下单人手机
        if (null != properties.get("accountMobile")) {
            sql.append(" AND so.account_mobile like :accountMobile ");
            properties.put("accountMobile", properties.get("accountMobile") + "%");
        }

        //下单时间
        if (null != properties.get("beginTime")) {
            sql.append(" AND so.add_time >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append(" AND so.add_time <= :endTime ");
        }
    }

    /**
     * 得到退单查看记录数
     *
     * @param map
     * @return int
     */
    public int countRefundView(Map<String, Object> map) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_refund` where 1=1 ");
        buildSQL(map, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map, Integer.class);
    }


    /**
     * 退单查看模块：查看列表
     *
     * @return List<BdAuditLog>
     */
    public List<BdAuditLog> findRefundViewListByIds(Integer typeId,Integer statusId,Integer addUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status_id",statusId);
        map.put("type_id",typeId);
        map.put("add_user_id",addUserId);
        StringBuffer sql = new StringBuffer("SELECT DISTINCT form_id FROM bd_audit_log WHERE 1=1");
        buildSQL(map, sql);
        return getNamedParameterJdbcTemplate().query(sql.toString(), map, getRefundViewRowMapper());
    }


    public RowMapper<BdAuditLog> getRefundViewRowMapper() {
        RowMapper<BdAuditLog> refundViewRowMapper = (rs, i) -> {
            BdAuditLog auditLog = new BdAuditLog();
            auditLog.setFormId(rs.getInt("form_id"));
            return auditLog;
        };
        return refundViewRowMapper;
    }


    public List<SoOrder> getRefundList(List<Integer> formIdList, Map<String, Object> map1) {
        Map<String, Object> map = new HashMap<>();
        map.put("formIdList", formIdList);
        //查询条件
        StringBuffer sql = new StringBuffer(" SELECT * FROM so_refund AS sr INNER JOIN  so_order AS so  ON sr.order_id = so.`pkid` where sr.pkid IN (:formIdList) ");

        return getNamedParameterJdbcTemplate().query(sql.toString(), map, getViewRowMapper());
    }


    public RowMapper<SoOrder> getViewRowMapper() {
        RowMapper<SoOrder> rowMapper = (rs, i) -> {
            SoOrder soOrder = new SoOrder();
            SoRefund soRefund = new SoRefund();

            soOrder.setPkid(rs.getInt("so.pkid"));
            soOrder.setType(rs.getInt("so.type"));
            soOrder.setNo(rs.getString("so.no"));
            soOrder.setAccountId(rs.getInt("so.account_id"));
            soOrder.setPayStatusId(rs.getInt("so.pay_status_id"));
            soOrder.setProcessStatusId(rs.getInt("so.process_status_id"));
            soOrder.setRefundStatusId(rs.getInt("so.refund_status_id"));
            soOrder.setTotalPrice(rs.getInt("so.total_price"));
            soOrder.setSourceTypeId(rs.getInt("so.source_type_id"));
            soOrder.setIsInstallment(rs.getInt("so.is_installment"));
            soOrder.setInstallmentMode(rs.getString("so.installment_mode"));
            soOrder.setInstallmentAuditStatusId(rs.getInt("so.installment_audit_status_id"));
            soOrder.setIsChangePrice(rs.getInt("so.is_change_price"));
            soOrder.setChangePriceAuditStatusId(rs.getInt("so.change_price_audit_status_id"));
            soOrder.setDescription(rs.getString("so.description"));
            soOrder.setIsPackage(rs.getInt("so.is_package"));
            soOrder.setPackageId(rs.getInt("so.package_id"));
            soOrder.setAddTime(rs.getTimestamp("so.add_time"));
            soOrder.setAddUserId(rs.getInt("so.add_user_id"));


            soRefund.setPkid(rs.getInt("sr.pkid"));
            soRefund.setOrderId(rs.getInt("sr.order_id"));
            soRefund.setAuditStatusId(rs.getInt("sr.audit_status_id"));
            soRefund.setWayTypeId(rs.getInt("sr.way_type_id"));
            soRefund.setIsFullRefund(rs.getInt("sr.is_full_refund"));
            soRefund.setNo(rs.getString("sr.no"));
            soRefund.setPayerName(rs.getString("sr.payer_name"));
            soRefund.setBankNo(rs.getString("sr.bank_no"));
            soRefund.setAmount(rs.getInt("sr.amount"));
            soRefund.setCost(rs.getInt("sr.cost"));
            soRefund.setRemark(rs.getString("sr.remark"));
            soRefund.setAddTime(rs.getTimestamp("sr.add_time"));
            soRefund.setAddUserId(rs.getInt("sr.add_user_id"));
            soOrder.setSoRefund(soRefund);
            return soOrder;
        };
        return rowMapper;
    }


    /**
     * 退单详细模块:展示列表
     *
     * @param orderId
     * @return List<SoRefund>
     */
    public List<SoRefund> findDetailsList(Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT * FROM so_order_prod AS sop");
        sql.append(" INNER JOIN so_refund AS sr ON  sop.order_id = sr.order_id");
        sql.append(" INNER JOIN so_refund_item AS sri ON sop.pkid = sri.order_prod_id");
        sql.append(" WHERE sop.is_refund = 1 AND sop.order_id= 2");
        return getNamedParameterJdbcTemplate().query(sql.toString(), map, getDetialsRowMapper());
    }


    public RowMapper<SoRefund> getDetialsRowMapper() {
        RowMapper<SoRefund> rowMapper = (rs, i) -> {
            SoRefund soRefund = new SoRefund();
            SoOrderProd soOrderProd = new SoOrderProd();

            soRefund.setPkid(rs.getInt("sr.pkid"));
            soRefund.setOrderId(rs.getInt("sr.order_id"));
            soRefund.setAuditStatusId(rs.getInt("sr.audit_status_id"));
            soRefund.setWayTypeId(rs.getInt("sr.way_type_id"));
            soRefund.setIsFullRefund(rs.getInt("sr.is_full_refund"));
            soRefund.setNo(rs.getString("sr.no"));
            soRefund.setPayerName(rs.getString("sr.payer_name"));
            soRefund.setBankNo(rs.getString("sr.bank_no"));
            soRefund.setAmount(rs.getInt("sr.amount"));
            soRefund.setCost(rs.getInt("sr.cost"));
            soRefund.setRemark(rs.getString("sr.remark"));
            soRefund.setAddTime(rs.getTimestamp("sr.add_time"));
            soRefund.setAddUserId(rs.getInt("sr.add_user_id"));

            soOrderProd.setPkid(rs.getInt("pkid"));
            soOrderProd.setNo(rs.getString("no"));
            soOrderProd.setOrderId(rs.getInt("order_id"));
            soOrderProd.setProductId(rs.getInt("product_id"));
            soOrderProd.setProductName(rs.getString("product_name"));
            soOrderProd.setCityId(rs.getInt("city_id"));
            soOrderProd.setProcessStatusId(rs.getInt("process_status_id"));
            soOrderProd.setAuditStatusId(rs.getInt("audit_status_id"));
            soOrderProd.setPrice(rs.getInt("price"));
            soOrderProd.setPriceOriginal(rs.getInt("price_original"));
            soOrderProd.setIsRefund(rs.getInt("is_refund"));
            soOrderProd.setProcessedDays(rs.getInt("processed_days"));

            soRefund.setSoOrderProd(soOrderProd);
            return soRefund;
        };
        return rowMapper;
    }

    public int updateAuditStatus(Integer orderPkid, Integer status, Integer oldStatus) {
        String sql = "UPDATE `so_refund` SET audit_status_id = ? WHERE `pkid` = ? AND audit_status_id = ?";
        return getJdbcTemplate().update(sql, status, orderPkid, oldStatus);
    }
}