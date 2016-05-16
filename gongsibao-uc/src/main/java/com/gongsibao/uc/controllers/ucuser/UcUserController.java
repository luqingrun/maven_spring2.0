package com.gongsibao.uc.controllers.ucuser;


import com.gongsibao.captcha.service.CaptchaService;
import com.gongsibao.common.constant.ConstantWeb;
import com.gongsibao.common.util.*;
import com.gongsibao.common.util.constant.CacheConstant;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdsync.entity.BdSync;
import com.gongsibao.module.sys.bdsync.service.BdSyncService;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucrole.entity.RoleTag;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.module.uc.ucuserorganizationmap.entity.UcUserOrganizationMap;
import com.gongsibao.module.uc.ucuserrolemap.entity.UcUserRoleMap;
import com.gongsibao.util.cache.CacheService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/ucuser")
public class UcUserController {

    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private CacheService cacheService;

    @Autowired
    private BdSyncService bdSyncService;

    @Autowired
    private UcOrganizationService ucOrganizationService;

    @Autowired
    private CaptchaService captchaService;

    private static final long LOGIN_MAX_TIMES = 3;
    private static final String NEED_CAPTCHA = "need_captcha";

    @RequestMapping("/info")
    public ResponseData info(HttpServletRequest request) {
        ResponseData data = new ResponseData();
        int userId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userPkidStr")));
        if (userId == 0) {
            throw new IllegalArgumentException("userId[" + request.getParameter("userPkidStr") + "]");
        }

        data.setData(ucUserService.findUserInfoById(userId));
        return data;
    }

    @RequestMapping("/save")
    public ResponseData save(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setCode(-1);

        Integer currentUserId = user.getUcUser().getPkid();

        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userPkidStr")));
        int orgId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orgIdStr")));

        String mobilePhone = StringUtils.trimToEmpty(request.getParameter("mobilePhone"));
        String realName = StringUtils.trimToEmpty(request.getParameter("realName"));
        String email = StringUtils.trimToEmpty(request.getParameter("email"));
        String qq = StringUtils.trimToEmpty(request.getParameter("qq"));
        String weixin = StringUtils.trimToEmpty(request.getParameter("weixin"));
        String passwd = StringUtils.trimToEmpty(request.getParameter("passwd"));
        String passwdConfirm = StringUtils.trimToEmpty(request.getParameter("passwdConfirm"));
        String headThumbFileUrl = StringUtils.trimToEmpty(request.getParameter("headThumbFileUrl"));

        int sex = NumberUtils.toInt(request.getParameter("sex"));
        int isInner = NumberUtils.toInt(request.getParameter("isInner"));

        String roleIdStrs = StringUtils.trimToEmpty(request.getParameter("roleIds"));
        String[] canPassArr = StringUtils.trimToEmpty(request.getParameter("canPass")).split(",");

        List<Integer> roleIds = SecurityUtils.rc4DecryptBatch(roleIdStrs.split(","));

        // 手机号码
        if (StringUtils.isBlank(mobilePhone)) {
            data.setMsg("请填写手机号码");
            return data;
        }

        // 姓名
        if (StringUtils.isBlank(realName)) {
            data.setMsg("请填写手机号码");
            return data;
        }

        // 组织机构
        if (orgId == 0) {
            data.setMsg("请先选择组织机构");
            return data;
        }

        // 角色
        if (CollectionUtils.isEmpty(roleIds)) {
            data.setMsg("请先选择角色");
            return data;
        }

        if (roleIds.size() != canPassArr.length) {
            data.setMsg("角色数据错误");
            return data;
        }

        // 密码
        if (StringUtils.isBlank(passwd)) {
            data.setMsg("请输入密码");
            return data;
        }

        if (!passwd.equals(passwdConfirm)) {
            data.setMsg("两次密码不一致");
            return data;
        }

        UcUser ucUser = new UcUser() {{
            setMobilePhone(mobilePhone);
            setRealName(realName);
            setIsInner(isInner);
            setEmail(email);
            setQq(qq);
            setWeixin(weixin);
            setSex(sex);
            setPasswd(SecurityUtils.MD5Encode(passwd));
            setHeadThumbFileUrl(headThumbFileUrl);
            setHeadThumbFileId(0);
            setAddUserId(currentUserId);

            setUserTypeId(1021); // 系统用户
            setIsEnabled(1);     // 默认启用
            setIsAcceptOrder(0); // 默认不接单
        }};
        ucUser.setPkid(pkid);

        UcUserOrganizationMap ucUserOrganizationMap = new UcUserOrganizationMap();
        ucUserOrganizationMap.setOrganizationId(orgId);
        ucUserOrganizationMap.setAddUserId(currentUserId);

        // 组织
        ucUser.setUserOrganizationMapList(new ArrayList<UcUserOrganizationMap>() {{
            add(ucUserOrganizationMap);
        }});

        List<UcUserRoleMap> userRoleMapList = new ArrayList<>();
        // 权限
        for (int i = 0; i < roleIds.size(); i++) {
            int roleId = roleIds.get(i);
            int canPass = NumberUtils.toInt(canPassArr[i]);
            UcUserRoleMap map = new UcUserRoleMap();
            map.setRoleId(roleId);
            map.setCanPass(canPass);
            map.setAddUserId(currentUserId);

            userRoleMapList.add(map);
        }

        ucUser.setUserRoleMapList(userRoleMapList);
        ucUserService.saveUcUser(ucUser);
        data.setCode(200);
        return data;
    }

