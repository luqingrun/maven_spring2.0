package com.gongsibao.module.order.sorefunditem.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.sorefunditem.dao.SoRefundItemDao;
import com.gongsibao.module.order.sorefunditem.entity.SoRefundItem;
import com.gongsibao.module.order.sorefunditem.service.SoRefundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soRefundItemService")
public class SoRefundItemServiceImpl implements SoRefundItemService {
    @Autowired
    private SoRefundItemDao soRefundItemDao;

    @Override
    public SoRefundItem findById(Integer pkid) {
        return soRefundItemDao.findById(pkid);
    }

    @Override
    public int update(SoRefundItem soRefundItem) {
        return soRefundItemDao.update(soRefundItem);
    }

    @Override
    public int delete(Integer pkid) {
        return soRefundItemDao.delete(pkid);
    }

    @Override
    public Integer insert(SoRefundItem soRefundItem) {
        return soRefundItemDao.insert(soRefundItem);
    }

    @Override
    public List<SoRefundItem> findByIds(List<Integer> pkidList) {
        return soRefundItemDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoRefundItem> findMapByIds(List<Integer> pkidList) {
        List<SoRefundItem> list = findByIds(pkidList);
        Map<Integer, SoRefundItem> map = new HashMap<>();
        for (SoRefundItem soRefundItem : list) {
            map.put(soRefundItem.getPkid(), soRefundItem);
        }
        return map;
    }

    @Override
    public Pager<SoRefundItem> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soRefundItemDao.countByProperties(map);
        Pager<SoRefundItem> pager = new Pager<>(totalRows, page);
        List<SoRefundItem> soRefundItemList = soRefundItemDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soRefundItemList);
        return pager;
    }

    /**
     * 批量保存审核日志记录
     *
     * @param list
     */
    @Override
    public void insertBatch(final List<SoRefundItem> list) {
        soRefundItemDao.insertBatch(list);
    }

    @Override
    public List<SoRefundItem> getListByRefundId(Integer refundId) {
        return soRefundItemDao.getListByRefundId(refundId);
    }

}