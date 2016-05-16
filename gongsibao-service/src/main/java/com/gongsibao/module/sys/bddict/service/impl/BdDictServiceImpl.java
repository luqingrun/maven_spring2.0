package com.gongsibao.module.sys.bddict.service.impl;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.constant.CacheConstant;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bddict.dao.BdDictDao;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.util.cache.CacheService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("bdDictService")
public class BdDictServiceImpl implements BdDictService, InitializingBean {
    @Autowired
    private BdDictDao bdDictDao;
    @Autowired
    private CacheService cacheService;

    @Override
    public BdDict findById(Integer pkid) {
        return bdDictDao.findById(pkid);
    }

    @Override
    public int update(BdDict bdDict) {
        return bdDictDao.update(bdDict);
    }

    @Override
    public int delete(Integer pkid) {
        return bdDictDao.delete(pkid);
    }

    @Override
    public Integer insert(BdDict bdDict) {
        return bdDictDao.insert(bdDict);
    }

    @Override
    public List<BdDict> findByIds(List<Integer> pkidList) {
        return bdDictDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, BdDict> findMapByIds(List<Integer> pkidList) {
        List<BdDict> list = findByIds(pkidList);
        Map<Integer, BdDict> map = new HashMap<>();
        for(BdDict bdDict : list){
            map.put(bdDict.getPkid(), bdDict);
        }
        return map;
    }

    @Override
    public Pager<BdDict> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = bdDictDao.countByProperties(map);
        Pager<BdDict> pager = new Pager<>(totalRows, page);
        List<BdDict> bdDictList = bdDictDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(bdDictList);
        return pager;
    }

    @Override
    public List<BdDict> findAll() {
        List<BdDict> list = (List<BdDict>) cacheService.get(CacheConstant.DIC_KEY);
        if(CollectionUtils.isEmpty(list)) {
            list = bdDictDao.findAll();
            cacheService.put(CacheConstant.DIC_KEY, list, CacheConstant.TIME_ONE_WEEK);
        }
        return list;
    }

