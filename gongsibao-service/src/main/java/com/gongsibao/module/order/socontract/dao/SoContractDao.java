package com.gongsibao.module.order.socontract.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.order.socontract.entity.ContractList;
import com.gongsibao.module.order.socontract.entity.SoContract;
import com.gongsibao.module.order.soorder.dao.SoOrderDao;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderproditem.entity.ProdItemName;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Repository("soContractDao")
public class SoContractDao extends BaseDao<SoContract> {

    public static String INSERT_COLUMNS = " so_contract.order_id, so_contract.sgining_time, so_contract.sgining_company_id, so_contract.is_urgeney," +
            " so_contract.sgining_user_id, so_contract.customer_id, so_contract.real_amount, so_contract.has_data_fee, so_contract.data_fee_count_type_id, " +
            "so_contract.has_liquidated_damages, so_contract.has_breach, so_contract.liquidated_damages, so_contract.breach_info, so_contract.file_id, " +
            "so_contract.audit_status_id, so_contract.add_time, so_contract.add_user_id, so_contract.remark ";


    public static String ALL_COLUMNS = "so_contract.pkid, " + INSERT_COLUMNS;

    public static String PRODITEM_COLUMNS = " so_order_prod.product_name, so_order_prod_item.service_name, so_order_prod_item.quantity," +
            "so_order_prod_item.unit_name, so_order_prod_item.price_original, so_order_prod_item.city_id ";

    @Override
    public Integer insert(SoContract soContract) {
        insertObject(soContract);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoContract soContract) {
        getJdbcTemplate().update("insert into `so_contract`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soContract.getOrderId(),
                soContract.getSginingTime(),
                soContract.getSginingCompanyId(),
                soContract.getIsUrgeney(),
                soContract.getSginingUserId(),
                soContract.getCustomerId(),
                soContract.getRealAmount(),
                soContract.getHasDataFee(),
                soContract.getDataFeeCountTypeId(),
                soContract.getHasLiquidatedDamages(),
                soContract.getHasBreach(),
                soContract.getLiquidatedDamages(),
                soContract.getBreachInfo(),
                soContract.getFileId(),
                soContract.getAuditStatusId(),
                soContract.getAddTime(),
                soContract.getAddUserId(),
                soContract.getRemark()
        );
    }

