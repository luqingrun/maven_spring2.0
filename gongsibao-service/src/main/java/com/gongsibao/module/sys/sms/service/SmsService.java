package com.gongsibao.module.sys.sms.service;


import com.gongsibao.module.sys.sms.entity.SmsRequest;
import com.gongsibao.module.sys.sms.entity.SmsResponse;
import com.gongsibao.module.sys.sms.entity.SmsTemplateResponse;

import java.util.Map;

/**
 * Created by huoquanfu on 2016/4/25.
 */
public interface SmsService {
    /**发送短信*/
    public SmsResponse send(Integer appId, String mobilePhone, String content);

    /**通过模板发送短信*/
    public SmsResponse send(Integer appId, String mobilPhone, Integer templateId, Map<String, String> paras);

    /**
     * 发送短信（支持模板发送和非模板发送）
     */
    public SmsResponse send(SmsRequest smsRequest);

    /**
     * 获取短信模板
     */
    public SmsTemplateResponse getTemplateBy(Integer templateId, Map<String, String> smsRequestParas);


}
