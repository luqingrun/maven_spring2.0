package com.gongsibao.common.util.http;

import java.io.Serializable;

public class HttpResponseModel implements Serializable {

    private static final long serialVersionUID = -5566118482725748353L;

    public byte[] bytes;

    public String contentType;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
