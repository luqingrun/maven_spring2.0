package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsRecommendServiceDao;
import com.gongsibao.module.sys.cms.entity.CmsRecommendCategory;
import com.gongsibao.module.sys.cms.entity.CmsRecommendService;
import com.gongsibao.module.sys.cms.service.CmsRecommendCategoryService;
import com.gongsibao.module.sys.cms.service.CmsRecommendServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsRecommendServiceService")
public class CmsRecommendServiceServiceImpl implements CmsRecommendServiceService {
    @Autowired
    private CmsRecommendServiceDao cmsRecommendServiceDao;

    @Autowired
    private CmsRecommendCategoryService cmsRecommendCategoryService;

    @Override
    public CmsRecommendService findById(Integer pkid) {
        CmsRecommendService recommendService = cmsRecommendServiceDao.findById(pkid);
        setCmsRecommendCategory(recommendService);
        return recommendService;
    }

    @Override
    public int update(CmsRecommendService cmsRecommendService) {
        return cmsRecommendServiceDao.update(cmsRecommendService);
    }

    @Override
    public int updateInfo(CmsRecommendService cmsRecommendService) {
        return cmsRecommendServiceDao.updateInfo(cmsRecommendService);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsRecommendServiceDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsRecommendService cmsRecommendService) {
        Integer pkid = cmsRecommendServiceDao.insert(cmsRecommendService);
        cmsRecommendServiceDao.updateSort(pkid, pkid, cmsRecommendService.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsRecommendService> findByIds(List<Integer> pkidList) {
        List<CmsRecommendService> list = cmsRecommendServiceDao.findByIds(pkidList);
        setCmsRecommendCategory(list);
        return list;
    }

    @Override
    public Map<Integer, CmsRecommendService> findMapByIds(List<Integer> pkidList) {
        List<CmsRecommendService> list = findByIds(pkidList);
        Map<Integer, CmsRecommendService> map = new HashMap<>();
        for (CmsRecommendService cmsRecommendService : list) {
            map.put(cmsRecommendService.getPkid(), cmsRecommendService);
        }
        return map;
    }

    @Override
    public Pager<CmsRecommendService> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsRecommendServiceDao.countByProperties(map);
        Pager<CmsRecommendService> pager = new Pager<>(totalRows, page);
        List<CmsRecommendService> cmsRecommendServiceList = cmsRecommendServiceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsRecommendServiceList);
        return pager;
    }

    private void setCmsRecommendCategory(CmsRecommendService recommendService) {
        setCmsRecommendCategory(new ArrayList<CmsRecommendService>() {{
            add(recommendService);
        }});
    }

    private void setCmsRecommendCategory(List<CmsRecommendService> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<Integer> categoryIds = new ArrayList<>();
        for (CmsRecommendService recommendService : list) {
            categoryIds.add(recommendService.getCategoryId());
        }

        Map<Integer, CmsRecommendCategory> categoryMap = cmsRecommendCategoryService.findMapByIds(categoryIds);
        for (CmsRecommendService recommendService : list) {
            recommendService.setRecommendCategory(categoryMap.get(recommendService.getCategoryId()));
        }
    }

    @Override
    public Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId) {
        CmsRecommendService recommendService = findById(pkid);
        if (null == recommendService) {
            return false;
        }

        CmsRecommendService nearest = cmsRecommendServiceDao.getNearest(categoryId, recommendService.getSort(), isUp);
        if (null == nearest) {
            return true;
        }
        cmsRecommendServiceDao.updateSort(pkid, nearest.getSort(), userId);
        cmsRecommendServiceDao.updateSort(nearest.getPkid(), recommendService.getSort(), userId);
        return true;
    }

    @Override
    public void editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId) {
        cmsRecommendServiceDao.updateStatus(null, categoryId, CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsRecommendServiceDao.updateStatus(pkids, categoryId, CMSBase.STATUS_PUBLISH, userId);
        }
    }

    @Override
    public List<CmsRecommendService> listByProperties(Map<String, Object> properties) {
        return cmsRecommendServiceDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Map<Integer, List<CmsRecommendService>> getMapByCategoryIds(Collection<Integer> categoryIds, Integer... statuses) {
        Map<Integer, List<CmsRecommendService>> result = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("category_id", categoryIds);
        if (ArrayUtils.isNotEmpty(statuses)) {
            properties.put("status", statuses);
        }

        List<CmsRecommendService> list = listByProperties(properties);
        for (CmsRecommendService service : list) {
            Integer categoryId = service.getCategoryId();
            List<CmsRecommendService> serviceList = result.get(categoryId);
            if (null == serviceList) {
                serviceList = new ArrayList<>();
                result.put(categoryId, serviceList);
            }
            serviceList.add(service);
        }

        return result;
    }

//    @Override
//    public List<CmsRecommendService> getIndexList(Map<String, Object> properties) {
//        List<CmsRecommendService> list = listByProperties(properties);
//        if (CollectionUtils.isEmpty(list)) {
//            return list;
//        }
//
//        for (CmsRecommendService recommendService : list) {
//            recommendService.setPkid(0);
//        }
//        return list;
//    }
}