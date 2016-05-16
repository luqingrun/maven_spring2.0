package com.gongsibao.module.product.prodworkflowfile.entity;

import java.util.Date;


public class ProdWorkflowFile extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品处理流程序号 */
    private Integer prodWorkflowId;
    
    /** 材料名称 */
    private String name;
    
    /** 是否必须提供 */
    private Integer isMust;
    
    /** 排序 */
    private Double sort;
    

    
    public Integer getProdWorkflowId() {
        return prodWorkflowId;
    }

    public void setProdWorkflowId(Integer prodWorkflowId) {
        this.prodWorkflowId = prodWorkflowId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getIsMust() {
        return isMust;
    }

    public void setIsMust(Integer isMust) {
        this.isMust = isMust;
    }
    
    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
    

}