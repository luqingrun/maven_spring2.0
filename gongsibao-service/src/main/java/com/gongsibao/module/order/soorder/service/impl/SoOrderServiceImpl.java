package com.gongsibao.module.order.soorder.service.impl;

import com.gongsibao.common.util.ExcelUtils;
import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ParamType;
import com.gongsibao.module.order.socontract.service.SoContractService;
import com.gongsibao.module.order.soinvoice.entity.SoInvoice;
import com.gongsibao.module.order.soinvoice.entity.SoInvoiceAuditRequest;
import com.gongsibao.module.order.soinvoice.service.SoInvoiceService;
import com.gongsibao.module.order.soorder.dao.SoOrderDao;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;
import com.gongsibao.module.order.soorderdiscount.service.SoOrderDiscountService;
import com.gongsibao.module.order.soorderinvoicemap.entity.SoOrderInvoiceMap;
import com.gongsibao.module.order.soorderinvoicemap.service.SoOrderInvoiceMapService;
import com.gongsibao.module.order.soorderprod.dao.SoOrderProdDao;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderproditem.dao.SoOrderProdItemDao;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderprodusermap.dao.SoOrderProdUserMapDao;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.uc.ucrole.entity.RoleTag;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("soOrderService")
public class SoOrderServiceImpl implements SoOrderService {

    Log log = LogFactory.getLog(SoOrderService.class);

    @Autowired
    private SoOrderDao soOrderDao;
    @Autowired
    private SoOrderProdDao soOrderProdDao;
    @Autowired
    private SoOrderProdItemDao soOrderProdItemDao;

    @Autowired
    private SoOrderProdUserMapDao soOrderProdUserMapDao;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private SoInvoiceService soInvoiceService;

    @Autowired
    private SoOrderDiscountService soOrderDiscountService;

    @Autowired
    private SoOrderInvoiceMapService orderInvoiceMapService;

    @Override
    public SoOrder findById(Integer pkid) {
        return soOrderDao.findById(pkid);
    }