    @Override
    public List<BdDict> findByType(Integer type) {
        List<BdDict> allBdDict = findAll();
        List<BdDict> list = allBdDict.stream().filter(bdDict -> bdDict.getType().compareTo(type) == 0).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BdDict> findTreeByType(Integer type) {
        List<BdDict> allBdDict = findAll();
        tree(allBdDict);
        List<BdDict> list = allBdDict.stream().filter(bdDict -> bdDict.getType().compareTo(type) == 0 && bdDict.getPid().compareTo(0) == 0).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BdDict> findChildrenByParentId(Integer pkid) {
        List<BdDict> allBdDict = findAll();
        leaf(allBdDict);
        List<BdDict> list = allBdDict.stream().filter(bdDict -> bdDict.getPid().compareTo(pkid) == 0).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BdDict> findTreeChildrenByParentId(Integer pkid) {
        List<BdDict> allBdDict = findAll();
        tree(allBdDict);
        List<BdDict> list = allBdDict.stream().filter(bdDict -> bdDict.getPid().compareTo(pkid) == 0).collect(Collectors.toList());
        return list;
    }

    @Override
    public BdDict findTreeByParentId(int type, Integer pkid) {
        if(type < 0 || pkid == null || pkid < 0) {
            return null;
        }
        List<BdDict> lstType = findByType(type);
        if(CollectionUtils.isEmpty(lstType)) {
            return null;
        }
        Map<Integer, BdDict> map = list2Map(lstType);
        BdDict dict = map.get(pkid);
        if(dict == null) {
            return null;
        }
        List<BdDict> list = findTreeChildrenByParentId(pkid);
        if(CollectionUtils.isEmpty(list)) {
            return dict;
        }
        dict.setChildrenList(list);
        return dict;
    }

    private void tree(List<BdDict> bdDictList){
        setFullName(bdDictList);
        Map<Integer, BdDict> map = list2Map(bdDictList);
        bdDictList.stream().filter(bdDict -> bdDict.getPid().compareTo(0) != 0).forEach(bdDict -> {
            BdDict parent = map.get(bdDict.getPid());
            if (parent != null) {
                parent.getChildrenList().add(bdDict);
            }
        });
    }

    private Map<Integer, BdDict> list2Map(List<BdDict> bdDictList){
        Map<Integer, BdDict> map = new HashMap<>();
        for(BdDict bdDict : bdDictList){
            map.put(bdDict.getPkid(), bdDict);
        }
        return map;
    }

    private void leaf(List<BdDict> bdDictList){
        tree(bdDictList);
        bdDictList.stream().forEach(bdDict -> {
            if(CollectionUtils.isEmpty(bdDict.getChildrenList())) {
                bdDict.setIsLeaf(1);
            } else {
                bdDict.setChildrenList(new ArrayList<>());
            }
        });
    }

    private void setFullName(List<BdDict> list) {
        if(!CollectionUtils.isEmpty(list)) {
            // 地区类型
            Set<Integer> ids = new HashSet<>();
            for(BdDict dict : list) {
                if(dict != null && BdDict.TYPE_101.compareTo(dict.getType()) == 0) {
                    ids.add(dict.getPkid());
                }
            }
            Map<Integer, String> map = queryDictNames(BdDict.TYPE_101, ids);
            for(BdDict dict : list) {
                dict.setFullName(StringUtils.trimToEmpty(map.get(dict.getPkid())));
            }
        }
    }

    @Override
    public List<BdDict> findOneLevelByType(Integer type) {
        List<BdDict> lstTypeDict = findByType(type);
        leaf(lstTypeDict);
        List<BdDict> list = lstTypeDict.stream().filter(bdDict -> bdDict.getPid().compareTo(0) == 0).collect(Collectors.toList());
        return list;
    }

    @Override
    public Map<Integer, BdDict> findOneLevelMapByType(Integer type) {
        List<BdDict> lstTypeDict = findByType(type);
        List<BdDict> list = lstTypeDict.stream().filter(bdDict -> bdDict.getPid().compareTo(0) == 0).collect(Collectors.toList());
        return list2Map(list);
    }

    @Override
    public Map<Integer, String> queryDictNames(int type, Collection<Integer> ids) {
        Map<Integer, String> result = new HashMap<>();
        if(CollectionUtils.isEmpty(ids) || type < 0) {
            return result;
        }
        List<BdDict> list = findByType(type);
        if(CollectionUtils.isEmpty(list)) {
            return result;
        }
        Map<Integer, BdDict> dictMap = list2Map(list);

        int pid = 0;
        String name = null;
        BdDict dict = null;
        for(Integer id : ids) {
            dict = dictMap.get(id);
            if(dict == null){
                continue;
            }
            pid = dict.getPid();
            name = dict.getName();
            while (pid > 0) {
                dict = dictMap.get(pid);
                if(dict == null){
                    break;
                }
                pid = dict.getPid();
                name = dict.getName() + "-" + name;
            }
            result.put(id, name);
        }
        return result;
    }

    @Override
    public String queryDictName(int type, Integer id) {
        if(id == null || id <= 0) {
            return "";
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        Map<Integer, String> result = queryDictNames(type, ids);
        return StringUtils.trimToEmpty(result.get(id));
    }

    @Override
    public List<Map<String, Object>> queryParents(int type, Integer pkid) {
        if(type < 0 || pkid == null || pkid < 0) {
            return null;
        }
        List<BdDict> list = findByType(type);
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<Integer, BdDict> dictMap = list2Map(list);
        BdDict dict = dictMap.get(pkid);
        if(dict == null) {
            return null;
        }

        int pid = dict.getPid();

        List<BdDict> childrens = null;
        Map<String, Object> result = null;
        List<Map<String, Object>> dicts = new ArrayList<>();
        while (pid >= 0) {
            childrens = findChildrenByParentId(pid);
            if(pid == 0) {
                childrens = childrens.stream().filter(bdDict -> bdDict.getType().compareTo(type) == 0).collect(Collectors.toList());
            }

            result = new HashMap<>();
            result.put("pid", pid);
            result.put("dictList", childrens);
            dicts.add(result);

            if(pid > 0 && !CollectionUtils.isEmpty(childrens)) {
                dict = dictMap.get(pid);
                if(dict == null) {
                    return null;
                }
                pid = dict.getPid();
            } else {
                pid = -1;
            }
        }
        return dicts;
    }

    @Override
    public List<BdDict> queryParentsByLeafIds(int type, Collection<Integer> leafIds) {
        if(type <= 0 || CollectionUtils.isEmpty(leafIds)) {
            return null;
        }
        List<BdDict> list = findByType(type);
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<Integer, BdDict> dictMap = list2Map(list);

        BdDict dict = null;
        Map<Integer, BdDict> resultDictMap = new HashMap<>();
        for(Integer leafId : leafIds) {
            dict = dictMap.get(leafId);
            if(dict == null) {
                continue;
            }
            resultDictMap.put(leafId, dict);

            int pid = dict.getPid();
            while (pid >= 0) {
                dict = dictMap.get(pid);
                if(dict == null) {
                    break;
                }
                resultDictMap.put(pid, dict);

                pid = dict.getPid();
                if(pid == 0) {
                    break;
                }
            }
        }

        List<BdDict> lstResult = new ArrayList<>();
        for(Map.Entry<Integer, BdDict> entry : resultDictMap.entrySet()) {
            lstResult.add(entry.getValue());
        }
        tree(lstResult);
        lstResult = lstResult.stream().filter(bdDict -> bdDict.getPid().compareTo(0) == 0).collect(Collectors.toList());
        return lstResult;
    }

    @Override
    public List<String> queryNames(int type, String name) {
        if(type < 0 || StringUtils.isBlank(name)) {
            return null;
        }
        List<BdDict> list = findByType(type);
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for(BdDict dict : list) {
            if(dict.getName().contains(name)) {
                result.add(dict.getName());
            }
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {}

    public void insertBatch(final List<BdDict> itemList)
    {
        bdDictDao.insertBatch(itemList);
    }

    public BdDict getMaxPkidByType(int type){ return bdDictDao.getMaxPkidByType(type);}

}