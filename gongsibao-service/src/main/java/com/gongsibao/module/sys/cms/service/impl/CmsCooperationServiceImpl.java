package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsCooperationDao;
import com.gongsibao.module.sys.cms.entity.CmsCooperation;
import com.gongsibao.module.sys.cms.service.CmsCooperationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsCooperationService")
public class CmsCooperationServiceImpl implements CmsCooperationService {
    @Autowired
    private CmsCooperationDao cmsCooperationDao;

    @Override
    public CmsCooperation findById(Integer pkid) {
        return cmsCooperationDao.findById(pkid);
    }

    @Override
    public int update(CmsCooperation cmsCooperation) {
        return cmsCooperationDao.update(cmsCooperation);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsCooperationDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);

    }

    @Override
    public Integer insert(CmsCooperation cmsCooperation) {
        Integer pkid = cmsCooperationDao.insert(cmsCooperation);
        cmsCooperationDao.updateSort(pkid,pkid,cmsCooperation.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsCooperation> findByIds(List<Integer> pkidList) {
        return cmsCooperationDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsCooperation> findMapByIds(List<Integer> pkidList) {
        List<CmsCooperation> list = findByIds(pkidList);
        Map<Integer, CmsCooperation> map = new HashMap<>();
        for (CmsCooperation cmsCooperation : list) {
            map.put(cmsCooperation.getPkid(), cmsCooperation);
        }
        return map;
    }

    @Override
    public Pager<CmsCooperation> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsCooperationDao.countByProperties(map);
        Pager<CmsCooperation> pager = new Pager<>(totalRows, page);
        List<CmsCooperation> cmsCooperationList = cmsCooperationDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsCooperationList);
        return pager;
    }

    @Override
    public List<CmsCooperation> listByProperties(Map<String, Object> properties) {
        return cmsCooperationDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {

        CmsCooperation cmsCooperation = cmsCooperationDao.findById(pkid);
        if (null == cmsCooperation) {
            return false;
        }
        CmsCooperation nearest = cmsCooperationDao.getNearest(cmsCooperation.getSort(), isUp);
        if(null == nearest ){
            return  true;
        }
        cmsCooperationDao.updateSort(nearest.getPkid(), cmsCooperation.getSort(), userId);
        cmsCooperationDao.updateSort(cmsCooperation.getPkid(), nearest.getSort(), userId);

        return true;
    }



    @Override
    public List<CmsCooperation> getIndexList(Map<String, Object> properties) {
        List<CmsCooperation> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        for (CmsCooperation cooperation : list) {
            cooperation.setPkid(0);
        }
        return list;
    }

    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer userId) {

        cmsCooperationDao.updateStatus(new ArrayList<Integer>(), CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsCooperationDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }

}