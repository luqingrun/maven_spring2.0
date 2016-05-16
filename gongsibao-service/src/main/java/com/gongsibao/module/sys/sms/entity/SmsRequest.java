package com.gongsibao.module.sys.sms.entity;

import java.util.Map;

/**
 * Created by huoquanfu on 2016/4/25.
 */
public class SmsRequest {

    /**
     * 应用程序Id
     */
    private int appId;
    /**
     * 短信模板参数
     */
    private Map<String,String> templateParas;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 是否重试
     */
    private boolean isRetry;
    /**
     * 短信类型
     */
    private int SmsType;
    /**
     * 短信模板Id
     */
    private int templateId;

    public Map<String,String> getTemplateParas() {
        return templateParas;
    }

    public void setTemplateParas(Map<String,String> TemplateParas) {
        this.templateParas = TemplateParas;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int AppId) {
        this.appId = AppId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String MobilePhone) {
        this.mobilePhone = MobilePhone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String Content) {
        this.content = Content;
    }

    public boolean getIsRetry() {
        return isRetry;
    }

    public void setIsRetry(boolean IsRetry) {
        this.isRetry = IsRetry;
    }

    public int getSmsType() {
        return SmsType;
    }

    public void setSmsType(int SmsType) {
        this.SmsType = SmsType;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int TemplateId) {
        this.templateId = TemplateId;
    }

    public SmsRequest()
    {
        this.setIsRetry(false);
        this.setTemplateId(0);
    }

}
