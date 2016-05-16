package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsTopnavLink;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("cmsTopnavLinkDao")
public class CmsTopnavLinkDao extends BaseDao<CmsTopnavLink> {

    public static String INSERT_COLUMNS = " `category_id`, `name`, `url`, `recommend`, `status`, `sort`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsTopnavLink cmsTopnavLink) {
        insertObject(cmsTopnavLink);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsTopnavLink cmsTopnavLink) {
        getJdbcTemplate().update("insert into `cms_topnav_link`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsTopnavLink.getCategoryId(),
                cmsTopnavLink.getName(),
                cmsTopnavLink.getUrl(),
                cmsTopnavLink.getRecommend(),
                cmsTopnavLink.getStatus(),
                cmsTopnavLink.getSort(),
                cmsTopnavLink.getAddUser(),
                cmsTopnavLink.getUpdUser()
        );
    }

    @Override
    public int update(CmsTopnavLink cmsTopnavLink) {
        String sql = "update `cms_topnav_link` set `category_id` = :categoryId, `name` = :name, `url` = :url, `recommend` = :recommend, `status` = :status, `sort` = :sort, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsTopnavLink), Map.class));
    }

    public int updateInfo(CmsTopnavLink cmsTopnavLink) {
        String sql = "update `cms_topnav_link` set  `name` = :name, `url` = :url, `recommend` = :recommend, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsTopnavLink), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_topnav_link` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsTopnavLink findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_topnav_link` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsTopnavLink> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_topnav_link` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsTopnavLink> getRowMapper() {
        RowMapper<CmsTopnavLink> rowMapper = (rs, i) -> {
            CmsTopnavLink cmsTopnavLink = new CmsTopnavLink();
            cmsTopnavLink.setPkid(rs.getInt("pkid"));
            cmsTopnavLink.setCategoryId(rs.getInt("category_id"));
            cmsTopnavLink.setName(rs.getString("name"));
            cmsTopnavLink.setUrl(rs.getString("url"));
            cmsTopnavLink.setRecommend(rs.getInt("recommend"));
            cmsTopnavLink.setStatus(rs.getInt("status"));
            cmsTopnavLink.setSort(rs.getInt("sort"));
            cmsTopnavLink.setAddTime(rs.getTimestamp("add_time"));
            cmsTopnavLink.setAddUser(rs.getInt("add_user"));
            cmsTopnavLink.setUpdTime(rs.getTimestamp("upd_time"));
            cmsTopnavLink.setUpdUser(rs.getInt("upd_user"));
            return cmsTopnavLink;
        };
        return rowMapper;
    }

    @Override
    public List<CmsTopnavLink> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_topnav_link` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `sort` ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_topnav_link` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_topnav_link` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_topnav_link` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatusByCategory(Integer categoryId, Integer status, Integer userId) {
        String sql = "UPDATE `cms_topnav_link` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE category_id = ?  AND `status` <> 2 ";
        return getJdbcTemplate().update(sql, status, userId, categoryId);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        String sql = "UPDATE `cms_topnav_link` SET `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE pkid IN (:pkids) AND `status` <> 2 ";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        source.addValue("pkids", pkids);

        return getNamedParameterJdbcTemplate().update(sql, source);
    }

    public CmsTopnavLink getNearest(Integer sort, Integer categoryId, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_topnav_link` ");
        sql.append("WHERE `status` <> 2 ");

        List<Object> params = new ArrayList<>();
        if (null != categoryId) {
            sql.append("AND `category_id` = ? ");
            params.add(categoryId);
        }

        if (isUp) {
            sql.append("AND `sort` < ? ORDER BY `sort` DESC ");
        } else {
            sql.append("AND `sort` > ? ORDER BY `sort` ASC ");
        }
        params.add(sort);
        sql.append("LIMIT 1 ");

        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), params.toArray()));
    }
}