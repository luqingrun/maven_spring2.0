package com.gongsibao.module.sys.bdauditlog.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorder.entity.AuditNode;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.logging.Level;

public interface BdAuditLogService {

    /**
     * 得到审核日志记录接口
     *
     * @param pkid
     * @return BdAuditLog
     */
    BdAuditLog findById(Integer pkid);

    /**
     * 得到审核日志列表接口
     *
     * @param properties
     * @return List
     */
    List<BdAuditLog> listByProperties(Map<String, Object> properties);


    Map<Integer, List<BdAuditLog>> getLevelMapByProperties(Map<String, Object> properties);

    List<List<AuditNode>> getAuditProcess(Integer formId, Integer typeId);

    /**
     * 保存单条审核日志记录接口
     *
     * @param bdAuditLog
     * @return Integer
     */
    Integer insert(BdAuditLog bdAuditLog);

    /**
     * 批量保存审核日志记录接口
     *
     * @param list
     */
    void insertBatch(final List<BdAuditLog> list);

    /**
     * 更新审核日志记录接口
     *
     * @param bdAuditLog
     * @return int
     */
    int update(BdAuditLog bdAuditLog);


    List<BdAuditLog> findByIds(List<Integer> pkidList);

    Map<Integer, BdAuditLog> findMapByIds(List<Integer> pkidList);

    Pager<BdAuditLog> pageByProperties(Map<String, Object> properties, int page, int pageSize);

    int delete(Integer pkid);

    /**
     * 查询审核人待审核任务日志
     *
     * @param auditUserId
     * @param type
     * @param formId
     * @return
     */
    BdAuditLog getUserToauditLog(Integer auditUserId, Integer type, Integer formId, Integer status);

    /**
     * 更新当前状态
     *
     * @param pkid
     * @param status
     * @param oldStatus
     * @return
     */
    public int updateStatus(Integer pkid, Integer status, Integer oldStatus, String remark);

    /**
     * 更新整层节点状态
     *
     * @param formId
     * @param typeId
     * @param exceptId 不做更新的pkid
     * @param level
     * @param status
     * @return
     */
    default int updateLevelStatus(Integer formId, Integer typeId, Integer exceptId, Integer level, Integer status) {
        return updateLevelStatus(formId, typeId, exceptId, level, status, "=");
    }

    /**
     * 更新审核层级节点状态
     *
     * @param formId
     * @param typeId
     * @param exceptId
     * @param level
     * @param status
     * @param logic
     * @return
     */
    public int updateLevelStatus(Integer formId, Integer typeId, Integer exceptId, Integer level, Integer status, String logic);

    public int updateStatus(Collection<Integer> pkids, Integer status, Integer oldStatus);
}