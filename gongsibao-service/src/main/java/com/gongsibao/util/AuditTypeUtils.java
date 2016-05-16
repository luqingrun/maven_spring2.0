package com.gongsibao.util;

import com.gongsibao.common.core.spring.ApplicationContextHelper;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duan on 05-05.
 */
public class AuditTypeUtils {
    private static BdDictService bdDictService = ApplicationContextHelper.getBean(BdDictService.class);
    private static Map<Integer, String> Type_MAP = new HashMap<>();

    /**
     * 获取状态名称
     *
     * @param id
     * @return
     */
    public static String getName(Integer id) {
        if (null == id) {
            return "";
        }
        String name = Type_MAP.get(id);
        if (null != name) {
            return name;
        } else {
            synchronized (AuditTypeUtils.class) {
                name = Type_MAP.get(id);
                if (null != name) {
                    return name;
                }
                BdDict bdDict = bdDictService.findById(id);
                if (null == bdDict) {
                    return "";
                }
                name = bdDict.getName();
                Type_MAP.put(id, name);
            }
            return name;
        }
    }
}
