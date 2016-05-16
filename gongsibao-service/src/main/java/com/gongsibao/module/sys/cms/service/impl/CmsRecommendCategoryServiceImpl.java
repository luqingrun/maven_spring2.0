package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsRecommendCategoryDao;
import com.gongsibao.module.sys.cms.entity.CmsRecommendCategory;
import com.gongsibao.module.sys.cms.service.CmsRecommendCategoryService;
import com.gongsibao.module.sys.cms.entity.CmsRecommendService;
import com.gongsibao.module.sys.cms.service.CmsRecommendServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsRecommendCategoryService")
public class CmsRecommendCategoryServiceImpl implements CmsRecommendCategoryService {
    @Autowired
    private CmsRecommendCategoryDao cmsRecommendCategoryDao;

    @Autowired
    private CmsRecommendServiceService cmsRecommendServiceService;

    @Override
    public CmsRecommendCategory findById(Integer pkid) {
        return cmsRecommendCategoryDao.findById(pkid);
    }

    @Override
    public int update(CmsRecommendCategory cmsRecommendCategory) {
        return cmsRecommendCategoryDao.update(cmsRecommendCategory);
    }

    @Override
    public int updateInfo(CmsRecommendCategory cmsRecommendCategory) {
        return cmsRecommendCategoryDao.updateInfo(cmsRecommendCategory);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsRecommendCategoryDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsRecommendCategory cmsRecommendCategory) {
        Integer pkid = cmsRecommendCategoryDao.insert(cmsRecommendCategory);
        cmsRecommendCategoryDao.updateSort(pkid, pkid, cmsRecommendCategory.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsRecommendCategory> findByIds(List<Integer> pkidList) {
        return cmsRecommendCategoryDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsRecommendCategory> findMapByIds(List<Integer> pkidList) {
        List<CmsRecommendCategory> list = findByIds(pkidList);
        Map<Integer, CmsRecommendCategory> map = new HashMap<>();
        for (CmsRecommendCategory cmsRecommendCategory : list) {
            map.put(cmsRecommendCategory.getPkid(), cmsRecommendCategory);
        }
        return map;
    }

    @Override
    public Pager<CmsRecommendCategory> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsRecommendCategoryDao.countByProperties(map);
        Pager<CmsRecommendCategory> pager = new Pager<>(totalRows, page);
        List<CmsRecommendCategory> cmsRecommendCategoryList = cmsRecommendCategoryDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsRecommendCategoryList);
        return pager;
    }

    @Override
    public List<CmsRecommendCategory> listByProperties(Map<String, Object> properties) {
        return cmsRecommendCategoryDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {
        CmsRecommendCategory recommendCategory = findById(pkid);
        if (null == recommendCategory) {
            return false;
        }

        CmsRecommendCategory nearest = cmsRecommendCategoryDao.getNearest(recommendCategory.getSort(), isUp);
        if (null == nearest) {
            return true;
        }
        cmsRecommendCategoryDao.updateSort(pkid, nearest.getSort(), userId);
        cmsRecommendCategoryDao.updateSort(nearest.getPkid(), recommendCategory.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsRecommendCategory> getIndexList(Map<String, Object> properties) {
        List<CmsRecommendCategory> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        setRecommendService(list);

        for (CmsRecommendCategory category : list) {
            category.setPkid(0);
            List<CmsRecommendService> recommendServices = category.getRecommendServices();
            if (CollectionUtils.isNotEmpty(recommendServices)) {
                for (CmsRecommendService service : recommendServices) {
                    service.setPkid(0);
                }
            }
        }

        return list;
    }

    @Override
    public void editPublish(Collection<Integer> ids, Integer userId) {
        cmsRecommendCategoryDao.updateStatus(new ArrayList<Integer>(), CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(ids)) {
            cmsRecommendCategoryDao.updateStatus(ids, CMSBase.STATUS_PUBLISH, userId);
        }
    }

    private void setRecommendService(CmsRecommendCategory category) {
        setRecommendService(new ArrayList<CmsRecommendCategory>() {{
            add(category);
        }});
    }

    private void setRecommendService(List<CmsRecommendCategory> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Integer> categoryIds = new ArrayList<>();
        for (CmsRecommendCategory category : list) {
            categoryIds.add(category.getPkid());
        }

        Map<Integer, List<CmsRecommendService>> serviceMap = cmsRecommendServiceService.getMapByCategoryIds(categoryIds, CMSBase.STATUS_PUBLISH);
        for (CmsRecommendCategory category : list) {
            category.setRecommendServices(serviceMap.get(category.getPkid()));
        }
    }

}