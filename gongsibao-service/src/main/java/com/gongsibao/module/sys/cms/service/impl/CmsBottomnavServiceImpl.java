package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsBottomnavDao;
import com.gongsibao.module.sys.cms.entity.CmsBottomCategory;
import com.gongsibao.module.sys.cms.entity.CmsBottomnav;
import com.gongsibao.module.sys.cms.service.CmsBottomnavService;
import com.gongsibao.util.constant.ConstantDic;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsBottomnavService")
public class CmsBottomnavServiceImpl implements CmsBottomnavService {
    @Autowired
    private CmsBottomnavDao cmsBottomnavDao;

    @Override
    public CmsBottomnav findById(Integer pkid) {
        return cmsBottomnavDao.findById(pkid);
    }

    @Override
    public int update(CmsBottomnav cmsBottomnav) {
        return cmsBottomnavDao.update(cmsBottomnav);
    }

    @Override
    public int updateInfo(CmsBottomnav cmsBottomnav) {
        return cmsBottomnavDao.updateInfo(cmsBottomnav);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsBottomnavDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsBottomnav cmsBottomnav) {
        Integer pkid = cmsBottomnavDao.insert(cmsBottomnav);
        cmsBottomnavDao.updateSort(pkid, pkid, cmsBottomnav.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsBottomnav> findByIds(List<Integer> pkidList) {
        return cmsBottomnavDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsBottomnav> findMapByIds(List<Integer> pkidList) {
        List<CmsBottomnav> list = findByIds(pkidList);
        Map<Integer, CmsBottomnav> map = new HashMap<>();
        for (CmsBottomnav cmsBottomnav : list) {
            map.put(cmsBottomnav.getPkid(), cmsBottomnav);
        }
        return map;
    }

    @Override
    public Pager<CmsBottomnav> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsBottomnavDao.countByProperties(map);
        Pager<CmsBottomnav> pager = new Pager<>(totalRows, page);
        List<CmsBottomnav> cmsBottomnavList = cmsBottomnavDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsBottomnavList);
        return pager;
    }

    @Override
    public List<CmsBottomnav> listByProperties(Map<String, Object> properties) {
        return cmsBottomnavDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId) {
        CmsBottomnav cmsBottomnav = findById(pkid);
        if (null == cmsBottomnav) {
            return false;
        }

        CmsBottomnav nearest = cmsBottomnavDao.getNearest(cmsBottomnav.getSort(), categoryId, isUp);
        if (null == nearest) {
            return true;
        }
        cmsBottomnavDao.updateSort(pkid, nearest.getSort(), userId);
        cmsBottomnavDao.updateSort(nearest.getPkid(), cmsBottomnav.getSort(), userId);
        return true;
    }

    @Override
    public List<Map<String, Object>> getIndexList(Map<String, Object> properties) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<Integer, List<CmsBottomnav>> bottomMap = new HashMap<>();

        List<CmsBottomnav> bottomnavList = listByProperties(properties);
        if (CollectionUtils.isEmpty(bottomnavList)) {
            return result;
        }

        for (CmsBottomnav bottomnav : bottomnavList) {
            List<CmsBottomnav> cmsBottomnavList = bottomMap.get(bottomnav.getBottomCategory());
            if (null == cmsBottomnavList) {
                cmsBottomnavList = new ArrayList<>();
                bottomMap.put(bottomnav.getBottomCategory(), cmsBottomnavList);
            }
            cmsBottomnavList.add(bottomnav);
            bottomnav.setPkid(0);
        }

        for (CmsBottomCategory category : ConstantDic.BOTTOM_CATEGORY) {
            List<CmsBottomnav> blist = bottomMap.get(category.getPkid());

            Map<String, Object> map = new HashMap<>();
            map.put("name", category.getName());
            map.put("list", blist);

            result.add(map);
        }

        return result;
    }

    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId) {

        cmsBottomnavDao.updateStatus(new ArrayList<Integer>(), categoryId, CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsBottomnavDao.updateStatus(pkids, categoryId, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }
}