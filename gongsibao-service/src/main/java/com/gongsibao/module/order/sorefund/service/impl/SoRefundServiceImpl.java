package com.gongsibao.module.order.sorefund.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderproditem.service.SoOrderProdItemService;
import com.gongsibao.module.order.sorefund.dao.SoRefundDao;
import com.gongsibao.module.order.sorefund.entity.SoRefund;
import com.gongsibao.module.order.sorefund.service.SoRefundService;
import com.gongsibao.module.order.sorefunditem.entity.SoRefundItem;
import com.gongsibao.module.order.sorefunditem.service.SoRefundItemService;
import com.gongsibao.module.order.sorefunditemprice.entity.SoRefundItemPrice;
import com.gongsibao.module.order.sorefunditemprice.service.SoRefundItemPriceService;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.uc.ucrole.entity.RoleTag;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soRefundService")
public class SoRefundServiceImpl implements SoRefundService {
    @Autowired
    private SoRefundDao soRefundDao;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Autowired
    private UcUserService ucUserService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private SoOrderProdItemService soOrderProdItemService;

    @Autowired
    private SoRefundItemService soRefundItemService;

    @Autowired
    private SoRefundItemPriceService soRefundItemPriceService;

    @Autowired
    private ProdProductService prodProductService;

    @Autowired
    private BdDictService bdDictService;

    @Override
    public SoRefund findById(Integer pkid) {
        return soRefundDao.findById(pkid);
    }

    @Override
    public int update(SoRefund soRefund) {
        return soRefundDao.update(soRefund);
    }

    @Override
    public int delete(Integer pkid) {
        return soRefundDao.delete(pkid);
    }

    @Override
    public Integer insert(SoRefund soRefund) {
        Integer pkid = soRefundDao.insert(soRefund);
        soRefundDao.updateNo(pkid, pkid, soRefund.getAddUserId());
        return pkid;
    }

