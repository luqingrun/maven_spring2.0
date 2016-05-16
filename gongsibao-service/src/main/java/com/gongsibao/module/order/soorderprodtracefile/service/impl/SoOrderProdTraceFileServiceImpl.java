package com.gongsibao.module.order.soorderprodtracefile.service.impl;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.order.soorderprodtracefile.dao.SoOrderProdTraceFileDao;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.order.soorderprodtracefile.service.SoOrderProdTraceFileService;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflowfile.service.ProdWorkflowFileService;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("soOrderProdTraceFileService")
public class SoOrderProdTraceFileServiceImpl implements SoOrderProdTraceFileService {
    @Autowired
    private SoOrderProdTraceFileDao soOrderProdTraceFileDao;
    @Autowired
    private SoOrderProdTraceService soOrderProdTraceService;
    @Autowired
    private BdFileService bdFileService;
    @Autowired
    private ProdWorkflowFileService prodWorkflowFileService;
    @Autowired
    private UcUserService ucUserService;

    @Override
    public SoOrderProdTraceFile findById(Integer pkid) {
        return soOrderProdTraceFileDao.findById(pkid);
    }

    @Override
    public int update(SoOrderProdTraceFile soOrderProdTraceFile) {
        return soOrderProdTraceFileDao.update(soOrderProdTraceFile);
    }

    @Override
    public int updateAuditStatus(Collection<Integer> pkids, int newStatus, int oldStatus) {
        if(CollectionUtils.isEmpty(pkids) || newStatus <= 0 || oldStatus <= 0) {
            return -1;
        }
        return soOrderProdTraceFileDao.updateAuditStatus(pkids, newStatus, oldStatus);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdTraceFileDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProdTraceFile soOrderProdTraceFile) {
        return soOrderProdTraceFileDao.insert(soOrderProdTraceFile);
    }

    @Override
    public List<SoOrderProdTraceFile> findByIds(List<Integer> pkidList) {
        return soOrderProdTraceFileDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderProdTraceFile> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdTraceFile> list = findByIds(pkidList);
        Map<Integer, SoOrderProdTraceFile> map = new HashMap<>();
        for(SoOrderProdTraceFile soOrderProdTraceFile : list){
            map.put(soOrderProdTraceFile.getPkid(), soOrderProdTraceFile);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdTraceFile> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int orderProdId = NumberUtils.toInt(String.valueOf(map.get("orderProdId")));
        if(orderProdId <= 0) {
            return null;
        }
        // 根据产品订单ID和上传材料类型查询跟进记录ID集合
        List<Integer> traceIds = soOrderProdTraceService.queryOrderProdTraceIds(orderProdId, 3153);
        if(CollectionUtils.isEmpty(traceIds)) {
            return null;
        }
        map.put("orderProdId", null);
        map.put("order_prod_trace_id", traceIds);

        int totalRows = soOrderProdTraceFileDao.countByProperties(map);
        if(totalRows <= 0) {
            return null;
        }
        Pager<SoOrderProdTraceFile> pager = new Pager<>(totalRows, page, pageSize);
        List<SoOrderProdTraceFile> soOrderProdTraceFileList = soOrderProdTraceFileDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        List<Integer> fileIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for(SoOrderProdTraceFile orderProdTraceFile : soOrderProdTraceFileList) {
            fileIds.add(orderProdTraceFile.getFileId());
            userIds.add(orderProdTraceFile.getAddUserId());
        }

        // 根据用户ID集合查询MAP信息  TODO
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIds);
        // 根据文件ID集合查询上传材料信息
        Map<Integer, BdFile> fileMap = bdFileService.findMapByIds(fileIds);

        UcUser user = null;
        for(SoOrderProdTraceFile orderProdTraceFile : soOrderProdTraceFileList) {
            orderProdTraceFile.setFile(fileMap.get(orderProdTraceFile.getFileId()));

            user = userMap.get(orderProdTraceFile.getAddUserId());
            if(user == null) {
                orderProdTraceFile.setAddUserName("");
            } else {
                orderProdTraceFile.setAddUserName(user.getRealName());
            }
        }
        pager.setList(soOrderProdTraceFileList);


        // 已经上传文件的数量
        int alreadyUploadFilesCount = 0;
        // 需要上传文件的数量
        int needUploadFilesCount = 0;
        // 需要上传文件的名字集合
        List<String> needUploadFiles = new ArrayList<>();
        Map<Object, Object> extend = new HashMap<>();
        extend.put("alreadyUploadFilesCount", alreadyUploadFilesCount);
        extend.put("needUploadFilesCount", needUploadFilesCount);
        extend.put("needUploadFiles", StringUtils.join(needUploadFiles, "、"));
        pager.setExtend(extend);

        // 根据产品订单ID查询产品处理流程材料
        List<ProdWorkflowFile> lstWorkflowFile = prodWorkflowFileService.queryWorkflowFileListByOrderProdId(orderProdId);
        if(CollectionUtils.isEmpty(lstWorkflowFile)) {
            return pager;
        }
        // 必须上传的材料MAP
        Map<Integer, ProdWorkflowFile> workflowFileMap = new HashMap<>();
        for(ProdWorkflowFile workflowFile : lstWorkflowFile) {
            // isMust=1必须上传的材料
            if(workflowFile.getIsMust() == 1) {
                workflowFileMap.put(workflowFile.getPkid(), workflowFile);
            }
        }
        if(CollectionUtils.isEmpty(workflowFileMap)) {
            return pager;
        }
        map.put("prod_workflow_file_id", workflowFileMap.keySet());
        // 封装状态为1051:待审核,1052:审核通过的
        List<Integer> lstStatus = new ArrayList<>();
        lstStatus.add(1051);
        lstStatus.add(1052);
        map.put("audit_status_id", lstStatus);
        // 查询已经必须上传的文件材料的数量
        // (key为文件ID，value为这个文件的数量，这个数量可能会大于等于1，因为一个必传的材料可能会上传多次。所以下方用到此MAP的时候，需要去掉重复，只要这个文件在这个MAP中有数量就只算1即可)
        Map<Integer, Integer> fileCountMap = queryTraceFileMap(map);

        Integer count = null;
        ProdWorkflowFile workflowFile = null;
        for(Integer workflowFileId : workflowFileMap.keySet()) {
            count = fileCountMap.get(workflowFileId);
            if(count == null || count <= 0) {
                needUploadFilesCount++;
                workflowFile = workflowFileMap.get(workflowFileId);
                if(workflowFile != null) {
                    needUploadFiles.add(StringUtils.trimToEmpty(workflowFile.getName()));
                }
            } else {
                // fileCountMap中获取的count为这个文件的数量，这个数量可能会大于等于1.
                // 因为一个必传的材料可能会上传多次。所以需要去掉重复，只要这个文件在这个MAP中有数量就只算1，
                // 即alreadyUploadFilesCount++;而不是即alreadyUploadFilesCount+count
                alreadyUploadFilesCount++;
            }
        }
        extend.put("alreadyUploadFilesCount", alreadyUploadFilesCount);
        extend.put("needUploadFilesCount", needUploadFilesCount);
        extend.put("needUploadFiles", StringUtils.join(needUploadFiles, "、"));
        pager.setExtend(extend);
        return pager;
    }

