package com.gongsibao.module.product.prodworkflowbddictmap.entity;


public class ProdWorkflowBdDictMap extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 产品流程序号 */
    private Integer workflowId;
    
    /** 地区序号，type=1 */
    private Integer cityId;

    /** 地区名称 */
    private String regionName;
    

    
    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }
    
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}