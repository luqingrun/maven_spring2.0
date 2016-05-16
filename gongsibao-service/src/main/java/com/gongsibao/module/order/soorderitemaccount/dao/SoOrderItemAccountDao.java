package com.gongsibao.module.order.soorderitemaccount.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderitemaccount.entity.SoOrderItemAccount;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderItemAccountDao")
public class SoOrderItemAccountDao extends BaseDao<SoOrderItemAccount>{

    public static String INSERT_COLUMNS = " `order_item_id`, `account`, `passwd`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderItemAccount soOrderItemAccount) {
        insertObject(soOrderItemAccount);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderItemAccount soOrderItemAccount) {
        getJdbcTemplate().update("insert into `so_order_item_account`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ? )",
                soOrderItemAccount.getOrderItemId(),
                soOrderItemAccount.getAccount(),
                soOrderItemAccount.getPasswd(),
                soOrderItemAccount.getAddTime(),
                soOrderItemAccount.getAddUserId(),
                soOrderItemAccount.getRemark()
                );
    }

    @Override
    public int update(SoOrderItemAccount soOrderItemAccount) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_item_account` set pkid = pkid, `order_item_id` = :orderItemId, `account` = :account, `passwd` = :passwd, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderItemAccount),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_item_account` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderItemAccount findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_item_account` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderItemAccount> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_item_account` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderItemAccount> getRowMapper(){
        RowMapper<SoOrderItemAccount> rowMapper = (rs, i) -> {
            SoOrderItemAccount soOrderItemAccount = new SoOrderItemAccount();
            soOrderItemAccount.setPkid(rs.getInt("pkid"));
            soOrderItemAccount.setOrderItemId(rs.getInt("order_item_id"));
            soOrderItemAccount.setAccount(rs.getString("account"));
            soOrderItemAccount.setPasswd(rs.getString("passwd"));
            soOrderItemAccount.setAddTime(rs.getTimestamp("add_time"));
            soOrderItemAccount.setAddUserId(rs.getInt("add_user_id"));
            soOrderItemAccount.setRemark(rs.getString("remark"));
            return soOrderItemAccount;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderItemAccount> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_item_account` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_item_account` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}