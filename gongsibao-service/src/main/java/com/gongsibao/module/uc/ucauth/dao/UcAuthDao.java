package com.gongsibao.module.uc.ucauth.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Repository("ucAuthDao")
public class UcAuthDao extends BaseDao<UcAuth>{

    public static String INSERT_COLUMNS = " `pid`, `name`, `url`, `tag`, `description`, `icon`, `is_menu`, `sort`, `level`, `is_leaf`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcAuth ucAuth) {
        insertObject(ucAuth);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcAuth ucAuth) {
        getJdbcTemplate().update("insert into `uc_auth`("+INSERT_COLUMNS+") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                ucAuth.getPid(),
                ucAuth.getName(),
                ucAuth.getUrl(),
                ucAuth.getTag(),
                ucAuth.getDescription(),
                ucAuth.getIcon(),
                ucAuth.getIsMenu(),
                ucAuth.getSort(),
                ucAuth.getLevel(),
                ucAuth.getIsLeaf(),
                ucAuth.getIsEnabled(),
                ucAuth.getAddTime(),
                ucAuth.getAddUserId(),
                ucAuth.getRemark()
                );
    }

    @Override
    public int update(UcAuth ucAuth) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `uc_auth` set pkid = pkid, `pid` = :pid, `name` = :name, `url` = :url, `tag` = :tag, `description` = :description, `icon` = :icon, `is_menu` = :isMenu, `sort` = :sort, `level` = :level, `is_leaf` = :isLeaf, `is_enabled` = :isEnabled, `add_time` = :addTime, `add_user_id` = :addUserId, `remark` = :remark where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucAuth),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_auth` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public UcAuth findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select "+ALL_COLUMNS + " from `uc_auth` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<UcAuth> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select "+ALL_COLUMNS + " from `uc_auth` where pkid IN (:pkidList) " , map , getRowMapper());
    }


    @Override
    public RowMapper<UcAuth> getRowMapper(){
        RowMapper<UcAuth> rowMapper = (rs, i) -> {
            UcAuth ucAuth = new UcAuth();
            ucAuth.setPkid(rs.getInt("pkid"));
            ucAuth.setPid(rs.getInt("pid"));
            ucAuth.setName(rs.getString("name"));
            ucAuth.setUrl(rs.getString("url"));
            ucAuth.setTag(rs.getString("tag"));
            ucAuth.setDescription(rs.getString("description"));
            ucAuth.setIcon(rs.getString("icon"));
            ucAuth.setIsMenu(rs.getInt("is_menu"));
            ucAuth.setSort(rs.getDouble("sort"));
            ucAuth.setLevel(rs.getInt("level"));
            ucAuth.setIsLeaf(rs.getInt("is_leaf"));
            ucAuth.setIsEnabled(rs.getInt("is_enabled"));
            ucAuth.setAddTime(rs.getTimestamp("add_time"));
            ucAuth.setAddUserId(rs.getInt("add_user_id"));
            ucAuth.setRemark(rs.getString("remark"));
            return ucAuth;
        };
        return rowMapper;
    }

    @Override
    public List<UcAuth> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select "+ALL_COLUMNS+" from `uc_auth` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_auth` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<UcAuth> findByRoldPkidList(List<Integer> rolePkidList) {
        if(rolePkidList == null || rolePkidList.size()==0){
            return new ArrayList<>();
        }
        StringBuffer sb = new StringBuffer("");
        for(Integer rolePkid: rolePkidList){
            if(sb.length()==0){
                sb.append(rolePkid);
            }else{
                sb.append(", ").append(rolePkid);
            }
        }
        String sql = "select  t2.`pkid`, t2.`pid`, t2.`name`, t2.`url`, t2.`tag`, t2.`description`, t2.`icon`, t2.`is_menu`, t2.`sort`, t2.`level`, t2.`is_leaf`, t2.`is_enabled`, t2.`add_time`, t2.`add_user_id`, t2.`remark`  from uc_role_auth_map t1 left join uc_auth t2 on t1.`auth_id`=t2.`pkid` where t1.`role_id` in ("+ sb.toString() + ")";
        return getJdbcTemplate().query(sql, getRowMapper());
    }
}