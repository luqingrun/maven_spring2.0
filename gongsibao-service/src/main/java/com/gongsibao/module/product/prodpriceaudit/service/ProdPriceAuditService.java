package com.gongsibao.module.product.prodpriceaudit.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodprice.entity.ProdPriceDetail;
import com.gongsibao.module.product.prodpriceaudit.entity.*;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;

import java.util.List;
import java.util.Map;

public interface ProdPriceAuditService {

    ProdPriceAudit findById(Integer pkid);

    int update(ProdPriceAudit prodPriceAudit);

    int delete(Integer pkid);

    Integer insert(ProdPriceAudit prodPriceAudit);

    List<ProdPriceAudit> findByIds(List<Integer> pkidList);

    Map<Integer, ProdPriceAudit> findMapByIds(List<Integer> pkidList);

    Pager<ProdPriceAudit> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 获取产品在售列表
     * */
    Pager<ProdPriceAuditRow> pageProdPriceAuditRows(Map<String, Object> map, int page, int pageSize);

    Pager<ProdPriceOnSaleRow> pageProdPriceOnSaleRows(Map<String, Object> condition, int page, int pageSize);

    Map<String, Object> findDetailById(Integer pkid);

    /**
     * 审核
     */
    boolean updateProdPriceAuditStatus(Integer pkid, String content, boolean isAllowed, LoginUser loginUser);

    /**
     * 提交定价申请
     */
    boolean addProdPriceAudit(ProdPriceAuditRequest priceAuditRequest, LoginUser loginUser);

    /**
     * 改价申请
     */
    boolean updateProdPriceAudit(ProdPriceAuditUpdateRequest priceAuditUpdateRequest, LoginUser loginUser);

    /**
     * 查找未审核状态的产品定价
     */
    List<ProdPriceAudit> findUnAuditProdPriceAuditsBy(Integer prodId, Integer organizationId);

    /**
     * 统计产品
     */
    Integer countProdPriceAuditRows(Map<String, Object> condition);

    /**
     * 查找存在待审核和审核中的产品定价
     * */
    List<CityArea> findUnauditCityAreasByProdIdAndOrgId(Integer prodId, Integer orgId);


    ProdPriceOnSaleRow findOrgProdPricesDetailById(Map<String, Object> condition);

    List<ProdPriceDetail> getProdPricesList(Map<String, Object> condition);

}