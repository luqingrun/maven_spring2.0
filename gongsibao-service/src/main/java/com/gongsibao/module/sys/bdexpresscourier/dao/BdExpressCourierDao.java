package com.gongsibao.module.sys.bdexpresscourier.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.bdexpresscourier.entity.BdExpressCourier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Repository("bdExpressCourierDao")
public class BdExpressCourierDao extends BaseDao<BdExpressCourier>{

    public static String INSERT_COLUMNS = " `name`, `code`, `first_letter`, `is_hot`, `sort`, `is_enabled`, `add_time`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(BdExpressCourier bdExpressCourier) {
        insertObject(bdExpressCourier);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdExpressCourier bdExpressCourier) {
        getJdbcTemplate().update("insert into `bd_express_courier`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ? )",
                bdExpressCourier.getName(),
                bdExpressCourier.getCode(),
                bdExpressCourier.getFirstLetter(),
                bdExpressCourier.getIsHot(),
                bdExpressCourier.getSort(),
                bdExpressCourier.getIsEnabled(),
                bdExpressCourier.getAddTime(),
                bdExpressCourier.getRemark()
                );
    }

    @Override
    public int update(BdExpressCourier bdExpressCourier) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `bd_express_courier` set pkid = pkid, `name` = :name, `code` = :code, `first_letter` = :firstLetter, `is_hot` = :isHot, `sort` = :sort, `is_enabled` = :isEnabled, `add_time` = :addTime, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdExpressCourier),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `bd_express_courier` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public BdExpressCourier findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `bd_express_courier` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<BdExpressCourier> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `bd_express_courier` where pkid IN (:pkidList) " , map , getRowMapper());
    }

    public  List<BdExpressCourier> findAllBdExpressCourier() {
        List<BdExpressCourier> list = getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `bd_express_courier` " , getRowMapper());
        if(CollectionUtils.isEmpty(list)) {
            return  new ArrayList<>();
        }
        return  list;
    }

    @Override
    public RowMapper<BdExpressCourier> getRowMapper(){
        RowMapper<BdExpressCourier> rowMapper = (rs, i) -> {
            BdExpressCourier bdExpressCourier = new BdExpressCourier();
            bdExpressCourier.setPkid(rs.getInt("pkid"));
            bdExpressCourier.setName(rs.getString("name"));
            bdExpressCourier.setCode(rs.getString("code"));
            bdExpressCourier.setFirstLetter(rs.getString("first_letter"));
            bdExpressCourier.setIsHot(rs.getInt("is_hot"));
            bdExpressCourier.setSort(rs.getDouble("sort"));
            bdExpressCourier.setIsEnabled(rs.getInt("is_enabled"));
            bdExpressCourier.setAddTime(rs.getTimestamp("add_time"));
            bdExpressCourier.setRemark(rs.getString("remark"));
            return bdExpressCourier;
        };
        return rowMapper;
    }

    @Override
    public List<BdExpressCourier> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `bd_express_courier` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_express_courier` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}