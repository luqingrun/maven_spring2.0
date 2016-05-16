package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsTopnavCategoryDao;
import com.gongsibao.module.sys.cms.entity.CmsTopnavCategory;
import com.gongsibao.module.sys.cms.entity.CmsTopnavLink;
import com.gongsibao.module.sys.cms.service.CmsTopnavCategoryService;
import com.gongsibao.module.sys.cms.service.CmsTopnavLinkService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsTopnavCategoryService")
public class CmsTopnavCategoryServiceImpl implements CmsTopnavCategoryService {
    @Autowired
    private CmsTopnavCategoryDao cmsTopnavCategoryDao;

    @Autowired
    private CmsTopnavLinkService cmsTopnavLinkService;

    @Override
    public CmsTopnavCategory findById(Integer pkid) {
        return cmsTopnavCategoryDao.findById(pkid);
    }

    @Override
    public int update(CmsTopnavCategory cmsTopnavCategory) {
        return cmsTopnavCategoryDao.update(cmsTopnavCategory);
    }

    @Override
    public int updateNameAndUrl(CmsTopnavCategory cmsTopnavCategory) {
        return cmsTopnavCategoryDao.updateNameAndUrl(cmsTopnavCategory);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsTopnavCategoryDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsTopnavCategory cmsTopnavCategory) {
        Integer pkid = cmsTopnavCategoryDao.insert(cmsTopnavCategory);
        cmsTopnavCategoryDao.updateSort(pkid, pkid, cmsTopnavCategory.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsTopnavCategory> findByIds(List<Integer> pkidList) {
        return cmsTopnavCategoryDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsTopnavCategory> findMapByIds(List<Integer> pkidList) {
        List<CmsTopnavCategory> list = findByIds(pkidList);
        Map<Integer, CmsTopnavCategory> map = new HashMap<>();
        for (CmsTopnavCategory cmsTopnavCategory : list) {
            map.put(cmsTopnavCategory.getPkid(), cmsTopnavCategory);
        }
        return map;
    }

    @Override
    public Pager<CmsTopnavCategory> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsTopnavCategoryDao.countByProperties(map);
        Pager<CmsTopnavCategory> pager = new Pager<>(totalRows, page);
        List<CmsTopnavCategory> cmsTopnavCategoryList = cmsTopnavCategoryDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsTopnavCategoryList);
        return pager;
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {
        return editSort(pkid, isUp, Boolean.TRUE,userId);
    }

    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Boolean isRoot, Integer userId) {
        CmsTopnavCategory cmsTopnavCategory = findById(pkid);
        if (null == cmsTopnavCategory) {
            return false;
        }

        CmsTopnavCategory nearest = cmsTopnavCategoryDao.getNearest(cmsTopnavCategory.getSort(), isUp, isRoot);
        if (null == nearest) {
            return true;
        }
        cmsTopnavCategoryDao.updateSort(pkid, nearest.getSort(), userId);
        cmsTopnavCategoryDao.updateSort(nearest.getPkid(), cmsTopnavCategory.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsTopnavCategory> listByProperties(Map<String, Object> properties) {
        List<CmsTopnavCategory> list = cmsTopnavCategoryDao.findByProperties(properties, 0, Integer.MAX_VALUE);
        return list;
    }

    @Override
    public List<CmsTopnavCategory> getIndexList(Map<String, Object> paramMap) {
        // 查询所有分类
        List<CmsTopnavCategory> list = listByProperties(paramMap);

        // 一级分类
        List<CmsTopnavCategory> rsList = new ArrayList<>();

        if (CollectionUtils.isEmpty(list)) {
            return rsList;
        }

        Iterator<CmsTopnavCategory> iterator = list.iterator();
        while (iterator.hasNext()) {
            CmsTopnavCategory category = iterator.next();
            if (category.getPid() == 0) {
                rsList.add(category);
                iterator.remove();
            }
        }

        if (CollectionUtils.isEmpty(rsList)) {
            return rsList;
        }

        // 二级分类列表
        List<Integer> categoryIds = new ArrayList<>();
        for (CmsTopnavCategory category : list) {
            categoryIds.add(category.getPkid());
        }

        // 二级分类对应链接
        Map<Integer, List<CmsTopnavLink>> linkMap = cmsTopnavLinkService.mapByCategoryIds(categoryIds, CMSBase.STATUS_PUBLISH);

        Map<Integer, List<CmsTopnavCategory>> cateMap = new HashMap<>();
        for (CmsTopnavCategory category : list) {
            Integer pid = category.getPid();

            // 二级分类设置链接列表
            List<CmsTopnavLink> linkList = linkMap.get(category.getPkid());
            if (CollectionUtils.isNotEmpty(linkList)) {
                for (CmsTopnavLink link : linkList) {
                    link.setPkid(0);
                }
                category.setTopnavLinks(linkList);
            }

            List<CmsTopnavCategory> categoryList = cateMap.get(pid);
            if (null == categoryList) {
                categoryList = new ArrayList<>();
                cateMap.put(pid, categoryList);
            }
            category.setPkid(0);
            categoryList.add(category);
        }

        // 一级分类设置child分类(二级)
        for (CmsTopnavCategory category : rsList) {
            category.setChildCategories(cateMap.get(category.getPkid()));
            category.setPkid(0);
        }

        return rsList;
    }

    @Override
    public void editPublish(Collection<Integer> pkids, Integer pid, Integer userId) {

        // 更新未删除的为初始状态
        cmsTopnavCategoryDao.updateStatus(new ArrayList<Integer>(), pid, CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            // 更新为发布状态
            cmsTopnavCategoryDao.updateStatus(pkids, pid, CMSBase.STATUS_PUBLISH, userId);
        }
    }

}