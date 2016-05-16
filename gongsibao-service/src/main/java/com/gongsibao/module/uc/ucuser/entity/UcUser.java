package com.gongsibao.module.uc.ucuser.entity;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.uc.ucrole.entity.UcRole;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;

import java.util.Date;
import java.util.List;


public class UcUser extends com.gongsibao.common.db.BaseEntity {

    public static final String HEAD_IMG_URL_TAB = "uc_user_head";

    private static final long serialVersionUID = -1L;


    /**
     * 密码
     */
    private String passwd;

    /**
     * 登陆凭证
     */
    private String ticket;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * Email
     */
    private String email;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信
     */
    private String weixin;

    /**
     * 电话
     */
    private String mobilePhone;

    /**
     * 性别，0保密、1男、2女
     */
    private Integer sex;

    /**
     * 是否内部员工
     */
    private Integer isInner;

    /**
     * 头像图片序号
     */
    private Integer headThumbFileId;
    private String headThumbFileUrl;

    /**
     * 用户类型序号，type=2
     */
    private Integer userTypeId;

    /**
     * 是否启用
     */
    private Integer isEnabled;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;

    /**
     * 是否接单 0不接单 1接单
     */
    private Integer isAcceptOrder;

    /**
     * 说明
     */
    private String remark;

    // 组织结构名称
    private String organizationName;

    // 角色名称
    private String roleName;

    // 是否跟进了订单 0 否 1 是
    private Integer isFollow;

    private List<UcRole> roleList;

    private List<UcUserRoleMap> userRoleMapList;

    private List<UcUserOrganizationMap> userOrganizationMapList;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        if (sex != 0 && sex != 1 && sex != 2) {
            sex = 0;
        }
        this.sex = sex;
    }

    public Integer getIsInner() {
        return isInner;
    }

    public void setIsInner(Integer isInner) {
        if (isInner != 0 && isInner != 1) {
            isInner = 0;
        }
        this.isInner = isInner;
    }

    public Integer getHeadThumbFileId() {
        return headThumbFileId;
    }

    public void setHeadThumbFileId(Integer headThumbFileId) {
        this.headThumbFileId = headThumbFileId;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }

    public Integer getIsAcceptOrder() {
        return isAcceptOrder;
    }

    public void setIsAcceptOrder(Integer isAcceptOrder) {
        this.isAcceptOrder = isAcceptOrder;
    }

    public String getRemark() {
        return StringUtils.trimToEmpty(remark);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsInnerName() {
        if (null == isInner || 0 == isInner) {
            return "外部";
        } else {
            return "内部";
        }
    }

    public String getHeadThumbFileUrl() {
        return headThumbFileUrl;
    }

    public void setHeadThumbFileUrl(String headThumbFileUrl) {
        this.headThumbFileUrl = headThumbFileUrl;
    }

    public List<UcRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<UcRole> roleList) {
        this.roleList = roleList;
    }

    public List<UcUserRoleMap> getUserRoleMapList() {
        return userRoleMapList;
    }

    public void setUserRoleMapList(List<UcUserRoleMap> userRoleMapList) {
        this.userRoleMapList = userRoleMapList;
    }

    public List<UcUserOrganizationMap> getUserOrganizationMapList() {
        return userOrganizationMapList;
    }

    public void setUserOrganizationMapList(List<UcUserOrganizationMap> userOrganizationMapList) {
        this.userOrganizationMapList = userOrganizationMapList;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public String getIsFollowName() {
        if (null == isFollow) {
            return "";
        }

        if (isFollow == 0) {
            return "否";
        } else {
            return "是";
        }
    }
}