    @Override
    public int update(SoContract soContract) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_contract` set pkid = pkid, `order_id` = :orderId, `sgining_time` = :sginingTime, `sgining_company_id` = :sginingCompanyId, `is_urgeney` = :isUrgeney, `sgining_user_id` = :sginingUserId, `customer_id` = :customerId, `real_amount` = :realAmount, `has_data_fee` = :hasDataFee, `data_fee_count_type_id` = :dataFeeCountTypeId, `has_liquidated_damages` = :hasLiquidatedDamages, `has_breach` = :hasBreach, `liquidated_damages` = :liquidatedDamages, `breach_info` = :breachInfo, `file_id` = :fileId, `audit_status_id` = :auditStatusId, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soContract),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_contract` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoContract findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_contract` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<SoContract> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_contract` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    public List<SoContract> findByOrderIds(List<Integer> orderIdList) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderIdList", orderIdList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_contract` where order_id IN (:orderIdList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoContract> getRowMapper() {
        RowMapper<SoContract> rowMapper = (rs, i) -> {
            SoContract soContract = new SoContract();
            soContract.setPkid(rs.getInt("so_contract.pkid"));
            soContract.setOrderId(rs.getInt("so_contract.order_id"));
            soContract.setSginingTime(rs.getTimestamp("so_contract.sgining_time"));
            soContract.setSginingCompanyId(rs.getInt("so_contract.sgining_company_id"));
            soContract.setIsUrgeney(rs.getInt("so_contract.is_urgeney"));
            soContract.setSginingUserId(rs.getInt("so_contract.sgining_user_id"));
            soContract.setCustomerId(rs.getInt("so_contract.customer_id"));
            soContract.setRealAmount(rs.getInt("so_contract.real_amount"));
            soContract.setHasDataFee(rs.getInt("so_contract.has_data_fee"));
            soContract.setDataFeeCountTypeId(rs.getInt("so_contract.data_fee_count_type_id"));
            soContract.setHasLiquidatedDamages(rs.getInt("so_contract.has_liquidated_damages"));
            soContract.setHasBreach(rs.getInt("so_contract.has_breach"));
            soContract.setLiquidatedDamages(rs.getInt("so_contract.liquidated_damages"));
            soContract.setBreachInfo(rs.getString("so_contract.breach_info"));
            soContract.setFileId(rs.getInt("so_contract.file_id"));
            soContract.setAuditStatusId(rs.getInt("so_contract.audit_status_id"));
            soContract.setAddTime(rs.getTimestamp("so_contract.add_time"));
            soContract.setAddUserId(rs.getInt("so_contract.add_user_id"));
            soContract.setRemark(rs.getString("so_contract.remark"));
            return soContract;
        };
        return rowMapper;
    }

    public RowMapper<ContractList> getContracListRowMapper() {
        RowMapper<ContractList> rowMapper = (rs, i) -> {
            ContractList contractList = new ContractList();
            contractList.setPkid(rs.getInt("so_order.pkid"));
            contractList.setType(rs.getInt("so_order.type"));
            contractList.setNo(rs.getString("so_order.no"));
            contractList.setAccountId(rs.getInt("so_order.account_id"));
            contractList.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            contractList.setProcessStatusId(rs.getInt("so_order.process_status_id"));
            contractList.setRefundStatusId(rs.getInt("so_order.refund_status_id"));
            contractList.setTotalPrice(rs.getInt("so_order.total_price"));
            contractList.setPayablePrice(rs.getInt("so_order.payable_price"));
            contractList.setPaidPrice(rs.getInt("so_order.paid_price"));
            contractList.setSourceTypeId(rs.getInt("so_order.source_type_id"));
            contractList.setIsInstallment(rs.getInt("so_order.is_installment"));
            contractList.setInstallmentMode(rs.getString("so_order.installment_mode"));
            contractList.setInstallmentAuditStatusId(rs.getInt("so_order.installment_audit_status_id"));
            contractList.setIsChangePrice(rs.getInt("so_order.is_change_price"));
            contractList.setChangePriceAuditStatusId(rs.getInt("so_order.change_price_audit_status_id"));
            contractList.setDescription(rs.getString("so_order.description"));
            contractList.setIsPackage(rs.getInt("so_order.is_package"));
            contractList.setPackageId(rs.getInt("so_order.package_id"));
            contractList.setAddTime(rs.getTimestamp("so_order.add_time"));
            contractList.setAddUserId(rs.getInt("so_order.add_user_id"));
            contractList.setIsInvoice(rs.getInt("so_order.is_invoice"));
            contractList.setRealName(rs.getString("so_order.account_name"));
            contractList.setMobilePhone(rs.getString("so_order.account_mobile"));
            contractList.setProdName(rs.getString("so_order.prod_name"));
            contractList.setRealAmount(rs.getInt("so_contract.real_amount"));
            contractList.setLiquidatedDamages(rs.getInt("so_contract.liquidated_damages"));
            return contractList;
        };
        return rowMapper;
    }

    @Override
    public List<SoContract> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_contract` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    /***
     * 合同列表长度
     *
     * @return
     */
    public int findOrderCountByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_order.`pkid`)) ");
        sql.append(" FROM `so_order` INNER JOIN `so_contract` ON so_contract.order_id=so_order.pkid ");
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
    public List<ContractList> findContractListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(SoOrderDao.ALL_COLUMNS).append(" ,so_contract.real_amount,so_contract.liquidated_damages");
        sql.append(" FROM `so_order` INNER JOIN `so_contract` ON so_contract.order_id=so_order.pkid ");
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
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getContracListRowMapper());
    }

    //表连接
    private void setJoinSql(StringBuffer sql, Map<String, Object> properties) {
        // 产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        String userId = StringUtils.trimToEmpty(properties.get("userId"));
        // 产品名称连表
        if (StringUtils.isNotBlank(productName)) {
            sql.append(" INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
        }
        // 用户名称链表
        if (StringUtils.isNoneBlank(userId)) {
            sql.append(" INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
            sql.append(" INNER JOIN `so_order_prod_user_map` ON so_order_prod_user_map.order_prod_id = so_order_prod.pkid ");
        }
    }

    //查询条件
    private void setCondition(StringBuffer sql, Map<String, Object> properties) {
        if (null != properties.get("no")) {
            sql.append(" AND so_order.no like :no ");
            properties.put("no", properties.get("no") + "%");
        }
        //产品名称
        if (StringUtils.isNotBlank(StringUtils.trimToEmpty(properties.get("productName")))) {
            sql.append(" AND so_order_prod.product_name like :productName");
            properties.put("productName", properties.get("productName") + "%");
        }
        //订单类型
        if (null != properties.get("type")) {
            sql.append(" AND so_order.type = :type ");
        }
        //业务员ID
        if (null != properties.get("userId")) {
            sql.append(" AND so_order_prod_user_map.user_id = :userId ");
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
            sql.append(" AND unix_timestamp(so_order.add_time)*1000 >= :beginTime ");
        }
        if (null != properties.get("endTime")) {
            sql.append(" AND unix_timestamp(so_order.add_time)*1000 <= :endTime ");
        }
        //审核状态
        if (null != properties.get("auditStatusId")) {
            sql.append(" AND so_contract.audit_status_id = :auditStatusId");
        }
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_contract` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 通过订单PkId查找合同
     *
     * @param orderPkId
     * @return
     */
    public SoContract findByOrderPkId(Integer orderPkId) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + "from `so_contract` where order_id = " + orderPkId, getRowMapper());
    }

    /**
     * 通过订单查找合同
     *
     * @param orderId 订单编号
     */
    public SoContract findByOrderId(Integer orderId) {
        Map<String, Object> map = new HashedMap();
        map.put("order_id", orderId);
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_contract` where order_id=:order_id ");
        return getFirstObj(getNamedParameterJdbcTemplate().query(sql.toString(), map, getRowMapper()));
    }


    /**
     * 合同审核查询条件
     *
     * @param sql
     * @param properties
     */
    private void setContractAuditCondition(StringBuffer sql, Map<String, Object> properties) {
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

    public int getContractAuditCount(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`from_id` = so_order.`pkid`");
        sql.append("INNER JOIN so_contract ON so_order.`pkid` = so_contract.`order_id`");
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
        setContractAuditCondition(sql, properties);

        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 合同审核订单查询
     *
     * @param properties
     * @param start
     * @param pageSize
     * @return
     */
    public List<ContractList> getContractAuditList(Map<String, Object> properties, Integer start, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        String soOrderColumns = "so_order.no,so_order.pkid,so_order.product_name,so_order.total_price,so_order.payable_price" +
                "so_order.is_installment,so_order.account_name,so_order.account_mobile,so_order.add_time";
        sql.append(soOrderColumns);
        sql.append("FROM `bd_audit_log` ");
        sql.append("INNER JOIN so_order ON bd_audit_log.`from_id` = so_order.`pkid`");
        sql.append("INNER JOIN so_contract ON so_order.`pkid` = so_contract.`order_id`");
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
        setContractAuditCondition(sql, properties);

        sql.append(" GROUP BY bd_audit_log.pkid");
        sql.append(" ORDER BY bd_audit_log.pkid ASC");
        sql.append(" LIMIT :start, :pageSize ");

        properties.put("start", start);
        properties.put("pageSize", pageSize);

        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getSoContracListRowMapper());

    }

    public RowMapper<ContractList> getSoContracListRowMapper() {
        RowMapper<ContractList> rowMapper = (rs, i) -> {
            ContractList contractList = new ContractList();
            contractList.setPkid(rs.getInt("so_order.pkid"));
            contractList.setNo(rs.getString("so_order.no"));
            contractList.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            contractList.setProcessStatusId(rs.getInt("so_order.process_status_id"));
            contractList.setRefundStatusId(rs.getInt("so_order.refund_status_id"));
            contractList.setTotalPrice(rs.getInt("so_order.total_price"));
            contractList.setPayablePrice(rs.getInt("so_order.payable_price"));
            contractList.setPaidPrice(rs.getInt("so_order.paid_price"));
            contractList.setSourceTypeId(rs.getInt("so_order.source_type_id"));
            contractList.setIsInstallment(rs.getInt("so_order.is_installment"));
            contractList.setInstallmentMode(rs.getString("so_order.installment_mode"));
            contractList.setInstallmentAuditStatusId(rs.getInt("so_order.installment_audit_status_id"));
            contractList.setIsChangePrice(rs.getInt("so_order.is_change_price"));
            contractList.setChangePriceAuditStatusId(rs.getInt("so_order.change_price_audit_status_id"));
            contractList.setDescription(rs.getString("so_order.description"));
            contractList.setIsPackage(rs.getInt("so_order.is_package"));
            contractList.setPackageId(rs.getInt("so_order.package_id"));
            contractList.setAddTime(rs.getTimestamp("so_order.add_time"));
            contractList.setAddUserId(rs.getInt("so_order.add_user_id"));
            contractList.setIsInvoice(rs.getInt("so_order.is_invoice"));
            contractList.setRealName(rs.getString("so_order.account_name"));
            contractList.setMobilePhone(rs.getString("so_order.account_mobile"));
            contractList.setProdName(rs.getString("so_order.prod_name"));
            contractList.setRealAmount(rs.getInt("so_contract.real_amount"));
            contractList.setLiquidatedDamages(rs.getInt("so_contract.liquidated_damages"));
            return contractList;
        };
        return rowMapper;
    }

    /**
     * 合同订单-proditem
     *
     * @return
     */
    public RowMapper<OrderProdList> getSoOrderProdListRowMapper() {
        RowMapper<OrderProdList> rowMapper = (rs, i) -> {
            OrderProdList orderProdList = new OrderProdList();
            orderProdList.setProductName(rs.getString("so_order_prod.product_name"));
            orderProdList.setServiceName(rs.getString("so_order_prod_item.service_name"));
            orderProdList.setCityId(rs.getInt("so_order_prod.city_id"));
            orderProdList.setQuantity(rs.getInt("so_order_prod_item.quantity"));
            orderProdList.setUnitName(rs.getString("so_order_prod_item.unit_name"));
            orderProdList.setPriceOriginal(rs.getInt("so_order_prod_item.price_original"));
            return orderProdList;
        };

        return rowMapper;
    }

    /**
     * 合同浮层查询
     *
     * @param orderPkId
     * @return
     */
    public List<OrderProdList> getSoOrderProdList(Integer orderPkId) {
        return getNamedParameterJdbcTemplate().query("select " + PRODITEM_COLUMNS + " from `so_order_prod` " +
                "INNER JOIN so_order_prod_item ON so_order_prod.pkid = so_order_prod_item.order_prod_id WHERE so_order_prod.order_id =" + orderPkId, getSoOrderProdListRowMapper());

    }

    public int updateContractStatus(Integer orderPkid, Integer status, Integer oldStatus) {
        String sql = "UPDATE `so_contract` SET audit_status_id = ? WHERE `order_id` = ? AND audit_status_id = ?";
        return getJdbcTemplate().update(sql, status, orderPkid, oldStatus);
    }

    /***
     * 根据订单ID查询是否加急
     */
    public Map<Integer, Integer> findIsUrgeneyByOrderIds(HashSet<Integer> orderIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append(" FROM `so_contract` ");
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        sql.append(" AND so_contract.order_id IN (:orderIds)");
        Map<String, Object> map = new HashMap<>();
        map.put("orderIds", orderIds);
        List<SoContract> soContracts = getNamedParameterJdbcTemplate().query(sql.toString(), map, getRowMapper());
        Map<Integer, Integer> maps = new HashMap<>();
        for (SoContract s : soContracts) {
            maps.put(s.getOrderId(), s.getIsUrgeney());
        }
        return maps;
    }
}