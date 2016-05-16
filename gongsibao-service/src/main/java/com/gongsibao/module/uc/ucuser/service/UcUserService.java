package com.gongsibao.module.uc.ucuser.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucorganization.entity.ORG;
import com.gongsibao.module.uc.ucrole.entity.RoleTag;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UcUserService {

    UcUser findById(Integer pkid);

    UcUser findUserInfoById(Integer pkid);

    UcUser findByTicket(String ticket);

    UcUser findByLoginName(String loginName);

    LoginUser findLoginUser(Integer userPkid);

    LoginUser findLoginUser(String ticket);

    int saveUcUser(UcUser ucUser);

    int update(UcUser ucUser);

    int delete(Integer pkid);

    Integer insert(UcUser ucUser);

    List<UcUser> findByIds(List<Integer> pkidList);

    Map<Integer, UcUser> findMapByIds(List<Integer> pkidList);

    Pager<UcUser> pageByProperties(Map<String, Object> properties, int page);

    Pager<UcUser> findByCondition(Map<String, Object> properties, int currentPage, int pageSize);

    int updateTicket(Integer pkid, String ticket);

    /**
     * 查询组织机构下用户
     *
     * @param organizationIdList
     * @return
     */
    List<Integer> findByOrganizationIdList(List<Integer> organizationIdList);
//    List<Integer> findByRoleNameList(List<String> roleNameList);

    /**
     * 查询用户ids by 角色
     *
     * @param roleTagList
     * @return
     */
    List<Integer> findByRoleTagList(List<String> roleTagList);

    default List<Integer> findByRoleTag(String roleTag) {
        return findByRoleTagList(new ArrayList<String>() {{
            add(roleTag);
        }});
    }

    /**
     * 查询用户ids by 角色 and 组织结构对应字典项
     *
     * @param roleTagList
     * @param dictType    1服务产品分类、2服务地区
     * @param dictIds
     * @return
     */
    List<Integer> findByRoleTagAndDictIdList(List<String> roleTagList, Integer dictType, Collection<Integer> dictIds);

    /**
     * 根据产品分类列表获取事业部总经理
     *
     * @param dictIds
     * @return
     */
    default List<Integer> getBusinessBoss(Collection<Integer> dictIds) {
        if (CollectionUtils.isEmpty(dictIds)) {
            return null;
        }
        return findByRoleTagAndDictIdList(new ArrayList<String>() {{
            add(RoleTag.ROLE_SYBZJL);
        }}, 1, dictIds);
    }

    /**
     * 查询用户ids by 角色 and 组织机构ids
     *
     * @param roleTagList
     * @param organizationIdList
     * @return
     */
    List<Integer> findByRoleTagAndOrganizationId(List<String> roleTagList, List<Integer> organizationIdList);

    /**
     * 获取当前用户管理人员(包含该当前节点)
     *
     * @param userId
     * @return userIds
     */
    List<Integer> getUserPkid(Integer userId, String... roleTags);

    /**
     * 获取当前用户下属的组织机构ids(包含该当前节点)
     *
     * @param userId
     * @return organizationIds
     */
    List<Integer> getUserOrganizationIds(Integer userId);

    /**
     * 向上查找组织机构人员
     *
     * @param userId   当前用户id
     * @param level    指定组织机构级别, 没有null或0 见 ORG.java
     * @param roleTags 角色信息
     * @return userIds
     */
    List<Integer> getUserParentPkid(Integer userId, Integer level, String... roleTags);

    /**
     * 获取当前用户的分公司总经理
     *
     * @param userId
     * @return userIds
     */
    default List<Integer> getBranchBoss(Integer userId) {
        // 4层分公司
        return getUserParentPkid(userId, ORG.LEVEL_4, RoleTag.ROLE_FGSZJL);
    }

    // 查询层级ids
    List<Integer> getUserIdsByOrgLevel(Integer level, String... roleTags);

    /**
     * 查询组织机构下用户
     * @param orgIds
     * @return
     */
    List<UcUser> findUsersByOrgIds(List<Integer> orgIds);

    /**
     * 查询组织机构下用户
     *
     * @param orgId
     * @return
     */
    List<UcUser> findUsersByOrgId(Integer orgId);

    Map<String, Integer> findUserNums(Integer userId);

    /**
     * 用户管理列表
     * @param paramMap
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pager<UcUser> findManagerUserList(Map<String, Object> paramMap, Integer currentPage, Integer pageSize);

    int editUserEnabled(Integer pkid, Integer isEnabled, Integer operateUserId);
}