    @RequestMapping("/nums")
    public ResponseData nums(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        Map<String, Integer> map = ucUserService.findUserNums(user.getUcUser().getPkid());
        data.setData(map);
        return data;
    }

    @RequestMapping("/managerList")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"));
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"));
        int isEnabled = NumberUtils.toInt(request.getParameter("isEnabled"), -1);
        int orgPkid = NumberUtils.toInt(request.getParameter("orgPkidStr"));

        String mobilePhone = StringUtils.trimToEmpty(request.getParameter("mobilePhone"));
        String realName = StringUtils.trimToEmpty(request.getParameter("realName"));
        String roleName = StringUtils.trimToEmpty(request.getParameter("roleName"));

        Map<String, Object> param = new HashMap<>();
        param.put("currentUserId", user.getUcUser().getPkid());

        if (isEnabled > -1) {
            param.put("isEnabled", isEnabled);
        }

        if (orgPkid > 0) {
            param.put("orgPkid", orgPkid);
        }

        if (StringUtils.isNotBlank(mobilePhone)) {
            param.put("mobilePhone", mobilePhone);
        }

        if (StringUtils.isNotBlank(realName)) {
            param.put("realName", realName);
        }

        if (StringUtils.isNotBlank(roleName)) {
            param.put("roleName", roleName);
        }

