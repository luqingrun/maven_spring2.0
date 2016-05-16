package com.gongsibao.module.order.soorderprodusermap.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodusermap.dao.SoOrderProdUserMapDao;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderProdUserMapService")
public class SoOrderProdUserMapServiceImpl implements SoOrderProdUserMapService {
    @Autowired
    private SoOrderProdUserMapDao soOrderProdUserMapDao;
    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private UcOrganizationService ucOrganizationService;

    @Override
    public SoOrderProdUserMap findById(Integer pkid) {
        return soOrderProdUserMapDao.findById(pkid);
    }

    @Override
    public SoOrderProdUserMap findByOrderId(Integer orderId){return  soOrderProdUserMapDao.findByOrderId(orderId);}

    @Override
    public int update(SoOrderProdUserMap soOrderProdUserMap) {
        return soOrderProdUserMapDao.update(soOrderProdUserMap);
    }

    @Override
    public int updateStatus(Integer pkid, int newStatus, int oldStatus) {
        if (pkid == null || pkid.compareTo(0) <= 0 || newStatus < 0 || oldStatus < 0) {
            return -1;
        }
        return soOrderProdUserMapDao.updateStatus(pkid, newStatus, oldStatus);
    }

    @Override
    public int updateStatusByOrderProdId(Integer orderProdId, Integer typeId, int newStatus, int oldStatus) {
        if (orderProdId == null || orderProdId.compareTo(0) <= 0 || typeId == null || typeId.compareTo(0) <= 0 || newStatus < 0 || oldStatus < 0) {
            return -1;
        }
        return soOrderProdUserMapDao.updateStatusByOrderProdId(orderProdId, typeId, newStatus, oldStatus);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdUserMapDao.delete(pkid);
    }

    @Override
    public int deleteInfo(Integer userId, Integer orderProdId, Integer typeId) {
        if(userId == null || userId <= 0 || orderProdId == null || orderProdId <= 0 || typeId == null || typeId <= 0) {
            return -1;
        }
        return soOrderProdUserMapDao.deleteInfo(userId, orderProdId, typeId);
    }

    @Override
    public void insertBatch(final List<SoOrderProdUserMap> itemList) {
        soOrderProdUserMapDao.insertBatch(itemList);
    }

    @Override
    public Integer insert(SoOrderProdUserMap soOrderProdUserMap) {
        return soOrderProdUserMapDao.insert(soOrderProdUserMap);
    }

    @Override
    public List<SoOrderProdUserMap> findByIds(List<Integer> pkidList) {
        return soOrderProdUserMapDao.findByIds(pkidList);
    }


    public List<SoOrderProdUserMap> findRefundByIds(List<Integer> pkidList, Integer typeId) {
        return  soOrderProdUserMapDao.findListByIds(pkidList,typeId);
    }
    @Override
    public Map<Integer, SoOrderProdUserMap> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdUserMap> list = findByIds(pkidList);
        Map<Integer, SoOrderProdUserMap> map = new HashMap<>();
        for (SoOrderProdUserMap soOrderProdUserMap : list) {
            map.put(soOrderProdUserMap.getPkid(), soOrderProdUserMap);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdUserMap> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int loginUserId = NumberUtils.toInt(map.get("userId"));
        map.put("userId", null);
        int totalRows = soOrderProdUserMapDao.countByProperties(map);
        if (totalRows <= 0) {
            return null;
        }
        Pager<SoOrderProdUserMap> pager = new Pager<>(totalRows, page, pageSize);

        List<Integer> orderIds = null;
        // 查询满足条件的负责人ID集合
        List<Integer> ids = queryUserIdsByProperties(map);
        if (CollectionUtils.isNotEmpty(ids) && ids.contains(loginUserId)) {
            // 负责人列表：如当前操作人为正在负责人，排序首个负责人为当前操作人
            orderIds = new ArrayList<>();
            orderIds.add(loginUserId);
            orderIds.addAll(ids);
        }

        List<SoOrderProdUserMap> soOrderProdUserMapList = soOrderProdUserMapDao.findListByProperties(map, orderIds, pager.getStartRow(), pager.getPageSize());

        List<Integer> userIds = new ArrayList<>();
        for (SoOrderProdUserMap soOrderProdUserMap : soOrderProdUserMapList) {
            userIds.add(soOrderProdUserMap.getUserId());
        }

        // 根据用户ID集合查询MAP信息  TODO
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIds);

