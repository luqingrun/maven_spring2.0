package com.gongsibao.common.mvc.filter;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域访问接口
 */
@Component
public class SimpleCORSFilter implements Filter {

    Log log = LogFactory.getLog(SimpleCORSFilter.class);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String referer = StringUtils.trimToEmpty(request.getHeader("Referer"));
        String origins = request.getHeader("Access-Control-Allow-Origin");

        log.info("referer[" + referer + "], origin[" + origins + "]");

        if (referer.indexOf(WebUtils.DOMAIN_COM) > 0) {
            response.setHeader("Access-Control-Allow-Origin", WebUtils.DOMAIN_COM);
        } else if (referer.indexOf(WebUtils.DOMAIN_NET) > 0) {
            response.setHeader("Access-Control-Allow-Origin", WebUtils.DOMAIN_NET);
        } else {
            response.setHeader("Access-Control-Allow-Origin", StringUtils.isBlank(origins) ? "*" : origins);
        }


        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}