    @Override
    public OrderList findOrderListById(Integer pkid) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("pkid", pkid);
        List<OrderList> orderLists = soOrderDao.findOrderListByProperties(properties, 0, 1);
        if (null != orderLists && orderLists.size() > 0) {
            OrderList orderList = orderLists.get(0);
            setState(orderList);
            //查询业务姓名的聚合
            List<Integer> orderIds = new ArrayList<>();
            orderIds.add(orderList.getPkid());
            Map<Integer, String> businessMap = soOrderProdUserMapDao.findBusinessNameByOrderId(orderIds);
            for (OrderList o : orderLists) {
                o.setBusinessName(businessMap.get(o.getPkid()));
            }
            return orderList;
        } else {
            return new OrderList();
        }
    }

    public SoOrder findChangePriceById(Integer pkid) {
        return soOrderDao.findChangePriceById(pkid);
    }

    @Override
    public int update(SoOrder soOrder) {
        return soOrderDao.update(soOrder);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrder soOrder) {
        return soOrderDao.insert(soOrder);
    }

    @Override
    public List<SoOrder> findByIds(List<Integer> pkidList) {
        return soOrderDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrder> findMapByIds(List<Integer> pkidList) {
        List<SoOrder> list = findByIds(pkidList);
        Map<Integer, SoOrder> map = new HashMap<>();
        for (SoOrder soOrder : list) {
            map.put(soOrder.getPkid(), soOrder);
        }
        return map;
    }

    @Override
    public Pager<SoOrder> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderDao.countByProperties(map);
        int pageSize = 40;
        Pager<SoOrder> pager = new Pager<>(totalRows, page, pageSize);
        List<SoOrder> soOrderList = soOrderDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderList);
        return pager;
    }

    @Override
    public Pager<OrderList> pageOrderListByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderDao.findOrderCountByProperties(map);
        int pageSize = 40;
        Pager<OrderList> pager = new Pager<>(totalRows, page, pageSize);
        List<OrderList> orderLists = soOrderDao.findOrderListByProperties(map, pager.getStartRow(), pager.getPageSize());
        List<Integer> orderIds = new ArrayList<>();
        int state = null == map.get("state") ? 0 : Integer.valueOf(map.get("state").toString());
        for (OrderList o : orderLists) {
            orderIds.add(o.getPkid());
            if (state == 0) {
                setState(o);
            } else {
                o.setState(state);
            }
        }
        if (orderIds.size() > 0) {
            //查询业务姓名的聚合
            Map<Integer, String> businessMap = soOrderProdUserMapDao.findBusinessNameByOrderId(orderIds);
            for (OrderList o : orderLists) {
                o.setBusinessName(businessMap.get(o.getPkid()));
            }
        }
        pager.setList(orderLists);
        return pager;
    }

    private void setState(OrderList o) {
        long threeDays = 60 * 60 * 24 * 1000 * 1000 * 3;
        if (o.getPaidPrice() == 0 && new Date().getTime() - o.getAddTime().getTime() < threeDays) {
            o.setState(1);
        }
        if (Objects.equals(o.getPaidPrice(), o.getPayablePrice())) {
            o.setState(2);
        }
        if (!Objects.equals(o.getPaidPrice(), o.getPayablePrice()) && o.getPaidPrice() > 0) {
            o.setState(3);
        }
        if (o.getProcessStatusId() == 3024) {
            o.setState(4);
        }
        if (o.getPaidPrice() == 0 && new Date().getTime() - o.getAddTime().getTime() >= threeDays) {
            o.setState(5);
        }
    }

    /**
     * 创建新订单
     *
     * @param soOrder
     * @return
     */
    public int insertOrders(SoOrder soOrder, Integer currentUserId) {
        int orderId = soOrderDao.insert(soOrder);

        // 审核节点查询
        List<List<BdAuditLog>> logs = new ArrayList<>();
        addChangePriceAuditNode(logs, orderId, currentUserId);

        // 插入
        List<BdAuditLog> auditLogs = new ArrayList<>();
        for (List<BdAuditLog> auditLog : logs) {
            auditLogs.addAll(auditLog);
        }
        if (CollectionUtils.isNotEmpty(auditLogs)) {
            bdAuditLogService.insertBatch(auditLogs);
        }

        //写入优惠券
        if(soOrder.getSoOrderDiscountList().size() !=0){
            for (SoOrderDiscount item : soOrder.getSoOrderDiscountList()){
                item.setOrderId(orderId);

                soOrderDiscountService.insert(item);
            }
        }

        for (SoOrderProd soOrderProd : soOrder.getProdList()) {
            soOrderProd.setOrderId(orderId);
            int orderProdId = soOrderProdDao.insert(soOrderProd);

            for (SoOrderProdItem soOrderProdItem : soOrderProd.getItemList()) {
                soOrderProdItem.setOrderProdId(orderProdId);
                soOrderProdItemDao.insert(soOrderProdItem);
            }
        }

        return orderId;
    }

    @Override
    public Pager<OrderList> listInstallment(Map<String, Object> properties, Integer currentPage, Integer pageSize) {

        int total = soOrderDao.findOrderCountByProperties(properties);
        if (total == 0) {
            return new Pager<>(0, 0);
        }
        Pager<OrderList> pager = new Pager<>(total, currentPage, pageSize);

        List<OrderList> list = soOrderDao.findOrderListByProperties(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(list);
        return pager;
    }

    @Override
    public String exportInstallment(Map<String, Object> properties) {

        String currentUserId = StringUtils.trimToEmpty(properties.get("currentUserId"));

        List<List<String>> contents = new ArrayList<>();
        List<String> titles = genInstallmentTitle();
        contents.add(titles);

        List<OrderList> list = soOrderDao.findOrderListByProperties(properties, 0, Integer.MAX_VALUE);

        if (CollectionUtils.isNotEmpty(list)) {
            for (OrderList orderList : list) {
                contents.add(genInstallmentItem(orderList));
            }
        }

        String fileUrl = FileUtils.LOCAL_SAVE_PATH + currentUserId + ".csv";
        ExcelUtils.getListToCsv(contents, fileUrl);
        return fileUrl;
    }

    @Override
    public Pager<OrderList> listAuditInstallment(Map<String, Object> properties, Integer currentPage, Integer pageSize) {

        int total = soOrderDao.getInstallmentAuditCount(properties);
        if (total == 0) {
            return new Pager<>(0, 0);
        }

        Pager<OrderList> pager = new Pager<>(total, currentPage, pageSize);
        List<OrderList> list = soOrderDao.getInstallmentAuditList(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(list);

        return pager;
    }

    @Override
    public String exportAuditInstallment(Map<String, Object> properties) {

        String currentUserId = StringUtils.trimToEmpty(properties.get("currentUserId"));

        List<List<String>> contents = new ArrayList<>();
        List<String> titles = genInstallmentTitle();
        contents.add(titles);

        List<OrderList> list = soOrderDao.getInstallmentAuditList(properties, 0, Integer.MAX_VALUE);

        if (CollectionUtils.isNotEmpty(list)) {
            for (OrderList orderList : list) {
                List<String> itemList = genInstallmentItem(orderList);

                contents.add(itemList);
            }
        }

        String fileUrl = FileUtils.LOCAL_SAVE_PATH + currentUserId + ".csv";
        ExcelUtils.getListToCsv(contents, fileUrl);
        return fileUrl;
    }

    /**
     * 生成导出excel title
     *
     * @return
     */
    private List<String> genInstallmentTitle() {
        List<String> titles = new ArrayList<>();
        titles.add("订单编号");
        titles.add("产品名称");
        titles.add("原价金额");
        titles.add("订单金额");
        titles.add("分期次数");
        titles.add("下单人");
        titles.add("下单人电话");
        titles.add("下单时间");
        titles.add("审核状态");
        return titles;
    }

    /**
     * 生成导出分期excel item
     *
     * @param orderList
     * @return
     */
    private List<String> genInstallmentItem(OrderList orderList) {
        List<String> itemList = new ArrayList<>();
        itemList.add(StringUtils.trimToEmpty(orderList.getPkid()));
        itemList.add(StringUtils.trimToEmpty(orderList.getProdName()));
        itemList.add(StringUtils.trimToEmpty(orderList.getTotalPrice() / 100));
        itemList.add(StringUtils.trimToEmpty(orderList.getPaidPrice() / 100));
        itemList.add(StringUtils.trimToEmpty(orderList.getInstallmentNum()) + "期");
        itemList.add(StringUtils.trimToEmpty(orderList.getAccountName()));
        itemList.add(StringUtils.trimToEmpty(orderList.getAccountMoblie()));
        itemList.add(StringUtils.trimToEmpty(DateFormatUtils.format(orderList.getAddTime(), "yyyy-MM-dd HH:mm")));
        itemList.add(StringUtils.trimToEmpty(orderList.getInstallmentAuditStatusName()));
        return itemList;
    }

    @Override
    public int addInstallment(SoOrder soOrder, Integer currentUserId) {
        SoOrder order = findById(soOrder.getPkid());
        if (null == order) {
            return -1;
        }

        if (order.getType() != 1) {
            return -2;
        }

        // 分期标记
        int rows = soOrderDao.addInstallment(soOrder);
        if (rows == 0) {
            return rows;
        }

        // 审核节点查询
        List<List<BdAuditLog>> logs = new ArrayList<>();
        genInstallmentAuditList(logs, soOrder.getPkid(), currentUserId);

        // 插入
        List<BdAuditLog> auditLogs = new ArrayList<>();
        for (List<BdAuditLog> auditLog : logs) {
            auditLogs.addAll(auditLog);
        }
        if (CollectionUtils.isNotEmpty(auditLogs)) {
            bdAuditLogService.insertBatch(auditLogs);
        }
        return rows;
    }

    @Override
    public int addInvoice(SoOrder soOrder, SoInvoice soInvoice, Integer currentUserId) {
        SoOrder order = findById(soOrder.getPkid());
        if (null == order) {
            return -1;
        }
        //金额检测
        //下限
        if (soInvoice.getAmount() <= 0) {
            return -3;
        }
        List<SoInvoice> soInvoiceList = soInvoiceService.findByOrderId(order.getPkid());
        int totalPrice = 0;
        for (SoInvoice si : soInvoiceList) {
            totalPrice += si.getAmount();
        }
        //上限
        if (order.getPayablePrice() - totalPrice < soInvoice.getAmount()) {
            return -3;
        }
        // 发票标记
        int rows = soOrderDao.addInvoice(soOrder);
        if (rows == 0) {
            return rows;
        }
        // 审核节点查询
        List<List<BdAuditLog>> logs = new ArrayList<>();
        genInvoiceAuditList(logs, soOrder.getPkid(), currentUserId);
        // 插入
        List<BdAuditLog> auditLogs = new ArrayList<>();
        for (List<BdAuditLog> auditLog : logs) {
            auditLogs.addAll(auditLog);
        }
        if (CollectionUtils.isNotEmpty(auditLogs)) {
            bdAuditLogService.insertBatch(auditLogs);
        }
        //发票信息校验
        //发票类型 3081 普通发票、3082 增值税专用发票
        if (soInvoice.getTypeId() == 3081) {
            if (StringUtils.isBlank(soInvoice.getReceiverName())) {
                return -2;
            }
            if (StringUtils.isBlank(soInvoice.getReceiverMobilePhone())) {
                return -2;
            }
            if (StringUtils.isBlank(soInvoice.getReceiverAddress())) {
                return -2;
            }
        }
        if (soInvoice.getTypeId() == 3082) {
            if (StringUtils.isBlank(soInvoice.getVatTaxNo())) {
                return -2;
            }
            if (StringUtils.isBlank(soInvoice.getVatAddress())) {
                return -2;
            }
            if (StringUtils.isBlank(soInvoice.getVatPhone())) {
                return -2;
            }
            if (StringUtils.isBlank(soInvoice.getVatBankName())) {
                return -2;
            }
        }

        //插入map表和发票申请表
        Integer invoiceId = soInvoiceService.insert(soInvoice);
        orderInvoiceMapService.insert(new SoOrderInvoiceMap() {
            {
                setInvoiceId(invoiceId);
                setOrderId(order.getPkid());
            }
        });
        return rows;
    }


    @Override
    public int editInstallmentPass(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1047;
        SoOrder order = findById(pkid);
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
            rows = soOrderDao.updateInstallmentStatus(pkid, AuditStatusUtils.AUDIT_PASS, AuditStatusUtils.TO_AUDIT);
            if (rows == 0) {
                throw new Exception("audit pass error");
            }
        }
        return 1;
    }

    @Override
    public int editInstallmentReject(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1047;
        SoOrder order = findById(pkid);
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
        rows = soOrderDao.updateInstallmentStatus(pkid, AuditStatusUtils.AUDIT_REJECT, AuditStatusUtils.TO_AUDIT);
        if (rows == 0) {
            throw new Exception("audit reject error");
        }
        return 1;
    }

    @Override
    public Map<String, Integer> getAuditNums(Integer currentUserId, Integer type) {
        Map<String, Integer> result = new HashMap<String, Integer>() {{
            put("toauditNums", 0);
            put("passNums", 0);
            put("rejectNums", 0);
            put("allNums", 0);
        }};

        List<Map<String, Object>> list = soOrderDao.getAuditNums(currentUserId, type);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        int allNums = 0;
        for (Map<String, Object> map : list) {
            int statusId = NumberUtils.toInt(map.get("status_id"));
            int num = NumberUtils.toInt(map.get("num"));

            if (statusId == AuditStatusUtils.TO_AUDIT) {
                allNums = allNums + num;
                result.put("toauditNums", num);
            } else if (statusId == AuditStatusUtils.AUDIT_PASS) {
                allNums = allNums + num;
                result.put("passNums", num);
            } else if (statusId == AuditStatusUtils.AUDIT_REJECT) {
                allNums = allNums + num;
                result.put("rejectNums", num);
            }
        }
        result.put("allNums", allNums);
        return result;
    }

    /**
     * 生成审核节点
     */
    private void genInstallmentAuditList(List<List<BdAuditLog>> list, Integer orderPkid, Integer currentUserId) {
        int typeId = 1047; //分期类型，写死1047
        SoOrder order = findById(orderPkid);
        if (null == order) {
            return;
        }

        BdAuditLog addLog = new BdAuditLog() {{
            setAddUserId(currentUserId);
            setFormId(orderPkid);
            setTypeId(typeId);
            setContent("提交分期申请");
            setRemark("提交分期申请");
            setLevel(0);
            setStatusId(AuditStatusUtils.AUDIT_PASS);   // 通过
        }};
        // 提交申请人
        list.add(new ArrayList<BdAuditLog>() {{
            add(addLog);
        }});

        List<Integer> installmentList = order.getInstallmentList();
        if (CollectionUtils.isEmpty(installmentList)) {
            return;
        }

        // 调用组织结构接口-查分公司总经理
        List<Integer> compManagerIdList = ucUserService.getBranchBoss(currentUserId);
        List<BdAuditLog> managerLogs = new ArrayList<>();
        for (Integer managerId : compManagerIdList) {
            managerLogs.add(new BdAuditLog() {{
                setAddUserId(managerId);
                setFormId(orderPkid);
                setTypeId(typeId);
                setContent("分总审核");
                setRemark("");
                setLevel(1);
                setStatusId(AuditStatusUtils.TO_AUDIT);   // 1052审核中
            }});
        }
        list.add(managerLogs);

        int total = 0;
        for (Integer money : installmentList) {
            total = total + money;
        }
        // 首付款
        Integer first = installmentList.get(0);

        if (2 * first < total) {
            // 首付款比例小于50%, 事业部总审核
            // 查询事业部总

            // 1、查所有产品
            List<SoOrderProd> orderProd = soOrderProdService.getByOrderId(order.getPkid());
            List<Integer> productIds = new ArrayList<>();
            for (SoOrderProd soOrderProd : orderProd) {
                productIds.add(soOrderProd.getProductId());
            }

            // 2、获取产品
            List<ProdProduct> productList = prodProductService.findByIds(productIds);

            // 产品大类id
            List<Integer> categoryIds = new ArrayList<>();
            for (ProdProduct prodProduct : productList) {
                BdDict bdDict = bdDictService.findTreeByParentId(201, prodProduct.getTypeId());
                if (null != bdDict) {
                    categoryIds.add(bdDict.getPkid());
                }
            }

            // 通过产品大类列表找事业部经理
            List<Integer> businessIdList = ucUserService.getBusinessBoss(categoryIds);
            List<BdAuditLog> departmentLogs = new ArrayList<>();
            for (Integer departmentId : businessIdList) {
                departmentLogs.add(new BdAuditLog() {{
                    setAddUserId(departmentId);
                    setFormId(orderPkid);
                    setTypeId(typeId);
                    setContent("事业部总经理审核");
                    setRemark("");
                    setLevel(2);
                    setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
                }});
            }
            list.add(departmentLogs);
        }
    }

    /**
     * 生成审核节点
     */
    private void genInvoiceAuditList(List<List<BdAuditLog>> list, Integer orderPkid, Integer currentUserId) {
        int typeId = 1044; //分期类型，写死1044
        SoOrder order = findById(orderPkid);
        if (null == order) {
            return;
        }
        // 提交申请人
        BdAuditLog addLog0 = new BdAuditLog();
        addLog0.setAddUserId(currentUserId);
        addLog0.setFormId(orderPkid);
        addLog0.setTypeId(typeId);
        addLog0.setContent("提交发票申请");
        addLog0.setLevel(0);
        addLog0.setStatusId(AuditStatusUtils.AUDIT_PASS);   // 通过
        List<BdAuditLog> auditLogs0 = new ArrayList<>();
        auditLogs0.add(addLog0);
        //发票专员
        List<BdAuditLog> auditLogs1 = new ArrayList<>();
        BdAuditLog addLog1 = new BdAuditLog();
        //TODO 暂时写死发票专员ID
        addLog1.setAddUserId(1);
        addLog1.setFormId(orderPkid);
        addLog1.setTypeId(typeId);
        addLog1.setContent("发票专员审核");
        addLog1.setLevel(1);
        addLog1.setStatusId(AuditStatusUtils.TO_AUDIT);   // 待审核
        auditLogs1.add(addLog1);
        list.add(auditLogs0);
        list.add(auditLogs1);
    }

    @Override
    public String findMaxNo() {
        return soOrderDao.findMaxNo();
    }

    @Override
    public String exportOrderList(Map<String, Object> properties, Integer page) {
        String currentUserId = StringUtils.trimToEmpty(properties.get("currentUserId"));
        List<List<String>> contents = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("订单编号");
        titles.add("产品名称");
        titles.add("订单状态");
        titles.add("退单状态");
        titles.add("订单类型");
        titles.add("原价金额");
        titles.add("订单金额");
        titles.add("付款金额");
        titles.add("分期付款");
        titles.add("开发票");
        titles.add("业务员");
        titles.add("下单人");
        titles.add("下单人电话");
        titles.add("下单时间");
        titles.add("下单方式");
        contents.add(titles);
        Pager<OrderList> orderListPager = pageOrderListByProperties(properties, Integer.valueOf(page));
        List<OrderList> orderLists = orderListPager.getList();
        if (CollectionUtils.isNotEmpty(orderLists)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (OrderList o : orderLists) {
                List<String> content = new ArrayList<>();
                contents.add(content);
                content.add(StringUtils.trimToEmpty(o.getNo()));
                content.add(StringUtils.trimToEmpty(o.getProdName()));
                content.add(StringUtils.trimToEmpty(o.getStateName()));
                content.add(StringUtils.trimToEmpty(o.getRefundStatusName()));
                content.add(StringUtils.trimToEmpty(o.getTypeName()));
                content.add(StringUtils.trimToEmpty(o.getTotalPrice()));
                content.add(StringUtils.trimToEmpty(o.getPayablePrice()));
                content.add(StringUtils.trimToEmpty(o.getPaidPrice()));
                content.add(StringUtils.trimToEmpty(o.getIsInstallmentName()));
                content.add(StringUtils.trimToEmpty(o.getIsInvoiceName()));
                content.add(StringUtils.trimToEmpty(o.getBusinessName()));
                content.add(StringUtils.trimToEmpty(o.getAccountName()));
                content.add(StringUtils.trimToEmpty(o.getAccountMoblie()));
                String dateStr = sdf.format(o.getAddTime());
                content.add(StringUtils.trimToEmpty(dateStr));
                content.add(StringUtils.trimToEmpty(o.getSourceTypeName()));
            }
        }
        String fileUrl = FileUtils.LOCAL_SAVE_PATH + currentUserId + ".csv";
        ExcelUtils.getListToCsv(contents, fileUrl);
        return fileUrl;
    }


    public Pager<SoOrder> findBySoOrders(List<ParamType> paramTypeList, int page, int pagesize) {
        int totalRows = soOrderDao.countByOrders(paramTypeList);
        Pager<SoOrder> pager = new Pager<>(totalRows, page, pagesize);
        List<SoOrder> soOrderList = soOrderDao.findByOrders(paramTypeList, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderList);
        return pager;
    }

    @Override
    public Pager<OrderList> listAuditChangePrice(Map<String, Object> properties, Integer currentPage, Integer pageSize) {
        int total = soOrderDao.getChangePriceAuditCount(properties);
        if (total == 0) {
            return new Pager<>(0, 0);
        }

        Pager<OrderList> pager = new Pager<>(total, currentPage, pageSize);
        List<OrderList> list = soOrderDao.getChangePriceAuditList(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(list);

        return pager;
    }

    public int editAssignApply(Integer orderId, Integer applyUserId, Integer operateUserId) {
                // 当前用户下属业务员ids
        List<Integer> businessUserIds = ucUserService.getUserPkid(operateUserId, RoleTag.ROLE_YWY);
        // 检验是否为业务员
        if (!businessUserIds.contains(applyUserId)) {
            return -2;
        }

        int rs = editOrderAssign(applyUserId, orderId);
        log.info("assign_order: user[" + operateUserId + "] assign order[" + StringUtils.join(orderId, ",") + "] to user[" + applyUserId + "]");
        return rs;
    }

    @Override
    public int editAssignApply(Collection<Integer> orderIds, Integer applyUserId, Integer operateUserId) {

        // 当前用户下属业务员ids
        List<Integer> businessUserIds = ucUserService.getUserPkid(operateUserId, RoleTag.ROLE_YWY);
        // 检验是否为业务员
        if (!businessUserIds.contains(applyUserId)) {
            return -2;
        }

        for (Integer orderId : orderIds) {
            editOrderAssign(applyUserId, orderId);
        }

        log.info("assign_batch_order: user[" + operateUserId + "] assign batch orders[" + StringUtils.join(orderIds, ",") + "] to user[" + applyUserId + "]");
        return 1;
    }

    /**
     * 订单分配
     * @param applyUserId
     * @param orderId
     */
    private int editOrderAssign(Integer applyUserId, Integer orderId) {
        int type = 3061;// 业务员type
        List<SoOrderProd> productList = soOrderProdDao.getByOrderId(orderId);
        if (CollectionUtils.isEmpty(productList)) {
            // 订单不存在
            return -1;
        }
        List<SoOrderProdUserMap> prodUserMapList = new ArrayList<>();
        List<Integer> prodIds = new ArrayList<>();
        for (SoOrderProd orderProd : productList) {
            prodIds.add(orderProd.getPkid());

            SoOrderProdUserMap prodUserMap = new SoOrderProdUserMap();
            prodUserMap.setUserId(applyUserId);
            prodUserMap.setStatusId(314);
            prodUserMap.setTypeId(type);
            prodUserMap.setOrderProdId(orderProd.getPkid());

            prodUserMapList.add(prodUserMap);
        }

        soOrderProdUserMapDao.deleteByOrderProdIdsAndType(prodIds, type);

        if (CollectionUtils.isNotEmpty(prodUserMapList)) {
            soOrderProdUserMapDao.insertBatch(prodUserMapList);
        }
        return 1;
    }

    public int editInvoice(Integer orderId, Integer invoiceId, Integer userId, SoInvoiceAuditRequest invoiceAuditRequest) throws Exception {
        int typeId = 1044;
        SoOrder order = findById(orderId);
        if (order == null) {
            // 订单不存在
            return -1;
        }
        Map<String, Object> auditParam = new HashMap<>();
        auditParam.put("form_id", orderId);
        auditParam.put("type_id", typeId);
        auditParam.put("add_user_id", userId);
        auditParam.put("status_id", AuditStatusUtils.TO_AUDIT);
        // 查询当前用户 审核日志
        List<BdAuditLog> bdAuditLogs = bdAuditLogService.listByProperties(auditParam);
        if (CollectionUtils.isEmpty(bdAuditLogs)) {
            // 审核任务不存在
            return -2;
        }
        // 取下一个,由于发票审核只有一级，所以直接更新即可
        BdAuditLog auditLog = bdAuditLogs.get(0);
        if (invoiceAuditRequest.isAudit()) {
            auditLog.setStatusId(AuditStatusUtils.AUDIT_PASS);
        } else {
            auditLog.setStatusId(AuditStatusUtils.AUDIT_REJECT);
        }
        if (!StringUtils.isBlank(invoiceAuditRequest.getRemark())) {
            auditLog.setRemark(invoiceAuditRequest.getRemark());
        }
        // 更新日志状态
        int rows = bdAuditLogService.update(auditLog);
        if (rows == 0) {
            throw new Exception("audit pass error");
        }
        SoInvoice soInvoice = soInvoiceService.findById(invoiceId);
        if (invoiceAuditRequest.isAudit()) {
            soInvoice.setAuditStatusId(AuditStatusUtils.AUDIT_PASS);
        } else {
            soInvoice.setAuditStatusId(AuditStatusUtils.AUDIT_REJECT);
        }
        if (!StringUtils.isBlank(invoiceAuditRequest.getRemark())) {
            soInvoice.setRemark(invoiceAuditRequest.getRemark());
        }
        soInvoice.setAddTime(new Date());
        // 更新order状态为审核或者不通过
        rows = soInvoiceService.update(soInvoice);
        if (rows == 0) {
            throw new Exception("audit pass error");
        }
        return 1;
    }

    /**
     * 查询改价订单审核流
     *
     * @param list
     * @param orderPkId
     * @param currentUserId
     */
    private void addChangePriceAuditNode(List<List<BdAuditLog>> list, Integer orderPkId, Integer currentUserId) {
        int typeId = 1048; //改价类型
        SoOrder order = findById(orderPkId);
        if (null == order) {
            return;
        }

        BdAuditLog addLog = new BdAuditLog();
        addLog.setAddUserId(currentUserId);
        addLog.setFormId(orderPkId);
        addLog.setTypeId(typeId);
        addLog.setContent("提交改价申请");
        addLog.setRemark("提交改价申请");
        addLog.setLevel(0);
        addLog.setStatusId(AuditStatusUtils.AUDIT_PASS);   // 通过

        // 提交申请人
        list.add(new ArrayList<BdAuditLog>() {{
            add(addLog);
        }});

        List<Integer> installmentList = order.getInstallmentList();
        if (CollectionUtils.isEmpty(installmentList)) {
            return;
        }

        // TODO 调用组织结构接口-查分公司总经理 暂时用假数据
        //  getOfficeId(currentUserId);
        List<Integer> compManagerIdList = new ArrayList<Integer>() {{
            add(1);
            add(2);
        }};

        List<BdAuditLog> managerLogs = new ArrayList<>();
        for (Integer managerId : compManagerIdList) {
            BdAuditLog mLog = new BdAuditLog();
            mLog.setAddUserId(managerId);
            mLog.setFormId(orderPkId);
            mLog.setTypeId(typeId);
            mLog.setContent("分总审核");
            mLog.setRemark("");
            mLog.setLevel(1);
            mLog.setStatusId(AuditStatusUtils.TO_AUDIT);   // 1052审核中
            managerLogs.add(mLog);
        }
        list.add(managerLogs);

        int total = 0;
        for (Integer money : installmentList) {
            total = total + money;
        }
        // 首付款
        Integer first = installmentList.get(0);

        // 1、查所有产品
        List<SoOrderProd> orderProd = soOrderProdService.getByOrderId(order.getPkid());
        List<Integer> productIds = new ArrayList<>();
        for (SoOrderProd soOrderProd : orderProd) {
            // 审批流程具体到  折扣比例小于等于（百分比）  具体金额等产品确认
            // 查询事业部总
            if (soOrderProd.getPrice() > 10000)
                productIds.add(soOrderProd.getProductId());
        }

        // 2、获取产品
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
        List<BdAuditLog> departmentLogs = new ArrayList<>();
        for (Integer departmentId : compManagerIdList) {
            BdAuditLog dLog = new BdAuditLog();
            dLog.setAddUserId(departmentId);
            dLog.setFormId(orderPkId);
            dLog.setTypeId(typeId);
            dLog.setContent("事业部总经理审核");
            dLog.setRemark("");
            dLog.setLevel(2);
            dLog.setStatusId(AuditStatusUtils.AUDIT_WAITING);   // 排队
            departmentLogs.add(dLog);
        }
        list.add(departmentLogs);

    }

    /**
     * 改价订单-浮层
     *
     * @param orderPkId
     * @return
     */
    @Override
    public List<SoOrderProdItem> listChangePriceInfo(Integer orderPkId) {
        List<SoOrderProdItem> soOrderProdItems = new ArrayList<>();
        soOrderProdItems = soOrderDao.getSoOrderProdItem(orderPkId);
        for (SoOrderProdItem item : soOrderProdItems) {
            item.setCityName(bdDictService.queryDictName(101, item.getCityId()));
        }

        return soOrderProdItems;
    }

    @Override
    public int editChangePricePass(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1048;
        SoOrder order = findById(pkid);
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
            rows = soOrderDao.updateChangePriceStatus(pkid, AuditStatusUtils.AUDIT_PASS, AuditStatusUtils.TO_AUDIT);
            if (rows == 0) {
                throw new Exception("audit pass error");
            }
        }
        return 1;
    }

    @Override
    public int editChangePriceReject(Integer pkid, Integer currentUserId, String remark) throws Exception {
        int typeId = 1048;
        SoOrder order = findById(pkid);
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
        rows = soOrderDao.updateChangePriceStatus(pkid, AuditStatusUtils.AUDIT_REJECT, AuditStatusUtils.TO_AUDIT);
        if (rows == 0) {
            throw new Exception("audit reject error");
        }
        return 1;
    }

    @Override
    public List<SoOrder> findByAccountId(Integer accountId) {
        return soOrderDao.findByAccountId(accountId);
    }
}