    @Override
    public Map<Integer, Integer> queryTraceFileMap(Map<String, Object> properties) {
        return soOrderProdTraceFileDao.queryTraceFileMap(properties);
    }

    @Override
    public List<SoOrderProdTraceFile> findByProperties(Map<String, Object> properties) {
        return soOrderProdTraceFileDao.findByProperties(properties, -1, -1);
    }

    @Override
    public String getNeedUploadFileNames(Integer orderProdId) {
        if(orderProdId == null || orderProdId <= 0) {
            return null;
        }
        // 根据产品订单ID和上传材料类型查询跟进记录ID集合
        List<Integer> traceIds = soOrderProdTraceService.queryOrderProdTraceIds(orderProdId, 3153);
        if(CollectionUtils.isEmpty(traceIds)) {
            return null;
        }


        // 根据产品订单ID查询产品处理流程材料
        List<ProdWorkflowFile> lstWorkflowFile = prodWorkflowFileService.queryWorkflowFileListByOrderProdId(orderProdId);
        if(CollectionUtils.isEmpty(lstWorkflowFile)) {
            return null;
        }
        // 必须上传的材料MAP
        Map<Integer, ProdWorkflowFile> workflowFileMap = new HashMap<>();
        for(ProdWorkflowFile workflowFile : lstWorkflowFile) {
            // isMust=1必须上传的材料
            if(workflowFile.getIsMust() == 1) {
                workflowFileMap.put(workflowFile.getPkid(), workflowFile);
            }
        }
        if(CollectionUtils.isEmpty(workflowFileMap)) {
            return null;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("order_prod_trace_id", traceIds);
        condition.put("prod_workflow_file_id", workflowFileMap.keySet());
        // 封装状态为1051:待审核,1052:审核通过的
        List<Integer> lstStatus = new ArrayList<>();
        lstStatus.add(1051);
        lstStatus.add(1052);
        condition.put("audit_status_id", lstStatus);
        // 查询已经必须上传的文件材料的数量
        // (key为文件ID，value为这个文件的数量，这个数量可能会大于等于1，因为一个必传的材料可能会上传多次。所以下方用到此MAP的时候，需要去掉重复，只要这个文件在这个MAP中有数量就只算1即可)
        Map<Integer, Integer> fileCountMap = queryTraceFileMap(condition);

        Integer count = null;
        ProdWorkflowFile workflowFile = null;
        // 需要上传文件的名字集合
        List<String> needUploadFiles = new ArrayList<>();
        for(Integer workflowFileId : workflowFileMap.keySet()) {
            count = fileCountMap.get(workflowFileId);
            if(count == null || count <= 0) {
                workflowFile = workflowFileMap.get(workflowFileId);
                if(workflowFile != null) {
                    needUploadFiles.add(StringUtils.trimToEmpty(workflowFile.getName()));
                }
            }
        }
        return StringUtils.join(needUploadFiles, "、");
    }

    @Override
    public List<Integer> findProdWorkflowFileIdByOrderProdTraceId(List<Integer> pkidList)
    {
        return soOrderProdTraceFileDao.findProdWorkflowFileIdByOrderProdTraceId(pkidList);
    }

}