package com.gongsibao.module.order.soorderprodaccount.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.soorderprodaccount.dao.SoOrderProdAccountDao;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;
import com.gongsibao.module.order.soorderprodaccount.service.SoOrderProdAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("soOrderProdAccountService")
public class SoOrderProdAccountServiceImpl implements SoOrderProdAccountService {
    @Autowired
    private SoOrderProdAccountDao soOrderProdAccountDao;

    @Override
    public SoOrderProdAccount findById(Integer pkid) {
        return soOrderProdAccountDao.findById(pkid);
    }

    @Override
    public int update(SoOrderProdAccount soOrderProdAccount) {
        return soOrderProdAccountDao.update(soOrderProdAccount);
    }

    @Override
    public int delete(Integer pkid) {
        return soOrderProdAccountDao.delete(pkid);
    }

    @Override
    public Integer insert(SoOrderProdAccount soOrderProdAccount) {
        return soOrderProdAccountDao.insert(soOrderProdAccount);
    }

    @Override
    public List<SoOrderProdAccount> findByIds(List<Integer> pkidList) {
        return soOrderProdAccountDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, SoOrderProdAccount> findMapByIds(List<Integer> pkidList) {
        List<SoOrderProdAccount> list = findByIds(pkidList);
        Map<Integer, SoOrderProdAccount> map = new HashMap<>();
        for(SoOrderProdAccount soOrderProdAccount : list){
            map.put(soOrderProdAccount.getPkid(), soOrderProdAccount);
        }
        return map;
    }

    @Override
    public Pager<SoOrderProdAccount> pageByProperties(Map<String, Object> map, int page, int pageSize) {
        int totalRows = soOrderProdAccountDao.countByProperties(map);
        Pager<SoOrderProdAccount> pager = new Pager<>(totalRows, page, pageSize);
        List<SoOrderProdAccount> soOrderProdAccountList = soOrderProdAccountDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(soOrderProdAccountList);
        return pager;
    }

    public List<SoOrderProdAccount> saveAccount(Integer orderProdId,List<SoOrderProdAccount> sopalist)
    {
        List<SoOrderProdAccount> addList = new ArrayList();
        List<SoOrderProdAccount> editList = new ArrayList();
        List<SoOrderProdAccount> dropList = new ArrayList();
        List<SoOrderProdAccount> oldList = new ArrayList();

        Map opidMap = new HashMap() ;
        opidMap.put("order_prod_id", orderProdId);

        oldList = soOrderProdAccountDao.findByProperties(opidMap,0,1000);

        for(SoOrderProdAccount ps : sopalist)
        {
            if(ps.getPkid() == null)
            {
                addList.add(ps);
            }
            else
            {
                editList.add(ps);
            }
        }
        for(SoOrderProdAccount ps : oldList)
        {
            Boolean isnothave = true;
            for(SoOrderProdAccount psItem : editList)
            {
                if(ps.getPkid().equals(psItem.getPkid()))
                {
                    isnothave = false;
                }
            }
            if(isnothave)
            {
                dropList.add(ps);
            }
        }

        soOrderProdAccountDao.insertBatch(addList);
        soOrderProdAccountDao.updateBatch(editList);
        soOrderProdAccountDao.deleteBatch(dropList);

        List<SoOrderProdAccount> response_account = soOrderProdAccountDao.findByProperties(opidMap,0,1000);

        return response_account;
    }

}