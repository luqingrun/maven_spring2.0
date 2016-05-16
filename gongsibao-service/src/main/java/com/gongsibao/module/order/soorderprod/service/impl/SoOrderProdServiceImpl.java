package com.gongsibao.module.order.soorderprod.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ParamType;
import com.gongsibao.module.order.socontract.dao.SoContractDao;
import com.gongsibao.module.order.socontract.entity.SoContract;
import com.gongsibao.module.order.socontract.service.SoContractService;
import com.gongsibao.module.order.soorder.dao.SoOrderDao;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.dao.SoOrderProdDao;
import com.gongsibao.module.order.soorderprod.entity.*;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderproditem.dao.SoOrderProdItemDao;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderproditem.service.SoOrderProdItemService;

import com.gongsibao.module.order.soorderprodorganizationmap.dao.SoOrderProdOrganizationMapDao;
import com.gongsibao.module.order.soorderprodorganizationmap.entity.SoOrderProdOrganizationMap;

import com.gongsibao.module.order.soorderprodorganizationmap.service.SoOrderProdOrganizationMapService;

import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.order.soorderprodtracefile.service.SoOrderProdTraceFileService;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.product.prodpriceaudit.entity.CityArea;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflowfile.service.ProdWorkflowFileService;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("soOrderProdService")
public class SoOrderProdServiceImpl implements SoOrderProdService {
    @Autowired
    private SoOrderProdDao soOrderProdDao;
    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;
    @Autowired
    private SoOrderProdTraceService soOrderProdTraceService;
    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private ProdServiceService prodServiceService;
    @Autowired
    private SoOrderProdTraceFileService soOrderProdTraceFileService;
    @Autowired
    private ProdWorkflowFileService prodWorkflowFileService;
    @Autowired
    private BdFileService bdFileService;
    @Autowired
    private SoOrderProdItemDao soOrderProdItemDao;
    @Autowired
    private SoOrderDao soOrderDao;
    @Autowired
    private SoOrderProdItemService soOrderProdItemService;
    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private SoOrderProdUserMapService soOrderProdUserMapService;
    @Autowired
    private SoContractService soContractService;
    @Autowired
    private SoContractDao soContractDao;

    @Autowired
    private SoOrderProdOrganizationMapDao soOrderProdOrganizationMapDao;

    @Autowired
    private UcOrganizationService ucOrganizationService;

    @Autowired
    private SoOrderProdOrganizationMapService soOrderProdOrganizationMapService;

    @Override
    public SoOrderProd findById(Integer pkid) {
        return soOrderProdDao.findById(pkid);
    }

    @Override
    public Map<String, Object> getDetailById(Integer orderProdId) {//  TODO
        // 当前状态要求天数
        int statusDayCount = 0;
        // 当前状态停留天数
        int statusHandledDayCount = 0;
        Map<String, Object> result = new HashMap<>();
        result.put("statusDayCount", statusDayCount);
        result.put("statusHandledDayCount", statusHandledDayCount);

        // 查询产品订单
        SoOrderProd orderProd = findById(orderProdId);
        if (orderProd == null) {
            return result;
        }
        result.put("orderProd", orderProd);

        // 获取当前产品订单中的当前节点
        ProdWorkflowNode node = prodWorkflowNodeService.findById(orderProd.getProcessStatusId());
        if (node != null) {
            // 计算当前状态要求天数
            statusDayCount = node.getWeekdayCount();
        }
        SoOrderProdTrace orderProdTrace = soOrderProdTraceService.findLatestStatusByOrderProdId(orderProdId);
        if (orderProdTrace != null) {
            // 计算当前状态停留天数
            statusHandledDayCount = orderProdTrace.getProcessdDays();
        }

        result.put("statusDayCount", statusDayCount);
        result.put("statusHandledDayCount", statusHandledDayCount);
        return result;
    }

