package com.gongsibao.module.uc.ucuser.entity;

import com.gongsibao.module.sys.bdsync.entity.BdSync;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luqingrun on 16/4/8.
 */
public class LoginUser implements java.io.Serializable {
    private static final long serialVersionUID = 7383109775575577079L;
    /** 当前登录用户 */
    private UcUser ucUser;
    /** 当前登录拥有角色 */
    private List<UcRole> ucRoleList = new ArrayList<>();
    private List<String> roleTags = new ArrayList<>();
    /** 当前登录拥有权限角色 */
    private List<UcAuth> ucAuthList = new ArrayList<>();
    /** 当前登录拥有岗位 */
    private List<UcOrganization> ucOrganizationList = new ArrayList<>();
    /** 当前登录拥有菜单 */
    private List<UcAuth> menuList = new ArrayList<>();
    /** mysql 与 sqlserver 同步表 */
    private BdSync bdSync;

    public UcUser getUcUser() {
        return ucUser;
    }

    public void setUcUser(UcUser ucUser) {
        this.ucUser = ucUser;
    }

    public List<UcRole> getUcRoleList() {
        return ucRoleList;
    }

    public void setUcRoleList(List<UcRole> ucRoleList) {
        this.ucRoleList = ucRoleList;

        roleTags.clear();
        if (CollectionUtils.isNotEmpty(ucRoleList)) {
            for (UcRole ucRole : ucRoleList) {
                roleTags.add(ucRole.getTag());
            }
        }
    }

    public List<String> getRoleTags() {
        return roleTags;
    }

    public List<UcAuth> getUcAuthList() {
        return ucAuthList;
    }

    public void setUcAuthList(List<UcAuth> ucAuthList) {
        this.ucAuthList = ucAuthList;
    }

    public List<UcOrganization> getUcOrganizationList() {
        return ucOrganizationList;
    }

    public void setUcOrganizationList(List<UcOrganization> ucOrganizationList) {
        this.ucOrganizationList = ucOrganizationList;
    }

    public List<UcAuth> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<UcAuth> menuList) {
        this.menuList = menuList;
    }

    public BdSync getBdSync() {
        return bdSync;
    }

    public void setBdSync(BdSync bdSync) {
        this.bdSync = bdSync;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginUser{");
        sb.append("ucUser=").append(ucUser);
        sb.append(", ucRoleList=").append(ucRoleList);
        sb.append(", ucAuthList=").append(ucAuthList);
        sb.append(", ucOrganizationList=").append(ucOrganizationList);
        sb.append(", menuList=").append(menuList);
        sb.append('}');
        return sb.toString();
    }
}
