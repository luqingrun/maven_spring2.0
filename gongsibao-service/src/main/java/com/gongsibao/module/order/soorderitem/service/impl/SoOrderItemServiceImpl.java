package com.gongsibao.module.order.soorderitem.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderitem.dao.SoOrderItemDao;
import com.gongsibao.module.order.soorderitem.entity.SoOrderItem;
import com.gongsibao.module.order.soorderitem.service.SoOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderItemService")
public class SoOrderItemServiceImpl implements SoOrderItemService {
    @Autowired
    private SoOrderItemDao soOrderItemDao;

    @Override
    public SoOrderItem findById(Integer pkid) {
        return soOrderItemDao.findById(pkid);
    }

    @Override
    public int update(SoOrderItem soOrderItem) {
        return soOrderItemDao.update(soOrderItem);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderItemDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderItem soOrderItem) {
        return soOrderItemDao.insert(soOrderItem);
    }

    @Override
    public List<SoOrderItem> findByIds(List<Integer> pkidList) {
        return soOrderItemDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderItem> findMapByIds(List<Integer> pkidList) {
        List<SoOrderItem> list = findByIds(pkidList);
        Map<Integer, SoOrderItem> map = new HashMap<>();
        for(SoOrderItem soOrderItem : list){
            map.put(soOrderItem.getPkid(), soOrderItem);
        }
        return map;
    }

    @Override
    public Pager<SoOrderItem> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderItemDao.countByProperties(map);
        Pager<SoOrderItem> pager = new Pager<>(totalRows, page);
        List<SoOrderItem> soOrderItemList = soOrderItemDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderItemList);
        return pager;
    }

}