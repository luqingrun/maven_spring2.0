package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsTitleDesc;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsTitleDescDao")
public class CmsTitleDescDao extends BaseDao<CmsTitleDesc> {

    public static String INSERT_COLUMNS = " `name`, `description`, `keyword`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsTitleDesc cmsTitleDesc) {
        insertObject(cmsTitleDesc);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsTitleDesc cmsTitleDesc) {
        getJdbcTemplate().update("insert into `cms_title_desc`(" + INSERT_COLUMNS + ") values (?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsTitleDesc.getName(),
                cmsTitleDesc.getDescription(),
                cmsTitleDesc.getKeyword(),
                cmsTitleDesc.getAddUser(),
                cmsTitleDesc.getUpdUser()
        );
    }

    @Override
    public int update(CmsTitleDesc cmsTitleDesc) {
        String sql = "update `cms_title_desc` set `name` = :name, `description` = :description, `keyword` = :keyword, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsTitleDesc), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_title_desc` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsTitleDesc findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_title_desc` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsTitleDesc> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_title_desc` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsTitleDesc> getRowMapper() {
        RowMapper<CmsTitleDesc> rowMapper = (rs, i) -> {
            CmsTitleDesc cmsTitleDesc = new CmsTitleDesc();
            cmsTitleDesc.setPkid(rs.getInt("pkid"));
            cmsTitleDesc.setName(rs.getString("name"));
            cmsTitleDesc.setKeyword(rs.getString("keyword"));
            cmsTitleDesc.setDescription(rs.getString("description"));
            cmsTitleDesc.setAddTime(rs.getTimestamp("add_time"));
            cmsTitleDesc.setAddUser(rs.getInt("add_user"));
            cmsTitleDesc.setUpdTime(rs.getTimestamp("upd_time"));
            cmsTitleDesc.setUpdUser(rs.getInt("upd_user"));
            return cmsTitleDesc;
        };
        return rowMapper;
    }

    @Override
    public List<CmsTitleDesc> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_title_desc` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_title_desc` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }
}