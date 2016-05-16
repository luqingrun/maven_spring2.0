/**
 * techwolf.cn All rights reserved.
 */
package com.gongsibao.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * create time: 2014年4月16日 下午4:47:52
 * 
 * @author xiangan
 * 
 */
public class InputStreamUtil {
    static Log log = LogFactory.getLog(InputStreamUtil.class);

    public static byte[] transferInputStream2Bytes(InputStream is) {
        byte[] data = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int byteread = 0;
            while ((byteread = is.read(buffer, 0, 1024)) != -1) {
                outStream.write(buffer, 0, byteread);
            }
            outStream.flush();
            data = outStream.toByteArray();
        } catch (Exception e) {
            log.error(e);
        }
        return data;
    }

    public static InputStream transferBytes2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
