package com.gongsibao.module.order.soorder.entity;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.util.AuditStatusUtils;
import com.gongsibao.util.AuditTypeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gongsibao.module.sys.bddict.entity.BdDict;

/**
 * Created by duan on 04-21.
 */
public class OrderList extends com.gongsibao.common.db.BaseEntity {
    private static final long serialVersionUID = -1L;
    /**
     * 订单类型，1订单，2合同
     */
    private Integer type;

    private String typeName;

    /**
     * 编号
     */
    private String no;

    /**
     * 会员序号
     */
    private Integer accountId;

    /**
     * 付款状态序号，type=301
     */
    private Integer payStatusId;
    /***
     * 付款状态名称
     */
    private String payStatusName;

    /**
     * 处理状态序号，type=302
     */
    private Integer processStatusId;

    /***
     * 处理状态名称
     */
    private String processStatusName;

    /**
     * 退款状态序号，type=303
     */
    private Integer refundStatusId;

    /**
     * 退款状态名称
     */
    private String refundStatusName;

    /**
     * 总价，原价
     */
    private Integer totalPrice;

    /**
     * 应支付价格
     */
    private Integer payablePrice;

    /***
     * 已支付价格
     */
    private Integer paidPrice;

    /**
     * 来源类型序号，type=304
     */
    private Integer sourceTypeId;

    private String sourceTypeName;

    /**
     * 是否分期付款，默认否
     */
    private Integer isInstallment;

    private String isInstallmentName;
    /**
     * 分期形式，存储每期金额
     */
    private String installmentMode;

    /**
     * 分期审核状态，type=105
     */
    private Integer installmentAuditStatusId;

    /***
     * 分期审核状态名称
     */
    private String installmentAuditStatusName;

    /**
     * 是否改价订单，默认否
     */
    private Integer isChangePrice;

    /**
     * 改价审核状态，type=105
     */
    private Integer changePriceAuditStatusId;

    /***
     * 改价审核状态名称
     */
    private String changePriceAuditStatusName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 是否套餐，默认否
     */
    private Integer isPackage;

    /***
     * 是否套餐名称
     */
    private String isPackageName;

    /**
     * 套餐序号
     */
    private Integer packageId;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 添加人序号
     */
    private Integer addUserId;
    /***
     * 下单人姓名
     */
    private String accountName;

    /***
     * 下单人电话
     */
    private String accountMoblie;

    /***
     * 是否开发票
     *
     * @return
     */
    private Integer isInvoice;

    private String isInvoiceName;
    /***
     * 产品名称
     */
    private String prodName;

    /***
     * 业务员姓名
     */
    private String businessName;

    /***
     * 订单状态
     */
    private Integer state;

    /***
     * 订单状态描述
     */
    private String stateName;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getPayStatusId() {
        return payStatusId;
    }

    public void setPayStatusId(Integer payStatusId) {
        this.payStatusId = payStatusId;
    }

    public Integer getProcessStatusId() {
        return processStatusId;
    }

    public void setProcessStatusId(Integer processStatusId) {
        this.processStatusId = processStatusId;
    }

    public Integer getRefundStatusId() {
        return refundStatusId;
    }

    public void setRefundStatusId(Integer refundStatusId) {
        this.refundStatusId = refundStatusId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getPayablePrice() {
        return payablePrice;
    }

    public void setPayablePrice(Integer payablePrice) {
        this.payablePrice = payablePrice;
    }

    public Integer getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(Integer paidPrice) {
        this.paidPrice = paidPrice;
    }

    public Integer getSourceTypeId() {
        return sourceTypeId;
    }

    public void setSourceTypeId(Integer sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
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


    // 分期次数
    public Integer getInstallmentNum() {
        return getInstallmentList().size();
    }

    // 分期金额列表
    public List<Integer> getInstallmentList() {
        List<Integer> list = new ArrayList<>();
        if (null == isInstallment || isInstallment == 0) {
            return list; // 默认1期
        }

        String[] arr = StringUtils.trimToEmpty(installmentMode).split("\\|");
        for (String str : arr) {
            int money = NumberUtils.toInt(str);

            if (money > 0) {
                list.add(money);
            }
        }
        return list;
    }

    public Integer getInstallmentAuditStatusId() {
        return installmentAuditStatusId;
    }

    public void setInstallmentAuditStatusId(Integer installmentAuditStatusId) {
        this.installmentAuditStatusId = installmentAuditStatusId;
    }

    public Integer getIsChangePrice() {
        return isChangePrice;
    }

    public void setIsChangePrice(Integer isChangePrice) {
        this.isChangePrice = isChangePrice;
    }

    public Integer getChangePriceAuditStatusId() {
        return changePriceAuditStatusId;
    }

    public void setChangePriceAuditStatusId(Integer changePriceAuditStatusId) {
        this.changePriceAuditStatusId = changePriceAuditStatusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(Integer isPackage) {
        this.isPackage = isPackage;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
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


    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountMoblie() {
        return accountMoblie;
    }

    public void setAccountMoblie(String accountMoblie) {
        this.accountMoblie = accountMoblie;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateName() {
        if (null == state) {
            return "";
        }

        switch (state) {
            case 1:
                return "等待付款";
            case 2:
                return "已付全款";
            case 3:
                return "已付部分款";
            case 4:
                return "办理完成";
            case 5:
                return "失效订单";
            default:
                return "无效状态";
        }
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRefundStatusName() {
        return AuditTypeUtils.getName(refundStatusId);
    }

    public void setRefundStatusName(String refundStatusName) {
        this.refundStatusName = refundStatusName;
    }

    public String getTypeName() {
        switch (type) {
            case 1:
                return "订单";
            case 2:
                return "合同";
            default:
                return "";
        }
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsInstallmentName() {
        if (isInstallment == 1) {
            return "是";
        } else {
            return "否";
        }
    }

    public void setIsInstallmentName(String isInstallmentName) {
        this.isInstallmentName = isInstallmentName;
    }

    public String getIsInvoiceName() {
        if (isInvoice == 1) {
            return "是";
        } else {
            return "否";
        }
    }

    public void setIsInvoiceName(String isInvoiceName) {
        this.isInvoiceName = isInvoiceName;
    }

    public String getSourceTypeName() {
        return AuditTypeUtils.getName(sourceTypeId);
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public String getPayStatusName() {
        return AuditTypeUtils.getName(payStatusId);
    }

    public void setPayStatusName(String payStatusName) {
        this.payStatusName = payStatusName;
    }

    public String getProcessStatusName() {
        return AuditTypeUtils.getName(processStatusId);
    }

    public void setProcessStatusName(String processStatusName) {
        this.processStatusName = processStatusName;
    }

    public String getInstallmentAuditStatusName() {
        return AuditStatusUtils.getName(installmentAuditStatusId);
    }

    public void setInstallmentAuditStatusName(String installmentAuditStatusName) {
        this.installmentAuditStatusName = installmentAuditStatusName;
    }

    public String getChangePriceAuditStatusName() {
        return AuditStatusUtils.getName(changePriceAuditStatusId);
    }

    public void setChangePriceAuditStatusName(String changePriceAuditStatusName) {
        this.changePriceAuditStatusName = changePriceAuditStatusName;
    }

    public String getIsPackageName() {
        if (isPackage == 1) {
            return "是";
        } else {
            return "否";
        }
    }

    public void setIsPackageName(String isPackageName) {
        this.isPackageName = isPackageName;
    }
}
