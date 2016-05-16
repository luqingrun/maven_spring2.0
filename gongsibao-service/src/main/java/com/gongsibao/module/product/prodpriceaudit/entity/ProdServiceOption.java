package com.gongsibao.module.product.prodpriceaudit.entity;

import com.gongsibao.common.db.BaseEntity;

/**
 * Created by huoquanfu on 2016/4/20.
 */
public class ProdServiceOption extends BaseEntity {

    private String name;

    public boolean getIsMust() {
        return isMust;
    }

    public void setIsMust(boolean must) {
        isMust = must;
    }

    private boolean isMust;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
