package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsTopnavCategory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsTopnavCategoryDao")
public class CmsTopnavCategoryDao extends BaseDao<CmsTopnavCategory> {

    public static String INSERT_COLUMNS = " `pid`, `name`, `url`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsTopnavCategory cmsTopnavCategory) {
        insertObject(cmsTopnavCategory);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsTopnavCategory cmsTopnavCategory) {
        getJdbcTemplate().update("insert into `cms_topnav_category`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsTopnavCategory.getPid(),
                cmsTopnavCategory.getName(),
                cmsTopnavCategory.getUrl(),
                cmsTopnavCategory.getSort(),
                cmsTopnavCategory.getStatus(),
                cmsTopnavCategory.getAddUser(),
                cmsTopnavCategory.getUpdUser()
        );
    }

    @Override
    public int update(CmsTopnavCategory cmsTopnavCategory) {
        String sql = "update `cms_topnav_category` set `pid` = :pid, `name` = :name, `url` = :url, `sort` = :sort, `status` = :status, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsTopnavCategory), Map.class));
    }

    public int updateNameAndUrl(CmsTopnavCategory cmsTopnavCategory) {
        String sql = "update `cms_topnav_category` set `name` = :name, `url` = :url, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsTopnavCategory), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `cms_topnav_category` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsTopnavCategory findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_topnav_category` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsTopnavCategory> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_topnav_category` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public RowMapper<CmsTopnavCategory> getRowMapper() {
        RowMapper<CmsTopnavCategory> rowMapper = (rs, i) -> {
            CmsTopnavCategory cmsTopnavCategory = new CmsTopnavCategory();
            cmsTopnavCategory.setPkid(rs.getInt("pkid"));
            cmsTopnavCategory.setPid(rs.getInt("pid"));
            cmsTopnavCategory.setName(rs.getString("name"));
            cmsTopnavCategory.setUrl(rs.getString("url"));
            cmsTopnavCategory.setSort(rs.getInt("sort"));
            cmsTopnavCategory.setStatus(rs.getInt("status"));
            cmsTopnavCategory.setAddTime(rs.getTimestamp("add_time"));
            cmsTopnavCategory.setAddUser(rs.getInt("add_user"));
            cmsTopnavCategory.setUpdTime(rs.getTimestamp("upd_time"));
            cmsTopnavCategory.setUpdUser(rs.getInt("upd_user"));
            return cmsTopnavCategory;
        };
        return rowMapper;
    }

    @Override
    public List<CmsTopnavCategory> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_topnav_category` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_topnav_category` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_topnav_category` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer pid, Integer status, Integer userId) {

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `cms_topnav_category` ");
        sql.append("SET `status` = :status, `upd_time` = NOW(), `upd_user` = :userId ");
        sql.append("WHERE `status` <> 2 AND pid = :pid ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        source.addValue("pid", pid);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid IN (:pkids ) ");
            source.addValue("pkids", pkids);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }


    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_topnav_category` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public CmsTopnavCategory getNearest(Integer sort, Boolean isUp, Boolean isRoot) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_topnav_category` ");
        sql.append("WHERE `status` <> 2  ");
        if (null == isRoot || isRoot) {
            sql.append("AND pid = 0 ");
        } else {
            sql.append("AND pid <> 0 ");
        }

        if (isUp) {
            sql.append("AND `sort` < ? ORDER BY `sort` DESC ");
        } else {
            sql.append("AND `sort` > ? ORDER BY `sort` ASC ");
        }

        sql.append("LIMIT 1 ");

        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), sort));
    }
}