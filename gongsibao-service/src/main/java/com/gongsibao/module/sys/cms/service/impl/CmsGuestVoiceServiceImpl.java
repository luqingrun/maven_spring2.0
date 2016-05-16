package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsGuestVoiceDao;
import com.gongsibao.module.sys.cms.entity.CmsGuestVoice;
import com.gongsibao.module.sys.cms.service.CmsGuestVoiceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsGuestVoiceService")
public class CmsGuestVoiceServiceImpl implements CmsGuestVoiceService {
    @Autowired
    private CmsGuestVoiceDao cmsGuestVoiceDao;

    @Override
    public CmsGuestVoice findById(Integer pkid) {
        return cmsGuestVoiceDao.findById(pkid);
    }

    @Override
    public int update(CmsGuestVoice cmsGuestVoice) {
        return cmsGuestVoiceDao.update(cmsGuestVoice);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsGuestVoiceDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsGuestVoice cmsGuestVoice) {
        Integer pkid = cmsGuestVoiceDao.insert(cmsGuestVoice);
        cmsGuestVoiceDao.updateSort(pkid, pkid, cmsGuestVoice.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsGuestVoice> findByIds(List<Integer> pkidList) {
        return cmsGuestVoiceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsGuestVoice> findMapByIds(List<Integer> pkidList) {
        List<CmsGuestVoice> list = findByIds(pkidList);
        Map<Integer, CmsGuestVoice> map = new HashMap<>();
        for(CmsGuestVoice cmsGuestVoice : list){
            map.put(cmsGuestVoice.getPkid(), cmsGuestVoice);
        }
        return map;
    }

    @Override
    public Pager<CmsGuestVoice> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsGuestVoiceDao.countByProperties(map);
        Pager<CmsGuestVoice> pager = new Pager<>(totalRows, page);
        List<CmsGuestVoice> cmsGuestVoiceList = cmsGuestVoiceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsGuestVoiceList);
        return pager;
    }

    @Override
    public List<CmsGuestVoice> listByProperties(Map<String, Object> properties) {
        return cmsGuestVoiceDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }


    @Override
    public Boolean editSort(Integer pkid, Boolean isUp, Integer userId) {
        CmsGuestVoice guestVoice = findById(pkid);
        if (null == guestVoice) {
            return false;
        }

        CmsGuestVoice nearest = cmsGuestVoiceDao.getNearest(guestVoice.getSort(), isUp);
        if (null == nearest) {
            return true;
        }
        cmsGuestVoiceDao.updateSort(pkid, nearest.getSort(), userId);
        cmsGuestVoiceDao.updateSort(nearest.getPkid(), guestVoice.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsGuestVoice> getIndexList(Map<String, Object> properties) {
        List<CmsGuestVoice> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        for (CmsGuestVoice guestVoice : list) {
            guestVoice.setPkid(0);
        }
        return list;
    }



    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer userId) {

        cmsGuestVoiceDao.updateStatus(new ArrayList<Integer>(), CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsGuestVoiceDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }

}