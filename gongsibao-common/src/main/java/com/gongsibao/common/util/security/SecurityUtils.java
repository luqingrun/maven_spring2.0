package com.gongsibao.common.util.security;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import org.apache.commons.lang.ArrayUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SecurityUtils {
    //    private static Logger logger = Logger.getLogger(SecurityUtils.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String RC4_DEFAULT_PASSWORD = "3984aF333#@213";

//    /**
//     * 根据MD5加密的ucid的后8位为密钥生成密文
//     *
//     * @param sourceText
//     * @param securtKey
//     * @return
//     */
//    public static String encryptValue(String sourceText, String secretKey) {
//        if (StringUtils.isEmpty(sourceText) || StringUtils.isEmpty(secretKey)) {
//            return "";
//        }
//
//        BasicTextEncryptor bte = new BasicTextEncryptor();
//        bte.setPassword(getSecretKey(secretKey));
//
//        String encryptText = bte.encrypt(StringUtils.trim(sourceText));
//        int i = 0;
//        while (encryptText.contains("+")) {
//            if (++i > 30) {
//                break;
//            }
//            encryptText = bte.encrypt(StringUtils.trim(sourceText));
//        }
//        try {
//            encryptText = URLEncoder.encode(encryptText, DEFAULT_CHARSET);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        if(logger.isDebugEnabled()){
//            logger.debug("###sourceText:"+sourceText +"#####encryptText:"+encryptText);
//        }
//        return encryptText;
//    }
//
//    public static String decrypt(String encryptText, String secretKey) {
//        if (StringUtils.isEmpty(encryptText) || StringUtils.isEmpty(secretKey)) {
//            return "";
//        }
//        if (encryptText.equals("0") || secretKey.equals("0")) {
//            return "";
//        }
//
//        BasicTextEncryptor bte = new BasicTextEncryptor();
//        bte.setPassword(getSecretKey(secretKey));
//
//        if (encryptText.contains("%")) {
//            try {
//                encryptText = URLDecoder.decode(encryptText, DEFAULT_CHARSET);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        String decryptText = "";
//        decryptText = bte.decrypt(encryptText);
//        if(logger.isDebugEnabled()){
//            logger.debug("###encryptText:"+encryptText +"#####decryptText:"+decryptText);
//        }
//        return decryptText;
//    }

    /**
     * 取MD5加密的secretKey的后8位当做密钥
     *
     * @param sourceSecretKey
     * @return
     */
    private static String getSecretKey(String sourceSecretKey) {
        String md5secretKey = MD5Encode(sourceSecretKey);
        return md5secretKey.substring(md5secretKey.length() - 8);
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String MD5Encode(String str) {
        if (str == null) return null;

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String MD5Encode(String str, String encoding) {
        if (str == null) return null;

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(str.getBytes(encoding)));
        } catch (Exception e) {
            return null;
        }
    }

    public static String MD5Encode(InputStream input) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            byte[] b = new byte[1024 * 10];
            int length = -1;

            while ((length = input.read(b)) > -1) {
                messagedigest.update(b, 0, length);
            }
            return bufferToHex(messagedigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("InputStream md5 name error");
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        String c0 = hexDigits[(bt & 0xf0) >> 4];
        String c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final char[] c = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz".toCharArray();

    /**
     * 获得指定长度的随机字符串
     *
     * @param len
     * @return
     */
    public static String getRandomString(int len) {
        if (len < 1) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(c[random.nextInt(c.length)]);
        }
        return sb.toString();
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 加密
     * 加密字符串和原字符串长度基本相同
     * 主要针对页面ID的加密
     *
     * @param source
     * @return
     */
    public static String rc4Encrypt(Object source) {
        String encrypt = "";
        try {
            encrypt = Base64.encode(RC4.RC4encrypt(RC4_DEFAULT_PASSWORD.getBytes(DEFAULT_CHARSET),
                    String.valueOf(source).getBytes(DEFAULT_CHARSET)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encrypt.replaceAll("/", "_").replaceAll("\\+", "-").replaceAll("=", "~");
    }

    /**
     * 解密
     * 加密字符串和原字符串长度基本相同
     * 主要针对页面ID的加密
     *
     * @return
     */
    public static String rc4Decrypt(String encryptStr) {
        encryptStr = StringUtils.trimToEmpty(encryptStr);
        encryptStr = encryptStr.replaceAll("_", "/").replaceAll("-", "+").replaceAll("\\~", "=");
        String decrypt = "";
        try {
            decrypt = new String(RC4.RC4encrypt(RC4_DEFAULT_PASSWORD.getBytes(DEFAULT_CHARSET),
                    Base64.decode(encryptStr)), DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decrypt;
    }

    public static List<Integer> rc4DecryptBatch(String[] idStrs) {
        List<Integer> list = new ArrayList<>();
        if (ArrayUtils.isEmpty(idStrs)) {
            return list;
        }

        for (String idStr : idStrs) {
            int id = NumberUtils.toInt(rc4Decrypt(idStr));
            if (id > 0) {
                list.add(id);
            }
        }

        return list;
    }

    public static void main(String[] args) {
        System.out.println(SecurityUtils.rc4Encrypt(101120101));
//        System.out.println(SecurityUtils.rc4Encrypt("22001@wangkun@gongsibao.com"));
    }
}
