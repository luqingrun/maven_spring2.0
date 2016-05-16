package com.gongsibao.module.sys.cms.service.impl;

import com.gongsibao.common.util.ExcelUtils;
import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.dao.CmsTopnavLinkDao;
import com.gongsibao.module.sys.cms.entity.CmsTopnavLink;
import com.gongsibao.module.sys.cms.service.CmsTopnavLinkService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("cmsTopnavLinkService")
public class CmsTopnavLinkServiceImpl implements CmsTopnavLinkService {
    @Autowired
    private CmsTopnavLinkDao cmsTopnavLinkDao;

    @Override
    public CmsTopnavLink findById(Integer pkid) {
        return cmsTopnavLinkDao.findById(pkid);
    }

    @Override
    public int update(CmsTopnavLink cmsTopnavLink) {
        return cmsTopnavLinkDao.update(cmsTopnavLink);
    }

    @Override
    public int updateInfo(CmsTopnavLink cmsTopnavLink) {
        return cmsTopnavLinkDao.updateInfo(cmsTopnavLink);
    }

    @Override
    public int delete(Integer pkid, Integer userId) {
        return cmsTopnavLinkDao.updateStatus(pkid, CMSBase.STATUS_DEL, userId);
    }

    @Override
    public Integer insert(CmsTopnavLink cmsTopnavLink) {
        Integer pkid = cmsTopnavLinkDao.insert(cmsTopnavLink);
        cmsTopnavLinkDao.updateSort(pkid, pkid, cmsTopnavLink.getAddUser());
        return pkid;
    }

    @Override
    public List<CmsTopnavLink> findByIds(List<Integer> pkidList) {
        return cmsTopnavLinkDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, CmsTopnavLink> findMapByIds(List<Integer> pkidList) {
        List<CmsTopnavLink> list = findByIds(pkidList);
        Map<Integer, CmsTopnavLink> map = new HashMap<>();
        for (CmsTopnavLink cmsTopnavLink : list) {
            map.put(cmsTopnavLink.getPkid(), cmsTopnavLink);
        }
        return map;
    }

    @Override
    public Pager<CmsTopnavLink> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = cmsTopnavLinkDao.countByProperties(map);
        Pager<CmsTopnavLink> pager = new Pager<>(totalRows, page);
        List<CmsTopnavLink> cmsTopnavLinkList = cmsTopnavLinkDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(cmsTopnavLinkList);
        return pager;
    }

    @Override
    public Boolean editSort(Integer pkid, Integer categoryId, Boolean isUp, Integer userId) {
        CmsTopnavLink cmsTopnavCategory = findById(pkid);
        if (null == cmsTopnavCategory) {
            return false;
        }

        CmsTopnavLink nearest = cmsTopnavLinkDao.getNearest(cmsTopnavCategory.getSort(), categoryId, isUp);
        if (null == nearest) {
            return true;
        }
        cmsTopnavLinkDao.updateSort(pkid, nearest.getSort(), userId);
        cmsTopnavLinkDao.updateSort(nearest.getPkid(), cmsTopnavCategory.getSort(), userId);
        return true;
    }

    @Override
    public List<CmsTopnavLink> listByProperties(Map<String, Object> properties) {
        List<CmsTopnavLink> byProperties = cmsTopnavLinkDao.findByProperties(properties, 0, Integer.MAX_VALUE);
        return byProperties;
    }

    @Override
    public Map<Integer, List<CmsTopnavLink>> mapByCategoryIds(Collection<Integer> categoryIds, Integer... statuses) {
        Map<Integer, List<CmsTopnavLink>> result = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();

        properties.put("category_id", categoryIds);
        if (ArrayUtils.isNotEmpty(statuses)) {
            properties.put("status", statuses);
        }

        List<CmsTopnavLink> list = listByProperties(properties);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }

        for (CmsTopnavLink link : list) {
            Integer categoryId = link.getCategoryId();
            List<CmsTopnavLink> linkList = result.get(categoryId);
            if (null == linkList) {
                linkList = new ArrayList<>();
                result.put(categoryId, linkList);
            }

            linkList.add(link);
        }
        return result;
    }

    @Override
    public void editPublish(Collection<Integer> pkids, Integer categoryId, Integer userId) {

        // 更新未删除的为初始状态
        cmsTopnavLinkDao.updateStatusByCategory(categoryId, CMSBase.STATUS_INIT, userId);


        if (CollectionUtils.isNotEmpty(pkids)) {
            // 更新为发布状态
            cmsTopnavLinkDao.updateStatus(pkids, CMSBase.STATUS_PUBLISH, userId);
        }
    }

    @Override
    public String export(Map<String, Object> properties) {
        String currentUserId = StringUtils.trimToEmpty(properties.get("currentUserId"));

        List<List<String>> contents = new ArrayList<List<String>>();
        List<String> titles = new ArrayList<String>();
        titles.add("分类编号");
        titles.add("名称");
        titles.add("链接地址");
        titles.add("是否推荐");

        contents.add(titles);

        List<CmsTopnavLink> list = listByProperties(properties);

        if (CollectionUtils.isNotEmpty(list)) {
            for (CmsTopnavLink link : list) {
                List<String> content = new ArrayList<String>();
                contents.add(content);

                content.add(StringUtils.trimToEmpty(link.getCategoryId()));
                content.add(StringUtils.trimToEmpty(link.getName()));
                content.add(StringUtils.trimToEmpty(link.getUrl()));
                content.add(StringUtils.trimToEmpty(link.getRecommend() == 0 ? "" : "推荐"));
            }
        }

        String fileUrl = FileUtils.LOCAL_SAVE_PATH + currentUserId + ".csv";
        ExcelUtils.getListToCsv(contents, fileUrl);

        return fileUrl;
    }
}