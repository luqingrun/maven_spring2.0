package com.gongsibao.module.sys.cms.entity;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.util.constant.ConstantDic;

public class CmsBottomnav extends CMSBase {

    private static final long serialVersionUID = -1L;

    /** 底部导航分类id */
    private Integer bottomCategory;
    private String bottomCategoryName;

    /** 链接地址 */
    private String url;
    
    /** 图片url */
    private String img;
    
    /** 屏蔽百度蜘蛛 0否, 1是 */
    private Integer spider;
    
    /** 排序 */
    private Integer sort;

    public Integer getBottomCategory() {
        return bottomCategory;
    }

    public String getBottomCategoryName() {
        if (null != bottomCategory) {
            CmsBottomCategory category = ConstantDic.BOTTOM_CATEGORY_MAP.get(bottomCategory);
            if (null != category) {
                bottomCategoryName = category.getName();
            }
        }
        return StringUtils.trimToEmpty(bottomCategoryName);
    }

    public void setBottomCategory(Integer bottomCategory) {
        this.bottomCategory = bottomCategory;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    
    public Integer getSpider() {
        return spider;
    }

    public void setSpider(Integer spider) {
        this.spider = spider;
    }
    
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
}