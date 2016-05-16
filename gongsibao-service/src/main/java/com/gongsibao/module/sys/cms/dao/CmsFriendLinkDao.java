package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsFriendLink;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsFriendLinkDao")
public class CmsFriendLinkDao extends BaseDao<CmsFriendLink> {

    public static String INSERT_COLUMNS = " `type`, `name`, `url`, `img`, `sort`, `spider`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsFriendLink cmsFriendLink) {
        insertObject(cmsFriendLink);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsFriendLink cmsFriendLink) {
        getJdbcTemplate().update("insert into `cms_friend_link`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsFriendLink.getType(),
                cmsFriendLink.getName(),
                cmsFriendLink.getUrl(),
                cmsFriendLink.getImg(),
                cmsFriendLink.getSort(),
                cmsFriendLink.getSpider(),
                cmsFriendLink.getStatus(),
                cmsFriendLink.getAddUser(),
                cmsFriendLink.getUpdUser()
        );
    }

    @Override
    public int update(CmsFriendLink cmsFriendLink) {
        String sql = "update `cms_friend_link` set `type` = :type, `name` = :name, `url` = :url, `img` = :img, `sort` = :sort, `spider` = :spider, `status` = :status, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsFriendLink), Map.class));
    }

    public int updateInfo(CmsFriendLink cmsFriendLink) {
        String sql = "update `cms_friend_link` set `name` = :name, `url` = :url, `img` = :img, `spider` = :spider, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsFriendLink), Map.class));
    }

    @Override
    public int delete(Integer id) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsFriendLink findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_friend_link` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsFriendLink> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_friend_link` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsFriendLink> getRowMapper() {
        RowMapper<CmsFriendLink> rowMapper = (rs, i) -> {
            CmsFriendLink cmsFriendLink = new CmsFriendLink();
            cmsFriendLink.setPkid(rs.getInt("pkid"));
            cmsFriendLink.setType(rs.getInt("type"));
            cmsFriendLink.setName(rs.getString("name"));
            cmsFriendLink.setUrl(rs.getString("url"));
            cmsFriendLink.setImg(rs.getString("img"));
            cmsFriendLink.setSort(rs.getInt("sort"));
            cmsFriendLink.setSpider(rs.getInt("spider"));
            cmsFriendLink.setStatus(rs.getInt("status"));
            cmsFriendLink.setAddTime(rs.getTimestamp("add_time"));
            cmsFriendLink.setAddUser(rs.getInt("add_user"));
            cmsFriendLink.setUpdTime(rs.getTimestamp("upd_time"));
            cmsFriendLink.setUpdUser(rs.getInt("upd_user"));
            return cmsFriendLink;
        };
        return rowMapper;
    }

    @Override
    public List<CmsFriendLink> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_friend_link` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_friend_link` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_friend_link` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_friend_link` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer type, Integer status, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `cms_friend_link` ");
        sql.append("SET `status` = :status, `upd_time` = NOW(), `upd_user` = :userId ");
        sql.append("WHERE `status` <> 2 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND `pkid` IN (:pkids) ");
            source.addValue("pkids", pkids);
        }

        if (null != type) {
            sql.append("AND `type` = :type ");
            source.addValue("type", type);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    public CmsFriendLink getNearest(Integer sort, Integer type, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_friend_link` ");
        sql.append("WHERE `status` <> 2 AND `type` = ? ");
        if (isUp) {
            sql.append("AND `sort` < ? ORDER BY `sort` DESC ");
        } else {
            sql.append("AND `sort` > ? ORDER BY `sort` ASC ");
        }

        sql.append("LIMIT 1 ");
        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), type, sort));
    }

}