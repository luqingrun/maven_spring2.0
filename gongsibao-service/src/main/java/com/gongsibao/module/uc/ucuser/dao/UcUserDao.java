package com.gongsibao.module.uc.ucuser.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository("ucUserDao")
public class UcUserDao extends BaseDao<UcUser> {

    public static String INSERT_COLUMNS = " `passwd`, `ticket`, `real_name`, `email`, `qq`, `weixin`, `mobile_phone`, `sex`, `is_inner`, `head_thumb_file_id`, `user_type_id`, `is_enabled`, `add_time`, `add_user_id`, `is_accept_order`, `remark` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(UcUser ucUser) {
        insertObject(ucUser);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(UcUser ucUser) {
        getJdbcTemplate().update("insert into `uc_user`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                ucUser.getPasswd(),
                ucUser.getTicket(),
                ucUser.getRealName(),
                ucUser.getEmail(),
                ucUser.getQq(),
                ucUser.getWeixin(),
                ucUser.getMobilePhone(),
                ucUser.getSex(),
                ucUser.getIsInner(),
                ucUser.getHeadThumbFileId(),
                ucUser.getUserTypeId(),
                ucUser.getIsEnabled(),
                ucUser.getAddTime(),
                ucUser.getAddUserId(),
                ucUser.getIsAcceptOrder(),
                ucUser.getRemark()
        );
    }

