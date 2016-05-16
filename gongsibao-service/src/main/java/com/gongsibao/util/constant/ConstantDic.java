package com.gongsibao.util.constant;

import com.gongsibao.module.sys.cms.entity.CmsBottomCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wk on 2016/4/7.
 */
public class ConstantDic {

    public final static List<CmsBottomCategory> BOTTOM_CATEGORY = new ArrayList<CmsBottomCategory>() {{
        add(new CmsBottomCategory(1, "公司宝"));
        add(new CmsBottomCategory(2, "订单服务"));
        add(new CmsBottomCategory(3, "特色服务"));
        add(new CmsBottomCategory(4, "售后服务"));
        add(new CmsBottomCategory(5, "热门产品"));
    }};

    public static final Map<Integer, CmsBottomCategory> BOTTOM_CATEGORY_MAP = new HashMap<>();

    static {
        initMap();
    }

    private static synchronized void initMap() {
        BOTTOM_CATEGORY_MAP.clear();
        for (CmsBottomCategory category : BOTTOM_CATEGORY) {
            BOTTOM_CATEGORY_MAP.put(category.getPkid(), category);
        }
    }
}
