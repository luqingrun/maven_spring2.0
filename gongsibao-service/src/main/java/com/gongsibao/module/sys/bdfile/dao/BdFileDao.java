package com.gongsibao.module.sys.bdfile.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("bdFileDao")
public class BdFileDao extends BaseDao<BdFile> {

    public static String INSERT_COLUMNS = " `tab_name`, `form_id`, `name`, `url`, `add_time`, `add_user_id` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(BdFile bdFile) {
        insertObject(bdFile);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(BdFile bdFile) {
        getJdbcTemplate().update("insert into `bd_file`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, NOW(), ? )",
                bdFile.getTabName(),
                bdFile.getFormId(),
                bdFile.getName(),
                bdFile.getUrl(),
                bdFile.getAddUserId()
        );
    }

    public int[] insertBatch(List<BdFile> bdFiles) {
        return getJdbcTemplate().batchUpdate("INSERT INTO `bd_file`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, NOW(), ? )", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BdFile bdFile = bdFiles.get(i);

                ps.setString(1, bdFile.getTabName());
                ps.setInt(2, bdFile.getFormId());
                ps.setString(3, bdFile.getName());
                ps.setString(4, bdFile.getUrl());
                ps.setInt(5, bdFile.getAddUserId());
            }

            @Override
            public int getBatchSize() {
                return bdFiles.size();
            }
        });
    }

    @Override
    public int update(BdFile bdFile) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `bd_file` set pkid = pkid, `tab_name` = :tabName, `form_id` = :formId, `name` = :name, `url` = :url, `add_time` = :addTime, `add_user_id` = :addUserId where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdFile),Map.class));
    }

    public int updateurl(BdFile bdFile) {
        //TODO,需要自己决定如何实现
        //throw new java.lang.UnsupportedOperationException();
        String sql = "update `bd_file` set `name` = :name, `url` = :url where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(bdFile),Map.class));
    }

    @Override
    public int delete(Integer pkid) {
        String sql = "DELETE FROM `bd_file` WHERE `pkid` = ? ";
        return getJdbcTemplate().update(sql, pkid);
    }

    @Override
    public BdFile findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `bd_file` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<BdFile> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `bd_file` where pkid IN (:pkidList) ", map, getRowMapper());
    }

    @Override
    public RowMapper<BdFile> getRowMapper() {
        RowMapper<BdFile> rowMapper = (rs, i) -> {
            BdFile bdFile = new BdFile();
            bdFile.setPkid(rs.getInt("pkid"));
            bdFile.setFormId(rs.getInt("from_id"));
            bdFile.setTabName(rs.getString("tab_name"));
            bdFile.setName(rs.getString("name"));
            bdFile.setUrl(rs.getString("url"));
            bdFile.setAddTime(rs.getTimestamp("add_time"));
            bdFile.setAddUserId(rs.getInt("add_user_id"));
            return bdFile;
        };
        return rowMapper;
    }

    @Override
    public List<BdFile> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `bd_file` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `bd_file` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /**
     * 删除 by  tab_name and form_id
     * @param tabName
     * @param formId
     * @return
     */
    public int deleteByFormId(Integer formId, String tabName) {
        String sql = "DELETE FROM `bd_file` WHERE form_id = ? AND `tab_name` = ? ";
        return getJdbcTemplate().update(sql, formId, tabName);
    }

    /**
     * 查询 by  tabName and form_id
     */
    public List<BdFile> getListByFormId(Integer formId, String tabName) {
        String sql = "SELECT " + ALL_COLUMNS + " FROM `bd_file` WHERE form_id = ? AND `tab_name` = ? ";
        return getJdbcTemplate().query(sql, getRowMapper(), formId, tabName);
    }

    /**
     * 查询 by  tab_name and form_ids
     */
    public List<BdFile> getListByFormIds(Collection<Integer> formIds, String tabName) {
        String sql = "SELECT " + ALL_COLUMNS + " FROM `bd_file` WHERE form_id IN (:formIds) AND `tab_name` = :tabName ";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("formIds", formIds);
        source.addValue("tabName", tabName);

        return getNamedParameterJdbcTemplate().query(sql, source, getRowMapper());
    }

    public int updatePayFile(List<Integer> fileIds,int payId){
        String sql = "update bd_file set form_id = "+payId+" where pkid in (:fileIds)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("fileIds", fileIds);
        return getNamedParameterJdbcTemplate().update(sql,source);
    }
}