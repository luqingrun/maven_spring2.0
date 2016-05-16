package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsRecommendCategory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsRecommendCategoryDao")
public class CmsRecommendCategoryDao extends BaseDao<CmsRecommendCategory> {

    public static String INSERT_COLUMNS = " `name`, `description`, `img`, `bg_img`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";

    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsRecommendCategory cmsRecommendCategory) {
        insertObject(cmsRecommendCategory);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsRecommendCategory cmsRecommendCategory) {
        getJdbcTemplate().update("insert into `cms_recommend_category`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsRecommendCategory.getName(),
                cmsRecommendCategory.getDescription(),
                cmsRecommendCategory.getImg(),
                cmsRecommendCategory.getBgImg(),
                cmsRecommendCategory.getSort(),
                cmsRecommendCategory.getStatus(),
                cmsRecommendCategory.getAddUser(),
                cmsRecommendCategory.getUpdUser()
        );
    }

    @Override
    public int update(CmsRecommendCategory cmsRecommendCategory) {
        String sql = "update `cms_recommend_category` set `name` = :name, `description` = :description, `img` = :img, `bg_img` = :bgImg, `sort` = :sort, `status` = :status, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsRecommendCategory), Map.class));
    }

    public int updateInfo(CmsRecommendCategory cmsRecommendCategory) {
        String sql = "update `cms_recommend_category` set `name` = :name, `description` = :description, `img` = :img, `bg_img` = :bgImg, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsRecommendCategory), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_recommend_category` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsRecommendCategory findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_recommend_category` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsRecommendCategory> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_recommend_category` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public RowMapper<CmsRecommendCategory> getRowMapper() {
        RowMapper<CmsRecommendCategory> rowMapper = (rs, i) -> {
            CmsRecommendCategory cmsRecommendCategory = new CmsRecommendCategory();
            cmsRecommendCategory.setPkid(rs.getInt("pkid"));
            cmsRecommendCategory.setName(rs.getString("name"));
            cmsRecommendCategory.setDescription(rs.getString("description"));
            cmsRecommendCategory.setImg(rs.getString("img"));
            cmsRecommendCategory.setBgImg(rs.getString("bg_img"));
            cmsRecommendCategory.setSort(rs.getInt("sort"));
            cmsRecommendCategory.setStatus(rs.getInt("status"));
            cmsRecommendCategory.setAddTime(rs.getTimestamp("add_time"));
            cmsRecommendCategory.setAddUser(rs.getInt("add_user"));
            cmsRecommendCategory.setUpdTime(rs.getTimestamp("upd_time"));
            cmsRecommendCategory.setUpdUser(rs.getInt("upd_user"));
            return cmsRecommendCategory;
        };
        return rowMapper;
    }

    @Override
    public List<CmsRecommendCategory> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_recommend_category` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `sort` ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_recommend_category` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_recommend_category` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_recommend_category` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> ids, Integer status, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `cms_recommend_category` ");
        sql.append("SET `status` = :status, `upd_time` = NOW(), `upd_user` = :userId ");
        sql.append("WHERE `status` <> 2 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);

        if (CollectionUtils.isNotEmpty(ids)) {
            sql.append("AND `pkid` IN (:pkids)");
            source.addValue("pkids", ids);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    public CmsRecommendCategory getNearest(Integer sort, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_recommend_category` ");
        sql.append("WHERE `status` <> 2 ");
        if (isUp) {
            sql.append("AND `sort` < ? ORDER BY `sort` DESC ");
        } else {
            sql.append("AND `sort` > ? ORDER BY `sort` ASC ");
        }
        sql.append("LIMIT 1 ");
        return getFirstObj(getJdbcTemplate().query(sql.toString(), getRowMapper(), sort));
    }

}