package com.gongsibao.module.sys.bdauditlog.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorder.entity.AuditNode;
import com.gongsibao.module.sys.bdauditlog.dao.BdAuditLogDao;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("bdAuditLogService")
public class BdAuditLogServiceImpl implements BdAuditLogService {
    @Autowired
    private BdAuditLogDao bdAuditLogDao;

    @Autowired
    private UcUserService ucUserService;
    /**
     * 根据ID得到审核日志记录
     *
     * @param pkid
     * @return BdAuditLog
     */
    @Override
    public BdAuditLog findById(Integer pkid) {
        return bdAuditLogDao.findById(pkid);
    }

    /**
     * 得到审核日志列表
     *
     * @param properties
     * @return List
     */
    @Override
    public List<BdAuditLog> listByProperties(Map<String, Object> properties) {
        return bdAuditLogDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Map<Integer, List<BdAuditLog>> getLevelMapByProperties(Map<String, Object> properties) {
        Map<Integer, List<BdAuditLog>> result = new HashMap<>();

        List<BdAuditLog> logs = listByProperties(properties);
        if (CollectionUtils.isEmpty(logs)) {
            return result;
        }

        for (BdAuditLog log : logs) {
            Integer level = log.getLevel();
            List<BdAuditLog> auditLogList = result.get(level);
            if (null == auditLogList) {
                auditLogList = new ArrayList<>();
                result.put(level, auditLogList);
            }
            auditLogList.add(log);
        }
        return result;
    }

    @Override
    public List<List<AuditNode>> getAuditProcess(Integer formId, Integer typeId) {
        List<List<AuditNode>> result = new ArrayList<>();
        List<List<BdAuditLog>> list = new ArrayList<>();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("form_id", formId);
        paramMap.put("type_id", typeId);
        Map<Integer, List<BdAuditLog>> logsMap = getLevelMapByProperties(paramMap);
        if (MapUtils.isEmpty(logsMap)) {
            return result;
        }
        for (int i = 0; i < logsMap.keySet().size(); i++) {
            if (CollectionUtils.isNotEmpty(logsMap.get(i))) {
                list.add(logsMap.get(i));
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        // 封装用户信息
        List<Integer> ucids = new ArrayList<>();
        for (List<BdAuditLog> bdAuditLogs : list) {
            for (BdAuditLog bdAuditLog : bdAuditLogs) {
                ucids.add(bdAuditLog.getAddUserId());
            }
        }

        // 用户查询
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(ucids);

        for (List<BdAuditLog> bdAuditLogs : list) {
            List<AuditNode> levelList = new ArrayList<>();
            for (BdAuditLog bdAuditLog : bdAuditLogs) {
                UcUser ucUser = userMap.get(bdAuditLog.getAddUserId());
                if (null != ucUser) {
                    AuditNode an = new AuditNode();
                    an.setAuditStatus(bdAuditLog.getStatusId());
                    an.setName(ucUser.getRealName());
                    levelList.add(an);
                }
            }
            result.add(levelList);
        }
        return result;
    }


    /**
     * 保存单条审核日志记录
     *
     * @param bdAuditLog
     * @return Integer
     */
    @Override
    public Integer insert(BdAuditLog bdAuditLog) {
        return bdAuditLogDao.insert(bdAuditLog);
    }

    /**
     * 批量保存审核日志记录
     *
     * @param list
     */
    @Override
    public void insertBatch(final List<BdAuditLog> list) {
        bdAuditLogDao.insertBatch(list);
    }

    /**
     * 更新审核日志记录
     *
     * @param bdAuditLog
     * @return int
     */
    @Override
    public int update(BdAuditLog bdAuditLog) {
        return bdAuditLogDao.update(bdAuditLog);
    }


    @Override
    public int delete(Integer pkid) {
        return bdAuditLogDao.delete(pkid);
    }

    @Override
    public BdAuditLog getUserToauditLog(Integer auditUserId, Integer type, Integer formId, Integer status) {
        return bdAuditLogDao.getUserToauditLog(auditUserId, type, formId, status);
    }

    @Override
    public int updateStatus(Integer pkid, Integer status, Integer oldStatus, String remark) {
        return bdAuditLogDao.updateStatus(pkid, status, oldStatus, remark);
    }


    @Override
    public int updateLevelStatus(Integer formId, Integer typeId, Integer exceptId, Integer level, Integer status, String logic) {
        return bdAuditLogDao.updateLevelStatus(formId, typeId, exceptId, level, status, logic);
    }

    @Override
    public int updateStatus(Collection<Integer> pkids, Integer status, Integer oldStatus) {
        return bdAuditLogDao.updateStatus(pkids, status, oldStatus);
    }

    @Override
    public List<BdAuditLog> findByIds(List<Integer> pkidList) {
        return bdAuditLogDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, BdAuditLog> findMapByIds(List<Integer> pkidList) {
        List<BdAuditLog> list = findByIds(pkidList);
        Map<Integer, BdAuditLog> map = new HashMap<>();
        for (BdAuditLog bdAuditLog : list) {
            map.put(bdAuditLog.getPkid(), bdAuditLog);
        }
        return map;
    }

    @Override
    public Pager<BdAuditLog> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int totalRows = bdAuditLogDao.countByProperties(map);
        Pager<BdAuditLog> pager = new Pager<>(totalRows, page, pageSize);
        List<BdAuditLog> bdAuditLogList = bdAuditLogDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(bdAuditLogList);
        return pager;
    }

}