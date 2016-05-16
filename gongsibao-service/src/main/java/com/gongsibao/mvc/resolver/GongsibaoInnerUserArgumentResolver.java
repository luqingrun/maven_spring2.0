package com.gongsibao.mvc.resolver;

import com.gongsibao.common.constant.ConstantWeb;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GongsibaoInnerUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        if (clazz.equals(LoginUser.class)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> clazz = parameter.getParameterType();
        if (clazz.equals(LoginUser.class)) {
            LoginUser user = (LoginUser) webRequest.getAttribute(ConstantWeb.LOGIN_USER,
                    RequestAttributes.SCOPE_REQUEST);
            if(user == null){
                user = new LoginUser();
                UcUser ucUser = new UcUser();
                ucUser.setPkid(10);
                ucUser.setAddTime(new Date());
                user.setUcUser(ucUser);

                List<UcOrganization> list = new ArrayList<>();
                list.add(new UcOrganization(){{
                    setPkid(12);
                    setName("北京大区");
                }});

                user.setUcOrganizationList(list);
            }

            return user;
        }
        return null;
    }

}
