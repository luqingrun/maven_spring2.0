package com.gongsibao.util;

import com.gongsibao.common.core.spring.ApplicationContextHelper;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wk on 2016/4/28.
 */
public class AuditStatusUtils {

    private static  BdDictService bdDictService = ApplicationContextHelper.getBean(BdDictService.class);
    private static Map<Integer, BdDict> STATUS_MAP = new HashMap<>();

    static {
        initSTATUS_MAP();
    }
    /**
     * 待审核
     */
    public static final int TO_AUDIT = 1051;
    /**
     * 审核中
     */
    public static final int AUDITING = 1052;
    /**
     * 驳回
     */
    public static final int AUDIT_REJECT = 1053;
    /**
     * 通过
     */
    public static final int AUDIT_PASS = 1054;
    /**
     * 排队
     */
    public static final int AUDIT_WAITING = 1055;
    /**
     * 关闭
     */
    public static final int AUDIT_CLOSE = 1056;

    public static void initSTATUS_MAP() {
        if (MapUtils.isEmpty(STATUS_MAP)) {
            List<BdDict> statusList = bdDictService.findByType(105);
            for (BdDict bdDict : statusList) {
                STATUS_MAP.put(bdDict.getPkid(), bdDict);
            }
        }
    }

    /**
     * 获取状态名称
     * @param id
     * @return
     */
    public static String getName(Integer id) {
        if (null == id) {
            return "";
        }
        BdDict bdDict = STATUS_MAP.get(id);
        if (null == bdDict) {
            return "";
        }
        return bdDict.getName();
    }

}
