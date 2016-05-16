package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.dao.CmsTitleDescDao;
import com.gongsibao.module.sys.cms.entity.CmsTitleDesc;
import com.gongsibao.module.sys.cms.service.CmsTitleDescService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("cmsTitleDescService")
public class CmsTitleDescServiceImpl implements CmsTitleDescService {
    @Autowired
    private CmsTitleDescDao cmsTitleDescDao;

    @Override
    public CmsTitleDesc findById(Integer pkid) {
        return cmsTitleDescDao.findById(pkid);
    }

    @Override
    public CmsTitleDesc get() {
        List<CmsTitleDesc> list = cmsTitleDescDao.findByProperties(null, 0, 1);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        CmsTitleDesc cmsTitleDesc = list.get(0);
        return cmsTitleDesc;
    }

    @Override
    public int update(CmsTitleDesc cmsTitleDesc) {
        return cmsTitleDescDao.update(cmsTitleDesc);
    }

    @Override
    public int delete(Integer pkid) {
        return cmsTitleDescDao.delete(pkid);
    }

    @Override
    public Integer insert(CmsTitleDesc cmsTitleDesc) {
        return cmsTitleDescDao.insert(cmsTitleDesc);
    }

    @Override
    public List<CmsTitleDesc> findByIds(List<Integer> pkidList) {
        return cmsTitleDescDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsTitleDesc> findMapByIds(List<Integer> pkidList) {
        List<CmsTitleDesc> list = findByIds(pkidList);
        Map<Integer, CmsTitleDesc> map = new HashMap<>();
        for (CmsTitleDesc cmsTitleDesc : list) {
            map.put(cmsTitleDesc.getPkid(), cmsTitleDesc);
        }
        return map;
    }

    @Override
    public Pager<CmsTitleDesc> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsTitleDescDao.countByProperties(map);
        Pager<CmsTitleDesc> pager = new Pager<>(totalRows, page);
        List<CmsTitleDesc> cmsTitleDescList = cmsTitleDescDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsTitleDescList);
        return pager;
    }

    @Override
    public CmsTitleDesc getIndex() {
        CmsTitleDesc cmsTitleDesc = get();
        if (null != cmsTitleDesc) {
            cmsTitleDesc.setPkid(0);
        }
        return cmsTitleDesc;
    }
}