package com.gongsibao.common.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesReader {
    static Map<String, ResourceBundle> resMap = new ConcurrentHashMap<String, ResourceBundle>();

    /**
     * 取得指定properties文件的指定key的value
     *
     * @param file_name properties文件的名字（没有扩展名）
     * @param key       所指定的key
     * @return 指定key对应的value值
     * @throws MissingResourceException 当没有这个properties文件，或该文件中不存在这个key时
     */
    public static String getValue(String file_name, String key) throws MissingResourceException {
        if (!resMap.containsKey(file_name)) {
            synchronized (resMap) {
                ResourceBundle res = ResourceBundle.getBundle(file_name);
                resMap.put(file_name, res);
            }
        }

        return StringUtils.trimToEmpty(resMap.get(file_name).getString(key));
    }

    /**
     * 将文件中配置信息填充到properties对象中
     *
     * @param file_name
     * @return
     * @see
     */
    public static Properties fillProperties(String file_name) throws MissingResourceException {
        Properties properties = new Properties();
        final ResourceBundle res = ResourceBundle.getBundle(file_name);
        Enumeration<String> en = res.getKeys();
        String key = null;
        String value = null;
        while (en.hasMoreElements()) {
            key = en.nextElement().trim();
            value = res.getString(key);
            properties.setProperty(key, value.trim());
        }
        return properties;
    }

}
