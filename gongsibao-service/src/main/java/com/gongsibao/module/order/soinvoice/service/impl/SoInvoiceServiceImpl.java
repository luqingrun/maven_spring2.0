package com.gongsibao.module.order.soinvoice.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soinvoice.dao.SoInvoiceDao;
import com.gongsibao.module.order.soinvoice.entity.InvoiceList;
import com.gongsibao.module.order.soinvoice.entity.SoInvoice;
import com.gongsibao.module.order.soinvoice.service.SoInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soInvoiceService")
public class SoInvoiceServiceImpl implements SoInvoiceService {
    @Autowired
    private SoInvoiceDao soInvoiceDao;

    @Override
    public SoInvoice findById(Integer pkid) {
        return soInvoiceDao.findById(pkid);
    }

    @Override
    public int update(SoInvoice soInvoice) {
        return soInvoiceDao.update(soInvoice);
    }

    @Override
    public int delete(Integer pkid) {
        return soInvoiceDao.delete(pkid);
    }

    @Override
    public Integer insert(SoInvoice soInvoice) {
        return soInvoiceDao.insert(soInvoice);
    }

    @Override
    public List<SoInvoice> findByIds(List<Integer> pkidList) {
        return soInvoiceDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoInvoice> findMapByIds(List<Integer> pkidList) {
        List<SoInvoice> list = findByIds(pkidList);
        Map<Integer, SoInvoice> map = new HashMap<>();
        for (SoInvoice soInvoice : list) {
            map.put(soInvoice.getPkid(), soInvoice);
        }
        return map;
    }

    @Override
    public Pager<SoInvoice> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soInvoiceDao.countByProperties(map);
        Pager<SoInvoice> pager = new Pager<>(totalRows, page);
        List<SoInvoice> soInvoiceList = soInvoiceDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soInvoiceList);
        return pager;
    }

    @Override
    public List<SoInvoice> findByOrderId(Integer orderId) {
        List<SoInvoice> soInvoiceList = soInvoiceDao.findListByOrderId(orderId);
        return soInvoiceList;
    }

    @Override
    public Pager<InvoiceList> pageInvoiceListByProperties(Map<String, Object> map, int page) {
        int totalRows = soInvoiceDao.findListCountByProperties(map);
        Pager<InvoiceList> pager = new Pager<>(totalRows, page);
        List<InvoiceList> soInvoiceList = soInvoiceDao.findInvoiceListByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soInvoiceList);
        return pager;
    }

}