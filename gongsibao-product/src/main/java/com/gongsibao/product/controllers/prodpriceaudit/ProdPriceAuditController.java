package com.gongsibao.product.controllers.prodpriceaudit;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodprice.entity.ProdPriceDetail;
import com.gongsibao.module.product.prodprice.entity.ProdPriceRequest;
import com.gongsibao.module.product.prodpriceaudit.entity.*;
import com.gongsibao.module.product.prodpriceaudit.service.ProdPriceAuditService;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prodpriceaudit")
public class ProdPriceAuditController {

    @Autowired
    private ProdPriceAuditService prodPriceAuditService;


    @RequestMapping(value = "/add_demo")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdPriceAudit prodPriceAudit) {
        ResponseData data = new ResponseData();
        prodPriceAuditService.insert(prodPriceAudit);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        prodPriceAuditService.delete(getId(request));
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update_demo")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute ProdPriceAudit prodPriceAudit) {
        ResponseData data = new ResponseData();
        prodPriceAudit.setPkid(getId(request));
        prodPriceAuditService.update(prodPriceAudit);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        ProdPriceAudit prodPriceAudit = prodPriceAuditService.findById(pkid);
        data.setData(prodPriceAudit);
        return data;
    }

    @RequestMapping({"/list_demo"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Pager<ProdPriceAudit> pager = prodPriceAuditService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping({"/count"})
    public ResponseData getRowsCount(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        Map<String, Object> condition = getQueryProdPriceRowsParas(request);
        Integer result = prodPriceAuditService.countProdPriceAuditRows(condition);

        data.setMsg("操作成功");
        data.setData(result);

        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData getRows(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        // 当前页
        String page = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "10";
        }
        Map<String, Object> condition = getQueryProdPriceRowsParas(request);


        Pager<ProdPriceAuditRow> pager = prodPriceAuditService.pageProdPriceAuditRows(condition,
                Integer.valueOf(page), NumberUtils.toInt(pageSize));
        data.setData(pager);
        return data;
    }

    @RequestMapping({"/listonsale"})
    public ResponseData getOnSaleProdPriceRows(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();

        // 当前页
        String page = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "10";
        }

        Map<String, Object> condition = new HashedMap();
        // 审核状态
        String auditStatusStr = StringUtils.trimToEmpty(request.getParameter("auditStatusIdStr"));
        if (StringUtils.isNotBlank(auditStatusStr)) {
            condition.put("audit_status_id", NumberUtils.toInt(auditStatusStr, -1));
        } else {
            data.setCode(400);
            data.setMsg("auditStatusIdStr不能为空");
            return data;
        }

        //组织ids
        //TODO 增加权限 ，限定能看到的组织集合
        String organizationIdStr = StringUtils.trimToEmpty(request.getParameter("organizationIdStr"));
        if (StringUtils.isNotBlank(organizationIdStr)) {
            condition.put("organization_id", organizationIdStr);
        }

        // 产品名称
        String prodName = StringUtils.trimToEmpty(request.getParameter("prodName"));
        if (StringUtils.isNotBlank(prodName)) {
            condition.put("product_name", "%" + prodName + "%");
        }

        // 地区ID
        String cityAreaIdStr = StringUtils.trimToEmpty(request.getParameter("cityIdStr"));
        if (StringUtils.isNotBlank(cityAreaIdStr)) {
            condition.put("city_id", getIntegerIdFromEncryptStr(cityAreaIdStr));
        }


        Pager<ProdPriceOnSaleRow> pager = prodPriceAuditService.pageProdPriceOnSaleRows(condition,
                Integer.valueOf(page), NumberUtils.toInt(pageSize));
        data.setData(pager);

        return data;
    }


    @RequestMapping({"/detail"})
    public ResponseData getProdPriceDetail(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Integer pkid = getId(request);
        Map<String, Object> result = prodPriceAuditService.findDetailById(pkid);
        data.setData(result);
        data.setMsg("成功获取数据");
        return data;
    }

    @RequestMapping({"/onsaledetail"})
    public ResponseData getProdPriceDetail(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        Map<String, Object> condition = new HashedMap();

        // 审核状态
        String auditStatusStr = StringUtils.trimToEmpty(request.getParameter("auditStatusIdStr"));
        if (StringUtils.isNotBlank(auditStatusStr)) {
            condition.put("audit_status_id", NumberUtils.toInt(auditStatusStr, -1));
        }

        //组织ids
        //TODO 增加权限 ，限定能看到的组织集合
        String organizationIdStr = StringUtils.trimToEmpty(request.getParameter("organizationIdStr"));
        if (StringUtils.isNotBlank(organizationIdStr)) {
            condition.put("organization_id", organizationIdStr);
        }

        // 产品Id
        String prodIdStr = StringUtils.trimToEmpty(request.getParameter("prodIdStr"));
        if (StringUtils.isNotBlank(prodIdStr)) {
            condition.put("product_id", getIntegerIdFromEncryptStr(prodIdStr));
        }

        ProdPriceOnSaleRow result = prodPriceAuditService.findOrgProdPricesDetailById(condition);

        data.setData(result);
        data.setMsg("成功获取数据");
        return data;
    }


    @RequestMapping("/prodpriceservices")
    public ResponseData getProdPriceServices(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        Map<String, Object> condition = new HashedMap();

        // 审核状态
        String auditStatusStr = StringUtils.trimToEmpty(request.getParameter("auditStatusIdStr"));
        if (StringUtils.isNotBlank(auditStatusStr)) {
            condition.put("audit_status_id", NumberUtils.toInt(auditStatusStr, -1));
        } else {
            data.setCode(400);
            data.setMsg("auditStatusIdStr 不能为空");
            return data;
        }

        //组织ids
        //TODO 增加权限 ，限定能看到的组织集合
        String organizationIdStr = StringUtils.trimToEmpty(request.getParameter("organizationIdStr"));
        if (StringUtils.isNotBlank(organizationIdStr)) {
            Integer orgId=getIntegerIdFromEncryptStr(organizationIdStr);
            condition.put("organization_id", orgId);
        } else {
            data.setCode(400);
            data.setMsg("organizationIdStr 不能为空");
            return data;
        }


        // 产品Id
        String prodIdStr = StringUtils.trimToEmpty(request.getParameter("prodIdStr"));
        if (StringUtils.isNotBlank(prodIdStr)) {
            condition.put("product_id", getIntegerIdFromEncryptStr(prodIdStr));
        } else {
            data.setCode(400);
            data.setMsg("prodIdStr 不能为空");
            return data;
        }

        // 地区ID
        String cityAreaIdStr = StringUtils.trimToEmpty(request.getParameter("cityIdStr"));
        if (StringUtils.isNotBlank(cityAreaIdStr)) {
            condition.put("city_id", getIntegerIdFromEncryptStr(cityAreaIdStr));
        } else {
            data.setCode(400);
            data.setMsg("cityIdStr 不能为空");
            return data;
        }


        List<ProdPriceDetail> prodPriceDetails = prodPriceAuditService.getProdPricesList(condition);


        data.setCode(200);
        data.setMsg("操作成功");
        data.setData(prodPriceDetails);

        return data;
    }


    /**
     * 增加产品定价申请
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseData addProdPrices(HttpServletRequest request, HttpServletResponse response
            , LoginUser loginUser) {

        ResponseData data = new ResponseData();
        ProdPriceAuditRequest priceAuditRequest = getProdPriceAuditRequest(request, loginUser, data);

        if (priceAuditRequest == null) {
            data.setCode(400);
            return data;
        }
        boolean isOk;
        if (!prodPriceAuditService.findUnAuditProdPriceAuditsBy(priceAuditRequest.getProdId()
                , priceAuditRequest.getOrganizationId()).isEmpty()) {
            data.setCode(400);
            data.setMsg("该产品还未审核通过的记录，不能进行再次上架操作！请联系管理员撤销上次上架申请！");
            return data;
        } else {
            isOk = prodPriceAuditService.addProdPriceAudit(priceAuditRequest, loginUser);
        }
        if (isOk) {
            data.setMsg("操作成功");
        } else {
            data.setCode(400);
            data.setMsg("请求出错");
        }

        return data;
    }

    /**
     * 更新产品定价
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseData updateProdPrices(HttpServletRequest request, HttpServletResponse response
            , LoginUser loginUser) {

        ResponseData data = new ResponseData();

        String json = getPostJsonString(request, data);

        ProdPriceAuditUpdateRequest priceAuditRequest = JsonUtils.jsonToObject(json, ProdPriceAuditUpdateRequest.class);

        if (priceAuditRequest == null) {
            data.setCode(400);
            return data;
        }

        boolean isOk;
        if (!prodPriceAuditService.findUnAuditProdPriceAuditsBy(priceAuditRequest.getProdId()
                , priceAuditRequest.getOrganizationId()).isEmpty()) {
            data.setCode(400);
            data.setMsg("该产品还未审核通过上架申请，不能进行再次上架操作！请联系管理员审核上架申请！");
            return data;
        } else {
            isOk = prodPriceAuditService.updateProdPriceAudit(priceAuditRequest, loginUser);
        }
        if (isOk) {
            data.setMsg("操作成功");
        } else {
            data.setCode(400);
            data.setMsg("请求出错");
        }

        return data;
    }


    /**
     * 审核产品价格
     */
    @RequestMapping("/audit")
    public ResponseData auditProdPrice(HttpServletRequest request, HttpServletResponse response
            , LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);

        String content = request.getParameter("content");
        String isAllowStr = request.getParameter("isAllowed");
        if (StringUtils.isBlank(isAllowStr)) {
            data.setCode(400);
            data.setMsg("isAllowed 参数必须指定");
            return data;
        }
        Boolean isAllowed = Boolean.valueOf(isAllowStr);

        boolean isOk = prodPriceAuditService.updateProdPriceAuditStatus(pkid, content, isAllowed, loginUser);


        if (isOk) {
            data.setMsg("操作成功");
        } else {
            data.setMsg("操作失败");
        }
        return data;
    }

    /**
     * 获取尚未审核的上架地区
     */
    @RequestMapping("/UnAuidtCitys")
    public ResponseData getUnauditPriceCitys(HttpServletRequest request, HttpServletResponse response
            , LoginUser loginUser) {

        ResponseData data = new ResponseData();

        List<UcOrganization> orgs = loginUser.getUcOrganizationList();
        if (orgs.isEmpty()) {
            data.setCode(400);
            data.setMsg("找不到登陆用户的相关组织");
            return data;
        }

        //获取产品Id
        Integer prodId = getIntegerIdFromEncryptStr(request.getParameter("prodIdStr"));
        if (prodId < 1) {
            data.setCode(400);
            data.setMsg("找不到相关产品");
            return data;
        }

        List<CityArea> cityAreass = prodPriceAuditService.findUnauditCityAreasByProdIdAndOrgId(prodId, orgs.get(1).getPkid());
        data.setCode(200);
        data.setMsg("成功获取数据");
        data.setData(cityAreass);

        return data;
    }

    private Integer getId(HttpServletRequest request) {

        // 产品定价审批ID
        String pkidStr = request.getParameter("pkidStr");
        if (StringUtils.isNotBlank(pkidStr)) {
            //TODO 正式环境需要开启解密
            pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        }
        Integer pkid = NumberUtils.toInt(pkidStr, -1);

        return pkid;
    }

    private Integer getIntegerIdFromEncryptStr(String encryKeyStr) {
        String pkidStr = encryKeyStr;
        if (StringUtils.isNotBlank(pkidStr)) {
            //TODO 正式环境需要开启解密
            pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        }
        return NumberUtils.toInt(pkidStr, -1);
    }

    private ProdPriceAuditRequest getProdPriceAuditRequest(HttpServletRequest request, LoginUser loginUser, ResponseData data) {
        ProdPriceAuditRequest priceAuditRequest = null;
        List<UcOrganization> orgs = loginUser.getUcOrganizationList();

        if (loginUser == null || orgs.isEmpty()) {
            data.setMsg("无法获取相应的组织Id");
            return null;
        }

        String json = getPostJsonString(request, data);

        priceAuditRequest = JsonUtils.jsonToObject(json, ProdPriceAuditRequest.class);

        if (priceAuditRequest == null) {
            data.setCode(400);
            data.setMsg("请求参数不符合格式");
            return null;
        }
        //设置组织Id
        Integer organizationId = orgs.get(0).getPkid();
        priceAuditRequest.setOrganizationId(organizationId);

        //解密产品Id
        Integer prodId = getIntegerIdFromEncryptStr(priceAuditRequest.getProdIdStr());
        priceAuditRequest.setProdId(prodId);

        //解密服务Id
        for (ProdPriceRequest priceRequest : priceAuditRequest.getProdServices()) {
            Integer serviceId = getIntegerIdFromEncryptStr(priceRequest.getServiceIdStr());
            if (serviceId < 0) {
                data.setMsg("找不到相应的服务");
                return null;
            }
            priceRequest.setProdServiceId(serviceId);
        }
        //解密城市Id
        priceAuditRequest.setCityAreaIds(new ArrayList<Integer>());
        List<Integer> cityIds=priceAuditRequest.getCityAreaIds();
        cityIds.clear();
        for (String cityIdStr : priceAuditRequest.getCityAreaIdStrs()) {
            Integer cityId = getIntegerIdFromEncryptStr(cityIdStr);
            if (cityId < 0) {
                data.setMsg("找不到相应的城市");
                return null;
            }
            cityIds.add(cityId);
        }


        return priceAuditRequest;
    }

    /**
     * 获取Post方法时，Post 的Body中Json。
     */
    private String getPostJsonString(HttpServletRequest request, ResponseData data) {
        String json = null;
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            try {
                json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                System.out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
                data.setMsg(e.getMessage());
            }
        }
        return json;
    }

    /**
     * 获取产品价格参数
     */
    private Map<String, Object> getQueryProdPriceRowsParas(HttpServletRequest request) {

        // 产品分类ID（套餐，还是单品）
        String prodTypeId = StringUtils.trimToEmpty(request.getParameter("prodType"));
        // 产品名称
        String prodName = StringUtils.trimToEmpty(request.getParameter("prodName"));
        // 产品编号
        String prodNo = StringUtils.trimToEmpty(request.getParameter("prodNo"));
        //组织id
        String organizationIdStr = StringUtils.trimToEmpty(request.getParameter("organizationIdStr"));
        // 审核状态
        String auditStatusStr = StringUtils.trimToEmpty(request.getParameter("auditStatusIdStr"));
        // 地区ID
        String cityAreaIdStr = StringUtils.trimToEmpty(request.getParameter("cityIdStr"));


        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();

        if (NumberUtils.toInt(prodTypeId, -1) > -1) {
            condition.put("type_id", prodTypeId);
        }
        if (StringUtils.isNotBlank(prodName)) {
            condition.put("name", prodName);
        }
        if (StringUtils.isNotBlank(prodNo)) {
            condition.put("no", prodNo);
        }
        if (StringUtils.isNotBlank(organizationIdStr)) {
            //TODO 需要增加解密ID
            Integer organizationId = getIntegerIdFromEncryptStr(organizationIdStr);
            condition.put("organization_id", organizationId);
        }
        if (StringUtils.isNotBlank(cityAreaIdStr)) {
            //TODO 需要增加解密ID

            Integer cityAreaId = getIntegerIdFromEncryptStr(cityAreaIdStr);
            condition.put("city_id", cityAreaId);
        }
        if (StringUtils.isNotBlank(auditStatusStr)) {
            //TODO 需要增加解密ID
            //auditStatusStr = SecurityUtils.rc4Decrypt(auditStatusStr);
            //Integer auditStatusId = Integer.valueOf(auditStatusStr);
            Integer auditStatusId = getIntegerIdFromEncryptStr(auditStatusStr);
            condition.put("audit_status_id", auditStatusId);
        }
        return condition;
    }
}


