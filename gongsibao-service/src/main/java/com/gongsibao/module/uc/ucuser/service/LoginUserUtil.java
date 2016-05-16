package com.gongsibao.module.uc.ucuser.service;

import com.gongsibao.common.constant.ConstantWeb;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucrole.entity.RoleTag;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by luqingrun on 16/4/28.
 */
public class LoginUserUtil {
    /**
     * 获取当前登陆用户信息
     *
     * @return
     */
    public static LoginUser getLoginUser(HttpServletRequest request) {
        return (LoginUser) request.getAttribute(ConstantWeb.LOGIN_USER);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    public static UcUser getUcUser(HttpServletRequest request) {
        LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            return loginUser.getUcUser();
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户具有角色
     *
     * @param request
     * @return
     */
    public static List<UcRole> getUcRole(HttpServletRequest request) {
        LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            return loginUser.getUcRoleList();
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户具有岗位(以Tree形式)
     *
     * @param request
     * @return
     */
    public static List<UcOrganization> getUcOrganizationTree(HttpServletRequest request) {
        LoginUser loginUser = getLoginUser(request);
        if (loginUser != null) {
            return loginUser.getUcOrganizationList();
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户具有岗位(以List形式)
     *
     * @param request
     * @return
     */
    public static List<UcOrganization> getUcOrganizationList(HttpServletRequest request) {
        List<UcOrganization> list = getUcOrganizationTree(request);
        Set<UcOrganization> set = new HashSet<>();
        for (UcOrganization ucOrganization : list) {
            add2Set(ucOrganization, set);
        }
        return new ArrayList<>(set);
    }

    /**
     * 获取当前用户具有岗位(以List形式)
     *
     * @param list
     * @return
     */
    public static List<UcOrganization> getUcOrganizationList(List<UcOrganization> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<UcOrganization> set = new HashSet<>();
        for (UcOrganization ucOrganization : list) {
            add2Set(ucOrganization, set);
        }
        return new ArrayList<>(set);
    }

    private static void add2Set(UcOrganization ucOrganization, Set<UcOrganization> set) {
        set.add(ucOrganization);
        set.addAll(ucOrganization.getChildrenList());
        for (UcOrganization ucOrganization1 : ucOrganization.getChildrenList()) {
            add2Set(ucOrganization1, set);
        }
    }

    /**
     * 获取当前用户管理人员(包含该当前节点)
     *
     * @param request
     * @return
     */
    public static List<Integer> getUserPkid(HttpServletRequest request, String... roleTags) {
        List<UcOrganization> list = getUcOrganizationList(request);
        List<Integer> orgIdList = new ArrayList<>();
        for (UcOrganization ucOrganization : list) {
            orgIdList.add(ucOrganization.getPkid());
        }
        if (orgIdList.size() == 0) {
            return new ArrayList<>();
        }
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        UcUserService ucUserService = applicationContext.getBean(UcUserService.class);
        if (ArrayUtils.isEmpty(roleTags)) {
            return ucUserService.findByOrganizationIdList(orgIdList);
        } else {
            return ucUserService.findByRoleTagAndOrganizationId(Arrays.asList(roleTags), orgIdList);
        }
    }
}
