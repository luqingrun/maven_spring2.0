package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsBottomnav;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsBottomnavDao")
public class CmsBottomnavDao extends BaseDao<CmsBottomnav> {

    public static String INSERT_COLUMNS = " `bottom_category`, `name`, `url`, `img`, `spider`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsBottomnav cmsBottomnav) {
        insertObject(cmsBottomnav);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsBottomnav cmsBottomnav) {
        getJdbcTemplate().update("insert into `cms_bottomnav`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsBottomnav.getBottomCategory(),
                cmsBottomnav.getName(),
                cmsBottomnav.getUrl(),
                cmsBottomnav.getImg(),
                cmsBottomnav.getSpider(),
                cmsBottomnav.getSort(),
                cmsBottomnav.getStatus(),
                cmsBottomnav.getAddUser(),
                cmsBottomnav.getUpdUser()
        );
    }

    @Override
    public int update(CmsBottomnav cmsBottomnav) {
        String sql = "update `cms_bottomnav` set `bottom_category` = :bottomCategory, `name` = :name, `url` = :url, `img` = :img, `spider` = :spider, `sort` = :sort, `status` = :status, upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsBottomnav), Map.class));
    }

    public int updateInfo(CmsBottomnav cmsBottomnav) {
        String sql = "update `cms_bottomnav` set `name` = :name, `url` = :url, `spider` = :spider, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsBottomnav), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_bottomnav` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsBottomnav findById(Integer pkid) {

        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_bottomnav` where pkid = " + pkid, getRowMapper());


    }

    @Override
    public List<CmsBottomnav> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_bottomnav` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsBottomnav> getRowMapper() {
        RowMapper<CmsBottomnav> rowMapper = (rs, i) -> {
            CmsBottomnav cmsBottomnav = new CmsBottomnav();
            cmsBottomnav.setPkid(rs.getInt("pkid"));
            cmsBottomnav.setBottomCategory(rs.getInt("bottom_category"));
            cmsBottomnav.setName(rs.getString("name"));
            cmsBottomnav.setUrl(rs.getString("url"));
            cmsBottomnav.setImg(rs.getString("img"));
            cmsBottomnav.setSpider(rs.getInt("spider"));
            cmsBottomnav.setSort(rs.getInt("sort"));
            cmsBottomnav.setStatus(rs.getInt("status"));
            cmsBottomnav.setAddTime(rs.getTimestamp("add_time"));
            cmsBottomnav.setAddUser(rs.getInt("add_user"));
            cmsBottomnav.setUpdTime(rs.getTimestamp("upd_time"));
            cmsBottomnav.setUpdUser(rs.getInt("upd_user"));
            return cmsBottomnav;
        };
        return rowMapper;
    }

    @Override
    public List<CmsBottomnav> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_bottomnav` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_bottomnav` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_bottomnav` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_bottomnav` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer categoryId, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("update `cms_bottomnav` ");
        sql.append("set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId ");
        sql.append("WHERE `status` <> 2 AND bottom_category = :categoryId ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        source.addValue("categoryId", categoryId);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid in (:pkids)");
            source.addValue("pkids", pkids);
        }
        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }
    public CmsBottomnav getNearest(Integer sort, Integer categoryId, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_bottomnav` ");
        sql.append("WHERE `status` <> 2 AND bottom_category = ? ");

        if (isUp) {
            sql.append("AND `sort` < ? ORDER BY `sort` DESC ");
        } else {
            sql.append("AND `sort` > ? ORDER BY `sort` ASC ");
        }

        sql.append("LIMIT 1 ");
        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), categoryId, sort));
    }

}