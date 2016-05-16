package com.gongsibao.order.controllers.soorder;


import com.gongsibao.common.util.DateUtils;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderdiscount.entity.SoOrderDiscount;
import com.gongsibao.module.order.soorderdiscount.service.SoOrderDiscountService;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodprice.service.ProdPriceService;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceAudit;
import com.gongsibao.module.product.prodpriceaudit.service.ProdPriceAuditService;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import com.gongsibao.module.sys.bdsync.entity.BdSync;
import com.gongsibao.module.sys.bdsync.service.BdSyncService;
import com.gongsibao.module.uc.ucaccount.entity.UcAccount;
import com.gongsibao.module.uc.ucaccount.service.UcAccountService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;

@RestController
@RequestMapping("/soorder")
public class SoOrderController {

    @Autowired
    private SoOrderService soOrderService;
    @Autowired
    private ProdServiceService prodServiceService;
    @Autowired
    private ProdPriceService prodPriceService;
    @Autowired
    private ProdPriceAuditService prodPriceAuditService;
    @Autowired
    private UcAccountService ucAccountService;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private SoOrderDiscountService soOrderDiscountService;
    @Autowired
    private BdSyncService bdSyncService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrder soOrder) {
        ResponseData data = new ResponseData();
        soOrderService.insert(soOrder);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrder soOrder) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrder.setPkid(pkid);
        soOrderService.update(soOrder);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Pager<SoOrder> pager = soOrderService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        SoOrder soOrder = soOrderService.findById(pkid);
        data.setData(soOrder);
        return data;
    }

    @RequestMapping("/create")
    public ResponseData addOrder(HttpServletRequest request, LoginUser loginUser){
        ResponseData data = new ResponseData();

        String json = StringUtils.trimToEmpty(request.getParameter("param"));
        SoOrder soOrder = JsonUtils.jsonToObject(json, SoOrder.class);

        //产品必选项是否选中
        if(!checkMustServiceItem(soOrder.getProdList())){
            data.setCode(200);
            data.setMsg("必选服务未选中");
            return data;
        }

        //通过手机号获取用户信息
        UcAccount account = ucAccountService.findByMobile(soOrder.getAccountMobile());

        //总价
        int totalPrice = 0;
        int payablePrice = 0;

        Date dt = new Date();

        String prodName = new String();

        //通过产品服务定价ID查找原价
        if(soOrder.getProdList().size() != 0) {
            for (SoOrderProd soOrderProd : soOrder.getProdList()) {
                //产品总价
                int priceOriginal=0;
                int price = 0;
                //查询prod信息
                for (SoOrderProdItem soOrderProdItem : soOrderProd.getItemList()){
                    //查询产品定价相关信息
                    ProdPrice prodPrice =  prodPriceService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(soOrderProdItem.getPriceIdStr())));
                    priceOriginal += prodPrice.getOriginalPrice();
                    price += soOrderProdItem.getPrice();

                    soOrderProdItem.setPriceId(prodPrice.getPkid());
                    //产品服务项
                    ProdService prodService = prodServiceService.findById(prodPrice.getServiceId());

                    soOrderProdItem.setPriceOriginal(prodPrice.getOriginalPrice()); //订单原价
                    soOrderProdItem.setUnitName(bdDictService.queryDictName(202,prodService.getUnitId()));

                    //特性+服务名称
                    String propertServicename;
                    if(prodService.getPropertyId().equals(0))
                        propertServicename = bdDictService.queryDictName(5,prodService.getTypeId());
                    else
                        propertServicename = bdDictService.queryDictName(10,prodService.getPropertyId()) + bdDictService.queryDictName(5,prodService.getTypeId());
                    soOrderProdItem.setServiceName(propertServicename); //产品服务名称
                    soOrderProdItem.setPrice(prodPrice.getOriginalPrice());
                    soOrderProdItem.setPriceRefund(0);
                }

                ProdProduct prodProduct = prodProductService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(soOrderProd.getProductIdStr())));

                //产品名称
                if(StringUtils.isNotBlank(prodName))
                    prodName = prodProduct.getName();
                else
                    prodName += "," + prodProduct.getName();

                soOrderProd.setNo("");
                soOrderProd.setProductId(prodProduct.getPkid());
                soOrderProd.setProductName(prodProduct.getName());
                soOrderProd.setCityId(Integer.valueOf(SecurityUtils.rc4Decrypt(soOrderProd.getCityIdStr())));
                soOrderProd.setProcessStatusId(0);
                soOrderProd.setAuditStatusId(0);
                soOrderProd.setPrice(price);
                soOrderProd.setPriceOriginal(priceOriginal);
                soOrderProd.setIsRefund(0);
                soOrderProd.setProcessedDays(0);

                totalPrice += priceOriginal;
                payablePrice += price;
            }

            //是否改价
            if(totalPrice != payablePrice) {
                soOrder.setIsChangePrice(1);
                soOrder.setChangePriceAuditStatusId(1051); //改价申请状态
            }
            else {
                soOrder.setIsChangePrice(0);
                soOrder.setChangePriceAuditStatusId(0); //改价申请状态
            }

            //订单编号
            soOrder.setType(1);
            soOrder.setNo(soOrderService.findMaxNo()+1);
            soOrder.setAccountId(account.getPkid());
            soOrder.setAccountMobile(account.getMobilePhone());
            soOrder.setAccountName(account.getRealName());
            soOrder.setTotalPrice(totalPrice);
            //优惠券
            if(StringUtils.isNotBlank(soOrder.getOrderDiscount())) {
                List<Map<String, Object>> discounts = soOrderDiscountService.discountList(soOrder.getOrderDiscount());
                BdSync bdsync = bdSyncService.findByMPkidAndTableName(account.getPkid(), "uc_account");
                Integer preferential =0;
                List<SoOrderDiscount> soOrderdiscountList = new ArrayList<>();

                for (Map<String, Object> item : discounts) {
                    SoOrderDiscount soOrderdiscount = new SoOrderDiscount();

                    if (NumberUtils.toInt(item.get("IsDisabled")) == 1) {
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]已经被禁用");
                        return data;
                    }

                    if (NumberUtils.toInt(item.get("IsUse")) == 1) {
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]已经被使用");
                        return data;
                    }

                    if (!StringUtils.isNotBlank(StringUtils.trimToEmpty(item.get("Account_ID")))) {
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]还没有被激活");
                        return data;
                    }

                    if (!StringUtils.trimToEmpty(item.get("Account_ID")).equals(bdsync.getsId())) {
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]不属于你");
                        return data;
                    }

                    BigDecimal amountLimit = new BigDecimal(item.get("AmountLimit").toString());
                    if(BigDecimal.valueOf(payablePrice).compareTo(amountLimit) == -1){

                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]必须满"+amountLimit+"才能使用");
                        return data;
                    }

                    if(NumberUtils.toInt(item.get("OverlayType")) == 0 && discounts.size() >1){
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]不能叠加使用");
                        return data;
                    }

                    if(NumberUtils.toInt(item.get("FirstOrderUse")) == 1){
                        List<SoOrder> soOrderList = soOrderService.findByAccountId(account.getPkid());
                        if(soOrderList.size() >0){
                            data.setCode(-1);
                            data.setMsg("优惠券[" + item.get("No") + "]必须首单才能使用");
                            return data;
                        }
                    }

                    if(DateUtils.strToDate(item.get("StartDate").toString()).compareTo(dt) == 1){
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]的开始日期是"+item.get("StartDate").toString()+"还没有到使用期,禁止使用");
                        return data;
                    }

                    if(DateUtils.strToDate(item.get("EndDate").toString()).compareTo(dt) == -1){
                        data.setCode(-1);
                        data.setMsg("优惠券[" + item.get("No") + "]的结束日期是"+item.get("EndDate").toString()+"已过期,禁止使用");
                        return data;
                    }

                    if(soOrder.getProdList().size() != 0){
                        for (SoOrderProd soOrderProd: soOrder.getProdList()) {
                            BdSync productGuid = bdSyncService.findByMPkidAndTableName(soOrderProd.getProductId(), "prod_product");
                            BdSync cityGuid = bdSyncService.findByMPkidAndTableName(soOrderProd.getCityId(), "bd_dict");

                            if(soOrderDiscountService.findByProductId(productGuid.getsId(), StringUtils.trimToEmpty(item.get("ID"))).size() == 0){
                                data.setCode(-1);
                                data.setMsg("优惠券[" + item.get("No") + "]不支持产品"+soOrderProd.getProductName());
                                return data;
                            };

                            if(soOrderDiscountService.findByCityId(cityGuid.getsId(), StringUtils.trimToEmpty(item.get("ID"))).size() ==0){
                                BdDict cityDict = bdDictService.findById(soOrderProd.getCityId());
                                data.setCode(-1);
                                data.setMsg("优惠券[" + item.get("No") + "]不支持区域"+cityDict.getName());
                                return data;
                            }
                        }
                    }

                    preferential += NumberUtils.toInt(item.get("preferential"));

                    soOrderdiscount.setAddTime(dt);
                    soOrderdiscount.setAmount(NumberUtils.toInt(item.get("preferential")));
                    soOrderdiscount.setRemark(StringUtils.trimToEmpty(item.get("Remark")));
                    soOrderdiscount.setTypeId(3029);

                    soOrderdiscountList.add(soOrderdiscount);
                }

                payablePrice = payablePrice - preferential;
                soOrder.setSoOrderDiscountList(soOrderdiscountList);
            }

            soOrder.setPayablePrice(payablePrice);
            soOrder.setPaidPrice(0);

            soOrder.setPayStatusId(3011); //待付款
            soOrder.setProcessStatusId(0); //未付款,不初始化状态
            soOrder.setRefundStatusId(0); //退款
            soOrder.setSourceTypeId(3041); //来源类型 PC端
            soOrder.setIsInstallment(0);  //分期
            soOrder.setInstallmentMode("");  //分期形式,存储金额
            soOrder.setInstallmentAuditStatusId(0);

            soOrder.setIsInvoice(0); //是否开发票
            soOrder.setDescription("");
            soOrder.setIsPackage(0);
            soOrder.setPackageId(0);
            soOrder.setAddTime(dt);
            soOrder.setAddUserId(loginUser.getUcUser().getPkid());
            soOrder.setProdName(prodName);
        }
        else
        {
            data.setCode(200);
            data.setMsg("没有添加产品");
            return data;
        }

        soOrderService.insertOrders(soOrder,loginUser.getUcUser().getPkid());
        data.setCode(200);
        data.setData("操作成功");

        return data;
    }

    public boolean checkMustServiceItem(List<SoOrderProd> list) {
        for(SoOrderProd soOrderProd : list) {
            HashMap<String, Object> propertiesMap1 = new HashMap<>();
            propertiesMap1.put("product_id", soOrderProd.getPkid());
            //查询出当前产品下所有服务项
            List<ProdService>  prodServiceList= prodServiceService.pageByProperties(propertiesMap1, 0).getList();
            //创建必选项服务项集合
            ArrayList<ProdPrice> mustServiceItemList = new ArrayList<>();
            for(ProdService prodService : prodServiceList) {
                HashMap<String, Object>  propertiesMap2 = new HashMap<>();
                propertiesMap2.put("service_id", prodService.getPkid());
                propertiesMap2.put("city_id", soOrderProd.getCityId());
                propertiesMap2.put("is_must", 1);
                //查询出当必选服务项且价格且审核状态通过
                List<ProdPrice> prodPriceList = prodPriceService.pageByProperties(propertiesMap2, 0).getList();
                for(ProdPrice prodPrice : prodPriceList) {
                    ProdPriceAudit prodPriceAudit = prodPriceAuditService.findById(prodPrice.getPriceAuditId());
                    if(prodPriceAudit.getAuditStatusId() == 1052) {
                        mustServiceItemList.add(prodPrice);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(mustServiceItemList)) {
                //有非必选项,遍历价格id开始检查,必选项必须全部选择才为true
                List<SoOrderProdItem> soOrderProdItemList = soOrderProd.getItemList();
                //订单中的价格Id集合(包含了必选项及非必项)
                ArrayList<Integer> soOrderProdItemPriceIdList = new ArrayList<>();
                for(SoOrderProdItem soOrderProdItem : soOrderProdItemList) {
                    soOrderProdItemPriceIdList.add(soOrderProdItem.getPriceId());
                }
                //必选项目中价格Id集合
                ArrayList<Integer> mustServiceItemPriceIdList = new ArrayList<>();
                for(ProdPrice prodPrice : mustServiceItemList) {
                    mustServiceItemPriceIdList.add(prodPrice.getPkid());
                }
                if(!soOrderProdItemPriceIdList.containsAll(mustServiceItemPriceIdList)) {
                    return false;
                }
            }
        }
        return  true;
    }

    @RequestMapping("/prodPrice/list")
    public ResponseData getProdPriceInfo(HttpServletRequest request, HttpServletResponse response){
        ResponseData data = new ResponseData();
        String productIdStr = request.getParameter("productIdStr");
        String cityIdStr = request.getParameter("cityIdStr");
        Integer productId = Integer.valueOf(SecurityUtils.rc4Decrypt(productIdStr));
        Integer cityId = Integer.valueOf(SecurityUtils.rc4Decrypt(cityIdStr));
        List<ProdPrice> prodPriceList = prodPriceService.findByCityIdAndProductId(cityId, productId);
        data.setData(prodPriceList);

        return data;
    }

}