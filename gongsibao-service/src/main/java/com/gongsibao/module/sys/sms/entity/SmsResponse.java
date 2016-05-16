package com.gongsibao.module.sys.sms.entity;

/**
 * Created by huoquanfu on 2016/4/25.
 */
public class SmsResponse {
    /**
     * 发送是否成功
     */
    private boolean isSuccessful;
    /**
     * 短信发送Id
     */
    private int smsId;
    /**
     * 状态码
     */
    private int statusCode;
    /**
     * 状态信息
     */
    private String statusMessage;

    public boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(boolean IsSuccessful) {
        this.isSuccessful = IsSuccessful;
    }

    public int getSmsId() {
        return smsId;
    }

    public void setSmsId(int SmsId) {
        this.smsId = SmsId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.statusCode = StatusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String StatusMessage) {
        this.statusMessage = StatusMessage;
    }
}
