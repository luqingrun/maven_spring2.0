package com.gongsibao.module.order.soorderproditem.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderproditem.dao.SoOrderProdItemDao;
import com.gongsibao.module.order.soorderproditem.entity.IteamServerInfo;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderproditem.service.SoOrderProdItemService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("soOrderProdItemService")
public class SoOrderProdItemServiceImpl implements SoOrderProdItemService {
    @Autowired
    private SoOrderProdItemDao soOrderProdItemDao;

    @Override
    public SoOrderProdItem findById(Integer pkid) {
        return soOrderProdItemDao.findById(pkid);
    }

    @Override
    public int update(SoOrderProdItem soOrderProdItem) {
        return soOrderProdItemDao.update(soOrderProdItem);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdItemDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProdItem soOrderProdItem) {
        return soOrderProdItemDao.insert(soOrderProdItem);
    }

    @Override
    public List<SoOrderProdItem> findByIds(List<Integer> pkidList) {
        return soOrderProdItemDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderProdItem> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdItem> list = findByIds(pkidList);
        Map<Integer, SoOrderProdItem> map = new HashMap<>();
        for (SoOrderProdItem soOrderProdItem : list) {
            map.put(soOrderProdItem.getPkid(), soOrderProdItem);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdItem> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soOrderProdItemDao.countByProperties(map);
        Pager<SoOrderProdItem> pager = new Pager<>(totalRows, page);
        List<SoOrderProdItem> soOrderProdItemList = soOrderProdItemDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderProdItemList);
        return pager;
    }

    @Override
    public Map<Integer, List<SoOrderProdItem>> getMapByProdIds(Collection<Integer> orderProdIds) {
        Map<Integer, List<SoOrderProdItem>> result = new HashMap<>();
        List<SoOrderProdItem> list = soOrderProdItemDao.getByProdIds(orderProdIds);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (SoOrderProdItem prodItem : list) {
            Integer orderProdId = prodItem.getOrderProdId();
            List<SoOrderProdItem> itemList = result.get(orderProdId);
            if (null == itemList) {
                itemList = new ArrayList<>();
                result.put(orderProdId, itemList);
            }
            itemList.add(prodItem);
        }
        return result;
    }

    @Override
    public Map<Integer, List<SoOrderProdItem>> getRefundMapByProdIds(Collection<Integer> orderProdIds) {
        /**创建返回对象result*/
        Map<Integer, List<SoOrderProdItem>> result = new HashMap<>();
        /**根据订单产品orderProdIds 得到订单服务项的List*/
        List<SoOrderProdItem> list = soOrderProdItemDao.getByProdIds(orderProdIds);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        /**通过list得到服务项中priceId的list集合*/
        List<Integer> priceIdList = new ArrayList<>();
        for (SoOrderProdItem soOrderProdItem : list) {
            priceIdList.add(soOrderProdItem.getPriceId());
        }

        /**通过priceIdList查询得到列表记录结果*/
        Map<Integer, IteamServerInfo> relationMap = new HashMap<>();
        List<IteamServerInfo> listInfo = soOrderProdItemDao.getByIteamIds(priceIdList);
        for (IteamServerInfo iteamServerInfo : listInfo) {
            relationMap.put(iteamServerInfo.getPkId(), iteamServerInfo);
        }
        /**将两个list中的记录一一关联起来*/
        for (SoOrderProdItem soOrderProdItem:list) {
            Integer pkid = soOrderProdItem.getPkid();
            if(null == pkid){
                continue;
            }
            soOrderProdItem.setFeature(relationMap.get(pkid).getIteamServerFeature());
        }
        /**遍历List<SoOrderProdItem>,首先得到订单产品的id,根据id去result得到对应的 List<SoOrderProdItem>
         * 如果可以得到List,说明map中id对应的List已经存在,之后将遍历的对象prodItem，添加到List中
         * 如果不可以得到List,说明map中id对应的List不存在,之后创建新的List对象，并将对象List和相应的id put到result中，最后将遍历的对象prodItem，添加到List中*/
        for (SoOrderProdItem prodItem : list) {
            Integer orderProdId = prodItem.getOrderProdId();
            List<SoOrderProdItem> itemList = result.get(orderProdId);
            if (null == itemList) {
                itemList = new ArrayList<>();
                result.put(orderProdId, itemList);
            }
            itemList.add(prodItem);
        }
        return result;
    }




}