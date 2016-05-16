package com.gongsibao.module.uc.ucorganization.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UcOrganizationService {

    UcOrganization findById(Integer pkid);

    default UcOrganization findTreeById(Integer pkid) {
        return findTreeById(pkid, false);
    }

    List<UcOrganization> findByUserPkid(Integer userPkid);

    default List<Integer> findIdsByUserPkid(Integer userPkid) {
        List<Integer> list = new ArrayList<>();
        List<UcOrganization> orgList = findByUserPkid(userPkid);
        if (CollectionUtils.isEmpty(orgList)) {
            return null;
        }

        for (UcOrganization ucOrganization : orgList) {
            list.add(ucOrganization.getPkid());
        }
        return list;
    }

    /**
     * 自动补全父节点
     *
     * @param pkid
     * @param autoCompleteParent
     * @return
     */
    UcOrganization findTreeById(Integer pkid, Boolean autoCompleteParent);

    UcOrganization findOrgDetail(Integer pkid);

    int update(UcOrganization ucOrganization);

    int delete(Integer pkid, Integer userId);

    int save(UcOrganization ucOrganization);

    Integer insert(UcOrganization ucOrganization);

    List<UcOrganization> findByIds(List<Integer> pkidList);

    Map<Integer, UcOrganization> findMapByIds(List<Integer> pkidList);

    Pager<UcOrganization> pageByProperties(Map<String, Object> properties, int page);

    public Map<Integer, UcOrganization> findTreeMap();

    Pager<UcOrganization> findOrgList(Map<String, Object> paraMap, int currentPage, int pageSize);

    List<UcOrganization> findByLevel(Integer level);

    Map<Integer, List<UcOrganization>> findMapByUserIds(Collection<Integer> userIds);

    Map<Integer, String> findNameMapByUserIds(Collection<Integer> userIds);

    List<Integer> queryIdsByProperties(Map<String, Object> properties);

    List<UcOrganization> getUserOrgList(Integer userId);

}