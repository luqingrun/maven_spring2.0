package com.gongsibao.module.order.socontract.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.order.socontract.entity.ContractList;
import com.gongsibao.module.order.socontract.entity.SoContract;

import java.util.List;
import java.util.Map;

public interface SoContractService {

    SoContract findById(Integer pkid);

    SoContract findByOrderId(Integer orderId);

    int update(SoContract soContract);

    int delete(Integer pkid);

    Integer insert(SoContract soContract);

    List<SoContract> findByIds(List<Integer> pkidList);

    Map<Integer, SoContract> findMapByIds(List<Integer> pkidList);

    List<SoContract> findByOrderIds(List<Integer> orderIdList);

    Map<Integer, SoContract> findMapByOrderIds(List<Integer> orderIdList);

    Pager<SoContract> pageByProperties(Map<String, Object> properties, int page);

    Pager<ContractList> pageContractListByProperties(Map<String, Object> properties, int page);

    SoContract findByOrderPkId(Integer orderPkId);

    int insertSoContract(SoContract soContract,Integer currentUserId);

    /**
     * 合同审核列表
     * @param properties
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pager<ContractList> listAuditContract(Map<String,Object> properties,Integer currentPage,Integer pageSize);

    /**
     * 合同浮层查询
     * @param orderPkId
     * @return
     */
    SoContract getSoContractInfo(Integer orderPkId);

    /**
     * 合同浮层-审核通过
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    int editSoContractPass(Integer pkid, Integer currentUserId, String remark) throws Exception;

    /**
     * 合同浮层-审核不通过
     * @param pkid
     * @param currentUserId
     * @param remark
     * @return
     * @throws Exception
     */
    int editSoContractReject(Integer pkid, Integer currentUserId, String remark) throws Exception;
}