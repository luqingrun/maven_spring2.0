package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsFriendLinkDao;
import com.gongsibao.module.sys.cms.entity.CmsFriendLink;
import com.gongsibao.module.sys.cms.service.CmsFriendLinkService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsFriendLinkService")
public class CmsFriendLinkServiceImpl implements CmsFriendLinkService {
    @Autowired
    private CmsFriendLinkDao cmsFriendLinkDao;

    @Override
    public CmsFriendLink findById(Integer pkid) {
        return cmsFriendLinkDao.findById(pkid);
    }

    @Override
    public int update(CmsFriendLink cmsFriendLink) {
        return cmsFriendLinkDao.update(cmsFriendLink);
    }

    @Override
    public int updateInfo(CmsFriendLink cmsFriendLink) {
        return cmsFriendLinkDao.updateInfo(cmsFriendLink);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsFriendLinkDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsFriendLink cmsFriendLink) {
        Integer pkid = cmsFriendLinkDao.insert(cmsFriendLink);
        cmsFriendLinkDao.updateSort(pkid, pkid, cmsFriendLink.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsFriendLink> findByIds(List<Integer> pkidList) {
        return cmsFriendLinkDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsFriendLink> findMapByIds(List<Integer> pkidList) {
        List<CmsFriendLink> list = findByIds(pkidList);
        Map<Integer, CmsFriendLink> map = new HashMap<>();
        for (CmsFriendLink cmsFriendLink : list) {
            map.put(cmsFriendLink.getPkid(), cmsFriendLink);
        }
        return map;
    }

    @Override
    public Pager<CmsFriendLink> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsFriendLinkDao.countByProperties(map);
        Pager<CmsFriendLink> pager = new Pager<>(totalRows, page);
        List<CmsFriendLink> cmsFriendLinkList = cmsFriendLinkDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsFriendLinkList);
        return pager;
    }

    @Override
    public List<CmsFriendLink> listByProperties(Map<String, Object> properties) {
        return cmsFriendLinkDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public Boolean editSort(Integer pkid, Integer type, Boolean isUp, Integer userId) {
        CmsFriendLink cmsFriendLink = findById(pkid);
        if (null == cmsFriendLink) {
            return false;
        }
        CmsFriendLink nearest = cmsFriendLinkDao.getNearest(cmsFriendLink.getSort(), type, isUp);
        if (null == nearest) {
            return true;
        }
        cmsFriendLinkDao.updateSort(pkid, nearest.getSort(), userId);
        cmsFriendLinkDao.updateSort(nearest.getPkid(), cmsFriendLink.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsFriendLink> getIndexList(Map<String, Object> properties) {
        List<CmsFriendLink> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        for (CmsFriendLink friendLink : list) {
            friendLink.setPkid(0);
        }
        return list;
    }

    @Override
    public void editPublish(Collection<Integer> pkids, Integer type, Integer userId) {
        cmsFriendLinkDao.updateStatus(new ArrayList<Integer>(), type, CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsFriendLinkDao.updateStatus(pkids, type, CMSBase.STATUS_PUBLISH, userId);
        }
    }
}