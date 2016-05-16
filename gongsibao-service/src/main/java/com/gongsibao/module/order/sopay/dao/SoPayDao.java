package com.gongsibao.module.order.sopay.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.sopay.entity.PayAudit;
import com.gongsibao.module.order.sopay.entity.SoPay;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soPayDao")
public class SoPayDao extends BaseDao<SoPay>{

    public static String INSERT_COLUMNS = " `no`, `amount`, `pay_way_type_id`, `success_status_id`, `offline_way_type_id`, `offline_installment_type`, `offline_payer_name`, `offline_bank_no`, `offline_remark`, `offline_audit_status_id`, `offline_add_user_id`, `online_bank_code_id`, `online_confirm_time`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoPay soPay) {
        insertObject(soPay);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoPay soPay) {
        getJdbcTemplate().update("insert into `so_pay`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                soPay.getNo(),
                soPay.getAmount(),
                soPay.getPayWayTypeId(),
                soPay.getSuccessStatusId(),
                soPay.getOfflineWayTypeId(),
                soPay.getOfflineInstallmentTypeId(),
                soPay.getOfflinePayerName(),
                soPay.getOfflineBankNo(),
                soPay.getOfflineRemark(),
                soPay.getOfflineAuditStatusId(),
                soPay.getOfflineAddUserId(),
                soPay.getOnlineBankCodeId(),
                soPay.getOnlineConfirmTime(),
                soPay.getAddTime()
                );
    }

    @Override
    public int update(SoPay soPay) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_pay` set pkid = pkid, `no` = :no, `amount` = :amount, `pay_way_type_id` = :payWayTypeId, `success_status_id` = :successStatusId, `offline_way_type_id` = :offlineWayTypeId, `offline_installment_type_id` = :offlineInstallmentTypeId, `offline_payer_name` = :offlinePayerName, `offline_bank_no` = :offlineBankNo, `offline_remark` = :offlineRemark, `offline_audit_status_id` = :offlineAuditStatusId, `offline_add_user_id` = :offlineAddUserId, `online_bank_code_id` = :onlineBankCodeId, `online_confirm_time` = :onlineConfirmTime, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soPay),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_pay` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoPay findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_pay` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoPay> findByIds(List<Integer> pkidList) {
        if(pkidList==null || pkidList.isEmpty()){
            return new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_pay` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoPay> getRowMapper(){
        RowMapper<SoPay> rowMapper = (rs, i) -> {
            SoPay soPay = new SoPay();
            soPay.setPkid(rs.getInt("pkid"));
            soPay.setNo(rs.getString("no"));
            soPay.setAmount(rs.getInt("amount"));
            soPay.setPayWayTypeId(rs.getInt("pay_way_type_id"));
            soPay.setSuccessStatusId(rs.getInt("success_status_id"));
            soPay.setOfflineWayTypeId(rs.getInt("offline_way_type_id"));
            soPay.setOfflineInstallmentTypeId(rs.getInt("offline_installment_type"));
            soPay.setOfflinePayerName(rs.getString("offline_payer_name"));
            soPay.setOfflineBankNo(rs.getString("offline_bank_no"));
            soPay.setOfflineRemark(rs.getString("offline_remark"));
            soPay.setOfflineAuditStatusId(rs.getInt("offline_audit_status_id"));
            soPay.setOfflineAddUserId(rs.getInt("offline_add_user_id"));
            soPay.setOnlineBankCodeId(rs.getString("online_bank_code_id"));
            soPay.setOnlineConfirmTime(rs.getInt("online_confirm_time"));
            soPay.setAddTime(rs.getTimestamp("add_time"));
            return soPay;
        };
        return rowMapper;
    }

    @Override
    public List<SoPay> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_pay` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_pay` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<PayAudit> findPayAuditByIds(Map<String,Object> properties,List<Integer> payIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("payIds", payIds);
        StringBuffer sql = new StringBuffer("select * from `so_pay`,`so_order`,`so_order_pay_map` where 1=1 ");
        sql.append("and so_pay.pkid = so_order_pay_map.pay_id");
        sql.append("and so_order.pkid = so_order_pay_map.order_id");
        sql.append("and so_pay.pkid in (:payIds)");
        sql.append("where 1=1 ");
        if(properties.get("orderNo")!= null){
            sql.append(" and  no = "+properties.get("orderNo"));
        }
        if(properties.get("productName")!= null){
            sql.append(" and  product_name like '%"+properties.get("orderNo")+"'");
        }
        if(properties.get("payStatusId")!= null){
            sql.append(" and  pay_status_id = "+properties.get("payStatusId"));
        }
        if(properties.get("type")!= null){
            sql.append(" and  type = "+properties.get("type"));
        }
        if(properties.get("accountName")!= null){
            sql.append(" and  account_name = "+properties.get("accountName"));
        }
        if(properties.get("accountMobile")!= null){
            sql.append(" and  account_mobile = "+properties.get("accountMobile"));
        }
        if(properties.get("beginTime")!= null){
            sql.append(" and  add_time > "+properties.get("beginTime"));
        }
        if(properties.get("endTime")!= null){
            sql.append(" and  add_time < "+properties.get("endTime"));
        }


        return getJdbcTemplate().query(sql.toString() , getPayAuditRowMapper());
    }

    public RowMapper<PayAudit> getPayAuditRowMapper(){
        RowMapper<PayAudit> rowMapper = (rs, i) -> {
            PayAudit payAudit = new PayAudit();
            payAudit.setOrderPkidStr(SecurityUtils.rc4Encrypt(rs.getInt("so_order.pkid")));
            payAudit.setOrderNo(rs.getString("so_order.no"));
            payAudit.setPayPkidStr(SecurityUtils.rc4Encrypt(rs.getInt("so_pay.pkid")));
            payAudit.setProductNames(rs.getString("so_order.prod_name"));
            payAudit.setPayStatusId(rs.getInt("so_order.pay_status_id"));
            payAudit.setTypeId(rs.getInt("so_order.type"));
            payAudit.setTotalPrice(rs.getInt("so_order.total_price"));
            payAudit.setPayPrice(rs.getInt("so_order.paid_price"));
            payAudit.setInstallmentTypeId(rs.getInt("so_pay.offline_installment_type"));
            payAudit.setAddUserName(rs.getString("so_order.account_name"));
            payAudit.setAdduserPhone(rs.getString("so_order.account_mobile"));
            payAudit.setAddTime(rs.getTimestamp("so_order.add_time"));
            payAudit.setAuditStatusId(rs.getString("so_pay.offline_audit_status_id"));
            return payAudit;
        };
        return rowMapper;
    }


    public Integer findPayAuditCountByIds(Map<String,Object> properties,List<Integer> payIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("payIds", payIds);
        StringBuffer sql = new StringBuffer("select count(*) from `so_pay`,`so_order`,`so_order_pay_map` where 1=1 ");
        sql.append("and so_pay.pkid = so_order_pay_map.pay_id");
        sql.append("and so_order.pkid = so_order_pay_map.order_id");
        sql.append("and so_pay.pkid in (:payIds)");
        if(properties.get("orderNo")!= null){
            sql.append(" and  no = "+properties.get("orderNo"));
        }
        if(properties.get("productName")!= null){
            sql.append(" and  product_name like '%"+properties.get("orderNo")+"'");
        }
        if(properties.get("payStatusId")!= null){
            sql.append(" and  pay_status_id = "+properties.get("payStatusId"));
        }
        if(properties.get("type")!= null){
            sql.append(" and  type = "+properties.get("type"));
        }
        if(properties.get("accountName")!= null){
            sql.append(" and  account_name = "+properties.get("accountName"));
        }
        if(properties.get("accountMobile")!= null){
            sql.append(" and  account_mobile = "+properties.get("accountMobile"));
        }
        if(properties.get("beginTime")!= null){
            sql.append(" and  add_time > "+properties.get("beginTime"));
        }
        if(properties.get("endTime")!= null){
            sql.append(" and  add_time < "+properties.get("endTime"));
        }

        return getJdbcTemplate().queryForObject(sql.toString() , Integer.class);
    }

    public int updateAuditId(int payId,int AuditStatusId){
        String sql = "update so_pay set offline_audit_status_id = "+AuditStatusId+" where pkid = "+payId;
        return getJdbcTemplate().update(sql);
    }

}