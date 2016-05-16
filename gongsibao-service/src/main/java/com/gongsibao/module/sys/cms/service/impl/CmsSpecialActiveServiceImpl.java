package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsSpecialActiveDao;
import com.gongsibao.module.sys.cms.entity.CmsSpecialActive;
import com.gongsibao.module.sys.cms.entity.CmsSpecialMonth;
import com.gongsibao.module.sys.cms.entity.CmsSpecialYear;
import com.gongsibao.module.sys.cms.service.CmsSpecialActiveService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsSpecialActiveService")
public class CmsSpecialActiveServiceImpl implements CmsSpecialActiveService {
    @Autowired
    private CmsSpecialActiveDao cmsSpecialActiveDao;

    @Override
    public CmsSpecialActive findById(Integer pkid) {
        return cmsSpecialActiveDao.findById(pkid);
    }

    @Override
    public int update(CmsSpecialActive cmsSpecialActive) {
        return cmsSpecialActiveDao.update(cmsSpecialActive);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {

        return cmsSpecialActiveDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsSpecialActive cmsSpecialActive) {
        return cmsSpecialActiveDao.insert(cmsSpecialActive);
    }

    @Override
    public List<CmsSpecialActive> findByIds(List<Integer> pkidList) {
        return cmsSpecialActiveDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsSpecialActive> findMapByIds(List<Integer> pkidList) {
        List<CmsSpecialActive> list = findByIds(pkidList);
        Map<Integer, CmsSpecialActive> map = new HashMap<>();
        for (CmsSpecialActive cmsSpecialActive : list) {
            map.put(cmsSpecialActive.getPkid(), cmsSpecialActive);
        }
        return map;
    }

    @Override
    public Pager<CmsSpecialActive> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsSpecialActiveDao.countByProperties(map);
        Pager<CmsSpecialActive> pager = new Pager<>(totalRows, page);
        List<CmsSpecialActive> cmsSpecialActiveList = cmsSpecialActiveDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsSpecialActiveList);
        return pager;
    }

    @Override
    public List<CmsSpecialActive> listByProperties(Map<String, Object> properties) {
        return cmsSpecialActiveDao.findByProperties(properties, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<CmsSpecialYear> getIndexActive(Map<String, Object> properties) {
        List<CmsSpecialYear> result = new ArrayList<>();

        List<CmsSpecialActive> list = cmsSpecialActiveDao.findByProperties(properties);

        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        List<String> years = new ArrayList<>();
        for (CmsSpecialActive active : list) {
            String year = DateFormatUtils.format(active.getActiveTime(), "yyyy");
            if (years.contains(year)) {
                continue;

            }
            years.add(year);
        }

        Map<String, CmsSpecialYear> responseMap = new HashMap<>();
        // 封装年
        for (String year : years) {
            CmsSpecialYear response = new CmsSpecialYear();
            response.setYear(year);
            response.setMonthList(new ArrayList<>());

            responseMap.put(year, response);
            result.add(response);
        }

        Map<String, List<CmsSpecialActive>> activeMap = new HashMap<>();

        // 月去重
        List<String> keyList = new ArrayList<>();
        // 月
        for (CmsSpecialActive active : list) {
            String year = DateFormatUtils.format(active.getActiveTime(), "yyyy");
            String month = DateFormatUtils.format(active.getActiveTime(), "MM");
            System.out.println(year + " ===========month :" + month);
            // 封装key=年-月, value=list
            String key = year + "-" + month;
            List<CmsSpecialActive> activeList = activeMap.get(key);
            if (null == activeList) {
                activeList = new ArrayList<>();
                activeMap.put(key, activeList);
            }
            activeList.add(active);

            if (keyList.contains(key)) {
                continue;
            }

            CmsSpecialYear cmsSpecialYear = responseMap.get(year);
            List<CmsSpecialMonth> monthList = cmsSpecialYear.getMonthList();

            CmsSpecialMonth monthResponse = new CmsSpecialMonth();
            monthResponse.setMonth(month);
            monthList.add(monthResponse);
            years.add(year);
            keyList.add(key);
        }

        // 活动
        for (CmsSpecialYear specialYear : result) {
            List<CmsSpecialMonth> monthList = specialYear.getMonthList();
            int total = 0;  // 年总活动数
            for (CmsSpecialMonth specialMonth : monthList) {
                String key = specialYear.getYear() + "-" + specialMonth.getMonth();
                List<CmsSpecialActive> activeList = activeMap.get(key);
                specialMonth.setActiveList(activeList);

                if (null != activeList) {
                    total = total + activeList.size();
                }
            }
            specialYear.setTotal(total);
        }

        return result;
    }


    @Override
    public Boolean editPublish(Collection<Integer> pkids, Integer userId) {
        cmsSpecialActiveDao.updateStatus(new ArrayList<Integer>(), CMSBase.STATUS_INIT, userId);

        if (CollectionUtils.isNotEmpty(pkids)) {
            cmsSpecialActiveDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
        return true;
    }

}