package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsRecommendPackage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsRecommendPackageDao")
public class CmsRecommendPackageDao extends BaseDao<CmsRecommendPackage> {

    public static String INSERT_COLUMNS = " `name`, `description`, `url`, `img`, `img_focus`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsRecommendPackage cmsRecommendPackage) {
        insertObject(cmsRecommendPackage);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsRecommendPackage cmsRecommendPackage) {
        getJdbcTemplate().update("insert into `cms_recommend_package`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsRecommendPackage.getName(),
                cmsRecommendPackage.getDescription(),
                cmsRecommendPackage.getUrl(),
                cmsRecommendPackage.getImg(),
                cmsRecommendPackage.getImgFocus(),
                cmsRecommendPackage.getSort(),
                cmsRecommendPackage.getStatus(),
                cmsRecommendPackage.getAddUser(),
                cmsRecommendPackage.getUpdUser()
        );
    }

    @Override
    public int update(CmsRecommendPackage cmsRecommendPackage) {
        StringBuffer sql = new StringBuffer("update `cms_recommend_package` set `name` = :name, `description` = :description, `url` = :url, `img` = :img, `img_focus` = :imgFocus, `sort` = :sort, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid");
        return getNamedParameterJdbcTemplate().update(sql.toString(), JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsRecommendPackage), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_recommend_package` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsRecommendPackage findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_recommend_package` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsRecommendPackage> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_recommend_package` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsRecommendPackage> getRowMapper() {
        RowMapper<CmsRecommendPackage> rowMapper = (rs, i) -> {
            CmsRecommendPackage cmsRecommendPackage = new CmsRecommendPackage();
            cmsRecommendPackage.setPkid(rs.getInt("pkid"));
            cmsRecommendPackage.setName(rs.getString("name"));
            cmsRecommendPackage.setDescription(rs.getString("description"));
            cmsRecommendPackage.setUrl(rs.getString("url"));
            cmsRecommendPackage.setImg(rs.getString("img"));
            cmsRecommendPackage.setImgFocus(rs.getString("img_focus"));
            cmsRecommendPackage.setSort(rs.getInt("sort"));
            cmsRecommendPackage.setStatus(rs.getInt("status"));
            cmsRecommendPackage.setAddTime(rs.getTimestamp("add_time"));
            cmsRecommendPackage.setAddUser(rs.getInt("add_user"));
            cmsRecommendPackage.setUpdTime(rs.getTimestamp("upd_time"));
            cmsRecommendPackage.setUpdUser(rs.getInt("upd_user"));
            return cmsRecommendPackage;
        };
        return rowMapper;
    }

    @Override
    public List<CmsRecommendPackage> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_recommend_package` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_recommend_package` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {

        return getJdbcTemplate().update("update `cms_recommend_package` set `status`=? ,upd_time=NOW(),upd_user=? where pkid=?", status, userId, pkid);
    }

    public int updateStatus(Integer status, Integer userId) {
        return getJdbcTemplate().update("update `cms_recommend_package` set `status` = ?, upd_time = NOW(), upd_user = ? where `status` <> 2 ", status, userId);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer("update `cms_recommend_package` set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE pkid in (:pkids) ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        source.addValue("pkids", pkids);
        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    /**
     * 获取最近的banner -- 排序使用
     *
     * @param sort
     * @param isUp
     * @return CmsRecommendPackage
     */
    public CmsRecommendPackage getNearestRecommendPackage(Integer sort, Boolean isUp) {
        StringBuffer sql = new StringBuffer("");
        if (isUp) {//上移
            sql.append("SELECT " + ALL_COLUMNS + " FROM `cms_recommend_package` WHERE `status` <> 2 AND sort < ? ORDER BY sort DESC LIMIT 1");
        } else {// 下移
            sql.append("SELECT " + ALL_COLUMNS + "  FROM `cms_recommend_package` WHERE `status` <> 2 AND sort > ? ORDER BY sort ASC LIMIT 1");
        }
        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), sort));
    }

    /**
     * 更新该记录的Sort字段值为pkid
     *
     * @param pkid
     * @param sort
     * @param userId
     * @return Integer
     */
    public Integer updateSort(Integer pkid, Integer sort, Integer userId) {
        return getJdbcTemplate().update("UPDATE `cms_recommend_package` SET `sort` = ?, upd_time = NOW(), upd_user = ? WHERE pkid = ?", sort, userId, pkid);
    }
}