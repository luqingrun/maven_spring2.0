package com.gongsibao.module.order.sopay.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.dao.SoOrderDao;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorderpaymap.dao.SoOrderPayMapDao;
import com.gongsibao.module.order.soorderpaymap.entity.SoOrderPayMap;
import com.gongsibao.module.order.sopay.dao.SoPayDao;
import com.gongsibao.module.order.sopay.entity.AuditFlow;
import com.gongsibao.module.order.sopay.entity.PayAudit;
import com.gongsibao.module.order.sopay.entity.PayType;
import com.gongsibao.module.order.sopay.entity.SoPay;
import com.gongsibao.module.order.sopay.service.SoPayService;
import com.gongsibao.module.sys.bdauditlog.dao.BdAuditLogDao;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdfile.dao.BdFileDao;
import com.gongsibao.module.uc.ucuser.dao.UcUserDao;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service("soPayService")
public class SoPayServiceImpl implements SoPayService {
    @Autowired
    private SoPayDao soPayDao;

    @Autowired
    private SoOrderPayMapDao soOrderPayMapDao;

    @Autowired
    private BdAuditLogDao bdAuditLogDao;

    @Autowired
    private UcUserDao ucUserDao;

    @Autowired
    private SoOrderDao soOrderDao;

    @Autowired
    private BdFileDao bdFileDao;

    @Override
    public SoPay findById(Integer pkid) {
        return soPayDao.findById(pkid);
    }

    @Override
    public int update(SoPay soPay) {
        return soPayDao.update(soPay);
    }

    @Override
    public int delete(Integer pkid) {
        return soPayDao.delete(pkid);
    }

    @Override
    public Integer insert(SoPay soPay) {
        return soPayDao.insert(soPay);
    }

    @Override
    public List<SoPay> findByIds(List<Integer> pkidList) {
        return soPayDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoPay> findMapByIds(List<Integer> pkidList) {
        List<SoPay> list = findByIds(pkidList);
        Map<Integer, SoPay> map = new HashMap<>();
        for(SoPay soPay : list){
            map.put(soPay.getPkid(), soPay);
        }
        return map;
    }

    @Override
    public Pager<SoPay> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = soPayDao.countByProperties(map);
        Pager<SoPay> pager = new Pager<>(totalRows, page);
        List<SoPay> soPayList = soPayDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soPayList);
        return pager;
    }

    @Override
    public List<SoPay> findByOrderId(Integer orderPkid){
        List<Integer> payIds = soOrderPayMapDao.findPayIds(orderPkid);
        List<SoPay> soPays = soPayDao.findByIds(payIds);
        return soPays;
    }

    @Override
    public int insertPay(SoPay soPay,int orderPkid,List<Integer> voucherList,int loginUserId){
        int payId=soPayDao.insert(soPay);
        SoOrderPayMap soOrderPayMap = new SoOrderPayMap();
        soOrderPayMap.setPayId(payId);
        soOrderPayMap.setOrderId(orderPkid);
        soOrderPayMapDao.insert(soOrderPayMap);
        bdFileDao.updatePayFile(voucherList,payId);


        BdAuditLog bdAuditLog0 = new BdAuditLog();
        bdAuditLog0.setTypeId(1045);
        bdAuditLog0.setFormId(payId);
        bdAuditLog0.setStatusId(AuditStatusUtils.AUDIT_PASS);
        bdAuditLog0.setAddUserId(loginUserId);
        bdAuditLog0.setLevel(0);
        bdAuditLogDao.insert(bdAuditLog0);


        BdAuditLog bdAuditLog1 = new BdAuditLog();
        bdAuditLog1.setTypeId(1045);
        bdAuditLog1.setFormId(payId);
        bdAuditLog1.setStatusId(AuditStatusUtils.TO_AUDIT);
        bdAuditLog1.setAddUserId(2);
        bdAuditLog1.setLevel(1);
        bdAuditLogDao.insert(bdAuditLog1);
        return 1;
    }

    @Override
    public List<AuditFlow> getAuditFlow(int payPkid){
        Map<String,Object> properties = new HashMap<>();
        properties.put("form_id",payPkid);
        List<BdAuditLog> auditLogList = bdAuditLogDao.findByProperties(properties, 1, 100);
        List<Integer> userIdList = new ArrayList<>();
        List<AuditFlow> auditFlow = new ArrayList();
        for (BdAuditLog a:auditLogList) {
            userIdList.add(a.getAddUserId());
        }
        List<UcUser> users = ucUserDao.findByIds(userIdList);
        Map<Integer,String> userNames = new HashMap<>();
        for (UcUser u:users) {
            userNames.put(u.getPkid(),u.getRealName());
        }
        for (BdAuditLog a:auditLogList) {
            auditFlow.add(new AuditFlow(SecurityUtils.rc4Encrypt(a.getAddUserId()),userNames.get(a.getAddUserId()), a.getLevel(), a.getStatusId()));
        }
        return auditFlow;
    }

    @Override
    public List<PayType> getPayType(int orderPkid){
        SoOrder soOrder = soOrderDao.findById(orderPkid);
        List<PayType> payTypeList =new ArrayList<>();
        if(soOrder.getIsInstallment()==0){
            payTypeList.add(new PayType(0,((double)soOrder.getPayablePrice())/100));
        }
        else{
            String[] amounts = soOrder.getInstallmentMode().split("\\|");
            for(int i=0;i<amounts.length-1;i++){
                payTypeList.add(new PayType(i+1,(NumberUtils.toDouble(amounts[i])/100)));
            }
            payTypeList.add(new PayType(-1,(NumberUtils.toDouble(amounts[amounts.length-1])/100)));
        }
        return payTypeList;


    }

    @Override
    public Pager<PayAudit> getPayAuditList(Map<String,Object> map, Integer page){
        Map<String,Object> properties = new HashMap<>();
        properties.put("addUserId",map.get("userId"));
        if(map.containsKey("payAuditStatusId")){
            properties.put("auditStatusId",map.get("payAuditStatusId"));
        }

        List<Integer> payIds = bdAuditLogDao.findFormIdsByProperties(properties);
        List<PayAudit> payAudits = soPayDao.findPayAuditByIds(map, payIds);

        int totalRows = soPayDao.findPayAuditCountByIds(map,payIds);
        int pageSize = 40;
        Pager<PayAudit> pager = new Pager<>(totalRows, page, pageSize);
        pager.setList(payAudits);
        return pager;

    }

    @Override
    public int updateAudit(int payId,int auditStatusId,String remark,int userId){
        int res = bdAuditLogDao.updateAuditStatus(payId, auditStatusId, remark, userId);
        if(!(auditStatusId==1054  && res != 0)){
            soPayDao.updateAuditId(payId,auditStatusId);
        }
        return 1;
    }
}