package com.gongsibao.captcha.service;

/**
 * Created by wk on 2016/3/29.
 */
public interface CaptchaService {

    String getCaptchaText(String key);

    String getCaptchaText(String key, boolean autoClean);

    boolean cleanCaptchaText(String key);

    boolean setCaptchaText(String key, String value);

    boolean validCaptchaText(String key, String code);
}
