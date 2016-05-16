package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsSpecialActive;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsSpecialActiveDao")
public class CmsSpecialActiveDao extends BaseDao<CmsSpecialActive> {

    public static String INSERT_COLUMNS = " `name`, `description`, `active_time`, `url`, `img`, `img_big`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsSpecialActive cmsSpecialActive) {
        insertObject(cmsSpecialActive);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsSpecialActive cmsSpecialActive) {
        getJdbcTemplate().update("insert into `cms_special_active`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsSpecialActive.getName(),
                cmsSpecialActive.getDescription(),
                cmsSpecialActive.getActiveTime(),
                cmsSpecialActive.getUrl(),
                cmsSpecialActive.getImg(),
                cmsSpecialActive.getImgBig(),
                cmsSpecialActive.getStatus(),
                cmsSpecialActive.getAddUser(),
                cmsSpecialActive.getUpdUser()
        );
    }

    @Override
    public int update(CmsSpecialActive cmsSpecialActive) {
        StringBuffer sql = new StringBuffer("update `cms_special_active` set `name` = :name, `description` = :description, `active_time` = :activeTime, `url` = :url, `img` = :img, `img_big` = :imgBig, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid");
        return getNamedParameterJdbcTemplate().update(sql.toString(), JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsSpecialActive), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_special_active` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsSpecialActive findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_special_active` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsSpecialActive> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_special_active` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsSpecialActive> getRowMapper() {
        RowMapper<CmsSpecialActive> rowMapper = (rs, i) -> {
            CmsSpecialActive cmsSpecialActive = new CmsSpecialActive();
            cmsSpecialActive.setPkid(rs.getInt("pkid"));
            cmsSpecialActive.setName(rs.getString("name"));
            cmsSpecialActive.setDescription(rs.getString("description"));
            cmsSpecialActive.setActiveTime(rs.getTimestamp("active_time"));
            cmsSpecialActive.setUrl(rs.getString("url"));
            cmsSpecialActive.setImg(rs.getString("img"));
            cmsSpecialActive.setImgBig(rs.getString("img_big"));
            cmsSpecialActive.setStatus(rs.getInt("status"));
            cmsSpecialActive.setAddTime(rs.getTimestamp("add_time"));
            cmsSpecialActive.setAddUser(rs.getInt("add_user"));
            cmsSpecialActive.setUpdTime(rs.getTimestamp("upd_time"));
            cmsSpecialActive.setUpdUser(rs.getInt("upd_user"));
            return cmsSpecialActive;
        };
        return rowMapper;
    }

    @Override
    public List<CmsSpecialActive> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_special_active` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `pkid` DESC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    public List<CmsSpecialActive> findByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_special_active` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `active_time` DESC ");
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_special_active` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 更新状态
     *
     * @param pkid
     * @param status
     * @param userId
     * @return int
     */
    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        return getJdbcTemplate().update("update `cms_special_active` set status=?,upd_time = NOW(), upd_user=? where pkid=?", status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer("update `cms_special_active` set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE `status` <> 2 ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid in (:pkids)");
            source.addValue("pkids", pkids);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }




}