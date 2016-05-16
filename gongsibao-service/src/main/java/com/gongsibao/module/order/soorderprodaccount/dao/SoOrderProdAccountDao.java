package com.gongsibao.module.order.soorderprodaccount.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderProdAccountDao")
public class SoOrderProdAccountDao extends BaseDao<SoOrderProdAccount>{

    public static String INSERT_COLUMNS = " `order_prod_id`, `account`, `passwd`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderProdAccount soOrderProdAccount) {
        insertObject(soOrderProdAccount);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderProdAccount soOrderProdAccount) {
        getJdbcTemplate().update("insert into `so_order_prod_account`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ? )",
                soOrderProdAccount.getOrderProdId(),
                soOrderProdAccount.getAccount(),
                soOrderProdAccount.getPasswd(),
                soOrderProdAccount.getAddTime(),
                soOrderProdAccount.getAddUserId(),
                soOrderProdAccount.getRemark()
                );
    }

    @Override
    public int update(SoOrderProdAccount soOrderProdAccount) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_prod_account` set pkid = pkid, `order_prod_id` = :orderProdId, `account` = :account, `passwd` = :passwd, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderProdAccount),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_prod_account` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderProdAccount findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_prod_account` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderProdAccount> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_prod_account` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderProdAccount> getRowMapper(){
        RowMapper<SoOrderProdAccount> rowMapper = (rs, i) -> {
            SoOrderProdAccount soOrderProdAccount = new SoOrderProdAccount();
            soOrderProdAccount.setPkid(rs.getInt("pkid"));
            soOrderProdAccount.setOrderProdId(rs.getInt("order_prod_id"));
            soOrderProdAccount.setAccount(rs.getString("account"));
            soOrderProdAccount.setPasswd(rs.getString("passwd"));
            soOrderProdAccount.setAddTime(rs.getTimestamp("add_time"));
            soOrderProdAccount.setAddUserId(rs.getInt("add_user_id"));
            soOrderProdAccount.setRemark(rs.getString("remark"));
            return soOrderProdAccount;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderProdAccount> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_prod_account` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_prod_account` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public void insertBatch(final List<SoOrderProdAccount> itemList) {
//" `order_prod_id`, `account`, `passwd`, `add_time`, `add_user_id`, `remark` ";
        String sql = "insert into `so_order_prod_account` ("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setObject(1, itemList.get(i).getOrderProdId());
                ps.setObject(2, itemList.get(i).getAccount());
                ps.setObject(3, itemList.get(i).getPasswd());
                ps.setObject(4, itemList.get(i).getAddTime());
                ps.setObject(5, itemList.get(i).getAddUserId());
                ps.setObject(6, itemList.get(i).getRemark());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }

    public int updateBatch(final List<SoOrderProdAccount> itemList) {
        String sql = "update `so_order_prod_account` set `account` = ?, `passwd` = ?, `remark` = ? where pkid = ?";
        int[] res = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {

                ps.setObject(1, itemList.get(i).getAccount());
                ps.setObject(2, itemList.get(i).getPasswd());
                ps.setObject(3, itemList.get(i).getRemark());
                ps.setObject(4, itemList.get(i).getPkid());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
        Integer resCount = 0;
        for(int item : res)
        {
            resCount += item;
        }
        return resCount;
    }

    public int deleteBatch(final List<SoOrderProdAccount> itemList) {
        String idList = "";
        if(itemList.size() > 0)
        {
            for(SoOrderProdAccount ps : itemList)
            {
                idList += ps.getPkid() + ",";
            }
            idList = idList.substring(0,(idList.length()-1));
            return getJdbcTemplate().update("delete from `so_order_prod_account` where pkid in (" + idList + ")");
        }
        else
        {
            return 0;
        }

    }
}