package com.gongsibao.module.order.soorderprod.entity;

import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import org.springframework.util.CollectionUtils;

import java.util.List;


public class SoOrderProd extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;


    /**
     * 编号
     */
    private String no;

    /**
     * 订单序号
     */
    private Integer orderId;

    /**
     * 产品序号
     */
    private Integer productId;

    /**
     * 产品序号(加密)
     */
    private String productIdStr;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 城市序号(加密)
     */
    private Integer cityId;

    /**
     * 城市序号
     */
    private String cityIdStr;

    /**
     * 处理状态序号，type=305
     */
    private Integer processStatusId;

    /**
     * 处理状态名称，type=305
     */
    private String processStatusName;

    /**
     * 审核状态序号，type=105
     */
    private Integer auditStatusId;

    /**
     * 总价
     */
    private Integer price;

    /**
     * 原价
     */
    private Integer priceOriginal;

    /**
     * 差额
     */
    private Integer margin;

    /**
     * 是否退单
     */
    private Integer isRefund;

    /**
     * 业务员
     */
    private UcUser businessUser;

    /**
     * 已办理天数
     */
    private Integer processedDays;

    /**
     * 是否投诉
     */
    private Integer isComplaint;

    /**
     * 需要天数
     */
    private Integer needDays;

    /**
     * 超时天数
     */
    private Integer timeoutDays;

    private List<SoOrderProdItem> itemList;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 产品关联的服务项
     */
    private List<ProdService> prodServiceList;

    /**
     * 产品订单关联跟进记录上传材料
     */
    private List<SoOrderProdTraceFile> orderProdTraceFileList;

    /**
     * 产品订单关联审核日志
     */
    private List<BdAuditLog> auditLogList;

    private List<Integer> orderProdItemIdList;

    /**
     * 业务员名称字符串
     */
    private String salesmanNames;


    public List<SoOrderProdItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SoOrderProdItem> itemList) {
        this.itemList = itemList;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public Integer getAuditStatusId() {
        return auditStatusId;
    }

    public void setAuditStatusId(Integer auditStatusId) {
        this.auditStatusId = auditStatusId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(Integer priceOriginal) {
        this.priceOriginal = priceOriginal;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<ProdService> getProdServiceList() {
        return prodServiceList;
    }

    public void setProdServiceList(List<ProdService> prodServiceList) {
        this.prodServiceList = prodServiceList;
    }

    public int getOrderProdTraceFileListSize() {
        if (CollectionUtils.isEmpty(getOrderProdTraceFileList())) {
            return 0;
        }
        return getOrderProdTraceFileList().size();
    }

    public List<SoOrderProdTraceFile> getOrderProdTraceFileList() {
        return orderProdTraceFileList;
    }

    public void setOrderProdTraceFileList(List<SoOrderProdTraceFile> orderProdTraceFileList) {
        this.orderProdTraceFileList = orderProdTraceFileList;
    }

    public List<Integer> getOrderProdItemIdList() {
        return orderProdItemIdList;
    }

    public void setOrderProdItemIdList(List<Integer> orderProdItemIdList) {
        this.orderProdItemIdList = orderProdItemIdList;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getProcessedDays() {
        return processedDays;
    }

    public void setProcessedDays(Integer processedDays) {
        this.processedDays = processedDays;
    }


    public String getProductIdStr() {
        return productIdStr;
    }

    public void setProductIdStr(String productIdStr) {
        this.productIdStr = productIdStr;
    }

    public String getCityIdStr() {
        return cityIdStr;
    }

    public void setCityIdStr(String cityIdStr) {
        this.cityIdStr = cityIdStr;
    }

    public UcUser getBusinessUser() {
        return businessUser;
    }

    public void setBusinessUser(UcUser businessUser) {
        this.businessUser = businessUser;
    }

    public List<BdAuditLog> getAuditLogList() {
        return auditLogList;
    }

    public void setAuditLogList(List<BdAuditLog> auditLogList) {
        this.auditLogList = auditLogList;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
        this.margin = margin;
    }

    public String getSalesmanNames() {
        return salesmanNames;
    }

    public void setSalesmanNames(String salesmanNames) {
        this.salesmanNames = salesmanNames;
    }

    public Integer getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(Integer isComplaint) {
        this.isComplaint = isComplaint;
    }

    public Integer getNeedDays() {
        return needDays;
    }

    public void setNeedDays(Integer needDays) {
        this.needDays = needDays;
    }

    public Integer getTimeoutDays() {
        return timeoutDays;
    }

    public void setTimeoutDays(Integer timeoutDays) {
        this.timeoutDays = timeoutDays;
    }

    public String getProcessStatusName() {
        return processStatusName;
    }

    public void setProcessStatusName(String processStatusName) {
        this.processStatusName = processStatusName;
    }
}