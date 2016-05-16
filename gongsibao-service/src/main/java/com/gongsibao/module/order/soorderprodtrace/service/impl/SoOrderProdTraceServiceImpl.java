package com.gongsibao.module.order.soorderprodtrace.service.impl;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprod.dao.SoOrderProdDao;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;
import com.gongsibao.module.order.soorderprodaccount.service.SoOrderProdAccountService;
import com.gongsibao.module.order.soorderprodtrace.dao.SoOrderProdTraceDao;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.order.soorderprodtracefile.dao.SoOrderProdTraceFileDao;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("soOrderProdTraceService")
public class SoOrderProdTraceServiceImpl implements SoOrderProdTraceService {
    @Autowired
    private SoOrderProdTraceDao soOrderProdTraceDao;
    @Autowired
    private SoOrderProdDao soOrderProdDao;
    @Autowired
    private SoOrderProdTraceFileDao soOrderProdTraceFileDao;
    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;
    @Autowired
    private SoOrderProdAccountService soOrderProdAccountService;
    @Autowired
    private UcUserService ucUserService;

    @Override
    public SoOrderProdTrace findById(Integer pkid) {
        return soOrderProdTraceDao.findById(pkid);
    }

    @Override
    public int update(SoOrderProdTrace soOrderProdTrace) {
        return soOrderProdTraceDao.update(soOrderProdTrace);
    }

    @Override
    public int updateJobDays(Integer orderprodid, int timeoutDays) {
        return soOrderProdTraceDao.updateJobDays(orderprodid, timeoutDays);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdTraceDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProdTrace soOrderProdTrace) {
        return soOrderProdTraceDao.insert(soOrderProdTrace);
    }

    @Override
    public List<SoOrderProdTrace> findByIds(List<Integer> pkidList) {
        return soOrderProdTraceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderProdTrace> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdTrace> list = findByIds(pkidList);
        Map<Integer, SoOrderProdTrace> map = new HashMap<>();
        for (SoOrderProdTrace soOrderProdTrace : list) {
            map.put(soOrderProdTrace.getPkid(), soOrderProdTrace);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdTrace> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int totalRows = soOrderProdTraceDao.countByProperties(map);
        if (totalRows <= 0) {
            return null;
        }
        Pager<SoOrderProdTrace> pager = new Pager<>(totalRows, page, pageSize);
        List<SoOrderProdTrace> soOrderProdTraceList = soOrderProdTraceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        List<Integer> orderProdStatusIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for (SoOrderProdTrace orderProdTrace : soOrderProdTraceList) {
            // 封装订单项状态ID集合和用户ID集合
            orderProdStatusIds.add(orderProdTrace.getOrderProdStatusId());
            userIds.add(orderProdTrace.getOperatorId());
        }

        // 根据用户ID集合查询MAP信息  TODO
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIds);
        // 根据订单项状态ID集合查询MAP信息
        Map<Integer, ProdWorkflowNode> workflowNodeMap = prodWorkflowNodeService.findMapByIds(orderProdStatusIds);

        UcUser user = null;
        ProdWorkflowNode workflowNode = null;
        for (SoOrderProdTrace orderProdTrace : soOrderProdTraceList) {
            workflowNode = workflowNodeMap.get(orderProdTrace.getOrderProdStatusId());
            if (workflowNode == null) {
                orderProdTrace.setOrderProdStatusName("");
            } else {
                orderProdTrace.setOrderProdStatusName(StringUtils.trimToEmpty(workflowNode.getName()));
            }

            user = userMap.get(orderProdTrace.getOperatorId());
            if (user == null) {
                orderProdTrace.setOperatorName("");
            } else {
                orderProdTrace.setOperatorName(user.getRealName());
            }

        }
        pager.setList(soOrderProdTraceList);
        return pager;
    }

    public SoOrderProdTrace updateStatus(SoOrderProd soOrderProd, SoOrderProdTrace soOrderProdTrace) {
        SoOrderProd finishSoOrderProd = soOrderProdDao.updateStatus(soOrderProd);
        if (finishSoOrderProd.getProcessStatusId().equals(soOrderProd.getProcessStatusId())) {
            Integer lastid = soOrderProdTraceDao.insert(soOrderProdTrace);
            return soOrderProdTraceDao.findById(lastid);
        } else {
            return new SoOrderProdTrace();
        }
    }

