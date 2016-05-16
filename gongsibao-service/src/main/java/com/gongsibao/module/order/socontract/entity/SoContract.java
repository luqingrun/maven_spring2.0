package com.gongsibao.module.order.socontract.entity;

import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;

import java.util.Date;
import java.util.List;


public class SoContract extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    
    /** 订单序号 */
    private Integer orderId;
    
    /** 签约日期 */
    private Date sginingTime;
    
    /** 签单公司，type=316，3161汉唐信通（北京）咨询股份有限公司、3162汉唐信通（北京）科技有限公司 */
    private Integer sginingCompanyId;

    /** 签单公司  加密 */
    private String sginingCompanyIdStr;
    
    /** 是否加急 */
    private Integer isUrgeney;
    
    /** 签单业务员序号 */
    private Integer sginingUserId;
    
    /** 客户序号 */
    private Integer customerId;

    /** 是否分期 */
    private Integer isInstallment;

    /** 分期形式 存储每期金额 */
    private String installmentMode;

    /** 实际合同额 */
    private Integer realAmount;
    
    /** 是否有材料撰写情况 */
    private Integer hasDataFee;
    
    /** 材料撰写次数类型序号，type=317，3171无、3172首期一次、3173末期一次、3174首期一次末期一次 */
    private Integer dataFeeCountTypeId;

    /** 材料撰写次数类型序号(加密) */
    private String dataFeeCountTypeIdStr;
    
    /** 是否有违约金 */
    private Integer hasLiquidatedDamages;
    
    /** 是否有违约责任事项 */
    private Integer hasBreach;
    
    /** 违约金额 */
    private Integer liquidatedDamages;
    
    /** 违约责任 */
    private String breachInfo;
    
    /** 附件序号 */
    private Integer fileId;
    
    /** 审核状态序号，type=105，1051待审核、1052通过、1053不通过 */
    private Integer auditStatusId;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;

    /** 订单类 */
    private SoOrder soOrder;

    private List<SoOrderProd> prodList;

    /**
     * prodItem类
     */
    private List<OrderProdList> orderProdLists;
    
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public Date getSginingTime() {
        return sginingTime;
    }

    public void setSginingTime(Date sginingTime) {
        this.sginingTime = sginingTime;
    }
    
    public Integer getSginingCompanyId() {
        return sginingCompanyId;
    }

    public void setSginingCompanyId(Integer sginingCompanyId) {
        this.sginingCompanyId = sginingCompanyId;
    }
    
    public Integer getIsUrgeney() {
        return isUrgeney;
    }

    public void setIsUrgeney(Integer isUrgeney) {
        this.isUrgeney = isUrgeney;
    }
    
    public Integer getSginingUserId() {
        return sginingUserId;
    }

    public void setSginingUserId(Integer sginingUserId) {
        this.sginingUserId = sginingUserId;
    }
    
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public Integer getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Integer realAmount) {
        this.realAmount = realAmount;
    }
    
    public Integer getHasDataFee() {
        return hasDataFee;
    }

    public void setHasDataFee(Integer hasDataFee) {
        this.hasDataFee = hasDataFee;
    }
    
    public Integer getDataFeeCountTypeId() {
        return dataFeeCountTypeId;
    }

    public void setDataFeeCountTypeId(Integer dataFeeCountTypeId) {
        this.dataFeeCountTypeId = dataFeeCountTypeId;
    }
    
    public Integer getHasLiquidatedDamages() {
        return hasLiquidatedDamages;
    }

    public void setHasLiquidatedDamages(Integer hasLiquidatedDamages) {
        this.hasLiquidatedDamages = hasLiquidatedDamages;
    }
    
    public Integer getHasBreach() {
        return hasBreach;
    }

    public void setHasBreach(Integer hasBreach) {
        this.hasBreach = hasBreach;
    }
    
    public Integer getLiquidatedDamages() {
        return liquidatedDamages;
    }

    public void setLiquidatedDamages(Integer liquidatedDamages) {
        this.liquidatedDamages = liquidatedDamages;
    }
    
    public String getBreachInfo() {
        return breachInfo;
    }

    public void setBreachInfo(String breachInfo) {
        this.breachInfo = breachInfo;
    }
    
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }
    
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    public Integer getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(Integer addUserId) {
        this.addUserId = addUserId;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SoOrderProd> getProdList() {
        return prodList;
    }

    public void setProdList(List<SoOrderProd> prodList) {
        this.prodList = prodList;
    }

    public String getSginingCompanyIdStr() {
        return sginingCompanyIdStr;
    }

    public void setSginingCompanyIdStr(String sginingCompanyIdStr) {
        this.sginingCompanyIdStr = sginingCompanyIdStr;
    }

    public String getDataFeeCountTypeIdStr() {
        return dataFeeCountTypeIdStr;
    }

    public void setDataFeeCountTypeIdStr(String dataFeeCountTypeIdStr) {
        this.dataFeeCountTypeIdStr = dataFeeCountTypeIdStr;
    }

    public Integer getIsInstallment() {
        return isInstallment;
    }

    public void setIsInstallment(Integer isInstallment) {
        this.isInstallment = isInstallment;
    }

    public String getInstallmentMode() {
        return installmentMode;
    }

    public void setInstallmentMode(String installmentMode) {
        this.installmentMode = installmentMode;
    }

    public SoOrder getSoOrder() {
        return soOrder;
    }

    public void setSoOrder(SoOrder soOrder) {
        this.soOrder = soOrder;
    }

    public List<OrderProdList> getOrderProdLists() {
        return orderProdLists;
    }

    public void setOrderProdLists(List<OrderProdList> orderProdLists) {
        this.orderProdLists = orderProdLists;
    }
}