        UcUser user = null;
        for (SoOrderProdUserMap soOrderProdUserMap : soOrderProdUserMapList) {
            user = userMap.get(soOrderProdUserMap.getUserId());
            if (user == null) {
                soOrderProdUserMap.setUserName("");
            } else {
                soOrderProdUserMap.setUserName(user.getRealName());
            }
        }
        pager.setList(soOrderProdUserMapList);
        return pager;
    }

    @Override
    public List<SoOrderProdUserMap> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        return soOrderProdUserMapDao.findByProperties(properties, start, pageSize);
    }

    @Override
    public List<Integer> queryUserIdsByProperties(Map<String, Object> properties) {
        return soOrderProdUserMapDao.queryUserIdsByProperties(properties);
    }

    @Override
    public Map<Integer, List<Integer>> queryUserIdsMapByProperties(Map<String, Object> properties) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (MapUtils.isEmpty(properties)) {
            return map;
        }
        List<SoOrderProdUserMap> list = findByProperties(properties, -1, -1);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        List<Integer> userIds = null;
        for (SoOrderProdUserMap info : list) {
            userIds = map.get(info.getOrderProdId());
            if (CollectionUtils.isEmpty(userIds)) {
                userIds = new ArrayList<>();
                map.put(info.getOrderProdId(), userIds);
            }
            userIds.add(info.getUserId());
        }
        return map;
    }

    @Override
    public Map<Integer, String> queryUserNameMapByProperties(Map<String, Object> properties) {
        Map<Integer, String> result = new HashMap<>();
        if (MapUtils.isEmpty(properties)) {
            return result;
        }
        Map<Integer, List<Integer>> map = queryUserIdsMapByProperties(properties);
        if (MapUtils.isEmpty(map)) {
            return result;
        }
        List<Integer> userIds = new ArrayList<>();
        for (Integer key : map.keySet()) {
            userIds.addAll(map.get(key));
        }
        Map<Integer, UcUser> userMap = ucUserService.findMapByIds(userIds);
        if (MapUtils.isEmpty(userMap)) {
            return result;
        }
        UcUser user = null;
        List<String> userNames = null;
        for (Integer key : map.keySet()) {
            userIds = map.get(key);
            if (CollectionUtils.isNotEmpty(userIds)) {
                userNames = new ArrayList<>();
                for (Integer userId : userIds) {
                    user = userMap.get(userId);
                    if (user == null) {
                        continue;
                    }
                    userNames.add(user.getRealName());
                }
                result.put(key, StringUtils.join(userNames, "、"));
            }
        }
        return result;
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        return soOrderProdUserMapDao.countByProperties(properties);
    }

    @Override
    public List<UcUser> findOperatorByOrderProdId(Integer orderProdId) {

        //关系类型序号，type=306，3061业务、3062客服（关注）、3063操作
        List<SoOrderProdUserMap> soOrderProdUserMaps=  soOrderProdUserMapDao.findByOderProdIdAndTypeId(orderProdId, 3061);
        List<UcUser> users=new ArrayList<>();
        for (SoOrderProdUserMap ou:soOrderProdUserMaps             ) {

            UcUser user = ucUserService.findById(ou.getUserId());
            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    @Override
    public List<Integer> queryOrderProdIdsByProperties(Map<String, Object> properties) {
        return soOrderProdUserMapDao.queryOrderProdIdsByProperties(properties);
    }


    @Override
    public Map<Integer, String> queryOrganizationNameMapByProperties(Map<String, Object> properties) {
        Map<Integer, String> result = new HashMap<>();
        if (MapUtils.isEmpty(properties)) {
            return result;
        }
        Map<Integer, List<Integer>> map = queryUserIdsMapByProperties(properties);
        if (MapUtils.isEmpty(map)) {
            return result;
        }
        List<Integer> userIds = new ArrayList<>();
        for (Integer key : map.keySet()) {
            userIds.addAll(map.get(key));
        }

        Map<Integer, String> organizationNameMap = ucOrganizationService.findNameMapByUserIds(userIds);
        if (MapUtils.isEmpty(organizationNameMap)) {
            return result;
        }
        List<String> organizationNames = null;
        for (Integer key : map.keySet()) {
            userIds = map.get(key);
            if (CollectionUtils.isNotEmpty(userIds)) {
                organizationNames = new ArrayList<>();
                for (Integer userId : userIds) {
                    if (StringUtils.isNotBlank(organizationNameMap.get(userId))) {
                        organizationNames.add(organizationNameMap.get(userId));
                    }
                }
                result.put(key, StringUtils.join(organizationNames, "、"));
            }
        }
        return result;
    }
}