    @Override
    public int update(UcUser ucUser) {
        String sql = "UPDATE `uc_user` SET `passwd` = :passwd, `real_name` = :realName, `email` = :email, `qq` = :qq, `weixin` = :weixin, `mobile_phone` = :mobilePhone, `sex` = :sex, `is_inner` = :isInner, `head_thumb_file_id` = :headThumbFileId, `user_type_id` = :userTypeId, `is_enabled` = :isEnabled, `is_accept_order` = :isAcceptOrder, `remark` = :remark where pkid = :pkid";
        return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(ucUser),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `uc_user` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public UcUser findById(Integer pkid) {
        return getFirstObj(getJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_user` where pkid = " + pkid, getRowMapper()));
    }

    @Override
    public List<UcUser> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `uc_user` where pkid IN (:pkidList) LIMIT 10 ", map, getRowMapper());
    }


    @Override
    public RowMapper<UcUser> getRowMapper() {
        RowMapper<UcUser> rowMapper = (rs, i) -> {
            UcUser ucUser = new UcUser();
            ucUser.setPkid(rs.getInt("pkid"));
            ucUser.setPasswd(rs.getString("passwd"));
            ucUser.setTicket(rs.getString("ticket"));
            ucUser.setRealName(rs.getString("real_name"));
            ucUser.setEmail(rs.getString("email"));
            ucUser.setQq(rs.getString("qq"));
            ucUser.setWeixin(rs.getString("weixin"));
            ucUser.setMobilePhone(rs.getString("mobile_phone"));
            ucUser.setSex(rs.getInt("sex"));
            ucUser.setIsInner(rs.getInt("is_inner"));
            ucUser.setHeadThumbFileId(rs.getInt("head_thumb_file_id"));
            ucUser.setUserTypeId(rs.getInt("user_type_id"));
            ucUser.setIsEnabled(rs.getInt("is_enabled"));
            ucUser.setAddTime(rs.getTimestamp("add_time"));
            ucUser.setAddUserId(rs.getInt("add_user_id"));
            ucUser.setIsAcceptOrder(rs.getInt("is_accept_order"));
            ucUser.setRemark(rs.getString("remark"));
            return ucUser;
        };
        return rowMapper;
    }

    @Override
    public List<UcUser> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `uc_user` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `uc_user` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    public int updateHeadFileId(Integer pkid, Integer headFileId) {
        String sql = "UPDATE uc_user SET head_thumb_file_id = ? WHERE pkid = ?";
        return getJdbcTemplate().update(sql, headFileId, pkid);
    }

    public int countByCondition(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) ");
        sql.append("FROM `uc_user` ");
        sql.append("WHERE 1 = 1 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        setCondition(properties, sql, source);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), source, Integer.class);
    }

    public List<UcUser> findByCondition(Map<String, Object> properties, Integer startRow, Integer pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append(ALL_COLUMNS);
        sql.append("FROM `uc_user` ");
        sql.append("WHERE 1 = 1 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        setCondition(properties, sql, source);
        sql.append("ORDER BY pkid ASC ");
        sql.append("LIMIT :startRow, :pageSize");

        source.addValue("startRow", startRow);
        source.addValue("pageSize", pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), source, getRowMapper());
    }

    private void setCondition(Map<String, Object> properties, StringBuffer sql, MapSqlParameterSource source) {
        if (null != properties.get("pkids")) {
            sql.append("AND pkid IN (:pkids) ");
            source.addValue("pkids", properties.get("pkids"));
        }

        if (null != properties.get("realName")) {
            sql.append("AND `real_name` LIKE :realName ");
            source.addValue("realName", properties.get("realName") + "%");
        }

        if (null != properties.get("mobilePhone")) {
            sql.append("AND `mobile_phone` LIKE :mobilePhone ");
            source.addValue("mobilePhone", properties.get("mobilePhone") + "%");
        }

        if (null != properties.get("isEnabled")) {
            sql.append("AND `is_enabled` LIKE :isEnabled ");
            source.addValue("isEnabled", properties.get("isEnabled"));
        }
        if (null != properties.get("isAcceptOrder")) {
            sql.append("AND `is_accept_order` LIKE :isAcceptOrder ");
            source.addValue("isAcceptOrder", properties.get("isAcceptOrder"));
        }
    }

    public UcUser findByLoginName(String loginName) {
        String sql = "select " + ALL_COLUMNS + " from `uc_user` where `mobile_phone` = ? or `real_name` = ? ";
        List<UcUser> ucUserList = getJdbcTemplate().query(sql, getRowMapper(), loginName, loginName);
        if (ucUserList != null && ucUserList.size() > 0) {
            return ucUserList.get(0);
        } else {
            return null;
        }
    }

    public int updateTicket(Integer pkid, String ticket) {
        String sql = "update `uc_user` set `ticket` = ?  where pkid = ?";
        return getJdbcTemplate().update(sql, ticket, pkid);
    }

    public List<Integer> findByOrganizationIdList(List<Integer> organizationIdList) {
        String ids = StringUtils.join(organizationIdList, ",");
        String sql = "select distinct user_id from uc_user_organization_map where organization_id in (" + ids + ")";
        List<Map<String, Object>> maps = getJdbcTemplate().queryForList(sql);
        List<Integer> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Integer userId = ((Number) map.get("user_id")).intValue();
            list.add(userId);
        }
        return list;
    }

//    public List<Integer> findByRoleNameList(List<String> roleNameList) {
//        StringBuffer sb = new StringBuffer("");
//        for (String roleName : roleNameList) {
//            roleName = "'" + roleName + "'";
//            if (sb.length() == 0) {
//                sb.append(roleName);
//            } else {
//                sb.append(", ").append(roleName);
//            }
//        }
//        String sql = "select distinct t1.user_id from uc_user_role_map t1 left join uc_role t2 on t1.`role_id`=t2.`pkid` where t2.`name` in (" + sb.toString() + ")";
//        List<Map<String, Object>> maps = getJdbcTemplate().queryForList(sql);
//        List<Integer> list = new ArrayList<>();
//        for (Map<String, Object> map : maps) {
//            Integer userId = ((Number) map.get("user_id")).intValue();
//            list.add(userId);
//        }
//        return list;
//    }

    public List<Integer> findByRoleTagList(List<String> roleTags) {
        if (CollectionUtils.isEmpty(roleTags)) {
            return null;
        }

        String sql = "select distinct t1.user_id from uc_user_role_map t1 left join uc_role t2 on t1.`role_id`=t2.`pkid` where t2.`tag` in (:roleTags)";

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("roleTags", roleTags);
        List<Map<String, Object>> maps = getNamedParameterJdbcTemplate().queryForList(sql, source);

        List<Integer> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Integer userId = ((Number) map.get("user_id")).intValue();
            list.add(userId);
        }
        return list;
    }

    public List<Map<String, Object>> findUserNums(Integer... userIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `is_enabled`, COUNT(1) as `num` ");
        sql.append("FROM `uc_user` ");
        sql.append("WHERE 1 = 1 ");

        MapSqlParameterSource source = new MapSqlParameterSource();
        if (ArrayUtils.isNotEmpty(userIds)) {
            sql.append("AND pkid IN (:pkids) ");
            source.addValue("pkids", Arrays.asList(userIds));
        }

        sql.append("GROUP BY `is_enabled` ");
        return getNamedParameterJdbcTemplate().queryForList(sql.toString(), source);
    }

    public int updateEnabled(Integer pkid, Integer isEnabled) {
        String sql = "UPDATE `uc_user` SET `is_enabled` = ? WHERE pkid = ? ";
        return getJdbcTemplate().update(sql, isEnabled, pkid);
    }


}