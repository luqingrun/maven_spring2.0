package com.gongsibao.module.product.prodserviceext.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodserviceext.entity.ProdServiceExt;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("prodServiceExtDao")
public class ProdServiceExtDao extends BaseDao<ProdServiceExt>{

    public static String INSERT_COLUMNS = " `service_id`, `show_typy` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdServiceExt prodServiceExt) {
        insertObject(prodServiceExt);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdServiceExt prodServiceExt) {
        getJdbcTemplate().update("insert into `prod_service_ext`("+INSERT_COLUMNS+") values (?, ? )",
                prodServiceExt.getServiceId(),
                prodServiceExt.getShowTypy()
                );
    }

    @Override
    public int update(ProdServiceExt prodServiceExt) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_service_ext` set pkid = pkid, `service_id` = :serviceId, `show_typy` = :showTypy where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodServiceExt),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_service_ext` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdServiceExt findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `prod_service_ext` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdServiceExt> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `prod_service_ext` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<ProdServiceExt> getRowMapper(){
        RowMapper<ProdServiceExt> rowMapper = (rs, i) -> {
            ProdServiceExt prodServiceExt = new ProdServiceExt();
            prodServiceExt.setPkid(rs.getInt("pkid"));
            prodServiceExt.setServiceId(rs.getInt("service_id"));
            prodServiceExt.setShowTypy(rs.getInt("show_typy"));
            return prodServiceExt;
        };
        return rowMapper;
    }

    @Override
    public List<ProdServiceExt> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `prod_service_ext` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_service_ext` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}