package com.gongsibao.module.order.soorderitemfile.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitemfile.dao.SoOrderItemFileDao;
import com.gongsibao.module.order.soorderitemfile.entity.SoOrderItemFile;
import com.gongsibao.module.order.soorderitemfile.service.SoOrderItemFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderItemFileService")
public class SoOrderItemFileServiceImpl implements SoOrderItemFileService {
    @Autowired
    private SoOrderItemFileDao soOrderItemFileDao;

    @Override
    public SoOrderItemFile findById(Integer pkid) {
        return soOrderItemFileDao.findById(pkid);
    }

    @Override
    public int update(SoOrderItemFile soOrderItemFile) {
        return soOrderItemFileDao.update(soOrderItemFile);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderItemFileDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderItemFile soOrderItemFile) {
        return soOrderItemFileDao.insert(soOrderItemFile);
    }

    @Override
    public List<SoOrderItemFile> findByIds(List<Integer> pkidList) {
        return soOrderItemFileDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderItemFile> findMapByIds(List<Integer> pkidList) {
        List<SoOrderItemFile> list = findByIds(pkidList);
        Map<Integer, SoOrderItemFile> map = new HashMap<>();
        for(SoOrderItemFile soOrderItemFile : list){
            map.put(soOrderItemFile.getPkid(), soOrderItemFile);
        }
        return map;
    }

    @Override
    public Pager<SoOrderItemFile> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderItemFileDao.countByProperties(map);
        Pager<SoOrderItemFile> pager = new Pager<>(totalRows, page);
        List<SoOrderItemFile> soOrderItemFileList = soOrderItemFileDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderItemFileList);
        return pager;
    }

}