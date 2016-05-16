package com.gongsibao.module.product.prodworkflownode.entity;

import java.util.Date;


public class ProdWorkflowNode extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 处理流程序号 */
    private Integer workflowId;
    
    /** 节点类型序号，type=9 */
    private Integer typeId;
    
    /** 名称 */
    private String name;
    
    /** 排序 */
    private Double sort;
    
    /** 办理工作日天数 */
    private Integer weekdayCount;
    

    
    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }
    
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
    
    public Integer getWeekdayCount() {
        return weekdayCount;
    }

    public void setWeekdayCount(Integer weekdayCount) {
        this.weekdayCount = weekdayCount;
    }
    

}