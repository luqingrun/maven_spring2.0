package com.gongsibao.sys.controllers.cms.base;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.security.SecurityUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wk on 2016/4/16.
 */
public class CmsBaseController {

   public int getPkid(HttpServletRequest request) {
        String pkidStr = StringUtils.trimToEmpty(request.getParameter("pkidStr"));
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        return NumberUtils.toInt(pkidStr);
    }
}
