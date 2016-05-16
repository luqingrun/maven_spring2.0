package com.gongsibao.module.sys.bddict.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BdDict extends com.gongsibao.common.db.BaseEntity {

    private static final long serialVersionUID = -1L;

    /** 101 地区 */
    public static final Integer TYPE_101 = 101;

    /** 102 用户类型：1021系统用户、1022普通用户、1023渠道商用户、1024供应商业务员 */
    public static final Integer TYPE_102 = 102;

    /** 103 会员注册客户端类型：1031 pc、1032 h5、1033 IOS、1034 android、1035 后台 */
    public static final Integer TYPE_103 = 103;

    /** 104 审核类型：1041定价申请审核、1042订单改价申请审核、1043合同申请审核、1044发票申请审核、1045收款申请审核、1046退单申请审核、1047分期申请审核 */
    public static final Integer TYPE_104 = 104;
    public static final Integer TYPE_104_INSTALLMENT = 1047;

    public static final Integer TYPE_104_REFUND = 1046;


    /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
    public static final Integer TYPE_105 = 105;
    /** 1061 产品列表显示图 1062 产品徽章图 1063 产品轮播图 */
    public static final Integer TYPE_106 = 106;


    /** 201 产品分类 */
    public static final Integer TYPE_201 = 201;

    /** 202 产品单位:2021 次、2022 件、2023 小时、2024 月、2025 季度、2026 半年、2027 年、2028 户、2029 元、20210 人月、20211 人12月 */
    public static final Integer TYPE_202 = 202;

    /** 203 产品服务类型：2031 工本费、2032 服务费、2033 地址费 */
    public static final Integer TYPE_203 = 203;

    /** 204 产品定价审核状态：2041 待审核、2042 审核中、2043 驳回审核、2044审核通过 */
    public static final Integer TYPE_204 = 204;

    /** 205 产品销售方类型：2051 兼营、2052 自营、2053 供应商 */
    public static final Integer TYPE_205 = 205;

    /** 206 产品处理流程节点类型：2061 开始、2062 结束、2063 暂停、2064 结算 */
    public static final Integer TYPE_206 = 206;

    /** 207 产品服务特性：2071 银行、2072 企业类型、2073 科技类、2074 商贸类、2075 投资类、2076 咨询类 */
    public static final Integer TYPE_207 = 207;



    /** 301 订单付款状态：3011 待付款、3012 已付部分款（根据“是否分期”判断处理流程）、3013 已付款 */
    public static final Integer TYPE_301 = 301;

    /** 302 订单处理状态：3021 待办理、3022 正在办理、3023 已取消、3024 已完成 */
    public static final Integer TYPE_302 = 302;

    /** 303 订单退款状态：3031 待审核、3032 退款中、3033 退款完成、3034 驳回退款 */
    public static final Integer TYPE_303 = 303;

    /** 304 订单来源：3041 pc、3042 h5、3043 IOS、3044 android、3045 后台 */
    public static final Integer TYPE_304 = 304;

    /** 305 订单项处理状态 */
    public static final Integer TYPE_305 = 305;

    /** 306 订单项和用户关系类型：3061 业务、3062 客服（关注）、3063 操作 */
    public static final Integer TYPE_306 = 306;

    /** 307 开票公司：3071 汉唐信通（北京）咨询股份有限公司、3072 汉唐信通（北京）科技有限公司 */
    public static final Integer TYPE_307 = 307;

    /** 308 发票类型：3081 普通发票、3082 增值税专用发票 */
    public static final Integer TYPE_308 = 308;

    /** 309 订单优惠类型：3091 后台优惠、3092 优惠券 */
    public static final Integer TYPE_309 = 309;

    /** 310 支付付款方式：3101 在线支付、3102 线下支付、3103 内部结转 */
    public static final Integer TYPE_310 = 310;

    /** 311 线下付款方式：3111 对公转账、3112 现金、3113 刷卡、3114 个人转账 */
    public static final Integer TYPE_311 = 311;

    /** 312 支付成功状态：3121 未支付、3122 待审核、3123 成功、3124 失败 */
    public static final Integer TYPE_312 = 312;

    /** 313 退款付款方式：3131 线上、3132 线下 */
    public static final Integer TYPE_313 = 313;

    /** 314 订单项和用户关系状态：3141 正在负责、3142 曾经负责 */
    public static final Integer TYPE_314 = 314;

    /** 315 订单项记录类型：3151 更改状态、3152 备注、3153 上传材料、3154 提示客户、3155 快递、3156 帐号密码、3157 标记投诉 */
    public static final Integer TYPE_315 = 315;


    /** 父序号，默认0 */
    private Integer pid;
    
    /** 类别 */
    private Integer type;
    
    /** 名称 */
    private String name;
    
    /** 编码 */
    private Integer code;
    
    /** 排序 */
    private Double sort;
    
    /** 层级 */
    private Integer level;
    
    /** 是否启用 */
    private Integer isEnabled;
    
    /** 创建时间 */
    private Date addTime;
    
    /** 添加人序号 */
    private Integer addUserId;
    
    /** 说明 */
    private String remark;

    /** 是否叶子节点(0:不是1:是) */
    private int isLeaf = 0;

    /** 父类 */
    private BdDict parentBdDict;

    /** 子集 */
    private List<BdDict> childrenList = new ArrayList<>();

    /** 长名称 */
    private String fullName;
    

    
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    
    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }
    
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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

    public BdDict getParentBdDict() {
        return parentBdDict;
    }

    public void setParentBdDict(BdDict parentBdDict) {
        this.parentBdDict = parentBdDict;
    }

    public List<BdDict> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<BdDict> childrenList) {
        this.childrenList = childrenList;
    }

    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}