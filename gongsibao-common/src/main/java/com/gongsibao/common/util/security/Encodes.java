package com.gongsibao.common.util.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.gongsibao.common.util.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

public class Encodes {

    private static final String CHAR_UTF8 = "UTF-8";

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();
    private static final byte[] SPECIAL_ACSII = new byte[]{0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,0x7E};

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
     */
    public static String encodeUrlSafeBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Base62编码。
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[(input[i] & 0xFF) % BASE62.length];
        }
        return new String(chars);
    }

    /**
     * Html 转码.
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html 解码.
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Xml 转码.
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * Xml 解码.
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
    	if(StringUtils.isBlank(part)){
    		return "";
    	}else{
	        try {
	            return URLEncoder.encode(part.trim(), CHAR_UTF8);
	        } catch (UnsupportedEncodingException e) {
                return "";
            }
    	}
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(String part) {
        if(StringUtils.isBlank(part)){
            return "";
        }

        try {
            return StringUtils.trimToEmpty(URLDecoder.decode(part.trim(), CHAR_UTF8));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String escapeSpecialACSII(String inStr){
        if(StringUtils.isNotEmpty(inStr)){
            StringBuilder sb = new StringBuilder();
            for(char c : inStr.toCharArray()){
                if(c <= 0xFF){
                    boolean isSpecialChar = false;
                    for(byte special : SPECIAL_ACSII){
                        if(c == special){
                            isSpecialChar = true;
                            break;
                        }
                    }
                    if(isSpecialChar){
                        sb.append(" ");
                    }else{
                        sb.append(c);
                    }
                }else{
                    sb.append(c);
                }
            }
            return sb.toString();
        }else{
            return inStr;
        }
    }
}
