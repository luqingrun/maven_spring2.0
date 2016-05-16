package com.gongsibao.module.sys.bdfile.service.impl;

import com.gongsibao.module.sys.bdfile.dao.BdFileDao;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("bdFileService")
public class BdFileServiceImpl implements BdFileService {
    @Autowired
    private BdFileDao bdFileDao;

    @Override
    public BdFile findById(Integer pkid) {
        return bdFileDao.findById(pkid);
    }

    @Override
    public int delete(Integer pkid) {
        return bdFileDao.delete(pkid);
    }

    @Override
    public Integer insert(BdFile bdFile) {
        return bdFileDao.insert(bdFile);
    }

    @Override
    public int[] insertBatch(List<BdFile> bdFiles) {
        if (CollectionUtils.isEmpty(bdFiles)) {
            return new int[0];
        }
        return bdFileDao.insertBatch(bdFiles);
    }

    @Override
    public List<BdFile> findByIds(List<Integer> pkidList) {
        return bdFileDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, BdFile> findMapByIds(List<Integer> pkidList) {
        Map<Integer, BdFile> map = new HashMap<>();
        if(CollectionUtils.isEmpty(pkidList)) {
            return map;
        }
        List<BdFile> list = findByIds(pkidList);
        if(CollectionUtils.isEmpty(list)) {
            return map;
        }
        for(BdFile file : list){
            map.put(file.getPkid(), file);
        }
        return map;
    }

    @Override
    public Integer deleteByFormId(Integer formId, String tabName) {
        return bdFileDao.deleteByFormId(formId, tabName);
    }

    @Override
    public List<BdFile> getListByFormId(Integer formId, String tabName) {
        return bdFileDao.getListByFormId(formId, tabName);
    }

    @Override
    public Map<Integer, List<BdFile>> getMapByFormIds(Collection<Integer> formIds, String tabName) {
        Map<Integer, List<BdFile>> result = new HashMap<>();

        List<BdFile> list = bdFileDao.getListByFormIds(formIds, tabName);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        for (BdFile file : list) {
            Integer formId = file.getFormId();
            List<BdFile> bdFiles = result.get(formId);
            if (null == bdFiles) {
                bdFiles = new ArrayList<>();
                result.put(formId, bdFiles);
            }

            bdFiles.add(file);
        }

        return result;
    }

}