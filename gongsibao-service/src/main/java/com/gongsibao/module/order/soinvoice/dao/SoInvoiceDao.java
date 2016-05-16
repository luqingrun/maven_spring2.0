package com.gongsibao.module.order.soinvoice.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soinvoice.entity.InvoiceList;
import com.gongsibao.module.order.soinvoice.entity.SoInvoice;
import com.gongsibao.module.order.soorder.entity.OrderList;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("soInvoiceDao")
public class SoInvoiceDao extends BaseDao<SoInvoice> {

    public static String INSERT_COLUMNS = " so_invoice.title, so_invoice.company_id, so_invoice.type_id," +
            " so_invoice.amount, so_invoice.content, so_invoice.audit_status_id, so_invoice.receiver_name, " +
            "so_invoice.receiver_mobile_phone, so_invoice.receiver_address, so_invoice.vat_tax_no, so_invoice.vat_address," +
            " so_invoice.vat_phone, so_invoice.vat_bank_name, so_invoice.vat_bank_no, so_invoice.file_id, so_invoice.add_time, " +
            "so_invoice.add_user_id, so_invoice.remark ";


    public static String ALL_COLUMNS = "so_invoice.pkid, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoInvoice soInvoice) {
        insertObject(soInvoice);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoInvoice soInvoice) {
        getJdbcTemplate().update("insert into `so_invoice`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soInvoice.getTitle(),
                soInvoice.getCompanyId(),
                soInvoice.getTypeId(),
                soInvoice.getAmount(),
                soInvoice.getContent(),
                soInvoice.getAuditStatusId(),
                soInvoice.getReceiverName(),
                soInvoice.getReceiverMobilePhone(),
                soInvoice.getReceiverAddress(),
                soInvoice.getVatTaxNo(),
                soInvoice.getVatAddress(),
                soInvoice.getVatPhone(),
                soInvoice.getVatBankName(),
                soInvoice.getVatBankNo(),
                soInvoice.getFileId(),
                soInvoice.getAddTime(),
                soInvoice.getAddUserId(),
                soInvoice.getRemark()
        );
    }

    @Override
    public int update(SoInvoice soInvoice) {
        String sql = "update `so_invoice` set pkid = pkid, `title` = :title, `company_id` = :companyId, `type_id` = :typeId, `amount` = :amount, `content` = :content, `audit_status_id` = :auditStatusId, `receiver_name` = :receiverName, `receiver_mobile_phone` = :receiverMobilePhone, `receiver_address` = :receiverAddress, `vat_tax_no` = :vatTaxNo, `vat_address` = :vatAddress, `vat_phone` = :vatPhone, `vat_bank_name` = :vatBankName, `vat_bank_no` = :vatBankNo, `file_id` = :fileId, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soInvoice), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_invoice` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoInvoice findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `so_invoice` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoInvoice> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `so_invoice` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<SoInvoice> getRowMapper() {
        RowMapper<SoInvoice> rowMapper = (rs, i) -> {
            SoInvoice soInvoice = new SoInvoice();
            soInvoice.setPkid(rs.getInt("so_invoice.pkid"));
            soInvoice.setTitle(rs.getString("so_invoice.title"));
            soInvoice.setCompanyId(rs.getInt("so_invoice.company_id"));
            soInvoice.setTypeId(rs.getInt("so_invoice.type_id"));
            soInvoice.setAmount(rs.getInt("so_invoice.amount"));
            soInvoice.setContent(rs.getString("so_invoice.content"));
            soInvoice.setAuditStatusId(rs.getInt("so_invoice.audit_status_id"));
            soInvoice.setReceiverName(rs.getString("so_invoice.receiver_name"));
            soInvoice.setReceiverMobilePhone(rs.getString("so_invoice.receiver_mobile_phone"));
            soInvoice.setReceiverAddress(rs.getString("so_invoice.receiver_address"));
            soInvoice.setVatTaxNo(rs.getString("so_invoice.vat_tax_no"));
            soInvoice.setVatAddress(rs.getString("so_invoice.vat_address"));
            soInvoice.setVatPhone(rs.getString("so_invoice.vat_phone"));
            soInvoice.setVatBankName(rs.getString("so_invoice.vat_bank_name"));
            soInvoice.setVatBankNo(rs.getString("so_invoice.vat_bank_no"));
            soInvoice.setFileId(rs.getInt("so_invoice.file_id"));
            soInvoice.setAddTime(rs.getTimestamp("so_invoice.add_time"));
            soInvoice.setAddUserId(rs.getInt("so_invoice.add_user_id"));
            soInvoice.setRemark(rs.getString("so_invoice.remark"));
            return soInvoice;
        };
        return rowMapper;
    }

    public RowMapper<InvoiceList> getInvoiceListMapper() {
        RowMapper<InvoiceList> rowMapper = (rs, i) -> {
            InvoiceList invoiceList = new InvoiceList();
            invoiceList.setPkid(rs.getInt("so_invoice.pkid"));
            invoiceList.setCompanyId(rs.getInt("so_invoice.company_id"));
            invoiceList.setTypeId(rs.getInt("so_invoice.type_id"));
            invoiceList.setAmount(rs.getInt("so_invoice.amount"));
            invoiceList.setAuditStatusId(rs.getInt("so_invoice.audit_status_id"));
            invoiceList.setAddTime(rs.getTimestamp("so_invoice.add_time"));
            invoiceList.setNo(rs.getString("so_order.no"));
            invoiceList.setOrderId(rs.getString("so_order.pkid"));
            invoiceList.setProdName(rs.getString("so_order.prod_name"));
            invoiceList.setType(rs.getInt("so_order.type"));
            invoiceList.setPayablePrice(rs.getInt("so_order.payable_price"));
            invoiceList.setPaidPrice(rs.getInt("so_order.paid_price"));
            invoiceList.setRealName(rs.getString("so_order.account_name"));
            invoiceList.setMobilePhone(rs.getString("so_order.account_mobile"));
            return invoiceList;
        };
        return rowMapper;
    }

    @Override
    public List<SoInvoice> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `so_invoice` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        return 0;
    }

    /**
     * 查询数量
     *
     * @param properties
     * @return
     */
    public int findListCountByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(so_invoice.`pkid`)) ");
        sql.append("FROM `so_invoice` ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append("WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<SoInvoice> findListByOrderId(Integer orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `so_invoice` ");
        sql.append("INNER JOIN `so_order_invoice_map` ON so_order_invoice_map.`invoice_id` = so_invoice.`pkid` ");
        sql.append("WHERE 1 = 1 ");
        sql.append("AND so_order_invoice_map.order_id = :orderId");
        Map<String, Object> properties = new HashMap<>();
        properties.put("orderId", orderId);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    public List<InvoiceList> findInvoiceListByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ").append(ALL_COLUMNS);
        String soOrderColumns = ",so_order.no,so_order.pkid,so_order.prod_name,so_order.type," +
                "so_order.payable_price,so_order.paid_price,so_order.account_name,so_order.account_mobile";
        sql.append(soOrderColumns);
        sql.append(" FROM `so_invoice` ");
        // 设置连表查询
        setJoinSql(sql, properties);
        sql.append(" WHERE 1 = 1 ");
        // 查询条件
        setCondition(sql, properties);
        sql.append(" GROUP BY so_invoice.pkid ");
        sql.append(" ORDER BY so_invoice.add_time DESC ");
        sql.append(" LIMIT :start, :pageSize ");
        properties.put("start", start);
        properties.put("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getInvoiceListMapper());
    }

    //表连接
    private void setJoinSql(StringBuffer sql, Map<String, Object> properties) {
        sql.append("INNER JOIN `so_order_invoice_map` ON so_order_invoice_map.invoice_id = so_invoice.pkid ");
        sql.append("INNER JOIN `so_order` ON so_order.pkid = so_order_invoice_map.order_id ");
        // 产品名称
        String productName = StringUtils.trimToEmpty(properties.get("productName"));
        // 产品名称连表
        if (StringUtils.isNotBlank(productName)) {
            sql.append("INNER JOIN `so_order_prod` ON so_order.pkid = so_order_prod.order_id ");
            sql.append("AND so_order_prod.product_name like :productName ");
            properties.put("productName", properties.get("productName") + "%");
        }
    }

    //查询条件
    private void setCondition(StringBuffer sql, Map<String, Object> properties) {
        //订单id
        if (null != properties.get("orderId")) {
            sql.append(" AND so_order.pkid = :orderId");
            return;
        }
        //当前登录用户发起的发票审核申请
        if (null != properties.get("userId")) {
            sql.append(" AND so_invoice.add_user_id = :userId");
        }
        //订单号
        if (null != properties.get("no")) {
            sql.append(" AND so_order.no like :no ");
            properties.put("no", properties.get("no") + "%");
        }
        //订单类型
        if (null != properties.get("type")) {
            sql.append(" AND so_order.type = :type ");
        }
        //发票类型
        if (null != properties.get("invoiceType")) {
            sql.append(" AND so_invoice.type_id = :invoiceType ");
        }
        //发票公司
        if (null != properties.get("invoiceCompany")) {
            sql.append(" AND so_invoice.company_id = :invoiceCompany ");
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
        //审核状态
        if (null != properties.get("auditStatus")) {
            sql.append(" AND so_invoice.audit_status_id = :auditStatus ");
        }
    }
}