package com.gongsibao.module.order.sopay.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.sopay.entity.AuditFlow;
import com.gongsibao.module.order.sopay.entity.PayAudit;
import com.gongsibao.module.order.sopay.entity.PayType;
import com.gongsibao.module.order.sopay.entity.SoPay;

import java.util.List;
import java.util.Map;

public interface SoPayService {

    SoPay findById(Integer pkid);

    int update(SoPay soPay);

    int delete(Integer pkid);

    Integer insert(SoPay soPay);

    List<SoPay> findByIds(List<Integer> pkidList);

    Map<Integer, SoPay> findMapByIds(List<Integer> pkidList);

    Pager<SoPay> pageByProperties(Map<String, Object> properties, int page);

    List<SoPay> findByOrderId(Integer orderPkid);

    int insertPay(SoPay soPay,int orderPkid,List<Integer> voucherList,int loginUserId);

    List<AuditFlow> getAuditFlow(int payPkid);

    List<PayType> getPayType(int orderPkid);

    Pager<PayAudit> getPayAuditList(Map<String,Object> map, Integer page);

    int updateAudit(int payId,int auditStatusId,String remark,int UserId);
}