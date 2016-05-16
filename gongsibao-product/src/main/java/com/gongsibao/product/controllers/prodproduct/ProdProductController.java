package com.gongsibao.product.controllers.prodproduct;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.socontract.entity.SoContract;
import com.gongsibao.module.order.socontract.service.SoContractService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdRow;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodprice.service.ProdPriceService;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceAudit;
import com.gongsibao.module.product.prodpriceaudit.service.ProdPriceAuditService;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.entity.RequestProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.entity.ServiceList;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import com.gongsibao.module.sys.bdauditlog.entity.AuditStatuses;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/prodproduct")
public class ProdProductController {
    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private ProdPriceAuditService prodPriceAuditService;
    @Autowired
    private ProdServiceService prodServiceService;
    @Autowired
    private ProdPriceService prodPriceService;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private UcOrganizationService ucOrganizationService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProduct prodProduct, LoginUser user) {
        ResponseData data = new ResponseData();
//        if(user != null)
//        {
//            prodProduct.setAddUserId(user.getUcUser().getPkid());
//        }
//        else
//        {
//            data.setMsg("请登录");
//            return data;
//        }
        if(request.getParameter("param") == null || request.getParameter("param").equals(""))
        {
            data.setMsg("参数不能为空");
            data.setCode(402);
            return data;
        }
        RequestProduct list = JsonUtils.jsonToObject(request.getParameter("param"),RequestProduct.class);
        if(list == null)
        {
            data.setMsg("参数不正确");
            data.setCode(402);
            return data;
        }
        if(list.getName() == null || list.getName().equals(""))
        {
            data.setMsg("产品名不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getTypeIdStr() == null || list.getTypeIdStr().equals(""))
        {
            data.setMsg("产品类型不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getDealerTypeIdStr() == null || list.getDealerTypeIdStr().equals(""))
        {
            data.setMsg("产品销售方不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getProdServiceList().size() == 0)
        {
            data.setMsg("产品服务项不能为空");
            data.setCode(402);
            return data;
        }
        for(ServiceList sl : list.getProdServiceList())
        {
            if(sl.getTypeIdStr() == null || sl.getTypeIdStr().equals(""))
            {
                data.setMsg("产品服务项类型不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getUnitIdStr() == null || sl.getUnitIdStr().equals(""))
            {
                data.setMsg("产品服务项单位不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getPropertyIdStr() == null || sl.getPropertyIdStr().equals(""))
            {
                data.setMsg("产品服务项服务特性不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getHasStock() == null)
            {
                data.setMsg("产品服务项有无库存不能为空");
                data.setCode(402);
                return data;
            }
        }

        prodProduct.setName(list.getName());
        prodProduct.setTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(list.getTypeIdStr())));
        prodProduct.setDealerTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(list.getDealerTypeIdStr())));
        prodProduct.setNo("");
        prodProduct.setDescription(list.getDescription() == null ? "" : list.getDescription());
        prodProduct.setSort(1000d);
        prodProduct.setIsEnabled(1);
        prodProduct.setAddUserId(0);
        prodProduct.setRemark("");

        List<ProdService> pslist = new ArrayList<>();
        for (ServiceList sl:list.getProdServiceList()) {
            ProdService ps = new ProdService();
            ps.setUnitId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getUnitIdStr())));
            ps.setPropertyId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getPropertyIdStr())));
            ps.setTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getTypeIdStr())));
            ps.setDescription("");
            ps.setSort(1000d);
            ps.setIsEnabled(1);
            ps.setHasStock(sl.getHasStock());
            ps.setAddUserId(0);
            ps.setRemark("");
            pslist.add(ps);
        }

        data.setData(prodProductService.saveProduct(prodProduct,pslist));

        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProduct prodProduct, LoginUser user) {
        ResponseData data = new ResponseData();
        if(request.getParameter("param") == null || request.getParameter("param").equals(""))
        {
            data.setMsg("参数不能为空");
            data.setCode(402);
            return data;
        }
        RequestProduct list = JsonUtils.jsonToObject(request.getParameter("param"),RequestProduct.class);

        if(list == null)
        {
            data.setMsg("参数不正确");
            data.setCode(402);
            return data;
        }
        if(list.getPkidStr() == null || list.getPkidStr().equals(""))
        {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getName() == null || list.getName().equals(""))
        {
            data.setMsg("产品名不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getTypeIdStr() == null || list.getTypeIdStr().equals(""))
        {
            data.setMsg("产品类型不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getDealerTypeIdStr() == null || list.getDealerTypeIdStr().equals(""))
        {
            data.setMsg("产品销售方不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getProdServiceList().size() == 0)
        {
            data.setMsg("产品服务项不能为空");
            data.setCode(402);
            return data;
        }
        for(ServiceList sl : list.getProdServiceList())
        {
            if(sl.getTypeIdStr() == null || sl.getTypeIdStr().equals(""))
            {
                data.setMsg("产品服务项类型不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getUnitIdStr() == null || sl.getUnitIdStr().equals(""))
            {
                data.setMsg("产品服务项单位不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getPropertyIdStr() == null || sl.getPropertyIdStr().equals(""))
            {
                data.setMsg("产品服务项服务特性不能为空");
                data.setCode(402);
                return data;
            }
            if(sl.getHasStock() == null)
            {
                data.setMsg("产品服务项有无库存不能为空");
                data.setCode(402);
                return data;
            }
        }

        prodProduct.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(list.getPkidStr())));
        prodProduct.setName(list.getName());
        prodProduct.setTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(list.getTypeIdStr())));
        prodProduct.setDealerTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(list.getDealerTypeIdStr())));
        prodProduct.setNo("");
        prodProduct.setDescription(list.getDescription());
        prodProduct.setSort(1000d);
        prodProduct.setIsEnabled(1);
        prodProduct.setAddUserId(0);
        prodProduct.setRemark("");

        List<ProdService> pslist = new ArrayList<>();
        for (ServiceList sl:list.getProdServiceList()) {
            ProdService ps = new ProdService();
            if(sl.getPkidStr() != null)
            {
                ps.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getPkidStr())));
            }
            ps.setProductId(prodProduct.getPkid());
            ps.setUnitId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getUnitIdStr())));
            ps.setPropertyId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getPropertyIdStr())));
            ps.setTypeId(Integer.valueOf(SecurityUtils.rc4Decrypt(sl.getTypeIdStr())));
            ps.setDescription("");
            ps.setSort(1000d);
            ps.setIsEnabled(1);
            ps.setHasStock(sl.getHasStock());
            ps.setAddUserId(0);
            ps.setRemark("");
            pslist.add(ps);
        }

        data.setData(prodProductService.updateProduct(prodProduct,pslist));

        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("currentPage");
        String pagesize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String dealerTypeId = request.getParameter("dealerTypeIdStr");
        String typeId = request.getParameter("typeIdStr");
        String isEnabled = request.getParameter("isEnabled");
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pagesize)) {
            pagesize = "10";
        }
        Map<String, Object> paramMap = new HashMap();
        if(StringUtils.isNotEmpty(name))
        {
            paramMap.put("name", name);
        }
        if(StringUtils.isNotEmpty(dealerTypeId))
        {
            paramMap.put("dealerTypeId", Integer.valueOf(SecurityUtils.rc4Decrypt(dealerTypeId)));
        }
        if(StringUtils.isNotEmpty(typeId))
        {
            paramMap.put("typeId", Integer.valueOf(SecurityUtils.rc4Decrypt(typeId)));
        }
        if(StringUtils.isNotEmpty(isEnabled))
        {
            paramMap.put("isEnabled", Integer.valueOf(isEnabled));
        }
        data.setData(prodProductService.pageByProducts(paramMap, Integer.valueOf(page), Integer.valueOf(pagesize)));
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        if(pkidStr == null || pkidStr.equals(""))
        {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        Integer pkid = Integer.valueOf(SecurityUtils.rc4Decrypt(pkidStr));
        ProdProduct prodProduct = prodProductService.findById(pkid);
        Map<String,Object> pidMap = new HashMap();
        pidMap.put("product_id",prodProduct.getPkid());
        List<ProdService> prodServiceList = prodServiceService.findByProperties(pidMap,0,1000);
        List<Integer> bdIds = new ArrayList();
        for (ProdService item : prodServiceList)
        {
            bdIds.add(item.getUnitId());
            bdIds.add(item.getTypeId());
            bdIds.add(item.getPropertyId());
        }
        List<BdDict> bdlist = bdDictService.findByIds(bdIds);
        List<ProdService> prodServiceList1 = new ArrayList();
        for (ProdService item : prodServiceList)
        {
            for(BdDict itemps : bdlist)
            {
                if(itemps.getPkid().equals(item.getTypeId()))
                {
                    item.setTypeName(itemps.getName());
                }
                if(itemps.getPkid().equals(item.getPropertyId()))
                {
                    item.setPropertyName(itemps.getName());
                }
                if(itemps.getPkid().equals(item.getUnitId()))
                {
                    item.setUnitName(itemps.getName());
                }
            }
            prodServiceList1.add(item);
        }
        prodProduct.setProdServiceList(prodServiceList1);
        data.setData(prodProduct);
        return data;
    }
    @RequestMapping("/getallorganization")
    public ResponseData getallorganization(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String productIdStr = checkParameters(request, "productId", data);
        if(StringUtils.isEmpty(productIdStr)) return data;
        Integer productId = Integer.valueOf(SecurityUtils.rc4Decrypt(productIdStr));
        HashMap<String, Object>  propertiesMap1 = new HashMap<>();
        propertiesMap1.put("product_id", productId);
        propertiesMap1.put("audit_status_id", AuditStatuses.Type_1054);
        //在审核表中查询当前产品所有审核通过的ProdPriceAudit
        List<ProdPriceAudit> prodPriceAuditlist = prodPriceAuditService.pageByProperties(propertiesMap1, 0).getList();
        ArrayList<Integer> organizationPkids = new ArrayList<>();
        //检查权限,拿到当前用户的组织及其子组织
        List<Integer> orgIds= ucUserService.getUserOrganizationIds(loginUser.getUcUser().getPkid());
        for (ProdPriceAudit prodPriceAudit : prodPriceAuditlist) {
            if (orgIds.contains(prodPriceAudit.getOrganizationId())) {
                HashMap<String, Object>  propertiesMap2 = new HashMap<>();
                propertiesMap2.put("price_audit_id", prodPriceAudit.getPkid());
                propertiesMap2.put("is_on_sale", 1);
                //根据审核Id查询出所有上架产品,当集合大于0时证明供应商有上架了此产品
                List<ProdPrice> prodPriceList = prodPriceService.pageByProperties(propertiesMap2, 0).getList();
                if (prodPriceList.size() > 0) {
                    organizationPkids.add(prodPriceAudit.getOrganizationId());
                }
            }
        }
        List<UcOrganization> ucOrganizationList  = ucOrganizationService.findByIds(organizationPkids);
        data.setData(ucOrganizationList);
        return data;
    }

    @RequestMapping("/getallcity")
    public ResponseData getallcity(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String productIdStr = checkParameters(request , "productId" , data);
        String organizationIdStr = request.getParameter("organizationId");
        if(StringUtils.isEmpty(productIdStr)) return data;
        Integer productId = Integer.valueOf(SecurityUtils.rc4Decrypt(productIdStr));
        //暂时没有拦截器, 等王昆开发完后 组织Id值需要改成null
        Integer organizationId = 47;
        if(StringUtils.isNotEmpty(organizationIdStr)) {
            organizationId = Integer.valueOf(SecurityUtils.rc4Decrypt(organizationIdStr));
        } else {
            List<UcOrganization> list = loginUser.getUcOrganizationList();
            if (CollectionUtils.isNotEmpty(list)) {
                organizationId = list.get(0).getPkid();
            }
        }
        HashMap<String, Object>  propertiesMap = new HashMap<>();
        propertiesMap.put("product_id", productId);
        if (organizationId != null) {
            propertiesMap.put("organization_id", organizationId);
        }
        List<BdDict> list = prodProductService.findAllCity(propertiesMap);
        data.setData(list);
        return data;
    }

    @RequestMapping("/isenabled")
    public ResponseData isenabled(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProduct prodProduct, LoginUser user) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        if(pkidStr == null || pkidStr.equals(""))
        {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        ProdProduct pp = new ProdProduct();
        pp.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(pkidStr)));
        pp.setIsEnabled(Integer.valueOf(request.getParameter("enabled")));
        data.setData(prodProductService.updateEnabled(pp));
        return data;
    }

    @RequestMapping("/undercarriage")
    public ResponseData undercarriage(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String productIdStr = checkParameters(request , "productId" , data);
        String organizationIdStr = checkParameters(request , "organizationId" , data);
        if(!StringUtils.isNotBlank(productIdStr)||!StringUtils.isNotBlank(organizationIdStr)) return data;
        String[] allCityId = request.getParameterValues("allCityId");
        if(allCityId == null ||allCityId.length == 0) {
            data.setCode(403);
            data.setMsg("参数allCityId错误");
            return data;
        }
        Integer productId = Integer.valueOf(SecurityUtils.rc4Decrypt(productIdStr));
        Integer organizationId = Integer.valueOf(SecurityUtils.rc4Decrypt(organizationIdStr));
        HashSet<Integer> citySet = new HashSet<>();
        //防止前端人员传递重复地区
        for(String string: allCityId) {
            citySet.add(Integer.valueOf(SecurityUtils.rc4Decrypt(string)));
        }
        List<Integer> list = new ArrayList<>(citySet);
        HashMap<String, Object>  propertiesMap1 = new HashMap<>();
        propertiesMap1.put("product_id", productId);
        propertiesMap1.put("organization_id", organizationId);
        propertiesMap1.put("audit_status_id", AuditStatuses.Type_1052);
        List<ProdPriceAudit> prodPriceAuditlist = prodPriceAuditService.pageByProperties(propertiesMap1, 0).getList();
        HashMap<Integer, ProdPrice> hashMap = new HashMap<>();
        for (ProdPriceAudit prodPriceAudit : prodPriceAuditlist) {
            List<ProdPrice> prodPriceList = prodPriceService.findByAuditIdAndCityID(prodPriceAudit.getPkid(), list, 1);
            for (ProdPrice prodPrice : prodPriceList) {
                hashMap.put(prodPrice.getPkid(), prodPrice);
            }
        }
        Collection<ProdPrice> collection = hashMap.values();
        HashMap<String, Object> resultMap = new HashMap();
        if(!CollectionUtils.isEmpty(collection)) {
            ArrayList<Integer> allPkis = new ArrayList<>();
            for(ProdPrice prodPrice : collection) {
                allPkis.add(prodPrice.getPkid());
            }
            prodPriceService.editProdPriceWithIsOnSale(allPkis, 0);
            resultMap.put("isSuccess", true);
            data.setMsg("下架成功");
        } else {
            resultMap.put("isSuccess", false);
            data.setMsg("操作失败");
        }
        data.setData(resultMap);
        return data;
    }

    private String checkParameters(HttpServletRequest request, String parameterName, ResponseData data) {
        String rc4EncryptStr = request.getParameter(parameterName);
        if (!StringUtils.isNotBlank(rc4EncryptStr)) {
            data.setCode(403);
            data.setMsg("缺少" + parameterName + "参数");
        }
        return  rc4EncryptStr;
    }

}