        Pager<UcUser> pager = ucUserService.findManagerUserList(param, currentPage, pageSize);
        data.setData(pager);
        return data;
    }

    // 当前用户组织机构下用户
    @RequestMapping("/orgUsers")
    public ResponseData orgUsers(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int orgPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orgPkidStr")));
        data.setData(ucUserService.findUsersByOrgId(orgPkid));
        return data;
    }

    /**
     * 在职
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/enable")
    public ResponseData enable(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userPkid")));
        if (pkid == 0) {
            throw new IllegalArgumentException("userPkid[" + request.getParameter("userPkid") + "]");
        }

        int rs = ucUserService.editUserEnabled(pkid, 1, user.getUcUser().getPkid());
        if (rs == -1) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    /**
     * 离职
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/unEnable")
    public ResponseData unEnable(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userPkid")));

        int rs = ucUserService.editUserEnabled(pkid, 0, user.getUcUser().getPkid());
        if (rs <= 0) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }

        return data;
    }


    @RequestMapping("/login/page")
    public ModelAndView tologin(HttpServletRequest request, String msg) {
        ModelAndView mv = new ModelAndView("login");
        request.setAttribute("redirectUrl", request.getParameter("redirectUrl"));

        if (StringUtils.isNotBlank(msg)) {
            mv.addObject("msg", msg);
        }

        int times = getCaptchaTimes(request);
        if (times > LOGIN_MAX_TIMES) {
            mv.addObject(NEED_CAPTCHA, true);
            mv.addObject("randomKey", RandomStringUtils.randomAlphanumeric(32));

            mv.addObject("ctx_sys", PropertiesReader.getValue("project", "project_addr") + "/gongsibao-sys/");
        }
        mv.addObject("loginName", StringUtils.trimToEmpty(request.getParameter("loginName")));
        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        String loginName = StringUtils.trimToEmpty(request.getParameter("loginName"));
        String passwd = StringUtils.trimToEmpty(request.getParameter("passwd"));
        String captcha = StringUtils.trimToEmpty(request.getParameter("captcha"));
        String randomKey = StringUtils.trimToEmpty(request.getParameter("randomKey"));

        String redirectUrl = StringUtils.trimToEmpty(request.getParameter("redirectUrl"));
        // 获取登录次数
        int times = getCaptchaTimes(request);
        if (times > LOGIN_MAX_TIMES) {
            // 验证图片验证码
            if (!captchaService.validCaptchaText(randomKey, captcha)) {
                return tologin(request, "验证码错误");
            }
        }

        // 登录次数 +1
        incrCaptchaTimes(request);

        UcUser ucUser = ucUserService.findByLoginName(loginName);

        if (ucUser != null) {
            if (ucUser.getIsEnabled() != 1) {
                //启动状态
                return tologin(request, "禁止登录");
            }
            if (!ucUser.getPasswd().equals(SecurityUtils.MD5Encode(passwd))) {
                //密码不正确
                return tologin(request, "用户名或密码错误");
            }
            LoginUser loginUser = ucUserService.findLoginUser(ucUser.getPkid());
            if (loginUser != null) {
                if (StringUtils.isBlank(loginUser.getUcUser().getTicket())) {
                    String ticket = UUID.randomUUID().toString();
                    loginUser.getUcUser().setTicket(ticket);
                    ucUserService.updateTicket(loginUser.getUcUser().getPkid(), ticket);
                }
                cacheService.put(CacheConstant.LOGIN_KEY + loginUser.getUcUser().getTicket(), loginUser);

                WebUtils.setCookieNet(response, ConstantWeb.COOKIE_LOGIN_TICKET, loginUser.getUcUser().getTicket(), CacheConstant.TIME_ONE_YEAR);

                // 清空登录次数
                removeCaptchaTimes(request);
            }
            if (StringUtils.isBlank(redirectUrl)) {
                mv = new ModelAndView(new RedirectView("/cms/"));
            } else {
                mv = new ModelAndView(new RedirectView(redirectUrl));
            }
        } else {
            return tologin(request, "用户名不正确");
        }
        return mv;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        String redirectUrl = StringUtils.trimToEmpty(request.getParameter("redirectUrl"));

        // 清cookie
        WebUtils.removeCookieNet(response, ConstantWeb.COOKIE_LOGIN_TICKET);

        String ticket = null;
        if (null != user) {
            ticket = user.getUcUser().getTicket();
        }
        ticket = StringUtils.trimToEmpty(ticket);
        // 清缓存
        cacheService.delete("login_" + ticket);

        ModelAndView mv = null;
        if (StringUtils.isBlank(redirectUrl)) {
            mv = new ModelAndView(new RedirectView(PropertiesReader.getValue("project", "project_addr") + "/gongsibao-uc/ucuser/login/page"));
        } else {
            mv = new ModelAndView(new RedirectView(redirectUrl));
        }
        return mv;
    }

    @RequestMapping("/login.json")
    public ResponseData loginJson(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String loginName = request.getParameter("loginName");
        String passwd = request.getParameter("passwd");

        UcUser ucUser = ucUserService.findByLoginName(loginName);
        if (ucUser != null) {
            if (ucUser.getIsEnabled() != 1) {
                data.setMsg("禁止登陆");
                data.setCode(403);
                return data;
            }
            if (!ucUser.getPasswd().equals(SecurityUtils.MD5Encode(passwd))) {
                //密码不正确
                data.setCode(403);
                data.setMsg("密码错误");
                return data;
            }
            LoginUser loginUser = ucUserService.findLoginUser(ucUser.getPkid());
            if (loginUser != null) {
                if (StringUtils.isBlank(loginUser.getUcUser().getTicket())) {
                    String ticket = UUID.randomUUID().toString();
                    loginUser.getUcUser().setTicket(ticket);
                    ucUserService.updateTicket(loginUser.getUcUser().getPkid(), ticket);
                }
                data.setData(loginUser);

                cacheService.put(CacheConstant.LOGIN_KEY + loginUser.getUcUser().getTicket(), loginUser);
                WebUtils.setCookieNet(response, ConstantWeb.COOKIE_LOGIN_TICKET, loginUser.getUcUser().getTicket(), CacheConstant.TIME_ONE_YEAR);
            }
        } else {
            data.setMsg("用户名不存在");
            data.setCode(403);
            return data;
        }
        return data;
    }

    @RequestMapping("/ticketValid")
    public ResponseData isLogin(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        data.setCode(-1);
        String ticket = request.getParameter("ticket");
        if (StringUtils.isBlank(ticket)) {
            // 空的滚粗
            return data;
        }

        // 缓存找
        LoginUser loginUser = cacheService.get(CacheConstant.LOGIN_KEY + ticket, LoginUser.class);
        BdSync bdSync = null;
        if (null == loginUser) {
            // 缓存没有的话
            UcUser ucUser = ucUserService.findByTicket(ticket);
            if (null == ucUser) {
                return data;
            }

            bdSync = bdSyncService.findByMPkidAndTableName(ucUser.getPkid(), "uc_user");
        } else {
            bdSync = loginUser.getBdSync();
        }

        if (null == bdSync || StringUtils.isBlank(bdSync.getsId())) {
            return data;
        }

        Map<String, String> rs = new HashMap<>();
        rs.put("sid", bdSync.getsId());
        data.setCode(200);
        data.setData(rs);
        return data;
    }


    @RequestMapping("/getInfo.json")
    public ResponseData getInfoJson(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        data.setData(loginUser);
        return data;
    }

    @RequestMapping("/manageList")
    public ResponseData manageList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 名称
        String realName = StringUtils.trimToEmpty(request.getParameter("realName"));
        // 当前页
        String currentPage = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();
        condition.put("currentUserId", loginUser.getUcUser().getPkid());// 当前登陆者
        if (StringUtils.isNotBlank(realName)) {
            condition.put("realName", realName);
        }

        Pager<UcUser> pager = ucUserService.findByCondition(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        data.setData(pager);
        return data;
    }

    /**
     * 业务员
     *
     * @param request
     * @param response
     * @param loginUser
     * @return
     */
    @RequestMapping("/ywyList")
    public ResponseData aList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 当前页
        int currentPage = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("currentPage")));
        // 每页显示记录数量
        int pageSize = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("pageSize")));
        // 城市id
        int cityId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("cityIdStr")));

        String realName = StringUtils.trimToEmpty(request.getParameter("realName"));

        int isAcceptOrder = NumberUtils.toInt(request.getParameter("isAcceptOrder"), -1);

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();
        condition.put("needFollow", "needFollow");  // 是否查询跟进状态
        condition.put("currentUserId", loginUser.getUcUser().getPkid());// 当前登陆者
        condition.put("roleTagList", new ArrayList<String>() {{
            add(RoleTag.ROLE_YWY);
            add(RoleTag.ROLE_CZY);
        }});

        if (cityId > 0) {
            condition.put("cityId", cityId);
        }

        if (StringUtils.isNotBlank(realName)) {
            condition.put("realName", realName);
        }

        if (isAcceptOrder > -1) {
            condition.put("isAcceptOrder", isAcceptOrder);
        }

        Pager<UcUser> pager = ucUserService.findByCondition(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        data.setData(pager);
        return data;
    }

    /**
     * 清空登录次数
     *
     * @param request
     */
    private void removeCaptchaTimes(HttpServletRequest request) {
        cacheService.delete(CacheConstant.LOGIN_CAPTCHA_TIMES + WebUtils.getIpAddr(request));
    }

    /**
     * 获取登录次数
     *
     * @param request
     * @return
     */
    private int getCaptchaTimes(HttpServletRequest request) {
        return NumberUtils.toInt(cacheService.get(CacheConstant.LOGIN_CAPTCHA_TIMES + WebUtils.getIpAddr(request)), 1);
    }

    /**
     * 自增登录次数
     *
     * @param request
     */
    private void incrCaptchaTimes(HttpServletRequest request) {
        String ip = WebUtils.getIpAddr(request);
        int times = NumberUtils.toInt(cacheService.get(CacheConstant.LOGIN_CAPTCHA_TIMES + ip));
        times++;
        cacheService.put(CacheConstant.LOGIN_CAPTCHA_TIMES + ip, times, CacheConstant.TIME_ONE_DAY);
    }

}