package com.gongsibao.module.order.socontract.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.socontract.dao.SoContractDao;
import com.gongsibao.module.order.socontract.entity.ContractList;
import com.gongsibao.module.order.socontract.entity.SoContract;
import com.gongsibao.module.order.socontract.service.SoContractService;
import com.gongsibao.module.order.soorder.dao.SoOrderDao;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.dao.SoOrderProdDao;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderproditem.dao.SoOrderProdItemDao;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soContractService")
public class SoContractServiceImpl implements SoContractService {
    @Autowired
    private SoContractDao soContractDao;

    @Autowired
    private SoOrderDao soOrderDao;

    @Autowired
    private SoOrderProdDao soOrderProdDao;

    @Autowired
    private SoOrderProdItemDao soOrderProdItemDao;

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private ProdProductService prodProductService;

    @Autowired
    private BdDictService bdDictService;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Override
    public SoContract findById(Integer pkid) {
        return soContractDao.findById(pkid);
    }

    @Override
    public SoContract findByOrderId(Integer orderId) {
        return soContractDao.findByOrderId(orderId);
    }

    @Override
    public int update(SoContract soContract) {
        return soContractDao.update(soContract);
    }

    @Override
    public int delete(Integer pkid) {
        return soContractDao.delete(pkid);
    }

    @Override
    public Integer insert(SoContract soContract) {
        return soContractDao.insert(soContract);
    }