    public SoOrderProdTrace addfile(SoOrderProdTrace soOrderProdTrace, SoOrderProdTraceFile soOrderProdTraceFile) {
        Integer lastId = soOrderProdTraceDao.insert(soOrderProdTrace);
        if (lastId != 0 && !lastId.equals(0)) {
            //查询同一orderProd下同时type是上传材料的所有orderProdTrace记录
            Map<String, Object> map = new HashMap();
            map.put("order_prod_id", soOrderProdTrace.getOrderProdId());
            map.put("type_id", soOrderProdTrace.getTypeId());
            List<SoOrderProdTrace> soOrderProdTraceList = soOrderProdTraceDao.findByProperties(map, 0, 1000);

            //有记录
            if (soOrderProdTraceList.size() != 0) {
                //把所有是材料上传又属于同一orderProd的id并且文件类型和名字同要添加的一样拿出来查询file文件
                List<Integer> filelist = new ArrayList();
                for (SoOrderProdTrace item : soOrderProdTraceList) {
                    filelist.add(item.getPkid());
                }
                List<SoOrderProdTraceFile> soOrderProdTraceFileList = soOrderProdTraceFileDao.findByOrderProdTraceId(filelist, soOrderProdTraceFile);

                //有记录
                if (soOrderProdTraceFileList.size() != 0) {
                    //将同一orderProd、文件类型id、文件类型名称又是材料上传的记录id放进list里批量修改isnew为2(1:最新;2:历史)
                    List<Integer> updatefilelist = new ArrayList();
                    for (SoOrderProdTraceFile item : soOrderProdTraceFileList) {
                        updatefilelist.add(item.getPkid());
                    }
                    soOrderProdTraceFileDao.updateIsNew(updatefilelist);
                }

            }
            soOrderProdTraceFile.setOrderProdTraceId(lastId);
            Integer lastFileId = soOrderProdTraceFileDao.insert(soOrderProdTraceFile);
            return soOrderProdTraceDao.findById(lastId);
        } else {
            return new SoOrderProdTrace();
        }
    }

    public SoOrderProdTrace saveAccount(SoOrderProdTrace soOrderProdTrace, List<SoOrderProdAccount> soOrderProdAccountList) {
        Integer lastId = soOrderProdTraceDao.insert(soOrderProdTrace);
        if (lastId != 0 && !lastId.equals(0)) {
            SoOrderProdTrace item = soOrderProdTraceDao.findById(lastId);
            soOrderProdAccountService.saveAccount(item.getOrderProdId(), soOrderProdAccountList);
            return item;
        } else {
            return new SoOrderProdTrace();
        }
    }

    @Override
    public List<SoOrderProdTrace> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        return soOrderProdTraceDao.findByProperties(properties, 0, 1000);
    }

    @Override
    public SoOrderProdTrace findLatestStatusByOrderProdId(Integer orderProdId) {
        return soOrderProdTraceDao.findLatestStatusByOrderProdId(orderProdId);
    }

    @Override
    public Map<Integer, List<SoOrderProdTrace>> findMapByUserIds(Collection<Integer> userIds) {
        Map<Integer, List<SoOrderProdTrace>> result = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        properties.put("operator_id", userIds);

        List<SoOrderProdTrace> list = findByProperties(properties, 0, Integer.MAX_VALUE);
        for (SoOrderProdTrace soOrderProdTrace : list) {
            Integer operatorId = soOrderProdTrace.getOperatorId();

            List<SoOrderProdTrace> traceList = result.get(operatorId);
            if (null == traceList) {
                traceList = new ArrayList<>();
                result.put(operatorId, traceList);
            }

            traceList.add(soOrderProdTrace);
        }

        return result;
    }

    @Override
    public List<Integer> queryOrderProdTraceIds(Integer orderProdId, Integer typeId) {
        if (orderProdId == null || orderProdId.compareTo(0) <= 0 || typeId == null || typeId.compareTo(0) <= 0) {
            return null;
        }
        return soOrderProdTraceDao.queryOrderProdTraceIds(orderProdId, typeId);
    }

    @Override
    public int queryTraceProcessdDays(Map<String, Object> properties) {
        return soOrderProdTraceDao.queryTraceProcessdDays(properties);
    }

}