package com.gongsibao.module.sys.cms.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.cms.entity.CmsGuestVoice;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("cmsGuestVoiceDao")
public class CmsGuestVoiceDao extends BaseDao<CmsGuestVoice> {

    public static String INSERT_COLUMNS = " `name`, `guest_name`, `guest_port`, `guest_evaluation`, `img`, `sort`, `spider`, `status`, `add_time`, `add_user`, `upd_time`, `upd_user` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(CmsGuestVoice cmsGuestVoice) {
        insertObject(cmsGuestVoice);

        return getLastInsertId();
    }

    @Override
    protected void insertObject(CmsGuestVoice cmsGuestVoice) {
        getJdbcTemplate().update("insert into `cms_guest_voice`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ? )",
                cmsGuestVoice.getName(),
                cmsGuestVoice.getGuestName(),
                cmsGuestVoice.getGuestPort(),
                cmsGuestVoice.getGuestEvaluation(),
                cmsGuestVoice.getImg(),
                cmsGuestVoice.getSort(),
                cmsGuestVoice.getSpider(),
                cmsGuestVoice.getStatus(),
                cmsGuestVoice.getAddUser(),
                cmsGuestVoice.getUpdUser()
        );
    }

    @Override
    public int update(CmsGuestVoice cmsGuestVoice) {
        String sql = "update `cms_guest_voice` set `name` = :name, `guest_name` = :guestName, `guest_port` = :guestPort, `guest_evaluation` = :guestEvaluation, `img` = :img, `spider` = :spider, `upd_time` = :updTime, `upd_user` = :updUser where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(cmsGuestVoice), Map.class));
    }

    @Override
    public int delete(Integer id) {
        //return getJdbcTemplate().update("delete from `cms_guest_voice` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public CmsGuestVoice findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `cms_guest_voice` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<CmsGuestVoice> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `cms_guest_voice` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<CmsGuestVoice> getRowMapper() {
        RowMapper<CmsGuestVoice> rowMapper = (rs, i) -> {
            CmsGuestVoice cmsGuestVoice = new CmsGuestVoice();
            cmsGuestVoice.setPkid(rs.getInt("pkid"));
            cmsGuestVoice.setName(rs.getString("name"));
            cmsGuestVoice.setGuestName(rs.getString("guest_name"));
            cmsGuestVoice.setGuestPort(rs.getString("guest_port"));
            cmsGuestVoice.setGuestEvaluation(rs.getString("guest_evaluation"));
            cmsGuestVoice.setImg(rs.getString("img"));
            cmsGuestVoice.setSort(rs.getInt("sort"));
            cmsGuestVoice.setSpider(rs.getInt("spider"));
            cmsGuestVoice.setStatus(rs.getInt("status"));
            cmsGuestVoice.setAddTime(rs.getTimestamp("add_time"));
            cmsGuestVoice.setAddUser(rs.getInt("add_user"));
            cmsGuestVoice.setUpdTime(rs.getTimestamp("upd_time"));
            cmsGuestVoice.setUpdUser(rs.getInt("upd_user"));
            return cmsGuestVoice;
        };
        return rowMapper;
    }

    @Override
    public List<CmsGuestVoice> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `cms_guest_voice` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" ORDER BY sort ASC");
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `cms_guest_voice` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
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
        return getJdbcTemplate().update("UPDATE `cms_guest_voice` SET `sort` = ?, upd_time = NOW(), upd_user = ? WHERE pkid = ?", sort, userId, pkid);
    }

    public int updateStatus(Integer pkid, Integer status, Integer userId) {
        String sql = "UPDATE `cms_guest_voice` SET `status` = ?, `upd_time` = NOW(), `upd_user` = ? WHERE `pkid` = ?";
        return getJdbcTemplate().update(sql, status, userId, pkid);
    }

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer userId) {
        StringBuffer sql = new StringBuffer("update `cms_guest_voice` set `status` = :status, `upd_time` = NOW(), `upd_user` = :userId WHERE `status` <> 2 ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", status);
        source.addValue("userId", userId);
        if (CollectionUtils.isNotEmpty(pkids)) {
            sql.append("AND pkid in (:pkids)");
            source.addValue("pkids", pkids);
        }
        return getNamedParameterJdbcTemplate().update(sql.toString(), source);
    }

    public CmsGuestVoice getNearest(Integer sort, Boolean isUp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(ALL_COLUMNS);
        sql.append("FROM `cms_guest_voice` ");
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