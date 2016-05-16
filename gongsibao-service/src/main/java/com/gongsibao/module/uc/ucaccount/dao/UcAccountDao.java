package com.gongsibao.module.uc.ucaccount.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucaccount.entity.UcAccount;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("ucAccountDao")
public class UcAccountDao extends BaseDao<UcAccount>{

    public static String INSERT_COLUMNS = " `name`, `passwd`, `email`, `mobile_phone`, `head_thumb_file_id`, `real_name`, `source_client_id`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcAccount ucAccount) {
        insertObject(ucAccount);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcAccount ucAccount) {
        getJdbcTemplate().update("insert into `uc_account`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ? )",
                ucAccount.getName(),
                ucAccount.getPasswd(),
                ucAccount.getEmail(),
                ucAccount.getMobilePhone(),
                ucAccount.getHeadThumbFileId(),
                ucAccount.getRealName(),
                ucAccount.getSourceClientId(),
                ucAccount.getAddTime()
        );
    }

    @Override
    public int update(UcAccount ucAccount) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_account` set pkid = pkid, `name` = :name, `passwd` = :passwd, `email` = :email, `mobile_phone` = :mobilePhone, `head_thumb_file_id` = :headThumbFileId, `real_name` = :realName, `source_client_id` = :sourceClientId, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucAccount),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_account` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public UcAccount findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select "+ALL_COLUMNS + " from `uc_account` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<UcAccount> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `uc_account` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<UcAccount> getRowMapper(){
        RowMapper<UcAccount> rowMapper = (rs, i) -> {
            UcAccount ucAccount = new UcAccount();
            ucAccount.setPkid(rs.getInt("pkid"));
            ucAccount.setName(rs.getString("name"));
            ucAccount.setPasswd(rs.getString("passwd"));
            ucAccount.setEmail(rs.getString("email"));
            ucAccount.setMobilePhone(rs.getString("mobile_phone"));
            ucAccount.setHeadThumbFileId(rs.getInt("head_thumb_file_id"));
            ucAccount.setRealName(rs.getString("real_name"));
            ucAccount.setSourceClientId(rs.getInt("source_client_id"));
            ucAccount.setAddTime(rs.getTimestamp("add_time"));
            return ucAccount;
        };
        return rowMapper;
    }

    @Override
    public List<UcAccount> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `uc_account` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_account` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public UcAccount findByMobile(String mobilePhone){
        return getJdbcTemplate().queryForObject("select" + ALL_COLUMNS + "from `uc_account` where mobile_phone =" + mobilePhone,getRowMapper());
    }

}