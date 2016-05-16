package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsRecommendPackageDao;
import com.gongsibao.module.sys.cms.entity.CmsRecommendPackage;
import com.gongsibao.module.sys.cms.service.CmsRecommendPackageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("cmsRecommendPackageService")
public class CmsRecommendPackageServiceImpl implements CmsRecommendPackageService {
    @Autowired
    private CmsRecommendPackageDao cmsRecommendPackageDao;

    @Override
    public CmsRecommendPackage findById(Integer pkid) {
        return cmsRecommendPackageDao.findById(pkid);
    }

    @Override
    public int update(CmsRecommendPackage cmsRecommendPackage) {
        return cmsRecommendPackageDao.update(cmsRecommendPackage);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsRecommendPackageDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsRecommendPackage cmsRecommendPackage) {
        Integer pkid = cmsRecommendPackageDao.insert(cmsRecommendPackage);
        cmsRecommendPackageDao.updateSort(pkid, pkid, cmsRecommendPackage.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsRecommendPackage> findByIds(List<Integer> pkidList) {
        return cmsRecommendPackageDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsRecommendPackage> findMapByIds(List<Integer> pkidList) {
        List<CmsRecommendPackage> list = findByIds(pkidList);
        Map<Integer, CmsRecommendPackage> map = new HashMap<>();
        for (CmsRecommendPackage cmsRecommendPackage : list) {
            map.put(cmsRecommendPackage.getPkid(), cmsRecommendPackage);
        }
        return map;
    }

    @Override
    public Pager<CmsRecommendPackage> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsRecommendPackageDao.countByProperties(map);
        Pager<CmsRecommendPackage> pager = new Pager<>(totalRows, page);
        List<CmsRecommendPackage> cmsRecommendPackageList = cmsRecommendPackageDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsRecommendPackageList);
        return pager;
    }

    @Override
    public List<CmsRecommendPackage> listByProperties(Map<String, Object> properties) {
        return cmsRecommendPackageDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {
        // findbyid
        CmsRecommendPackage cmsRecommendPackage = cmsRecommendPackageDao.findById(pkid);
        if (null == cmsRecommendPackage) {
            return false;
        }
        CmsRecommendPackage nearestRecommendPackage = cmsRecommendPackageDao.getNearestRecommendPackage(cmsRecommendPackage.getSort(), isUp);
        if (null == nearestRecommendPackage) {
            return true;
        }
        // 更新sort
        cmsRecommendPackageDao.updateSort(nearestRecommendPackage.getPkid(), cmsRecommendPackage.getSort(), userId);
        cmsRecommendPackageDao.updateSort(pkid, nearestRecommendPackage.getSort(), userId);
        return true;
    }



    @Override
    public List<CmsRecommendPackage> getIndexList(Map<String, Object> properties) {
        List<CmsRecommendPackage> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        for (CmsRecommendPackage recommendPackage : list) {
            recommendPackage.setPkid(0);
        }
        return list;
    }


    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer userId){
        cmsRecommendPackageDao.updateStatus(CMSBase.STATUS_INIT, userId);

        if(CollectionUtils.isNotEmpty(pkids)){
            cmsRecommendPackageDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }

}