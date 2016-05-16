package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsCooperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsCooperationDao")
public class CmsCooperationDao extends BaseDao<CmsCooperation> {

    public static String INSERT_COLUMNS = " `name`, `description`, `img`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsCooperation cmsCooperation) {
        insertObject(cmsCooperation);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsCooperation cmsCooperation) {
        getJdbcTemplate().update("insert into `cms_cooperation`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsCooperation.getName(),
                cmsCooperation.getDescription(),
                cmsCooperation.getImg(),
                cmsCooperation.getSort(),
                cmsCooperation.getStatus(),
                cmsCooperation.getAddUser(),
                cmsCooperation.getUpdUser()
        );
    }

    @Override
    public int update(CmsCooperation cmsCooperation) {
        String sql = "update `cms_cooperation` set `name` = :name, `description` = :description, `img` = :img, `sort` = :sort, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsCooperation), Map.class));
    }

    @Override
    public int delete(Integer id) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsCooperation findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_cooperation` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsCooperation> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_cooperation` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsCooperation> getRowMapper() {
        RowMapper<CmsCooperation> rowMapper = (rs, i) -> {
            CmsCooperation cmsCooperation = new CmsCooperation();
            cmsCooperation.setPkid(rs.getInt("pkid"));
            cmsCooperation.setName(rs.getString("name"));
            cmsCooperation.setDescription(rs.getString("description"));
            cmsCooperation.setImg(rs.getString("img"));
            cmsCooperation.setSort(rs.getInt("sort"));
            cmsCooperation.setStatus(rs.getInt("status"));
            cmsCooperation.setAddTime(rs.getTimestamp("add_time"));
            cmsCooperation.setAddUser(rs.getInt("add_user"));
            cmsCooperation.setUpdTime(rs.getTimestamp("upd_time"));
            cmsCooperation.setUpdUser(rs.getInt("upd_user"));
            return cmsCooperation;
        };
        return rowMapper;
    }

    @Override
    public List<CmsCooperation> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_cooperation` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_cooperation` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 更新status的状态
     *
     * @param pkid
     * @param status
     * @param userId
     * @return int
     */
    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        return getJdbcTemplate().update("update `cms_cooperation` set `status` = ?, upd_time = NOW(), upd_user = ? where pkid = ?", status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer("update `cms_cooperation` set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE `status` <> 2 ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid in (:pkids)");
            source.addValue("pkids", pkids);
        }
        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    /**
     * 获取最近记录 -- 排序使用
     *
     * @param sort
     * @param isUp true 向上, false 向下
     * @return CmsCooperation
     */
    public CmsCooperation getNearest(Integer sort, Boolean isUp) {
        StringBuffer sql = new StringBuffer("");
        if (isUp) {//上移
            sql.append("SELECT " + ALL_COLUMNS + " FROM `cms_cooperation` WHERE sort < ? ORDER BY sort DESC LIMIT 1");
        } else {// 下移
            sql.append("SELECT " + ALL_COLUMNS + " FROM `cms_cooperation` WHERE sort > ? ORDER BY sort ASC LIMIT 1");
        }
        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), sort));
    }

    /**
     * 更新Sort
     *
     * @param pkid
     * @param sort
     * @param userId
     * @return Integer
     */
    public Integer updateSort(Integer pkid, Integer sort, Integer userId) {
        return getJdbcTemplate().update("UPDATE `cms_cooperation` SET `sort` = ?, upd_time = NOW(), upd_user = ? WHERE pkid = ?", sort, userId, pkid);
    }
}