package com.gongsibao.module.uc.ucorganizationbddictmap.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucorganizationbddictmap.dao.UcOrganizationBdDictMapDao;
import com.gongsibao.module.uc.ucorganizationbddictmap.entity.UcOrganizationBdDictMap;
import com.gongsibao.module.uc.ucorganizationbddictmap.service.UcOrganizationBdDictMapService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("ucOrganizationBdDictMapService")
public class UcOrganizationBdDictMapServiceImpl implements UcOrganizationBdDictMapService {
    @Autowired
    private UcOrganizationBdDictMapDao ucOrganizationBdDictMapDao;

    @Override
    public UcOrganizationBdDictMap findById(Integer pkid) {
        return ucOrganizationBdDictMapDao.findById(pkid);
    }

    @Override
    public int update(UcOrganizationBdDictMap ucOrganizationBdDictMap) {
        return ucOrganizationBdDictMapDao.update(ucOrganizationBdDictMap);
    }

    @Override
    public int delete(Integer pkid) {
        return ucOrganizationBdDictMapDao.delete(pkid);
    }

    @Override
    public Integer insert(UcOrganizationBdDictMap ucOrganizationBdDictMap) {
        return ucOrganizationBdDictMapDao.insert(ucOrganizationBdDictMap);
    }

    @Override
    public List<UcOrganizationBdDictMap> findByIds(List<Integer> pkidList) {
        return ucOrganizationBdDictMapDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, UcOrganizationBdDictMap> findMapByIds(List<Integer> pkidList) {
        List<UcOrganizationBdDictMap> list = findByIds(pkidList);
        Map<Integer, UcOrganizationBdDictMap> map = new HashMap<>();
        for(UcOrganizationBdDictMap ucOrganizationBdDictMap : list){
            map.put(ucOrganizationBdDictMap.getPkid(), ucOrganizationBdDictMap);
        }
        return map;
    }

    @Override
    public Pager<UcOrganizationBdDictMap> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = ucOrganizationBdDictMapDao.countByProperties(map);
        Pager<UcOrganizationBdDictMap> pager = new Pager<>(totalRows, page);
        List<UcOrganizationBdDictMap> ucOrganizationBdDictMapList = ucOrganizationBdDictMapDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(ucOrganizationBdDictMapList);
        return pager;
    }

    @Override
    public List<UcOrganizationBdDictMap> listByProperties(Map<String, Object> properties) {
        return ucOrganizationBdDictMapDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<Integer> findOrganizationIdsByProperties(Map<String, Object> properties) {
        List<UcOrganizationBdDictMap> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        List<Integer> organizationIds = new ArrayList<>();

        for (UcOrganizationBdDictMap ucOrganizationBdDictMap : list) {
            organizationIds.add(ucOrganizationBdDictMap.getOrganizationId());
        }

        return organizationIds;
    }

    @Override
    public int[] insert(List<UcOrganizationBdDictMap> ucOrganizationBdDictMaps) {
        return ucOrganizationBdDictMapDao.insertBatch(ucOrganizationBdDictMaps);
    }

    @Override
    public void delByorganizationId(Integer organizationId) {
        ucOrganizationBdDictMapDao.deleteByOrganizationId(organizationId);
    }

    @Override
    public List<Integer> findDictIdsByOrganizationId(Collection<Integer> organizationIds, Integer type) {
        Map<String, Object> param = new HashMap<>();
        param.put("organization_id", organizationIds);
        param.put("type", type);

        List<UcOrganizationBdDictMap> list = listByProperties(param);

        List<Integer> idList = new ArrayList<>();
        for (UcOrganizationBdDictMap ucOrganizationBdDictMap : list) {
            idList.add(ucOrganizationBdDictMap.getDictId());
        }

        return idList;
    }

    @Override
    public Map<Integer, List<Integer>> findMapByOrganizationIds(Collection<Integer> organizationIds, Integer type) {
        Map<Integer, List<Integer>> result = new HashMap<>();

        Map<String, Object> param = new HashMap<>();
        param.put("organization_id", organizationIds);
        param.put("type", type);

        List<UcOrganizationBdDictMap> list = listByProperties(param);
        for (UcOrganizationBdDictMap ucOrganizationBdDictMap : list) {
            Integer organizationId = ucOrganizationBdDictMap.getOrganizationId();
            Integer dictId = ucOrganizationBdDictMap.getDictId();


            List<Integer> dictIds = result.get(organizationId);
            if (null == dictIds) {
                dictIds = new ArrayList<>();
                result.put(organizationId, dictIds);
            }
            dictIds.add(dictId);
        }

        return result;
    }

}