package com.gongsibao.captcha.service.impl;

import com.gongsibao.captcha.service.CaptchaService;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.util.cache.CacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wk on 2016/3/29.
 */
@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaService {
    private Log log = LogFactory.getLog(CaptchaServiceImpl.class);

    private static final String CAPTCHA_PRE = "captcha_";

    private static final int CAPTCHA_EXPIRE_TIME = 60 * 60; //1小时

    @Autowired
    private CacheService cacheService;

    @Override
    public String getCaptchaText(String key) {
        return getCaptchaText(key, true);
    }

    @Override
    public String getCaptchaText(String key, boolean autoClean) {
        String value = "";
        try {
            value = (String) cacheService.get(CAPTCHA_PRE + key);
            if (StringUtils.isNotBlank(value) && autoClean) {
                cacheService.delete(CAPTCHA_PRE + key);
            }
        } catch (Exception e) {
            log.error("getCaptchaText cache error", e);
        }
        return StringUtils.trimToEmpty(value);
    }

    @Override
    public boolean cleanCaptchaText(String key) {
        try {
            return cacheService.put(CAPTCHA_PRE + key, "", 0);
        } catch (Exception e) {
            log.error("cleanCaptchaText cache error", e);
        }
        return false;
    }

    @Override
    public boolean setCaptchaText(String key, String value) {
        try {
            return cacheService.put(CAPTCHA_PRE + key, value, CAPTCHA_EXPIRE_TIME);
        } catch (Exception e) {
            log.error("setCaptchaText cache error", e);
            return false;
        }
    }

    @Override
    public boolean validCaptchaText(String key, String code) {
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            return false;
        }
        String captchaText = getCaptchaText(key);
        if (StringUtils.isBlank(captchaText)) {
            return false;
        }

        if (!captchaText.equals(code)) {
            return false;
        }
        return true;
    }
}
