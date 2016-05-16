package com.gongsibao.mvc.interceptor;

import com.gongsibao.common.constant.ConstantWeb;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.UrlUtils;
import com.gongsibao.common.util.WebUtils;
import com.gongsibao.common.util.constant.CacheConstant;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.uc.ucauth.entity.UcAuth;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.util.cache.CacheService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = Logger.getLogger(AuthInterceptor.class);

    private SortedSet<String> exceptPath;

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getContextPath() + request.getServletPath();
        String ip = WebUtils.getIpAddr(request);

        // 是否需权限验证
        if (UrlUtils.urlMatch(exceptPath, path)) {
            return true;
        }

        String ticket = WebUtils.getCookieValue(ConstantWeb.COOKIE_LOGIN_TICKET, request);
        ResponseData data = new ResponseData();
        data.setCode(403);
        data.setMsg("forbidden");
        if (StringUtils.isBlank(ticket)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.objectToJson(data));
            return false;
        }

        LoginUser loginUser = (LoginUser) cacheService.get(CacheConstant.LOGIN_KEY + ticket);
        if (null == loginUser) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.objectToJson(data));
            return false;
        }

        List<UcAuth> urlList = loginUser.getUcAuthList();

        boolean isPass = false;
        if (CollectionUtils.isNotEmpty(urlList)) {
            SortedSet<String> dirtySet = new TreeSet<String>();
            for (UcAuth ucAuth : urlList) {
                dirtySet.add(ucAuth.getUrl() + "*");
            }
            isPass = UrlUtils.urlMatch(dirtySet, path);
        }

        if (!isPass) {
            log.info("############################ip[" + ip + "],user[" + loginUser.getUcUser().getPkid() + "],path[" + path + "],forbidden################################");
            data.setMsg("forbidden");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.objectToJson(data));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    @Override
    public void afterConcurrentHandlingStarted(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    }

    public void setExceptPath(SortedSet<String> exceptPath) {
        if (null == exceptPath) {
            this.exceptPath = new TreeSet<>();
        } else {
            this.exceptPath = exceptPath;
        }
    }
}