    @Override
    public List<SoRefund> findByIds(List<Integer> pkidList) {
        return soRefundDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoRefund> findMapByIds(List<Integer> pkidList) {
        List<SoRefund> list = findByIds(pkidList);
        Map<Integer, SoRefund> map = new HashMap<>();
        for (SoRefund soRefund : list) {
            map.put(soRefund.getPkid(), soRefund);
        }
        return map;
    }

    @Override
    public Pager<SoRefund> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int totalRows = soRefundDao.countByProperties(map);
        Pager<SoRefund> pager = new Pager<>(totalRows, page, pageSize);
        List<SoRefund> soRefundList = soRefundDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soRefundList);
        return pager;
    }

    @Override
    public Pager<SoRefund> pageRefundAuditByProperties(Map<String, Object> map, int page, int pageSize) {
        int totalRows = soRefundDao.countRefundAuditByProperties(map);
        if (totalRows == 0) {
            return new Pager<SoRefund>(0, 0);
        }
        Pager<SoRefund> pager = new Pager<>(totalRows, page, pageSize);
        List<SoRefund> refundAuditList = soRefundDao.findRefundAuditByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(refundAuditList);
        return pager;
    }

    /**
     * 得到退单查看列表List
     *
     * @param map
     * @return Pager<SoOrder>
     */
    @Override
    public List<SoOrder> getRefundViewList(Integer typeId, Integer statusId, Integer addUserId, Map<String, Object> map) {

        List<BdAuditLog> list = soRefundDao.findRefundViewListByIds(typeId, statusId, addUserId);
        List<Integer> formIdList = getFormIdList(list);
        List<SoOrder> ordList = soRefundDao.getRefundList(formIdList, map);
        return ordList;
    }

    /**
     * 退单查看模块：得到审核日志表FormId列表
     *
     * @param list
     * @return getFormIdList
     */
    public List<Integer> getFormIdList(List<BdAuditLog> list) {
        List<Integer> formIdList = new ArrayList<>();
        for (BdAuditLog bdAuditLog : list) {
            formIdList.add(bdAuditLog.getFormId());
        }
        return formIdList;
    }


    /**
     * 退单查看模块：退单查看列表
     *
     * @param pkidList
     * @return List<SoRefund>
     */
    @Override
    public List<SoRefund> findListByIds(List<Integer> pkidList) {
        return soRefundDao.findListByIds(pkidList);
    }


    /**
     * 退单明细模块：展示列表
     *
     * @param orderId
     * @return List<SoRefund>
     */
    @Override
    public List<SoRefund> findDetailsListByIds(Integer orderId) {
        List<SoRefund> list = soRefundDao.findDetailsList(orderId);
        return list;
    }


    /**
     * 退单申请模块：提交退单信息
     *
     * @param map
     * @return Integer
     */
    @Override
    public Integer insertRefundApply(Map<String, Object> map, Integer currentUserId) {
        Integer orderId = NumberUtils.toInt(StringUtils.trimToEmpty((String) map.get("pkidStr")), -1);          //订单ID
        Integer wayTypeId = NumberUtils.toInt(StringUtils.trimToEmpty((String) map.get("wayTypeId")), -1);      //退款方式
        Integer isFullRefund = NumberUtils.toInt(StringUtils.trimToEmpty((String) map.get("isFullRefund")), -1);//退款类别
        String payerName = StringUtils.trimToEmpty((String) map.get("payerName"));                              //退款账户名称
        String bankNo = StringUtils.trimToEmpty((String) map.get("bankNo"));                                    //退款账号
        Integer amount = NumberUtils.toInt(StringUtils.trimToEmpty((String) map.get("amount")), -1);            //退款总额
        Integer cost = NumberUtils.toInt(StringUtils.trimToEmpty((String) map.get("cost")), -1);                //支出成本总额
        String remark = StringUtils.trimToEmpty((String) map.get("remark"));                                    //补充说明
        //1、组装soRefund对象（插入）
        SoRefund soRefund = new SoRefund();
        soRefund.setWayTypeId(wayTypeId);
        soRefund.setIsFullRefund(isFullRefund);
        soRefund.setPayerName(payerName);
        soRefund.setBankNo(bankNo);
        soRefund.setAmount(amount);
        soRefund.setCost(cost);
        soRefund.setRemark(remark);
        soRefund.setOrderId(orderId);     //订单id
        soRefund.setAddUserId(111);       //添加人序号
        soRefund.setAuditStatusId(1051);  //审核状态
        Integer refundId = insert(soRefund);
        //2、组装soRefundItem对象（批量插入）、组装soRefundItemPrice对象（批量插入）
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        if (!CollectionUtils.isEmpty(list)) {
            List<SoRefundItem> soRefundItemList = new ArrayList<>();
            List<SoRefundItemPrice> soRefundItemPriceList = new ArrayList<>();
            List<SoOrderProd> soOrderProdList = new ArrayList<>();
            SoRefundItem soRefundItem = null;
            SoRefundItemPrice soRefundItemPrice = null;
            for (Map<String, Object> prodItemMap : list) {
                int orderProdId = (Integer) prodItemMap.get("orderProdId");//退款产品id
                int orderProdItemId = (Integer) prodItemMap.get("orderProdItemId");//退款产品项id
                int priceRefund = (Integer) prodItemMap.get("priceRefund");//退款金额
                SoOrderProd soOderProd = soOrderProdService.findById(orderProdId);//从数据库中查找退款产品记录
                if (null == soOderProd) {
                    throw new IllegalArgumentException("退款产品不存在");
                }
                soRefundItem = new SoRefundItem();
                soRefundItem.setOrderId(orderId);
                soRefundItem.setCost(cost);
                soRefundItem.setAmount(amount);
                soRefundItem.setOrderProdId(soOderProd.getPkid());
                soRefundItem.setOrderProdItemId(orderProdItemId);
                soRefundItem.setRefundId(refundId);//怎么处理？？
                soRefundItemList.add(soRefundItem);
                //从数据库中查找退款服务项记录
                SoOrderProdItem soOrderProdItem = soOrderProdItemService.findById(orderProdItemId);
                if (null == soOderProd) {
                    throw new IllegalArgumentException("退款产品服务项不存在");
                }
                //从数据库中得到服务项的单价
                int price = soOrderProdItem.getPrice();
                int priceCompare = price - priceRefund;
                //比较单价和退款金额大小
                if (priceCompare < 0) {
                    throw new IllegalArgumentException("退款金额大于单价");
                } else if (priceCompare > 0) {
                    soRefundItemPrice = new SoRefundItemPrice();
                    soRefundItemPrice.setRefundId(11);//退单id怎么处理
                    soRefundItemPrice.setAmount(amount);
                    soRefundItemPrice.setCost(cost);
                    soRefundItemPrice.setOrderProdItemId(orderProdItemId);
                    soOderProd.setIsRefund(1);
                    soRefundItemPriceList.add(soRefundItemPrice);
                    soOrderProdList.add(soOderProd);
                }
            }
            insertRefundApplyOperate(soRefundItemList, soRefundItemPriceList, soOrderProdList);
        }
        //3、组装bdAuditLog对象 （批量插入）
        List<List<BdAuditLog>> auditList = new ArrayList<>();
        genAuditList(auditList, refundId, currentUserId);
        List<BdAuditLog> logList = new ArrayList<>();
        if (CollectionUtils.isEmpty(auditList)) {
            throw new IllegalArgumentException("audit logs empty ");
        }
        for (List<BdAuditLog> bdAuditLogs : auditList) {
            logList.addAll(bdAuditLogs);
        }
        bdAuditLogService.insertBatch(logList);
        return 1;
    }


    public void insertRefundApplyOperate(List<SoRefundItem> soRefundItemList, List<SoRefundItemPrice> soRefundItemPriceList, List<SoOrderProd> soOrderProdList) {
        //批量插入退单商品记录
        soRefundItemService.insertBatch(soRefundItemList);
        //批量插入退单服务项记录
        soRefundItemPriceService.insertBatch(soRefundItemPriceList);
        //批量更新订单产品记录
        List<Integer> orderProdIds = new ArrayList<>();
        for (SoOrderProd soOrderProd : soOrderProdList) {
            orderProdIds.add(soOrderProd.getPkid());
        }
        soOrderProdService.updateRefund(orderProdIds, 1);
    }

    private void genAuditList(List<List<BdAuditLog>> list, Integer refundId, Integer currentUserId) {
        int typeId = 1046;
        SoRefund refund = findById(refundId);
        if (null == refund) {
            return;
        }
        int level = 0;
        // **************** 申请人begin ****************
        BdAuditLog addLog = new BdAuditLog();
        addLog.setAddUserId(currentUserId);
        addLog.setFormId(refundId);
        addLog.setTypeId(typeId);
        addLog.setContent("提交退单申请");
        addLog.setRemark("提交退单申请");
        addLog.setLevel(level);
        addLog.setStatusId(AuditStatusUtils.AUDIT_PASS);
        list.add(new ArrayList<BdAuditLog>() {{
            add(addLog);
        }});
        // **************** 申请人end ****************
        level++;
        // **************** 分公司渠道经理begin ****************
        // TODO 查分公司渠道经理
        List<Integer> channelManagerIds = ucUserService.getUserParentPkid(currentUserId, 0, RoleTag.ROLE_FGSQDJL);
        BdAuditLog channelManager = new BdAuditLog();
        channelManager.setAddUserId(currentUserId);
        channelManager.setFormId(refundId);
        channelManager.setTypeId(typeId);
        channelManager.setContent("分公司渠道经理");
        channelManager.setRemark("分公司渠道经理");
        channelManager.setLevel(level);
        channelManager.setStatusId(AuditStatusUtils.TO_AUDIT);

        list.add(new ArrayList<BdAuditLog>() {{
            add(channelManager);
        }});
        // **************** 分公司渠道经理 end ****************

        level++;
        // **************** 分总begin ****************
        // TODO 调用组织结构接口-查分公司总经理
        List<Integer> compManagerIdList = ucUserService.getBranchBoss(currentUserId);

        List<BdAuditLog> managerLogs = new ArrayList<>();
        for (Integer managerId : compManagerIdList) {
            BdAuditLog mLog = new BdAuditLog();
            mLog.setAddUserId(managerId);
            mLog.setFormId(refundId);
            mLog.setTypeId(typeId);
            mLog.setContent("分总审核");
            mLog.setRemark("分总审核");
            mLog.setLevel(level);
            mLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
            managerLogs.add(mLog);
        }

        list.add(managerLogs);
        // **************** 分总end ****************
        level++;

        // **************** 事业部总begin ****************
        // 查所有orderProdIds
        List<SoRefundItem> itemList = soRefundItemService.getListByRefundId(refund.getPkid());
        List<Integer> orderProdIds = new ArrayList<>();
        for (SoRefundItem refundItem : itemList) {
            orderProdIds.add(refundItem.getOrderProdId());
        }

        // 查询产品id
        List<SoOrderProd> soOrderProdList = soOrderProdService.findByIds(orderProdIds);
        List<Integer> productIds = new ArrayList<>();
        for (SoOrderProd soOrderProd : soOrderProdList) {
            productIds.add(soOrderProd.getProductId());
        }

        // 获取产品
        List<ProdProduct> productList = prodProductService.findByIds(productIds);

        // 产品大类id
        List<Integer> categoryIds = new ArrayList<>();
        for (ProdProduct prodProduct : productList) {
            BdDict bdDict = bdDictService.findTreeByParentId(201, prodProduct.getTypeId());
            if (null != bdDict) {
                categoryIds.add(bdDict.getPkid());
            }
        }

        // TODO 通过产品大类列表找事业部经理
        List<Integer> businessUserIds = ucUserService.getBusinessBoss(categoryIds);

        List<BdAuditLog> departmentLogs = new ArrayList<>();
        for (Integer departmentId : businessUserIds) {
            BdAuditLog dLog = new BdAuditLog();
            dLog.setAddUserId(departmentId);
            dLog.setFormId(refundId);
            dLog.setTypeId(typeId);
            dLog.setContent("事业部总经理审核");
            dLog.setRemark("事业部总经理审核");
            dLog.setLevel(level);
            dLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
            departmentLogs.add(dLog);
        }

        list.add(departmentLogs);
        // **************** 事业部总end ****************
        level++;

        Integer amount = refund.getAmount();
        if (amount / 100 > 10000) {
            // **************** 业务总监beign ****************
            List<Integer> businessIdList = ucUserService.getUserParentPkid(currentUserId, 0, RoleTag.ROLE_YWZJ);

            List<BdAuditLog> businessLogs = new ArrayList<>();
            for (Integer businessId : businessIdList) {
                BdAuditLog businessLog = new BdAuditLog();
                businessLog.setAddUserId(businessId);
                businessLog.setFormId(refundId);
                businessLog.setTypeId(typeId);
                businessLog.setContent("业务总监");
                businessLog.setRemark("业务总监");
                businessLog.setLevel(level);
                businessLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
                businessLogs.add(businessLog);
            }
            list.add(businessLogs);
            // **************** 业务总监end ****************
            level++;
        }

        // **************** 财务总监begin ****************
        List<Integer> financeIdList = ucUserService.getUserParentPkid(currentUserId, 0, RoleTag.ROLE_CWZJ);
        List<BdAuditLog> financeLogs = new ArrayList<>();
        for (Integer financeId : financeIdList) {
            BdAuditLog financeLog = new BdAuditLog();
            financeLog.setAddUserId(financeId);
            financeLog.setFormId(refundId);
            financeLog.setTypeId(typeId);
            financeLog.setContent("财务总监审核");
            financeLog.setRemark("财务总监审核");
            financeLog.setLevel(level);
            financeLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
            financeLogs.add(financeLog);
        }

        list.add(financeLogs);
        // **************** 财务总监end ****************

        level++;
        // **************** 收退款专员begin ****************
        List<Integer> moneyUserIdList =  ucUserService.getUserParentPkid(currentUserId, 0, RoleTag.ROLE_STKZY);

        List<BdAuditLog> moneyUserLogs = new ArrayList<>();
        for (Integer moneyUserId : moneyUserIdList) {
            BdAuditLog moneyUserLog = new BdAuditLog();
            moneyUserLog.setAddUserId(moneyUserId);
            moneyUserLog.setFormId(refundId);
            moneyUserLog.setTypeId(typeId);
            moneyUserLog.setContent("收退款专员");
            moneyUserLog.setRemark("收退款专员");
            moneyUserLog.setLevel(level);
            moneyUserLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
            moneyUserLogs.add(moneyUserLog);
        }
        list.add(moneyUserLogs);
        // **************** 收退款专员end ****************
    }

    /**
     * 退单审核通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return int
     * @throws Exception
     */
    @Override
    public int editRefundPass(Integer pkid, Integer currentUserId, String remark, Map<String, Object> map) throws Exception {
        int typeId = 1046;
        SoRefund refund = findById(pkid);
        if (refund == null) {
            // 订单不存在
            return -1;
        }

        Map<String, Object> auditParam = new HashMap<>();

        auditParam.put("form_id", pkid);
        auditParam.put("type_id", typeId);
        auditParam.put("add_user_id", currentUserId);
        auditParam.put("status_id", AuditStatusUtils.TO_AUDIT);
        // 查询当前用户 审核日志
        List<BdAuditLog> bdAuditLogs = bdAuditLogService.listByProperties(auditParam);
        if (CollectionUtils.isEmpty(bdAuditLogs)) {
            // 审核任务不存在
            return -2;
        }
        // 取一个
        BdAuditLog auditLog = bdAuditLogs.get(bdAuditLogs.size() - 1);

        Integer level = auditLog.getLevel();
        Integer nextLevel = level + 1;

        Integer logPkid = auditLog.getPkid();
        // pass当前log
        int rows = bdAuditLogService.updateStatus(logPkid, AuditStatusUtils.AUDIT_PASS, AuditStatusUtils.TO_AUDIT, remark);
        if (rows == 0) {
            throw new Exception("audit pass error");
        }
        // 当前level 审核状态为
        bdAuditLogService.updateLevelStatus(pkid, typeId, logPkid, level, AuditStatusUtils.AUDIT_CLOSE);

        // nextLevel更新为待审核
        rows = bdAuditLogService.updateLevelStatus(pkid, typeId, null, nextLevel, AuditStatusUtils.TO_AUDIT);
        if (rows == 0) {
            // 更新order状态为审核通过
            rows = soRefundDao.updateAuditStatus(pkid, AuditStatusUtils.AUDIT_PASS, AuditStatusUtils.TO_AUDIT);
            if (rows == 0) {
                throw new Exception("audit pass error");
            }
        }
        //根据传递的退单表的id和退单商品列表的id 更新so_refund和so_order_prod表的状态
        //退单id
        int soRefundId = (Integer) map.get("soRefundId");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        List<Integer> orderProdIds = new ArrayList<>();
        for (Map<String, Object> mapIds : list) {
            int orderProdId = (Integer) mapIds.get("orderProdId");
            orderProdIds.add(orderProdId);
        }
        //批量更新so_order_prod表中的audit_status_id字段为审核通过
        soOrderProdService.updateAuditRefund(orderProdIds, 1045);
        updateSoRefund(soRefundId, 1045);

        return 1;
    }

    /**
     * 退单审核不通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return int
     * @throws Exception
     */
    @Override
    public int editRefundReject(Integer pkid, Integer currentUserId, String remark, Map<String, Object> map) throws Exception {
        int typeId = 1046;
        SoRefund refund = findById(pkid);
        if (refund == null) {
            // 订单不存在
            return -1;
        }

        Map<String, Object> auditParam = new HashMap<>();

        auditParam.put("form_id", pkid);
        auditParam.put("type_id", typeId);
        auditParam.put("add_user_id", currentUserId);
        auditParam.put("status_id", AuditStatusUtils.TO_AUDIT);
        // 查询当前用户 审核日志
        List<BdAuditLog> bdAuditLogs = bdAuditLogService.listByProperties(auditParam);
        if (CollectionUtils.isEmpty(bdAuditLogs)) {
            // 审核任务不存在
            return -2;
        }
        // 取一个
        BdAuditLog auditLog = bdAuditLogs.get(bdAuditLogs.size() - 1);
        // 更新当前level和后续level状态为close
        int rows = bdAuditLogService.updateLevelStatus(pkid, typeId, auditLog.getPkid(), auditLog.getLevel(), AuditStatusUtils.AUDIT_CLOSE, ">=");
        if (rows == 0) {
            throw new Exception("audit reject error");
        }
        // log驳回状态
        bdAuditLogService.updateStatus(auditLog.getPkid(), AuditStatusUtils.AUDIT_REJECT, AuditStatusUtils.TO_AUDIT, remark);
        // 更新订单状态
        rows = soRefundDao.updateAuditStatus(pkid, AuditStatusUtils.AUDIT_REJECT, AuditStatusUtils.TO_AUDIT);
        if (rows == 0) {
            throw new Exception("audit reject error");
        }
        //根据传递的退单表的id和退单商品列表的id 更新so_refund和so_order_prod表的状态
        int soRefundId = (Integer) map.get("soRefundId");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        List<Integer> orderProdIds = new ArrayList<>();
        for (Map<String, Object> mapIds : list) {
            int orderProdId = (Integer) mapIds.get("orderProdId");
            orderProdIds.add(orderProdId);
        }
        //批量更新so_order_prod表中的audit_status_id字段为审核不通过
        soOrderProdService.updateAuditRefund(orderProdIds, 1046);
        updateSoRefund(soRefundId, 1046);
        return 1;
    }


    @Override
    public int updateSoRefund(Integer pkid, Integer auditStatusId) {
        return soRefundDao.updateSoRefund(pkid, auditStatusId);
    }
}