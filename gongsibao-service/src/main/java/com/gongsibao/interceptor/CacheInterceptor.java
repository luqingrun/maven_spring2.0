package com.gongsibao.interceptor;

import com.gongsibao.common.core.spring.ApplicationContextHelper;
import com.gongsibao.util.cache.CacheService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CacheInterceptor implements MethodInterceptor {

    private CacheService cacheService;

    //方法和缓存有效期的对应关系
    private Map<String, String> attributeSource = new HashMap<String, String>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final String methodName = invocation.getMethod().getName();
        if (!methodName.toLowerCase().endsWith("cache")) {
            return invocation.proceed();
        }
        String bestNameMatch = null;
        for (Iterator<String> it = this.attributeSource.keySet().iterator(); it.hasNext(); ) {
            String mappedName = it.next();
            if (isMatch(methodName, mappedName) && (bestNameMatch == null || bestNameMatch.length() <= mappedName.length())) {
                bestNameMatch = mappedName;
            }
        }
        String expiry = attributeSource.get(bestNameMatch);
        if ("-1".equals(expiry)) {
            return invocation.proceed();
        }

        String memKey = invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName() + "." + argsHashCode(invocation.getArguments());
        Object result = getCacheService().get(memKey);
        if (null != result) {
            return result;
        }

        result = invocation.proceed();
        boolean flag = getCacheService().put(memKey, result, NumberUtils.toInt(expiry) * 60);
        System.out.println(flag);
        return result;
    }

    public void setAttributes(Map<String, String> attributeSource) {
        this.attributeSource = attributeSource;
    }

    private boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    private static int argsHashCode(Object[] arguments) {
        if (null == arguments || arguments.length == 0) {
            return 0;
        }

        StringBuilder md5Strs = new StringBuilder();
        for (Object object : arguments) {
            if (null != object) {
                md5Strs.append(object.toString());
            }
            md5Strs.append("|");
        }

        return md5Strs.toString().hashCode();
    }

    private CacheService getCacheService() {
        if (null == cacheService) {
            cacheService = ApplicationContextHelper.getBean(CacheService.class);
        }
        return cacheService;
    }
}
