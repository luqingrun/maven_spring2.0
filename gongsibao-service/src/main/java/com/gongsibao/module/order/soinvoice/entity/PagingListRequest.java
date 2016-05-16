package com.gongsibao.module.order.soinvoice.entity;

import com.gongsibao.common.db.BaseEntity;

public class PagingListRequest extends BaseEntity {

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        if (pagesize < 0) {
            pagesize = 0;
        }
        if (pagesize > 1000) {
            pagesize = 1000;
        }

        this.pagesize = pagesize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page < 0) {
            page = 1;
        }

        this.page = page;
    }

    /**
     * SIZE
     */
    private Integer pagesize;
    /**
     * INDEX 页
     */
    private Integer page;
}
