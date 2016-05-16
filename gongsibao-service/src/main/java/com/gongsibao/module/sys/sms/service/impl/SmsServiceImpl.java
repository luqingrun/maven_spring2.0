package com.gongsibao.module.sys.sms.service.impl;

import com.gongsibao.common.util.PropertiesReader;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.http.HttpClientUtil;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.sys.sms.entity.SmsRequest;
import com.gongsibao.module.sys.sms.entity.SmsResponse;
import com.gongsibao.module.sys.sms.entity.SmsTemplateResponse;
import com.gongsibao.module.sys.sms.service.SmsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huoquanfu on 2016/4/25.
 */
@Service("smsService")
public class SmsServiceImpl implements SmsService, InitializingBean {

    // Read from config(sms_api_url)
    private String smsApiUrl = "http://192.168.16.52:2102/api";

    @Override
    public SmsResponse send(Integer appId, String mobilePhone, String content) {
        SmsRequest request = new SmsRequest();
        request.setAppId(appId);
        request.setMobilePhone(mobilePhone);
        request.setContent(content);
        return send(request);
    }

    @Override
    public SmsResponse send(Integer appId, String mobilPhone, Integer templateId, Map<String, String> paras) {
        SmsRequest request = new SmsRequest();
        request.setAppId(appId);
        request.setMobilePhone(mobilPhone);
        request.setTemplateId(templateId);
        request.setTemplateParas(paras);
        return send(request);
    }

    @Override
    public SmsResponse send(SmsRequest smsRequest) {

        SmsResponse response = new SmsResponse();
        if (StringUtils.isBlank(smsRequest.getMobilePhone())) {
            response.setIsSuccessful(false);
            response.setStatusMessage("手机号不能为空。");
            return response;
        }

        if (smsRequest.getAppId() < 1) {
            response.setIsSuccessful(false);
            response.setStatusMessage("应用程序Id必须指定。");
            return response;
        }


        String contentJson = JsonUtils.objectToJson(smsRequest);
        SmsResponse smsResponse = new SmsResponse();
        String result = null;
        try {
            Map<String, String> map = getCommonHearsMap();
            String sendUrl = smsApiUrl + "/Send";
            result = HttpClientUtil.httpPost(sendUrl, map, contentJson, "utf-8");

            if (StringUtils.isNotBlank(result)) {
                smsResponse = JsonUtils.jsonToObject(result, SmsResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            smsResponse.setIsSuccessful(false);
            smsResponse.setSmsId(-1);
            smsResponse.setStatusCode(-1);
            smsResponse.setStatusMessage(e.getMessage());
        }

        return smsResponse;
    }


    @Override
    public SmsTemplateResponse getTemplateBy(Integer templateId, Map<String, String> smsRequestParas) {
        SmsTemplateResponse response = null;
        String result = null;
        try {
            Map<String, String> map = getCommonHearsMap();
            String contentJson = JsonUtils.objectToJson(smsRequestParas);

            String sendUrl = smsApiUrl + "/SmsTemplates/?templateId=" + templateId + "&paras=" +  URLEncoder.encode(contentJson);
            result = HttpClientUtil.httpGet(sendUrl, "utf-8", map);

            if (StringUtils.isNotBlank(result)) {
                response = JsonUtils.jsonToObject(result, SmsTemplateResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 生成公用的
     */
    private Map<String, String> getCommonHearsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("accept", "application/json");
        map.put("content-type", " application/json");
        return map;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        smsApiUrl = PropertiesReader.getValue("project", "sms_api_url");
    }

    /*
    public static void main(String[] args) {
        SmsService s = new SmsServiceImpl();

        //测试直接发送短信
        SmsResponse response = s.send(1, "13800138000", "Test message from java");
        System.out.println(response.getIsSuccessful());

        //测试模板发送
        Map<String,String> map=new HashMap<>();
        map.put("Test","Test Tempalte from java");
        response = s.send(1, "13800138000",1,map);
        System.out.println(response.getIsSuccessful());

        //测试获取模板信息
        Map<String, String> paras = new HashMap<>();
        paras.put("Test", "Test Tempalte from java");
        SmsTemplateResponse t = s.getTemplateBy(1, paras);
        System.out.println(t.getTemplate());

    }
    */
}
