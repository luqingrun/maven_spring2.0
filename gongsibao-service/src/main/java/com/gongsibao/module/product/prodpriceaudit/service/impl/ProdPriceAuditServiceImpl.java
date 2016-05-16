package com.gongsibao.module.product.prodpriceaudit.service.impl;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodprice.dao.ProdPriceDao;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodprice.entity.ProdPriceDetail;
import com.gongsibao.module.product.prodprice.entity.ProdPriceRequest;
import com.gongsibao.module.product.prodprice.service.ProdPriceService;
import com.gongsibao.module.product.prodpriceaudit.dao.ProdPriceAuditDao;
import com.gongsibao.module.product.prodpriceaudit.entity.*;
import com.gongsibao.module.product.prodpriceaudit.service.ProdPriceAuditService;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import com.gongsibao.module.sys.bdauditlog.entity.AuditStatuses;
import com.gongsibao.module.sys.bdauditlog.entity.AuditTypes;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


@Service("prodPriceAuditService")
public class ProdPriceAuditServiceImpl implements ProdPriceAuditService {
    @Autowired
    private ProdPriceAuditDao prodPriceAuditDao;
    @Autowired
    private ProdPriceDao prodPriceDao;
    @Autowired
    private ProdPriceService prodPriceService;
    @Autowired
    private ProdServiceService prodServiceService;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private BdAuditLogService bdAuditLogService;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private UcOrganizationService ucOrganizationService;

    @Override
    public ProdPriceAudit findById(Integer pkid) {
        return prodPriceAuditDao.findById(pkid);
    }

    @Override
    public int update(ProdPriceAudit prodPriceAudit) {
        return prodPriceAuditDao.update(prodPriceAudit);
    }

    @Override
    public int delete(Integer pkid) {
        return prodPriceAuditDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdPriceAudit prodPriceAudit) {
        return prodPriceAuditDao.insert(prodPriceAudit);
    }

