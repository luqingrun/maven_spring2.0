package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsRecommendService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("cmsRecommendServiceDao")
public class CmsRecommendServiceDao extends BaseDao<CmsRecommendService> {

    public static String INSERT_COLUMNS = " `category_id`, `name`, `description`, `price`, `price_type`, `url`, `img`, `sort`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsRecommendService cmsRecommendService) {
        insertObject(cmsRecommendService);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsRecommendService cmsRecommendService) {
        getJdbcTemplate().update("insert into `cms_recommend_service`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsRecommendService.getCategoryId(),
                cmsRecommendService.getName(),
                cmsRecommendService.getDescription(),
                cmsRecommendService.getPrice(),
                cmsRecommendService.getPriceType(),
                cmsRecommendService.getUrl(),
                cmsRecommendService.getImg(),
                cmsRecommendService.getSort(),
                cmsRecommendService.getStatus(),
                cmsRecommendService.getAddUser(),
                cmsRecommendService.getUpdUser()
        );
    }

    @Override
    public int update(CmsRecommendService cmsRecommendService) {
        String sql = "update `cms_recommend_service` set `category_id` = :categoryId, `name` = :name, `description` = :description, `price` = :price, `url` = :url, `img` = :img, `sort` = :sort, `status` = :status, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsRecommendService), Map.class));
    }

    public int updateInfo(CmsRecommendService cmsRecommendService) {
        String sql = "update `cms_recommend_service` set `name` = :name, `description` = :description, `price` = :price, `price_type` = :priceType, `url` = :url, `img` = :img, `upd_time` = NOW(), `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsRecommendService), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_recommend_service` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsRecommendService findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_recommend_service` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsRecommendService> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_recommend_service` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsRecommendService> getRowMapper() {
        RowMapper<CmsRecommendService> rowMapper = (rs, i) -> {
            CmsRecommendService cmsRecommendService = new CmsRecommendService();
            cmsRecommendService.setPkid(rs.getInt("pkid"));
            cmsRecommendService.setCategoryId(rs.getInt("category_id"));
            cmsRecommendService.setName(rs.getString("name"));
            cmsRecommendService.setDescription(rs.getString("description"));
            cmsRecommendService.setPrice(rs.getString("price"));
            cmsRecommendService.setPriceType(rs.getInt("price_type"));
            cmsRecommendService.setUrl(rs.getString("url"));
            cmsRecommendService.setImg(rs.getString("img"));
            cmsRecommendService.setSort(rs.getInt("sort"));
            cmsRecommendService.setStatus(rs.getInt("status"));
            cmsRecommendService.setAddTime(rs.getTimestamp("add_time"));
            cmsRecommendService.setAddUser(rs.getInt("add_user"));
            cmsRecommendService.setUpdTime(rs.getTimestamp("upd_time"));
            cmsRecommendService.setUpdUser(rs.getInt("upd_user"));
            return cmsRecommendService;
        };
        return rowMapper;
    }

    @Override
    public List<CmsRecommendService> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_recommend_service` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY `sort` ASC ");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_recommend_service` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateSort(Integer pkid, Integer sort, Integer userId) {
        String sql = "UPDATE `cms_recommend_service` SET `sort` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_recommend_service` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> ids, Integer categoryId, Integer status, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE `cms_recommend_service` ");
        sql.append("SET `status` = :status, `upd_time` = NOW(), `upd_user` = :userId ");
        sql.append("WHERE `status` <> 2 AND `category_id` = :categoryId ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        source.addValue("categoryId", categoryId);

        if (CollectionUtils.isNotEmpty(ids)) {
            sql.append("AND `pkid` IN (:pkids) ");
            source.addValue("pkids", ids);
        }

        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    public CmsRecommendService getNearest(Integer categoryId, Integer sort, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_recommend_service` ");
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