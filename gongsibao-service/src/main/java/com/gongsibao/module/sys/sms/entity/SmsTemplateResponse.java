package com.gongsibao.module.sys.sms.entity;

/**
 * Created by huoquanfu on 2016/4/26.
 */
public class SmsTemplateResponse {

    /**
     * 模板Id
     */
    private int id;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     */
    private String template;
    /**
     * 模板备注
     */
    private String remark;
    /**
     * 是否激活
     */
    private boolean isActivated;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 应用程序Id
     */
    private int appId;

    /**
     * 根据模板和参数生成的最终短信内容
     * */
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String Template) {
        this.template = Template;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String Remark) {
        this.remark = Remark;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean IsActivated) {
        this.isActivated = IsActivated;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String CreateTime) {
        this.createTime = CreateTime;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int AppId) {
        this.appId = AppId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
