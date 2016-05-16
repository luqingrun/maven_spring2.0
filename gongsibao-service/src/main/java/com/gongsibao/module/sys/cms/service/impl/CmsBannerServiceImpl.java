package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsBannerDao;
import com.gongsibao.module.sys.cms.entity.CmsBanner;
import com.gongsibao.module.sys.cms.service.CmsBannerService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsBannerService")
public class CmsBannerServiceImpl implements CmsBannerService {
    @Autowired
    private CmsBannerDao cmsBannerDao;

    @Override
    public CmsBanner findById(Integer pkid) {
        return cmsBannerDao.findById(pkid);
    }

    @Override
    public int update(CmsBanner cmsBanner) {
        return cmsBannerDao.update(cmsBanner);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsBannerDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsBanner cmsBanner) {
        Integer pkid = cmsBannerDao.insert(cmsBanner);
        cmsBannerDao.updateSort(pkid, pkid, cmsBanner.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsBanner> findByIds(List<Integer> pkidList) {
        return cmsBannerDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsBanner> findMapByIds(List<Integer> pkidList) {
        List<CmsBanner> list = findByIds(pkidList);
        Map<Integer, CmsBanner> map = new HashMap<>();
        for (CmsBanner cmsBanner : list) {
            map.put(cmsBanner.getPkid(), cmsBanner);
        }
        return map;
    }

    @Override
    public Pager<CmsBanner> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsBannerDao.countByProperties(map);
        Pager<CmsBanner> pager = new Pager<>(totalRows, page);
        List<CmsBanner> cmsBannerList = cmsBannerDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsBannerList);
        return pager;
    }

    @Override
    public List<CmsBanner> listByProperties(Map<String, Object> properties) {
        return cmsBannerDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {
        // findbyid
        CmsBanner cmsBanner = cmsBannerDao.findById(pkid);
        if (null == cmsBanner) {
            return false;
        }
        CmsBanner nearestBanner = cmsBannerDao.getNearestBanner(cmsBanner.getSort(), isUp);
        if (null == nearestBanner) {
            return true;
        }
        // 更新sort
        cmsBannerDao.updateSort(nearestBanner.getPkid(), cmsBanner.getSort(), userId);
        cmsBannerDao.updateSort(pkid, nearestBanner.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsBanner> getIndexList(Map<String, Object> properties) {
        List<CmsBanner> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        for (CmsBanner cmsBanner : list) {
            cmsBanner.setPkid(0);
        }
        return list;
    }

    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer userId) {
        cmsBannerDao.updateStatus(new ArrayList<Integer>(), CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsBannerDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }

}