    @Override
    public SoOrder getOrderProdAuditById(Integer orderProdId) {
        // 查询产品订单
        SoOrderProd orderProd = findById(orderProdId);
        if (orderProd == null) {
            return null;
        }
        // 设置地区名称
        orderProd.setCityName(bdDictService.queryDictName(BdDict.TYPE_101, orderProd.getCityId()));

        SoOrder order = soOrderService.findById(orderProd.getOrderId());
        if (order == null) {
            return null;
        }
        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("type_id", 3063);
        propertiesMap.put("status_id", 3141);
        propertiesMap.put("order_prod_id", orderProdId);
        // 查询正在负责该产品订单的业务员，并设置
        Map<Integer, String> userNameMap = soOrderProdUserMapService.queryUserNameMapByProperties(propertiesMap);
        orderProd.setSalesmanNames(StringUtils.trimToEmpty(userNameMap.get(orderProdId)));
        order.setSoOrderProd(orderProd);

        // 查询并设置产品关联的服务集合
        List<ProdService> lstProdService = prodServiceService.findByIds(orderProd.getProductId());
        if (!CollectionUtils.isEmpty(lstProdService)) {
            Map<Integer, BdDict> unitDictMap = bdDictService.findOneLevelMapByType(BdDict.TYPE_202);
            Map<Integer, BdDict> typeDictMap = bdDictService.findOneLevelMapByType(BdDict.TYPE_203);
            for (ProdService prodService : lstProdService) {
                if (unitDictMap.get(prodService.getUnitId()) != null) {
                    prodService.setUnitName(unitDictMap.get(prodService.getUnitId()).getName());
                }
                prodService.setPropertyName(bdDictService.queryDictName(BdDict.TYPE_207, prodService.getPropertyId()));
                if (typeDictMap.get(prodService.getTypeId()) != null) {
                    prodService.setTypeName(typeDictMap.get(prodService.getTypeId()).getName());
                }
            }
            orderProd.setProdServiceList(lstProdService);
        }

        // 查询审核日志记录(只查询审核没通过的，上传材料的审核日志)
        Map<String, Object> condition = new HashMap<>();
        // 1049产品订单审核
        condition.put("type_id", 1049);
        condition.put("form_id", orderProdId);
        condition.put("status_id", 1053);
        List<BdAuditLog> lstAuditLog = bdAuditLogService.listByProperties(condition);

        UcUser user = null;
        Map<Integer, UcUser> userMap = null;
        List<Integer> userIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lstAuditLog)) {
            for (BdAuditLog auditLog : lstAuditLog) {
                userIds.add(auditLog.getAddUserId());
            }
            // 根据用户ID集合查询MAP信息  TODO
            userMap = ucUserService.findMapByIds(userIds);
            for (BdAuditLog auditLog : lstAuditLog) {
                user = userMap.get(auditLog.getAddUserId());
                if (user == null) {
                    auditLog.setAddUserName("");
                } else {
                    auditLog.setAddUserName(user.getRealName());
                }
            }
            orderProd.setAuditLogList(lstAuditLog);
        }

        // 根据产品订单ID和上传材料类型查询跟进记录ID集合
        List<Integer> traceIds = soOrderProdTraceService.queryOrderProdTraceIds(orderProdId, 3153);
        if (CollectionUtils.isEmpty(traceIds)) {
            return order;
        }

        // 根据产品订单ID查询产品处理流程材料
        List<ProdWorkflowFile> lstWorkflowFile = prodWorkflowFileService.queryWorkflowFileListByOrderProdId(orderProdId);
        if (CollectionUtils.isEmpty(lstWorkflowFile)) {
            return order;
        }
        List<Integer> workflowFileIds = new ArrayList<>();
        for (ProdWorkflowFile workflowFile : lstWorkflowFile) {
            // isMust=1必须上传的材料
            if (workflowFile.getIsMust() == 1) {
                workflowFileIds.add(workflowFile.getPkid());
            }
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("is_new", 1);
        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        properties.put("audit_status_id", 1051);
        properties.put("order_prod_trace_id", traceIds);
        if (!CollectionUtils.isEmpty(workflowFileIds)) {
            properties.put("prod_workflow_file_id", workflowFileIds);
        }
        // 查询材料列表
        List<SoOrderProdTraceFile> traceFileList = soOrderProdTraceFileService.findByProperties(properties);
        if (CollectionUtils.isEmpty(traceFileList)) {
            return order;
        }

        List<Integer> fileIds = new ArrayList<>();
        for (SoOrderProdTraceFile orderProdTraceFile : traceFileList) {
            fileIds.add(orderProdTraceFile.getFileId());
            userIds.add(orderProdTraceFile.getAddUserId());
        }

        // 根据用户ID集合查询MAP信息  TODO
        userMap = ucUserService.findMapByIds(userIds);
        // 根据文件ID集合查询上传材料信息
        Map<Integer, BdFile> fileMap = bdFileService.findMapByIds(fileIds);

        for (SoOrderProdTraceFile orderProdTraceFile : traceFileList) {
            orderProdTraceFile.setFile(fileMap.get(orderProdTraceFile.getFileId()));

            user = userMap.get(orderProdTraceFile.getAddUserId());
            if (user == null) {
                orderProdTraceFile.setAddUserName("");
            } else {
                orderProdTraceFile.setAddUserName(user.getRealName());
            }
        }
        orderProd.setOrderProdTraceFileList(traceFileList);

        return order;
    }

    @Override
    public int update(SoOrderProd soOrderProd) {
        return soOrderProdDao.update(soOrderProd);
    }

    @Override
    public int updateAuditStatus(Integer pkid, int newStatus, int oldStatus) {
        if (pkid == null || pkid.compareTo(0) <= 0 || newStatus < 0 || oldStatus < 0) {
            return -1;
        }
        return soOrderProdDao.updateAuditStatus(pkid, newStatus, oldStatus);
    }

    @Override
    public int updateOrderProdAudit(Integer orderProdId, Collection<Integer> passTraceFileIds, Collection<Integer> failTraceFileIds, String content, Integer userId) {
        if (orderProdId == null || orderProdId.compareTo(0) <= 0 || (CollectionUtils.isEmpty(passTraceFileIds) && CollectionUtils.isEmpty(failTraceFileIds))) {
            return -1;
        }
        SoOrderProd orderProd = findById(orderProdId);
        if (orderProd == null) {
            return -1;
        }
        /** 产品订单表审核状态  105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        if (orderProd.getAuditStatusId().compareTo(1052) != 0) {
            return -1;
        }

        int status = 0;
        int result = 0;
        /** 修改产品订单表审核状态  105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        if (CollectionUtils.isEmpty(failTraceFileIds)) {
            // 上传材料有一个不通过，则产品订单审核结果为不通过
            result = updateAuditStatus(orderProdId, 1054, 1052);
            status = 1054;

            // 产品订单审核通过，则该订单的负责人状态由正在负责改为曾经负责 type=306，3061业务、3062客服（关注）、3063操作 type=314，3141正在负责、3142曾经负责
            soOrderProdUserMapService.updateStatusByOrderProdId(orderProdId, 3063, 3142, 3141);
        } else {
            result = updateAuditStatus(orderProdId, 1053, 1052);
            status = 1053;

            /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
            soOrderProdTraceFileService.updateAuditStatus(failTraceFileIds, 1053, 1051);
        }

        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        if (!CollectionUtils.isEmpty(passTraceFileIds)) {
            soOrderProdTraceFileService.updateAuditStatus(passTraceFileIds, 1054, 1051);
        }

        // 插入审核日志记录
        BdAuditLog bdAuditLog = new BdAuditLog();
        // 1049产品订单审核
        bdAuditLog.setTypeId(1049);
        bdAuditLog.setAddUserId(userId);
        bdAuditLog.setContent(content);
        bdAuditLog.setStatusId(status);
        bdAuditLog.setFormId(orderProdId);
        bdAuditLog.setRemark("");
        bdAuditLog.setLevel(0);
        bdAuditLogService.insert(bdAuditLog);

        return result;
    }

    @Override
    public void updateJobDays(Integer orderprodid, int processedDays, int needDays, int timeoutDays, int traceTimeoutDays) {
        int result = soOrderProdDao.updateJobDays(orderprodid, processedDays, needDays, timeoutDays);
        if (result > 0) {
            soOrderProdTraceService.updateJobDays(orderprodid, traceTimeoutDays);
        }
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProd soOrderProd) {
        return soOrderProdDao.insert(soOrderProd);
    }

    @Override
    public List<SoOrderProd> findByIds(List<Integer> pkidList) {
        return soOrderProdDao.findByIds(pkidList);
    }

    @Override
    public List<OrderProdList> findOrderProdListByOrderId(Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<OrderProdList> list = soOrderProdDao.findOrderProdListByProperties(map, 0, 100);

        List<Integer> orderProdIds = list.stream().map(OrderProdList::getPkid).collect(Collectors.toList());

        List<SoOrderProdItem> itemList = soOrderProdItemDao.findItemsByOrderProdIds(orderProdIds);

        Map<Integer, List<SoOrderProdItem>> itemMaps = new HashMap<>();

        List<Integer> cityIds = itemList.stream().map(SoOrderProdItem::getCityId).collect(Collectors.toList());
        Map<Integer, String> cityNameMap = bdDictService.queryDictNames(BdDict.TYPE_101, cityIds);
        for (SoOrderProdItem item : itemList) {
            List<SoOrderProdItem> l = itemMaps.get(item.getOrderProdId());
            String cityName = cityNameMap.get(item.getCityId());
            if (null != cityName) {
                item.setCityName(cityName);
            }
            if (null == l) {
                l = new ArrayList<>();
                itemMaps.put(item.getOrderProdId(), l);
            }
            l.add(item);
        }
        for (OrderProdList orderProdList : list) {
            List<SoOrderProdItem> items = itemMaps.get(orderProdList.getPkid());
            if (null != items) {
                orderProdList.setSoOrderProdItems(items);
            }
        }
        return list;
    }

    @Override
    public Map<Integer, SoOrderProd> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProd> list = findByIds(pkidList);
        Map<Integer, SoOrderProd> map = new HashMap<>();
        for (SoOrderProd soOrderProd : list) {
            map.put(soOrderProd.getPkid(), soOrderProd);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProd> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderProdDao.countByProperties(map);
        Pager<SoOrderProd> pager = new Pager<>(totalRows, page);
        List<SoOrderProd> soOrderProdList = soOrderProdDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderProdList);
        return pager;
    }

    @Override
    public Pager<OrderProdList> pageOrderProdListByProperties(Map<String, Object> properties, int page) {
        int totalRows = soOrderProdDao.findOrderProdCountByProperties(properties);
        int pageSize = 40;
        Pager<OrderProdList> pager = new Pager<>(totalRows, page, pageSize);
        List<OrderProdList> orderProdLists = soOrderProdDao.findOrderProdListByProperties(properties, pager.getStartRow(), pager.getPageSize());
        //订单明细id
        List<Integer> orderProdIds = new ArrayList<>();
        //订单id
        HashSet<Integer> orderIds = new HashSet<>();
        for (OrderProdList o : orderProdLists) {
            orderProdIds.add(o.getPkid());
            orderIds.add(o.getOrderId());
        }
        if (orderProdIds.size() > 0) {
            //查询服务名的聚合
            Map<Integer, String> itemNameMap = soOrderProdItemDao.findItemNamesByOrderProdIds(orderProdIds);
            for (OrderProdList o : orderProdLists) {
                o.setItemNames(itemNameMap.get(o.getPkid()));
            }
            //查询订单状态
            setState(properties, orderIds, orderProdLists);
        }
        //是否加急
        if (orderIds.size() > 0) {
            Map<Integer, Integer> maps = soContractDao.findIsUrgeneyByOrderIds(orderIds);
            for (OrderProdList o : orderProdLists) {
                Integer isUrgeney = maps.get(o.getOrderId());
                if (isUrgeney == null) {
                    o.setIsUrgeney(0);
                } else {
                    o.setIsUrgeney(isUrgeney);
                }
            }
        }
        pager.setList(orderProdLists);
        return pager;
    }

    private void setState(Map<String, Object> properties, HashSet<Integer> orderIds, List<OrderProdList> orderProdLists) {
        //订单状态查询,如果未指定订单状态，需要去数据库中重新查询
        if (null == properties.get("state")) {
            //订单列表
            List<SoOrder> orderLists = soOrderDao.findByIds(new ArrayList<>(orderIds));
            Map<Integer, Integer> stateMap = new HashMap<>();
            //根据订单列表相关信息得到对应状态
            for (SoOrder o : orderLists) {
                setState(o, stateMap);
            }
            for (OrderProdList o : orderProdLists) {
                o.setState(stateMap.get(o.getOrderId()));
            }
        } else {
            int state = Integer.valueOf(properties.get("state").toString());
            for (OrderProdList o : orderProdLists) {
                o.setState(state);
            }
        }
    }

    @Override
    public List<SoOrderProd> getByOrderId(Integer orderId) {
        return soOrderProdDao.getByOrderId(orderId);
    }

    @Override
    public void setProdItem(SoOrderProd prod) {
        setProdItem(new ArrayList<SoOrderProd>() {{
            add(prod);
        }});
    }

    @Override
    public void setProdItem(List<SoOrderProd> prodList) {
        if (CollectionUtils.isEmpty(prodList)) {
            return;
        }

        List<Integer> prodIds = new ArrayList<>();
        for (SoOrderProd orderProd : prodList) {
            prodIds.add(orderProd.getPkid());
        }

        Map<Integer, List<SoOrderProdItem>> itemMap = soOrderProdItemService.getMapByProdIds(prodIds);

        for (SoOrderProd orderProd : prodList) {
            orderProd.setItemList(itemMap.get(orderProd.getPkid()));
        }
    }

    /**
     * 获取操作订单池
     *
     * @param map      过滤条件
     * @param page     当前页码
     * @param pageSize 页面大小
     */
    @Override
    public Pager<OrderProdRow> pageOrderProdRowsByProperties(Map<String, Object> map, Integer page, Integer pageSize) {
        int totalRows = soOrderProdDao.countOrderProdRowsByProperties(map);
        Pager<OrderProdRow> pager = new Pager<>(totalRows, page, pageSize);
        List<OrderProdRow> soOrderProdList = soOrderProdDao.findOrderProdRowsByProperties(map, pager.getStartRow()
                , pager.getPageSize());

        for (OrderProdRow row : soOrderProdList) {

            //剩余天数
            row.setRemainDays(row.getNeedDays() - row.getProcessedDays());

            //查询是否加急

            SoContract contract = soContractDao.findByOrderId(row.getOrderId());
            if (contract != null) {
                row.setIsUrgent(contract.getIsUrgeney());
            }

            //获取业务人员名
            List<UcUser> users = soOrderProdUserMapService.findOperatorByOrderProdId(row.getOrderProdId());
            row.setUserName(users.stream().map(UcUser::getRealName).collect(Collectors.joining("、")));


            //获取产品订单当前状态
            SoOrderProdTrace trace = soOrderProdTraceService.findLatestStatusByOrderProdId(row.getOrderProdId());
            if (trace != null) {
                row.setProcessStatusId(trace.getOrderProdStatusId());
                row.setProductName(trace.getOrderProdStatusName());
            }

            //获取城市
            if (row.getCityArea().getPkid() > 0) {
                CityArea cityArea = row.getCityArea();
                BdDict cityAreaFromDb = bdDictService.findById(cityArea.getPkid());
                if (cityAreaFromDb != null) {
                    cityArea.setFullName(bdDictService.queryDictName(BdDict.TYPE_101, cityArea.getPkid()));
                    cityArea.setName(cityAreaFromDb.getName());
                }
            }
            //获取审核状态
            if (row.getAuditStatusId() > 0) {
                BdDict auditStatus = bdDictService.findById(row.getAuditStatusId());
                if (auditStatus != null) {
                    row.setAuditStatus(auditStatus.getName());
                }
            }

        }

        pager.setList(soOrderProdList);
        return pager;
    }


    @Override
    public Integer getOrderProdCount(String statusStr, String orgIdsStr) {
        return soOrderProdDao.countOrderProdRows(statusStr, orgIdsStr);
    }

    private void setState(SoOrder o, Map<Integer, Integer> stateMap) {
        long threeDays = 60 * 60 * 24 * 1000 * 1000 * 3;
        if (o.getPaidPrice() == 0 && new Date().getTime() - o.getAddTime().getTime() < threeDays) {
            stateMap.put(o.getPkid(), 1);
        }
        if (Objects.equals(o.getPaidPrice(), o.getPayablePrice())) {
            stateMap.put(o.getPkid(), 2);
        }
        if (!Objects.equals(o.getPaidPrice(), o.getPayablePrice()) && o.getPaidPrice() > 0) {
            stateMap.put(o.getPkid(), 3);
        }
        if (o.getProcessStatusId() == 3024) {
            stateMap.put(o.getPkid(), 4);
        }
        if (o.getPaidPrice() == 0 && new Date().getTime() - o.getAddTime().getTime() >= threeDays) {
            stateMap.put(o.getPkid(), 5);
        }
    }

    public SoOrderProd updateStatus(SoOrderProd soOrderProd) {
        return soOrderProdDao.updateStatus(soOrderProd);
    }


    public SoOrderProd updateIsComplaint(SoOrderProd soOrderProd) {
        return soOrderProdDao.updateIsComplaint(soOrderProd);
    }

    public Pager<SoOrderProd> findBySoOrderProds(List<ParamType> paramTypeList, int page, int pagesize) {
        int totalRows = soOrderProdDao.countBySoOrderProds(paramTypeList);
        Pager<SoOrderProd> pager = new Pager<>(totalRows, page, pagesize);
        List<SoOrderProd> prodProductList = soOrderProdDao.findBySoOrderProds(paramTypeList, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductList);
        return pager;
    }

    public Pager<OrderProdRow> findMyOrderListByProperties(Map<String, Object> properties, Integer pageNum, Integer pageSize) {
        int totalRows = soOrderProdDao.findMyOrderListByProperties(properties);
        Pager<OrderProdRow> pager = new Pager<>(totalRows, pageNum, pageSize);
        List<OrderProdRow> list = soOrderProdDao.findMyOrderListByProperties(properties, pager.getStartRow(), pager.getPageSize());
        //去产品处理流程节点表中查找订单处理描述
        for (OrderProdRow orderProdRow : list) {
            ProdWorkflowNode prodWorkflowNode = prodWorkflowNodeService.findById(orderProdRow.getProcessStatusId());
            if (prodWorkflowNode != null) {
                orderProdRow.setAuditStatus(prodWorkflowNode.getName());
            }
        }
        pager.setList(list);
        return pager;
    }

    /**
     * 得到退款产品列表
     *
     * @param orderId
     * @return List<SoOrderProd>
     */
    @Override
    public List<SoOrderProd> getApplyRefundByOrderId(Integer orderId, Integer isRefund) {
        List<SoOrderProd> soOrderProdList = soOrderProdDao.getRefundByOrderId(orderId, isRefund);
        setApplyRefundProdItem(soOrderProdList);
        return soOrderProdList;
    }


    /**
     * 关联表查询
     *
     * @param prodList
     */
    public void setApplyRefundProdItem(List<SoOrderProd> prodList) {
        if (CollectionUtils.isEmpty(prodList)) {
            return;
        }

        /**获取订单产品表id的List集合*/
        List<Integer> prodIds = new ArrayList<>();
        for (SoOrderProd orderProd : prodList) {
            prodIds.add(orderProd.getPkid());
        }

        /**通过prodIds得到订单产品服务项的列表【一对多】*/
        Map<Integer, List<SoOrderProdItem>> itemMap = soOrderProdItemService.getRefundMapByProdIds(prodIds);

        /**将订单产品服务项组装到orderProd中*/
        for (SoOrderProd orderProd : prodList) {
            orderProd.setItemList(itemMap.get(orderProd.getPkid()));
        }
        /**通过prodIds得到关联表【so_order_prod_user_map】的列表【一对一】*/
        List<SoOrderProdUserMap> mapList = soOrderProdUserMapService.findRefundByIds(prodIds, 3061);

        /**遍历关联表【so_order_prod_user_map】得到userId*/
        List<Integer> userIdList = new ArrayList<>();
        Map<Integer, Integer> prodUserIdMap = new HashMap();
        for (SoOrderProdUserMap soOrderProdUserMap : mapList) {
            userIdList.add(soOrderProdUserMap.getUserId());
            /**将用户id和退款产品id关联起来*/
            prodUserIdMap.put(soOrderProdUserMap.getOrderProdId(), soOrderProdUserMap.getUserId());
        }
        /**通过userIdList集合得到用户列表*/
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIdList);
        /**将用户信息组装到soOrderProd中*/
        for (SoOrderProd soOrderProd : prodList) {
            Integer userId = prodUserIdMap.get(soOrderProd.getPkid());
            if (null == userId) {
                continue;
            }
            soOrderProd.setBusinessUser(userMap.get(userId));
        }

    }

    /**
     * 退单申请模块:更新状态
     *
     * @param pkidList
     * @param isRefund
     * @return int
     */
    @Override
    public int updateRefund(List<Integer> pkidList, Integer isRefund) {
        return soOrderProdDao.updateRefund(pkidList, isRefund);
    }

    /**
     * 退单审核模块：更新状态
     *
     * @param pkidList
     * @param auditStatusId
     * @return int
     */
    @Override
    public int updateAuditRefund(List<Integer> pkidList, Integer auditStatusId) {
        return soOrderProdDao.updateAuditRefund(pkidList, auditStatusId);
    }

    @Override

    public int updateReturnProduct(int productPkid, int userId) {
        soOrderProdOrganizationMapDao.deleteByProdId(productPkid);
        return soOrderProdDao.returnProduct(productPkid, userId);
    }

    public Pager<OrderProdRow> findOrderAuditListByProperties(Map<String, Object> properties, Integer pageNum, Integer pageSize) {
        int totalRows = soOrderProdDao.findOrderAuditByProperties(properties);
        Pager<OrderProdRow> pager = new Pager<>(totalRows, pageNum, pageSize);
        List<OrderProdRow> list = soOrderProdDao.findOrderAuditByProperties(properties, pager.getStartRow(), pager.getPageSize());
        List<OrderProdRow> orderProdRows = new ArrayList<>();
        //去合同表查看是否加急,没有查到为普通订单
        for (OrderProdRow orderProdRow : list) {
            SoContract soContract = soContractDao.findByOrderId(orderProdRow.getOrderId());
            if (soContract != null) {
                orderProdRow.setIsUrgent(soContract.getIsUrgeney());
            } else {
                orderProdRow.setIsUrgent(0);
            }
            orderProdRows.add(orderProdRow);
        }
        pager.setList(orderProdRows);
        return pager;
    }

    @Override
    public Pager<OrderProdMonitorList> findOrderProdMonitorListByProperties(Map<String, Object> properties, int page, int pageSize) {
        Pager<OrderProdMonitorList> pager = new Pager<>(0, page, pageSize);

        int userId = NumberUtils.toInt(properties.get("userId"));

        // 当前登录人所在组织关联的所有产品订单ID集合
        List<Integer> loginOrderProdIds = soOrderProdOrganizationMapService.queryOrderProdIdsByOrganizationIds(ucUserService.getUserOrganizationIds(userId));

        // 设置各种类型下的数量
        pager.setExtend(getOrderProdMonitorExtend(userId, loginOrderProdIds));

        if (CollectionUtils.isEmpty(loginOrderProdIds) || userId <= 0) {
            return pager;
        }

        List<Integer> organizationOrderProdIds = null;
        String organizationName = StringUtils.trimToEmpty(properties.get("organizationName"));
        if (StringUtils.isNotBlank(organizationName)) {
            // 组织名称不为空，则查询该名称关联的组织ID集合以及这些组织ID集合关联的产品订单ID集合A  该名称即长名称也是短名称 模糊查询
            organizationOrderProdIds = getOrderProdIdsByOrganizationName(organizationName);
            if (CollectionUtils.isEmpty(organizationOrderProdIds)) {
                return pager;
            }
        }

        List<Integer> resultOrderProdIds = null;
        int type = NumberUtils.toInt(StringUtils.trimToEmpty(properties.get("type")));
        if (type == 1) {
            // 查询登陆者自己关注的产品订单ID集合B  并且获取loginOrderProdIds与B的交集
            resultOrderProdIds = getFollowOrderProdIds(userId, loginOrderProdIds);
            if (CollectionUtils.isEmpty(resultOrderProdIds)) {
                return pager;
            }
        } else if (type == 3) {
            // 获取那些不是审核通过，且无人负责的产品订单ID集合C  并且获取loginOrderProdIds与C的交集
            resultOrderProdIds = getNoManageOrderProdIds(loginOrderProdIds);
            if (CollectionUtils.isEmpty(resultOrderProdIds)) {
                return pager;
            }
        }

        // 获取最后产品订单ID集合的交集 类型为1:重点关注 3:无人负责  集合A与他们各自集合取交集 否则集合A与loginOrderProdIds取交集
        if (CollectionUtils.isEmpty(organizationOrderProdIds)) {
            properties.put("orderProdIds", (type == 1 || type == 3) ? resultOrderProdIds : loginOrderProdIds);
        } else {
            organizationOrderProdIds.retainAll((type == 1 || type == 3) ? resultOrderProdIds : loginOrderProdIds);
            if (CollectionUtils.isEmpty(organizationOrderProdIds)) {
                return pager;
            }
            properties.put("orderProdIds", organizationOrderProdIds);
        }

        int totalRows = soOrderProdDao.findOrderProdMonitorCountByProperties(properties);
        pager = new Pager<>(totalRows, page, pageSize);
        List<OrderProdMonitorList> orderProdLists = soOrderProdDao.findOrderProdMonitorListByProperties(properties, pager.getStartRow(), pager.getPageSize());

        // 订单明细id
        List<Integer> orderProdIds = new ArrayList<>();
        // 订单id
        List<Integer> orderIds = new ArrayList<>();
        // 地区ID集合
        Set<Integer> cityIds = new HashSet<>();
        // 处理状态ID集合
        List<Integer> nodeIds = new ArrayList<>();
        for (OrderProdMonitorList o : orderProdLists) {
            orderProdIds.add(o.getPkid());
            orderIds.add(o.getOrderId());
            cityIds.add(o.getCityId());
            nodeIds.add(o.getProcessStatusId());
        }

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("type_id", 3063);
        propertiesMap.put("status_id", 3141);
        propertiesMap.put("order_prod_id", orderProdIds);
        // 查询正在负责该产品订单的业务员，并设置
        Map<Integer, String> userNameMap = soOrderProdUserMapService.queryUserNameMapByProperties(propertiesMap);
        // 查询正在负责该产品订单的业务员所处的组织机构，并设置
        Map<Integer, String> organizationNameMap = soOrderProdUserMapService.queryOrganizationNameMapByProperties(propertiesMap);
        // 根据订单ID集合查询合同MAP
        Map<Integer, SoContract> contractMap = soContractService.findMapByOrderIds(orderIds);
        // 根据地区ID集合查询地区MAP
        Map<Integer, String> dictMap = bdDictService.queryDictNames(101, cityIds);
        // 根据处理状态ID集合查询流程节点MAP
        Map<Integer, ProdWorkflowNode> nodeMap = prodWorkflowNodeService.findMapByIds(nodeIds);
        // 查询登录人关注的产品订单ID集合
        List<Integer> followOrderProdIds = getFollowOrderProdIds(userId, loginOrderProdIds);

        SoContract contract = null;
        ProdWorkflowNode node = null;
        for (OrderProdMonitorList o : orderProdLists) {
            contract = contractMap.get(o.getOrderId());
            if (contract == null) {
                o.setIsUrgeney(0);
            } else {
                o.setIsUrgeney(contract.getIsUrgeney());
            }

            o.setCityName(StringUtils.trimToEmpty(dictMap.get(o.getCityId())));
            o.setSalesmanNames(StringUtils.trimToEmpty(userNameMap.get(o.getPkid())));
            // 正在负责订单的业务员所属的组织机构
            o.setOrganizationNames(StringUtils.trimToEmpty(organizationNameMap.get(o.getPkid())));

            node = nodeMap.get(o.getProcessStatusId());
            if (node == null) {
                o.setProcessStatusName("");
            } else {
                o.setProcessStatusName(node.getName());
            }

            if (CollectionUtils.isNotEmpty(followOrderProdIds) && followOrderProdIds.contains(o.getPkid())) {
                // 设置该条产品订单已被关注
                o.setIsFollow(1);
            }
        }
        pager.setList(orderProdLists);
        // 设置各种类型下的数量
        pager.setExtend(getOrderProdMonitorExtend(userId, loginOrderProdIds));
        return pager;
    }

    private List<Integer> getFollowOrderProdIds(int userId, Collection<Integer> loginOrderProdIds) {
        // 查询登陆者自己关注的产品订单ID集合B  并且获取loginOrderProdIds与B的交集
        Map<String, Object> condition = new HashMap<>();
        condition.put("typeId", 3062);
        condition.put("userId", userId);
        condition.put("orderProdIds", loginOrderProdIds);
        return soOrderProdUserMapService.queryOrderProdIdsByProperties(condition);
    }

    private List<Integer> getNoManageOrderProdIds(Collection<Integer> loginOrderProdIds) {
        // 获取那些不是审核通过，且无人负责的产品订单ID集合C  并且获取loginOrderProdIds与C的交集
        Map<String, Object> condition = new HashMap<>();
        condition.put("typeId", 3063);
        condition.put("noStatusId", 3141);
        condition.put("orderProdIds", loginOrderProdIds);
        List<Integer> orderProdIds = soOrderProdUserMapService.queryOrderProdIdsByProperties(condition);
        if (CollectionUtils.isEmpty(orderProdIds)) {
            return null;
        }
        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        condition.put("noAuditStatusId", 1054);
        condition.put("orderProdIds", orderProdIds);
        return queryOrderProdIdsByProperties(condition);
    }

    private List<Integer> getOrderProdIdsByOrganizationName(String organizationName) {
        // 组织名称不为空，则查询该名称关联的组织ID集合以及这些组织ID集合关联的产品订单ID集合A  该名称即长名称也是短名称 模糊查询
        Map<String, Object> condition = new HashMap<>();
        condition.put("name", organizationName + "%");
        List<Integer> organizationIds = ucOrganizationService.queryIdsByProperties(condition);
        if (CollectionUtils.isEmpty(organizationIds)) {
            return null;
        }
        return soOrderProdOrganizationMapService.queryOrderProdIdsByOrganizationIds(organizationIds);
    }


    private Map<Object, Object> getOrderProdMonitorExtend(int userId, Collection<Integer> loginOrderProdIds) {
        Map<Object, Object> result = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("type", 2);
        int complaintCount = soOrderProdDao.findOrderProdMonitorCountByProperties(properties);
        result.put("complaintCount", complaintCount);

        properties.put("type", 4);
        int timeoutCount = soOrderProdDao.findOrderProdMonitorCountByProperties(properties);
        result.put("timeoutCount", timeoutCount);

        properties.put("type", 5);
        int refundCount = soOrderProdDao.findOrderProdMonitorCountByProperties(properties);
        result.put("refundCount", refundCount);

        List<Integer> ids = null;
        // 重点关注COUNT
        result.put("followCount", 0);
        if (CollectionUtils.isNotEmpty(loginOrderProdIds) && userId > 0) {
            ids = getFollowOrderProdIds(userId, loginOrderProdIds);
            if (CollectionUtils.isNotEmpty(ids)) {
                result.put("followCount", ids.size());
            }
        }

        // 无人负责COUNT
        result.put("noManageCount", 0);
        if (CollectionUtils.isNotEmpty(loginOrderProdIds)) {
            ids = getNoManageOrderProdIds(loginOrderProdIds);
            if (CollectionUtils.isNotEmpty(ids)) {
                result.put("noManageCount", ids.size());
            }
        }

        return result;
    }

    @Override

    public Integer updateBeginOperation(int productId, int OrganizationId) {
        int result = soOrderProdOrganizationMapDao.updateByProdId(productId, OrganizationId);
        if (result == 0) {
            SoOrderProdOrganizationMap soOrderProdOrganizationMap = new SoOrderProdOrganizationMap();
            soOrderProdOrganizationMap.setOrderProdId(productId);
            soOrderProdOrganizationMap.setOrganizationId(OrganizationId);
            soOrderProdOrganizationMapDao.insert(soOrderProdOrganizationMap);
        }
        return 1;
    }


    @Override
    public List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties) {
        return soOrderProdDao.queryOrderProdIdsByProperties(properties);
    }

    @Override
    public Pager<SoOrderProd> findOrderProdListByJobProperties(Map<String, Object> properties, int page, int pageSize) {
        int totalRows = soOrderProdDao.countByJobProperties(properties);
        if (totalRows <= 0) {
            return new Pager<>(0, page, pageSize);
        }
        Pager<SoOrderProd> pager = new Pager<>(totalRows, page, pageSize);
        List<SoOrderProd> orderProdLists = soOrderProdDao.findByJobProperties(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(orderProdLists);
        return pager;
    }

}