    @Override
    public List<ProdPriceAudit> findByIds(List<Integer> pkidList) {
        return prodPriceAuditDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdPriceAudit> findMapByIds(List<Integer> pkidList) {
        List<ProdPriceAudit> list = findByIds(pkidList);
        Map<Integer, ProdPriceAudit> map = new HashMap<>();
        for (ProdPriceAudit prodPriceAudit : list) {
            map.put(prodPriceAudit.getPkid(), prodPriceAudit);
        }
        return map;
    }

    @Override
    public Pager<ProdPriceAudit> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodPriceAuditDao.countByProperties(map);
        Pager<ProdPriceAudit> pager = new Pager<>(totalRows, page);
        List<ProdPriceAudit> prodPriceAuditList = prodPriceAuditDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodPriceAuditList);
        return pager;
    }

    @Override
    public Pager<ProdPriceAuditRow> pageProdPriceAuditRows(Map<String, Object> map, int page, int pageSize) {
        int totalRows = prodPriceAuditDao.countAuditRowByProperties(map);
        Pager<ProdPriceAuditRow> pager = new Pager<>(totalRows, page);
        List<ProdPriceAuditRow> rows = prodPriceAuditDao.findAuditRowByProperties(map, pager.getStartRow(), pageSize);

        for (ProdPriceAuditRow row : rows) {
            List<CityArea> cityAreaList = getCityAreaList(row.getPkid());
            row.setCityAreas(cityAreaList);
            row.setCityFullNamesStr(cityAreaList.stream().map(CityArea::getFullName).collect(Collectors.joining(",")).toString());
            row.setStatusName(getNameFromDictBy(row.getStatusId()));
            UcOrganization org = ucOrganizationService.findById(row.getOrganizationId());
            if (org != null) {
                row.setOrganizationName(org.getName());
            }
        }
        pager.setList(rows);
        return pager;
    }

    @Override
    public Pager<ProdPriceOnSaleRow> pageProdPriceOnSaleRows(Map<String, Object> condition, int page, int pageSize) {

        int totalRows = prodPriceAuditDao.countOnSaleRowsByProperties(condition);
        Pager<ProdPriceOnSaleRow> pager = new Pager<>(totalRows, page);
        List<ProdPriceOnSaleRow> rows = prodPriceAuditDao.findOnSaleRowsByProperties(condition, pager.getStartRow(), pageSize);

        for (ProdPriceOnSaleRow row : rows) {

            //审核通过的定价批次，所包含的上架地区Id 列表
            List<Integer> cityIds = prodPriceAuditDao.getCityIdsByOrgIdAndProdId(row.getOrganizationId(), row.getProductId()
                    , Integer.valueOf(condition.get("audit_status_id").toString()));

            //获取城市列表
            if (cityIds.size() > 0) {
                List<CityArea> cityAreaList = getCityAreasBy(cityIds);
                row.setCityAreas(cityAreaList);
                row.setCityFullNamesStr(cityAreaList.stream().map(CityArea::getFullName).collect(Collectors.joining(",")).toString());
            }

            //获取组织名称
            UcOrganization org = ucOrganizationService.findById(row.getOrganizationId());
            if (org != null) {
                row.setOrganizationName(org.getName());
                row.setOrganizationIdStr(SecurityUtils.rc4Encrypt(row.getProductId()));
            }

            if (row.getProductId() > 0) {
                row.setProductIdStr(SecurityUtils.rc4Encrypt(row.getProductId()));
            }
        }
        pager.setList(rows);
        return pager;
    }

    @Override
    public ProdPriceOnSaleRow findOrgProdPricesDetailById(Map<String, Object> condition) {

        ProdPriceOnSaleRow prodPriceOnSaleRow = new ProdPriceOnSaleRow();
        //获取组织信息
        int organizationId = NumberUtils.toInt(condition.get("organization_id"), -1);
        if (organizationId > 0) {
            UcOrganization ucOrganization = ucOrganizationService.findById(organizationId);
            prodPriceOnSaleRow.setOrganizationId(ucOrganization.getPkid());
            prodPriceOnSaleRow.setOrganizationName(ucOrganization.getName());
        }

        //获取产品信息
        int productId = NumberUtils.toInt(condition.get("product_id"), -1);
        if (productId > 0) {
            ProdProduct prodProduct = prodProductService.findById(productId);
            prodPriceOnSaleRow.setProductId(productId);
            prodPriceOnSaleRow.setProductName(prodProduct.getName());
        }

        int auditStatusId = NumberUtils.toInt(condition.get("audit_status_id"), -1);

        //审核通过的定价批次，所包含的上架地区Id 列表
        List<Integer> cityIds = prodPriceAuditDao.getCityIdsByOrgIdAndProdId(organizationId, productId, auditStatusId);

        //获取城市列表
        if (cityIds.size() > 0) {
            List<CityArea> cityAreaList = getCityAreasBy(cityIds);
            prodPriceOnSaleRow.setCityAreas(cityAreaList);
            prodPriceOnSaleRow.setCityFullNamesStr(cityAreaList.stream().map(CityArea::getFullName).collect(Collectors.joining(",")).toString());
        }

        return prodPriceOnSaleRow;
    }

    @Override
    public List<ProdPriceDetail> getProdPricesList(Map<String, Object> condition) {
        List<ProdPrice> prodPrices = prodPriceDao.getProdPricesListBy(condition);
        List<ProdPriceDetail> prodPriceDetails = getProdPriceDetails(prodPrices);
        return prodPriceDetails;
    }

    @Override
    public Integer countProdPriceAuditRows(Map<String, Object> condition) {
        return prodPriceAuditDao.countAuditRowByProperties(condition);
    }

    @Override
    public List<CityArea> findUnauditCityAreasByProdIdAndOrgId(Integer prodId, Integer organizationId) {

        List<ProdPriceAudit> unAuditProdPriceAudits = findUnAuditProdPriceAuditsBy(prodId, organizationId);
        List<CityArea> cityAreas = new ArrayList<>();

        for (ProdPriceAudit prodPriceAudit : unAuditProdPriceAudits) {
            cityAreas.addAll(getCityAreaList(prodPriceAudit.getPkid()));
        }

        cityAreas = new ArrayList(new HashSet(cityAreas));

        return cityAreas;
    }


    /**
     * 获取定价明细
     */
    @Override
    public Map<String, Object> findDetailById(Integer pkid) {

        Map<String, Object> result = new HashMap<>();
        ProdPriceAudit prodPriceAudit = findById(pkid);
        result.put("organization_id", prodPriceAudit.getOrganizationId());
        result.put("organization_name", ucOrganizationService.findById(prodPriceAudit.getOrganizationId()).getName());
        result.put("product_id", prodPriceAudit.getProductId());
        result.put("product_name", prodProductService.findById(prodPriceAudit.getProductId()).getName());

        List<ProdPrice> prodPrices = prodPriceService.findByAuditId(pkid);

        List<ProdPriceDetail> prodServices = getProdPriceDetails(prodPrices);
        result.put("productService", prodServices);

        List<Integer> serviceIds = prodPriceService.findServiceIdsByAuditId(pkid);
        List<ProdServiceOption> prodServiceOptions = new ArrayList<>();
        for (Integer serviceId : serviceIds) {

            ProdServiceOption serviceOption = new ProdServiceOption();
            ProdService prodService = prodServiceService.findById(serviceId);
            serviceOption.setPkid(serviceId);
            if (prodService != null) {
                serviceOption.setName(getNameFromDictBy(prodService.getTypeId()));//获取服务名称
                serviceOption.setIsMust(prodPriceService.findIsMustByServiceIdAndAuditId(pkid, serviceId));
            }
            prodServiceOptions.add(serviceOption);
        }

        result.put("productServiceOption", prodServiceOptions);

        List<CityArea> cityAreas = getCityAreaList(pkid);
        result.put("cityAreas", cityAreas);

        result.put("cityFullNamesStr", cityAreas.stream().map(CityArea::getFullName).collect(Collectors.joining(",")));

        return result;
    }

    public List<ProdPriceDetail> getProdPriceDetails(List<ProdPrice> prodPrices) {
        List<ProdPriceDetail> prodServices = new ArrayList<>();
        for (ProdPrice price : prodPrices) {
            ProdPriceDetail prodPriceDetail = new ProdPriceDetail();
            prodPriceDetail.setPkid(price.getPkid());
            prodPriceDetail.setServiceId(price.getServiceId());
            // 获取产品服务
            ProdService prodService = prodServiceService.findById(price.getServiceId());
            if (prodService != null) {
                prodPriceDetail.setUnit(getNameFromDictBy(prodService.getUnitId()));
                prodPriceDetail.setServiceTypeId(prodService.getProductIdStr());
                prodPriceDetail.setServiceTypeName(getNameFromDictBy(prodService.getPropertyId()));
                prodPriceDetail.setServiceName(getNameFromDictBy(prodService.getTypeId()));
                prodPriceDetail.setSort(prodService.getSort());
                prodPriceDetail.setHasStock(prodService.getHasStock() > 0);
            }
            prodPriceDetail.setIsMust(price.getIsMust() > 0);
            prodPriceDetail.setCost(price.getCost());
            prodPriceDetail.setPrice(price.getPrice());
            prodPriceDetail.setStock(price.getStock());
            prodPriceDetail.setOriginal_price(price.getOriginalPrice());

            prodServices.add(prodPriceDetail);
        }
        return prodServices;
    }

    /**
     * 审核产品价格
     */
    @Override
    public boolean updateProdPriceAuditStatus(Integer pkid, String content, boolean isAllowed, LoginUser loginUser) {

        Boolean result = false;

        ProdPriceAudit prodPriceAudit = prodPriceAuditDao.findById(pkid);
        prodPriceAudit.setPkid(pkid);
        prodPriceAudit.setRemark(content);
        Integer auditStatusId;
        if (isAllowed) {
            auditStatusId = AuditStatuses.Type_1052;//1052 审核通过
        } else {
            auditStatusId = AuditStatuses.Type_1053;//1053审核不通过
        }
        prodPriceAudit.setAuditStatusId(auditStatusId);

        if (isAllowed) {
            //审核通过
            prodPriceAuditDao.updateProdPriceAuditStatus(prodPriceAudit);

            //下架之前已经上架的相同产品服务,然后将价格上架
            prodPriceDao.updateSameProdPriceSaleStatusesBy(pkid);
        }

        //TODO: Add AuditLog To  Table (bd_audit_log)
        //记录审核日志
        addAuditLog(content, loginUser, prodPriceAudit);


        result = true;

        return result;
    }

    /**
     * 提交产品售价
     */
    @Override
    public boolean addProdPriceAudit(ProdPriceAuditRequest priceAuditRequest, LoginUser loginUser) {
        boolean result = false;
        if (priceAuditRequest != null) {

            priceAuditRequest.setAuditType(AuditTypes.Type_1041);
            Integer prodPriceAuditId = InsertProdPriceAudit(priceAuditRequest, loginUser);

            ProdPriceAudit prodPriceAudit = findById(prodPriceAuditId);

            addAuditLog("提交定价申请", loginUser, prodPriceAudit);

            insertProdPriceList(priceAuditRequest, prodPriceAuditId, loginUser);
            result = true;
        }
        return result;
    }

    /**
     * 改价申请
     */
    @Override
    public boolean updateProdPriceAudit(ProdPriceAuditUpdateRequest priceAuditRequest, LoginUser loginUser) {
        boolean result = false;
        if (priceAuditRequest != null) {
            priceAuditRequest.setAuditType(AuditTypes.Type_1048);
            Integer prodPriceAuditId = InsertProdPriceAudit(priceAuditRequest, loginUser);

            ProdPriceAudit prodPriceAudit = findById(prodPriceAuditId);
            addAuditLog("提交改价申请", loginUser, prodPriceAudit);

            updateProdPriceList(priceAuditRequest, prodPriceAuditId, loginUser);
            result = true;
        }
        return result;
    }

    /**
     * 增加改价地区的服务项价格列表
     */
    private void updateProdPriceList(ProdPriceAuditUpdateRequest priceAuditRequest, Integer prodPriceAuditId, LoginUser loginUser) {
        List<ProdPrice> prodPriceList = new ArrayList<>();
        Integer userId = loginUser.getUcUser().getPkid();

        for (String cityIdStr : priceAuditRequest.getCityProdPrices().keySet()) {
            Integer cityId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(cityIdStr));
            List<ProdPriceRequest> prodPriceRequestList = priceAuditRequest.getCityProdPrices().get(cityIdStr);
            List<ProdPrice> cityPriceList = ConvertToProdPrices(prodPriceAuditId, userId, cityId, prodPriceRequestList);
            prodPriceList.addAll(cityPriceList);
        }
        // Batch Insert ProdPrices
        prodPriceDao.insertBatch(prodPriceList);
    }

    private List<ProdPrice> ConvertToProdPrices(Integer prodPriceAuditId, Integer userId, Integer cityId, List<ProdPriceRequest> prodPriceRequestList) {
        List<ProdPrice> prodPriceList = new ArrayList<>();
        for (ProdPriceRequest priceRequest : prodPriceRequestList) {
            ProdPrice prodPrice = new ProdPrice();
            prodPrice.setPriceAuditId(prodPriceAuditId);//审计批次号
            prodPrice.setServiceId(priceRequest.getProdServiceId());
            prodPrice.setOriginalPrice(NumberUtils.toInt(priceRequest.getOriginalPrice(),-1));
            prodPrice.setPrice(NumberUtils.toInt(priceRequest.getPrice(),-1));
            prodPrice.setCost(NumberUtils.toInt(priceRequest.getCost(),-1));
            prodPrice.setCityId(cityId);
            prodPrice.setIsMust(BooleanUtils.toBoolean(priceRequest.getIsMust()) ? 1 : 0);

            prodPrice.setStock(-1);
            if (priceRequest.getHasStock()) {
                prodPrice.setStock(NumberUtils.toInt(priceRequest.getStock(),-1));
            }
            prodPrice.setIsOnSale(0);//审核通过后,才能上线
            prodPrice.setAddUserId(userId);
            prodPriceList.add(prodPrice);
        }
        return prodPriceList;
    }

    /**
     * 获取未审核状态的纪录
     */
    @Override
    public List<ProdPriceAudit> findUnAuditProdPriceAuditsBy(Integer prodId, Integer organizationId) {
        return prodPriceAuditDao.findProdPriceAuditRowsBy(prodId, organizationId, AuditStatuses.Type_1051);//未审核状态
    }

    /**
     * 插入价格审批记录
     */
    private Integer InsertProdPriceAudit(ProdPriceAuditRequestBase priceAuditRequest, LoginUser loginUser) {
        ProdPriceAudit prodPriceAudit = new ProdPriceAudit();
        prodPriceAudit.setProductId(priceAuditRequest.getProdId());
        prodPriceAudit.setOrganizationId(priceAuditRequest.getOrganizationId());
        prodPriceAudit.setAuditStatusId(AuditStatuses.Type_1051);//未审核状态
        prodPriceAudit.setAuditStatusType(priceAuditRequest.getAuditType());//审核类型
        prodPriceAudit.setAddUserId(loginUser.getUcUser().getPkid());
        return prodPriceAuditDao.insert(prodPriceAudit);
    }

    /**
     * 批量插入价格列表
     */
    private void insertProdPriceList(ProdPriceAuditRequest priceAuditRequest, Integer prodPriceAuditId, LoginUser loginUser) {
        List<ProdPrice> prodPriceList = new ArrayList<>();

        for (Integer cityId : priceAuditRequest.getCityAreaIds()) {
            List<ProdPriceRequest> prodPriceRequestList = priceAuditRequest.getProdServices();
            Integer userId = loginUser.getUcUser().getPkid();
            List<ProdPrice> cityPriceList = ConvertToProdPrices(prodPriceAuditId, userId, cityId, prodPriceRequestList);
            prodPriceList.addAll(cityPriceList);
        }
        // Batch Insert ProdPrices
        prodPriceDao.insertBatch(prodPriceList);
    }

    /**
     * 获取城市列表
     */
    private List<CityArea> getCityAreaList(Integer pkid) {
        List<Integer> cityAreaIds = prodPriceService.findCityIdsByAuditId(pkid);
        List<CityArea> cityAreas = getCityAreasBy(cityAreaIds);

        return cityAreas;
    }

    private List<CityArea> getCityAreasBy(List<Integer> cityAreadIds) {
        List<CityArea> cityAreas = new ArrayList<>();

        for (Integer cityId : cityAreadIds) {
            CityArea cityArea = new CityArea();
            cityArea.setPkid(cityId);
            cityArea.setName(bdDictService.findById(cityId).getName());// 获取城区信息
            cityArea.setFullName(bdDictService.queryDictName(BdDict.TYPE_101, cityId));
            cityAreas.add(cityArea);
        }
        return cityAreas;
    }

    /**
     * 添加审核日志
     */
    private void addAuditLog(String content, LoginUser loginUser, ProdPriceAudit prodPriceAudit) {
        BdAuditLog bdAuditLog = new BdAuditLog();
        bdAuditLog.setTypeId(prodPriceAudit.getAuditStatusType());
        bdAuditLog.setAddUserId(loginUser.getUcUser().getPkid());
        bdAuditLog.setContent(content);
        bdAuditLog.setStatusId(prodPriceAudit.getAuditStatusId());
        bdAuditLog.setFormId(prodPriceAudit.getPkid());
        bdAuditLog.setRemark("");
        bdAuditLog.setLevel(0);
        bdAuditLogService.insert(bdAuditLog);
    }

    private String getNameFromDictBy(Integer pkid) {
        BdDict item = bdDictService.findById(pkid);
        return item != null ? item.getName() : null;
    }

    /*public static void main(String[] args) {
        ProdPriceAuditUpdateRequest request = new ProdPriceAuditUpdateRequest();
        List<ProdPriceRequest> priceRequests = new ArrayList<>();
        priceRequests.add(new ProdPriceRequest());
        priceRequests.add(new ProdPriceRequest());
        Map<String, List<ProdPriceRequest>> cityProdPrices = new HashedMap();
        cityProdPrices.put("1g~~", priceRequests);
        request.setCityProdPrices(cityProdPrices);

        String json = JsonUtils.objectToJson(request);
        System.out.println(json);
    }*/
}