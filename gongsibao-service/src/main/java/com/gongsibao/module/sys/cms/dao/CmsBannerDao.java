package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsBanner;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsBannerDao")
public class CmsBannerDao extends BaseDao<CmsBanner> {

    public static String INSERT_COLUMNS = " `name`, `description`, `color`, `url`, `img`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsBanner cmsBanner) {
        insertObject(cmsBanner);
        Integer id = getLastInsertId();
        return id;
    }

    @Override
    protected void insertObject(CmsBanner cmsBanner) {
        getJdbcTemplate().update("insert into `cms_banner`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsBanner.getName(),
                cmsBanner.getDescription(),
                cmsBanner.getColor(),
                cmsBanner.getUrl(),
                cmsBanner.getImg(),
                cmsBanner.getSort(),
                cmsBanner.getStatus(),
                cmsBanner.getAddUser(),
                cmsBanner.getUpdUser()
        );
    }

    @Override
    public int update(CmsBanner cmsBanner) {
        StringBuffer sql = new StringBuffer("update `cms_banner` set `name` = :name, `description` = :description, `color` = :color, `url` = :url, `img` = :img ,`sort` = :sort ,`upd_user` =:updUser, upd_time = NOW() where  pkid = :pkid ");
        return getNamedParameterJdbcTemplate().update(sql.toString(), JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsBanner), Map.class));
    }

    @Override
    public int delete(Integer id) {
        return getJdbcTemplate().update("update `cms_banner` set `status` = '2' where `pkid` = " + id);
    }

    @Override
    public CmsBanner findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_banner` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsBanner> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_banner` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsBanner> getRowMapper() {
        RowMapper<CmsBanner> rowMapper = (rs, i) -> {
            CmsBanner cmsBanner = new CmsBanner();
            cmsBanner.setPkid(rs.getInt("pkid"));
            cmsBanner.setName(rs.getString("name"));
            cmsBanner.setDescription(rs.getString("description"));
            cmsBanner.setColor(rs.getInt("color"));
            cmsBanner.setUrl(rs.getString("url"));
            cmsBanner.setImg(rs.getString("img"));
            cmsBanner.setSort(rs.getInt("sort"));
            cmsBanner.setStatus(rs.getInt("status"));
            cmsBanner.setAddTime(rs.getTimestamp("add_time"));
            cmsBanner.setAddUser(rs.getInt("add_user"));
            cmsBanner.setUpdTime(rs.getTimestamp("upd_time"));
            cmsBanner.setUpdUser(rs.getInt("upd_user"));
            return cmsBanner;
        };
        return rowMapper;
    }

    @Override
    public List<CmsBanner> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_banner` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(1) from `cms_banner` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        return getJdbcTemplate().update("update `cms_banner` set `status` = ?, upd_time = NOW(), upd_user = ? where pkid = ?", status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer("update `cms_banner` set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE `status` <> 2 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid in (:pkids) ");
            source.addValue("pkids", pkids);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    /**
     * 获取最近的banner -- 排序使用
     *
     * @param sort
     * @param isUp true 向上, false 向下
     * @return
     */
    public CmsBanner getNearestBanner(Integer sort, Boolean isUp) {
        StringBuffer sql = new StringBuffer("");
        if (isUp) {//上移
            sql.append("SELECT " + ALL_COLUMNS + " FROM `cms_banner` WHERE `status` <> 2 AND sort < ? ORDER BY sort DESC LIMIT 1");
        } else {// 下移
            sql.append("SELECT " + ALL_COLUMNS + "  FROM `cms_banner` WHERE `status` <> 2 AND sort > ? ORDER BY sort ASC LIMIT 1");
        }

        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), sort));
    }

    /**
     * 插入一条新的记录后，更新该记录的Sort字段值为pkid
     *
     * @param pkid
     * @param sort
     * @param userId
     * @return
     */
    public Integer updateSort(Integer pkid, Integer sort, Integer userId) {
        return getJdbcTemplate().update("UPDATE `cms_banner` SET `sort` = ?, upd_time = NOW(), upd_user = ? WHERE pkid = ?", sort, userId, pkid);
    }
}