package com.gongsibao.module.order.soorderdiscount.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.sqlserver.DBHelper;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("soOrderDiscountDao")
public class SoOrderDiscountDao extends BaseDao<SoOrderDiscount>{

    public static String INSERT_COLUMNS = " `order_id`, `type_id`, `amount`, `add_time`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(SoOrderDiscount soOrderDiscount) {
        insertObject(soOrderDiscount);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(SoOrderDiscount soOrderDiscount) {
        getJdbcTemplate().update("insert into `so_order_discount`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ? )",
                soOrderDiscount.getOrderId(),
                soOrderDiscount.getTypeId(),
                soOrderDiscount.getAmount(),
                soOrderDiscount.getAddTime(),
                soOrderDiscount.getRemark()
                );
    }

    @Override
    public int update(SoOrderDiscount soOrderDiscount) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `so_order_discount` set pkid = pkid, `order_id` = :orderId, `type_id` = :typeId, `amount` = :amount, `add_time` = :addTime, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(soOrderDiscount),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `so_order_discount` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public SoOrderDiscount findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `so_order_discount` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<SoOrderDiscount> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `so_order_discount` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<SoOrderDiscount> getRowMapper(){
        RowMapper<SoOrderDiscount> rowMapper = (rs, i) -> {
            SoOrderDiscount soOrderDiscount = new SoOrderDiscount();
            soOrderDiscount.setPkid(rs.getInt("pkid"));
            soOrderDiscount.setOrderId(rs.getInt("order_id"));
            soOrderDiscount.setTypeId(rs.getInt("type_id"));
            soOrderDiscount.setAmount(rs.getInt("amount"));
            soOrderDiscount.setAddTime(rs.getTimestamp("add_time"));
            soOrderDiscount.setRemark(rs.getString("remark"));
            return soOrderDiscount;
        };
        return rowMapper;
    }

    @Override
    public List<SoOrderDiscount> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `so_order_discount` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `so_order_discount` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 获取优惠券信息
     * @param discounts
     * @return
     */
    public List<Map<String, Object>> discountList(String discounts) {
        // 初始化数据库连接
        Connection conn = DBHelper.getConn();
        List<Map<String, Object>> discountsList = new ArrayList<>();
        try {
            String sql = "select BaseDataPreferentialCode.IsDisabled, BaseDataPreferentialCode.IsUse," +
                    "BaseDataPreferentialCode.Account_ID,BaseDataPreferential.AmountLimit,BaseDataPreferential.OverlayType," +
                    "BaseDataPreferential.FirstOrderUse,BaseDataPreferential.StartDate,BaseDataPreferential.EndDate," +
                    "BaseDataPreferential.ID ,BaseDataPreferential.Preferential,BaseDataPreferential.Remark from dbo.BaseDataPreferentialCode" +
                    "Inner join BaseDataPreferential on BaseDataPreferentialCode.BaseDataPreferential_ID = BaseDataPreferential.ID" +
                    "where IsAccountDelete = 0 and NO in (" +discounts +")";

            // 查询
            discountsList = DBHelper.query(sql, conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            DBHelper.closeConn(conn);
        }
        return discountsList;
    }

    /**
     * 校验优惠券产品
     * @param productId
     * @param discountId
     * @return
     */
    public List<Map<String,Object>> findByProductId(String productId,String discountId){
        // 初始化数据库连接
        Connection conn = DBHelper.getConn();
        List<Map<String, Object>> discountsList = new ArrayList<>();
        try {
            String sql = "select * from dbo.BaseDataPreferentialProduct" +
                    "where BaseDataPreferential_ID = "+ discountId +" and Product_ID = (" +productId +")";

            // 查询
            discountsList = DBHelper.query(sql, conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            DBHelper.closeConn(conn);
        }
        return discountsList;
    }

    /**
     * 校验优惠券地区
     * @param cityId
     * @param discountId
     * @return
     */
    public List<Map<String,Object>> findByCityId(String cityId,String discountId){
        // 初始化数据库连接
        Connection conn = DBHelper.getConn();
        List<Map<String, Object>> discountsList = new ArrayList<>();
        try {
            String sql = "select * from dbo.BaseDataPreferentialBaseDataCityArea" +
                    "where BaseDataPreferential_ID = "+ discountId +" and BaseDataCityArea_ID = (" +cityId +")";

            // 查询
            discountsList = DBHelper.query(sql, conn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            DBHelper.closeConn(conn);
        }
        return discountsList;
    }
}