    @Override
    public List<SoContract> findByIds(List<Integer> pkidList) {
        return soContractDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoContract> findMapByIds(List<Integer> pkidList) {
        List<SoContract> list = findByIds(pkidList);
        Map<Integer, SoContract> map = new HashMap<>();
        for (SoContract soContract : list) {
            map.put(soContract.getPkid(), soContract);
        }
        return map;
    }

    @Override
    public List<SoContract> findByOrderIds(List<Integer> orderIdList) {
        return soContractDao.findByOrderIds(orderIdList);
    }

    @Override
    public Map<Integer, SoContract> findMapByOrderIds(List<Integer> orderIdList) {
        Map<Integer, SoContract> map = new HashMap<>();
        if (CollectionUtils.isEmpty(orderIdList)) {
            return map;
        }
        List<SoContract> list = findByOrderIds(orderIdList);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (SoContract soContract : list) {
            map.put(soContract.getOrderId(), soContract);
        }
        return map;
    }

    @Override
    public Pager<SoContract> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soContractDao.countByProperties(map);
        Pager<SoContract> pager = new Pager<>(totalRows, page);
        List<SoContract> soContractList = soContractDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soContractList);
        return pager;
    }

    @Override
    public Pager<ContractList> pageContractListByProperties(Map<String, Object> map, int page) {
        int totalRows = soContractDao.findOrderCountByProperties(map);
        int pageSize = 40;
        Pager<ContractList> pager = new Pager<>(totalRows, page, pageSize);
        List<ContractList> orderLists = soContractDao.findContractListByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(orderLists);
        return pager;
    }

    /**
     * 新建合同订单
     *
     * @param soContract
     * @return
     */
    public int insertSoContract(SoContract soContract, Integer currentUserId) {
        int orderId = soOrderDao.insert(soContract.getSoOrder());

        for (SoOrderProd soOrderProd : soContract.getSoOrder().getProdList()) {
            soOrderProd.setOrderId(orderId);
            int orderProdId = soOrderProdDao.insert(soOrderProd);
            for (SoOrderProdItem soOrderProdItem : soOrderProd.getItemList()) {
                soOrderProdItem.setOrderProdId(orderProdId);
                soOrderProdItemDao.insert(soOrderProdItem);
            }
        }

        soContract.setOrderId(orderId);
        soContractDao.insert(soContract);

        // 审核节点查询
        List<List<BdAuditLog>> logs = new ArrayList<>();
        addSoContractAuditNode(logs, orderId, currentUserId);

        // 插入
        List<BdAuditLog> auditLogs = new ArrayList<>();
        for (List<BdAuditLog> auditLog : logs) {
            auditLogs.addAll(auditLog);
        }
        if (CollectionUtils.isNotEmpty(auditLogs)) {
            bdAuditLogService.insertBatch(auditLogs);
        }

        return orderId;
    }

    /**
     * 通过订单PkId查找合同
     *
     * @param orderPkId
     * @return
     */
    @Override
    public SoContract findByOrderPkId(Integer orderPkId) {
        return soContractDao.findByOrderPkId(orderPkId);
    }

    /**
     * 查询合同订单审核流
     *
     * @param list
     * @param orderPkId
     * @param currentUserId
     */
    private void addSoContractAuditNode(List<List<BdAuditLog>> list, Integer orderPkId, Integer currentUserId) {
        SoContract soContract = findByOrderPkId(orderPkId);
        SoOrder soOrder = soOrderService.findById(orderPkId);
        if (null == soContract)
            return;

        Integer typeId = 1043; //合同类型

        //发起人
        BdAuditLog addLog = new BdAuditLog();
        addLog.setAddUserId(currentUserId);
        addLog.setFormId(soContract.getPkid());
        addLog.setTypeId(typeId);
        addLog.setContent("提交合同申请");
        addLog.setRemark("提交合同申请");
        addLog.setLevel(0);
        addLog.setStatusId(AuditStatusUtils.AUDIT_PASS);   // 通过

        // 提交申请人
        list.add(new ArrayList<BdAuditLog>() {{
            add(addLog);
        }});

        // 组织结构节点
        // TODO 调用组织结构接口-查分公司总经理 暂时用假数据
        // 获取分公司审核人节点
        List<Integer> compManagerIdList = new ArrayList<Integer>() {{
            add(1);
            add(2);
        }};

        List<BdAuditLog> managerLogs = new ArrayList<>();
        for (Integer managerId : compManagerIdList) {
            BdAuditLog mLog = new BdAuditLog();
            mLog.setAddUserId(managerId);
            mLog.setFormId(soContract.getPkid());
            mLog.setTypeId(typeId);
            mLog.setContent("分总审核");
            mLog.setRemark("");
            mLog.setLevel(1);
            mLog.setStatusId(AuditStatusUtils.TO_AUDIT);   // 1052审核中
            managerLogs.add(mLog);
        }
        list.add(managerLogs);

        List<Integer> installmentList = soOrder.getInstallmentList();
        if (CollectionUtils.isEmpty(installmentList)) {
            return;
        }

        int total = 0;
        for (Integer money : installmentList) {
            total = total + money;
        }
        // 首付款
        Integer first = installmentList.get(0);

        /** 审批流程具体到  首付款比例小于50%或总额小于30000或无违约责任
         *  数据权限获取节点信息
         */
        if (2 * first < total || soOrder.getPayablePrice() < 30000 || soContract.getHasBreach() == 0) {
            // 1、查所有产品
            List<SoOrderProd> orderProd = soOrderProdService.getByOrderId(soOrder.getPkid());
            List<Integer> productIds = new ArrayList<>();
            for (SoOrderProd soOrderProd : orderProd) {
                productIds.add(soOrderProd.getProductId());
            }

            // 2、获取产品
            List<ProdProduct> productList = prodProductService.findByIds(productIds);

            // 产品大类id
            List<Integer> categoryIds = new ArrayList<>();
            for (ProdProduct prodProduct : productList) {
                BdDict bdDict = bdDictService.findTreeByParentId(201, prodProduct.getPkid());
                if (null != bdDict) {
                    categoryIds.add(bdDict.getPkid());
                }
            }

            // TODO 通过产品大类列表找事业部经理
            List<BdAuditLog> departmentLogs = new ArrayList<>();
            for (Integer departmentId : compManagerIdList) {
                BdAuditLog dLog = new BdAuditLog();
                dLog.setAddUserId(departmentId);
                dLog.setFormId(soContract.getPkid());
                dLog.setTypeId(typeId);
                dLog.setContent("事业部总经理审核");
                dLog.setRemark("");
                dLog.setLevel(2);
                dLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
                departmentLogs.add(dLog);
            }
            list.add(departmentLogs);
        }
    }

    /**
     * 合同审核列表
     *
     * @param properties
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Pager<ContractList> listAuditContract(Map<String, Object> properties, Integer currentPage, Integer pageSize) {
        int total = soContractDao.getContractAuditCount(properties);
        if (total == 0) {
            return new Pager<>(0, 0);
        }

        Pager<ContractList> pager = new Pager<>(total, currentPage, pageSize);
        List<ContractList> list = soContractDao.getContractAuditList(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(list);

        return pager;
    }

    /**
     * 获取合同审核浮层信息
     *
     * @param orderPkId
     * @return
     */
    public SoContract getSoContractInfo(Integer orderPkId) {
        SoContract soContract = findByOrderPkId(orderPkId);
        List<OrderProdList> orderProdList = new ArrayList<>();
        orderProdList = soContractDao.getSoOrderProdList(orderPkId);
        for (OrderProdList item : orderProdList) {
            item.setCityName(bdDictService.queryDictName(101, item.getCityId()));
        }

        soContract.setOrderProdLists(orderProdList);
        return soContract;
    }

    /**
     * 合同审核-通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    @Override
    public int editSoContractPass(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1043;
        SoOrder order = soOrderService.findById(pkid);
        if (order == null) {
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
            rows = soContractDao.updateContractStatus(pkid, AuditStatusUtils.AUDIT_PASS, AuditStatusUtils.TO_AUDIT);
            if (rows == 0) {
                throw new Exception("audit pass error");
            }
        }
        return 1;
    }

    /**
     * 合同审核-不通过
     *
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    @Override
    public int editSoContractReject(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1043;
        SoOrder order = soOrderService.findById(pkid);
        if (order == null) {
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
        rows = soContractDao.updateContractStatus(pkid, AuditStatusUtils.AUDIT_REJECT, AuditStatusUtils.TO_AUDIT);
        if (rows == 0) {
            throw new Exception("audit reject error");
        }
        return 1;
    }
}