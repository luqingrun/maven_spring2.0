package com.gongsibao.common.util.page;

/**
 * Created by luqingrun on 16/3/30.
 */
public class ResponseData implements java.io.Serializable{
       //状态码,
    private int code = 200;

    private String msg = "";

    private Object data = "";

    public ResponseData(){
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
