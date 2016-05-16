package com.gongsibao.module.uc.ucorganization.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("ucOrganizationDao")
public class UcOrganizationDao extends BaseDao<UcOrganization> {

    public static String INSERT_COLUMNS = " `pid`, `name`, `short_name`, `leader_id`, `city_id`, `sort`, `level`, `is_leaf`, `is_enabled`, `add_time`, `add_user_id`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcOrganization ucOrganization) {
        insertObject(ucOrganization);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcOrganization ucOrganization) {
        getJdbcTemplate().update("insert into `uc_organization`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ? )",
                ucOrganization.getPid(),
                ucOrganization.getName(),
                ucOrganization.getShortName(),
                ucOrganization.getLeaderId(),
                ucOrganization.getCityId(),
                ucOrganization.getSort(),
                ucOrganization.getLevel(),
                ucOrganization.getIsLeaf(),
                ucOrganization.getIsEnabled(),
                ucOrganization.getAddUserId(),
                ucOrganization.getRemark()
        );
    }

    @Override
    public int update(UcOrganization ucOrganization) {
        String sql = "update `uc_organization` set `pid` = :pid, `name` = :name, `short_name` = :shortName, `leader_id` = :leaderId, `city_id` = :cityId, `sort` = :sort, `level` = :level, `is_leaf` = :isLeaf, `is_enabled` = :isEnabled, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucOrganization), Map.class));
    }

    @Override
    public int delete(Integer id) {
        return getJdbcTemplate().update("delete from `uc_organization` where pkid = ? ", id);
    }

    @Override
    public UcOrganization findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_organization` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<UcOrganization> findByIds(List<Integer> pkidList) {
        if (CollectionUtils.isEmpty(pkidList)) {
            return new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_organization` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    public List<UcOrganization> findByPid(Integer pid) {
        String sql = "select " + ALL_COLUMNS + " from `uc_organization` where pid = " + pid;
        return getJdbcTemplate().query(sql, getRowMapper());
    }

    public int updateLeaf(Integer pkid, Integer isLeaf) {
        String sql = "UPDATE `uc_organization` SET is_leaf = ? where pid = ? ";
        return getJdbcTemplate().update(sql, isLeaf, pkid);
    }

    @Override
    public RowMapper<UcOrganization> getRowMapper() {
        RowMapper<UcOrganization> rowMapper = (rs, i) -> {
            UcOrganization ucOrganization = new UcOrganization();
            ucOrganization.setPkid(rs.getInt("pkid"));
            ucOrganization.setPid(rs.getInt("pid"));
            ucOrganization.setName(rs.getString("name"));
            ucOrganization.setShortName(rs.getString("short_name"));
            ucOrganization.setLeaderId(rs.getInt("leader_id"));
            ucOrganization.setCityId(rs.getInt("city_id"));
            ucOrganization.setSort(rs.getDouble("sort"));
            ucOrganization.setLevel(rs.getInt("level"));
            ucOrganization.setIsLeaf(rs.getInt("is_leaf"));
            ucOrganization.setIsEnabled(rs.getInt("is_enabled"));
            ucOrganization.setAddTime(rs.getTimestamp("add_time"));
            ucOrganization.setAddUserId(rs.getInt("add_user_id"));
            ucOrganization.setRemark(rs.getString("remark"));
            ucOrganization.setLevel(rs.getInt("level"));
            return ucOrganization;
        };
        return rowMapper;
    }

    @Override
    public List<UcOrganization> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_organization` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    public List<UcOrganization> findByUserPkid(Integer userPkid) {
        String sql = "select  t2.`pkid`, t2.`pid`, t2.`name`, t2.`short_name`, t2.`leader_id`, t2.`city_id`, t2.`sort`, t2.`level`, t2.`is_leaf`, t2.`is_enabled`, t2.`add_time`, t2.`add_user_id`, t2.`remark`  from uc_user_organization_map t1 left join uc_organization t2 on t1.`organization_id` = t2.`pkid` where t1.`user_id` = " + userPkid;
        return getJdbcTemplate().query(sql, getRowMapper());
    }


    private void setCondition(StringBuilder sql, MapSqlParameterSource source, Map<String, Object> condition) {
        if (null != condition.get("pkids")) {
            sql.append("AND pkid IN (:pkids) ");
            source.addValue("pkids", condition.get("pkids"));
        }

        if (null != condition.get("name")) {
            sql.append("AND `name` like :name ");
            source.addValue("name", condition.get("name") + "%");
        }

        if (null != condition.get("shortName")) {
            sql.append("AND `short_name` like :shortName ");
            source.addValue("shortName", condition.get("shortName") + "%");
        }

    }

    public int countByCondition(Map<String, Object> condition) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) ");
        sql.append("FROM `uc_organization` ");
        sql.append("WHERE 1 = 1 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        setCondition(sql, source, condition);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), source, Integer.class);
    }

    public List<UcOrganization> findByCondition(Map<String, Object> condition, int startRow, int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(ALL_COLUMNS);
        sql.append("FROM `uc_organization` ");
        sql.append("WHERE 1 = 1 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        setCondition(sql, source, condition);

        sql.append("ORDER BY pkid ASC ");
        sql.append("LIMIT :startRow, :pageSize ");

        source.addValue("startRow", startRow);
        source.addValue("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), source, getRowMapper());
    }


    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_organization` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public List<UcOrganization> findAll() {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_organization` where is_enabled = 1 ");
        return getJdbcTemplate().query(sql.toString(), getRowMapper());
    }

    public List<Integer> queryIdsByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("DISTINCT pkid ");
        sql.append("FROM ");
        sql.append("uc_organization ");
        sql.append("WHERE 1=1 ");
        if (StringUtils.isNotBlank(StringUtils.trimToEmpty(properties.get("name")))) {
            sql.append("AND (name LIKE :name OR short_name LIKE :name) ");
        }
        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), properties, Integer.class);
    }
}