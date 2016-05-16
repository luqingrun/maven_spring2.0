package com.gongsibao.mvc.interceptor;

import com.gongsibao.common.constant.ConstantWeb;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.UrlUtils;
import com.gongsibao.common.util.WebUtils;
import com.gongsibao.common.util.constant.CacheConstant;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.util.cache.CacheService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Created by luqingrun on 16/3/30.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = Logger.getLogger(LoginInterceptor.class);

    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    private SortedSet<String> exceptPath;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UcUserService ucUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestId = WebUtils.getRequestId();
        String ip = WebUtils.getIpAddr(request);
        String uri = request.getRequestURI();
        String para = WebUtils.getParaStr(request);
        String contextPath = request.getContextPath();

        String path = request.getContextPath() + request.getServletPath();

        if (UrlUtils.urlMatch(exceptPath, path)) {
            return true;
        }
        //log.info("access_log, requestId=" + requestId + ", ip=" + ip + ", uri=" + uri + ", para=(" + para + "), handler=" + handler);
        String ticket = WebUtils.getCookieValue(ConstantWeb.COOKIE_LOGIN_TICKET, request);
        ResponseData data = new ResponseData();
        data.setCode(403);
        data.setMsg("请重新登陆");
        if(StringUtils.isBlank(ticket)){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.objectToJson(data));
            return false;
        }
        LoginUser loginUser = (LoginUser) cacheService.get(CacheConstant.LOGIN_KEY + ticket);
        if(loginUser == null){
            loginUser = ucUserService.findLoginUser(ticket);
        }
        if(loginUser == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtils.objectToJson(data));
            return false;
        }

        cacheService.put(CacheConstant.LOGIN_KEY + ticket, loginUser);
        request.setAttribute(ConstantWeb.LOGIN_USER, loginUser);
        request.setAttribute(ConstantWeb.LOGIN_USER_PKID, loginUser.getUcUser().getPkid());
        threadLocal.set(loginUser);
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
