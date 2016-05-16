package com.gongsibao.module.order.soorderdiscount.service.impl;

import com.gongsibao.common.sqlserver.DBHelper;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderdiscount.dao.SoOrderDiscountDao;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;
import com.gongsibao.module.order.soorderdiscount.service.SoOrderDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;


@Service("soOrderDiscountService")
public class SoOrderDiscountServiceImpl implements SoOrderDiscountService {
    @Autowired
    private SoOrderDiscountDao soOrderDiscountDao;

    @Override
    public SoOrderDiscount findById(Integer pkid) {
        return soOrderDiscountDao.findById(pkid);
    }

    @Override
    public int update(SoOrderDiscount soOrderDiscount) {
        return soOrderDiscountDao.update(soOrderDiscount);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderDiscountDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderDiscount soOrderDiscount) {
        return soOrderDiscountDao.insert(soOrderDiscount);
    }

    @Override
    public List<SoOrderDiscount> findByIds(List<Integer> pkidList) {
        return soOrderDiscountDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderDiscount> findMapByIds(List<Integer> pkidList) {
        List<SoOrderDiscount> list = findByIds(pkidList);
        Map<Integer, SoOrderDiscount> map = new HashMap<>();
        for(SoOrderDiscount soOrderDiscount : list){
            map.put(soOrderDiscount.getPkid(), soOrderDiscount);
        }
        return map;
    }

    @Override
    public Pager<SoOrderDiscount> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderDiscountDao.countByProperties(map);
        Pager<SoOrderDiscount> pager = new Pager<>(totalRows, page);
        List<SoOrderDiscount> soOrderDiscountList = soOrderDiscountDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderDiscountList);
        return pager;
    }

    /**
     * 优惠券信息
     * @param discounts
     * @return
     */
    @Override
    public List<Map<String, Object>> discountList(String discounts) {

        return soOrderDiscountDao.discountList(discounts);
    }

    /**
     * 通过产品查找优惠券
     * @param productId
     * @param discountId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByProductId(String productId, String discountId) {
        return soOrderDiscountDao.findByProductId(productId,discountId);
    }

    /**
     * 通过地区查找优惠券
     * @param cityId
     * @param discountId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByCityId(String cityId, String discountId) {
        return soOrderDiscountDao.findByCityId(cityId,discountId);
    }
}