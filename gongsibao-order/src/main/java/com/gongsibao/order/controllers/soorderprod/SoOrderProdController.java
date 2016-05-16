package com.gongsibao.order.controllers.soorderprod;


import com.gongsibao.common.util.DateUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ParamType;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdMonitorList;
import com.gongsibao.module.order.soorderprod.entity.OrderProdRow;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderprodtracefile.service.SoOrderProdTraceFileService;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/soorderprod")
public class SoOrderProdController {

    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private SoOrderProdTraceFileService soOrderProdTraceFileService;
    @Autowired
    private SoOrderProdUserMapService soOrderProdUserMapService;
    @Autowired
    private SoOrderService soOrderService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProd soOrderProd) {
        ResponseData data = new ResponseData();
        soOrderProdService.insert(soOrderProd);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProd soOrderProd) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProd.setPkid(pkid);
        soOrderProdService.update(soOrderProd);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping({"/list_demo"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Pager<SoOrderProd> pager = soOrderProdService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        SoOrderProd soOrderProd = soOrderProdService.findById(orderProdId);
        ResponseData data = new ResponseData();
        data.setData(soOrderProd);
        return data;
    }

    @RequestMapping("/getDetail")
    public ResponseData getDetailById(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        Map<String, Object> result = soOrderProdService.getDetailById(orderProdId);
        ResponseData data = new ResponseData();
        data.setData(result);
        return data;
    }

    @RequestMapping("/orderProdAudit")
    public ResponseData gotoOrderProdAudit(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        SoOrder result = soOrderProdService.getOrderProdAuditById(orderProdId);
        ResponseData data = new ResponseData();
        data.setData(result);
        return data;
    }

    @RequestMapping("/apply")
    public ResponseData apply(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        ResponseData data = new ResponseData();

        // 根据产品订单ID验证必须上传的材料是否都上传了
        String needUploadFileNames = soOrderProdTraceFileService.getNeedUploadFileNames(orderProdId);
        if (StringUtils.isNotBlank(needUploadFileNames)) {
            data.setCode(-2);
            data.setMsg("提交审核失败！您需要再次上传审核不通过的材料：" + needUploadFileNames + "。");
            return data;
        }

        SoOrderProd orderProd = soOrderProdService.findById(orderProdId);
        if (orderProd == null) {
            data.setCode(-1);
            data.setMsg("提交审核失败！");
            return data;
        }
        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        if (orderProd.getAuditStatusId().compareTo(1051) == 0 || orderProd.getAuditStatusId().compareTo(1053) == 0) {
            int result = soOrderProdService.updateAuditStatus(orderProdId, 1052, orderProd.getAuditStatusId());
            if (result <= 0) {
                data.setCode(-1);
                data.setMsg("提交审核失败！");
            }
        } else {
            data.setCode(-1);
            data.setMsg("提交审核失败！");
        }
        return data;
    }

    @RequestMapping("/audit")
    public ResponseData audit(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
//        Map<String, Object> mmpp = new HashMap<>();
//        mmpp.put("orderProdIdStr", "5");
//        mmpp.put("content", "身份证过期");
//        List<Map<String, Object>> lst1 = new ArrayList<>();
//        Map<String, Object> m1 = new HashMap<>();
//        m1.put("pkidStr","1");
//        m1.put("auditStatusId",1054);
//        lst1.add(m1);
//        Map<String, Object> m2 = new HashMap<>();
//        m2.put("pkidStr","2");
//        m2.put("auditStatusId",1053);
//        lst1.add(m2);
//        mmpp.put("traceFilesJson", lst1);
//        String orderProdAuditJson = JsonUtils.objectToJson(mmpp);

        // 获取参数JSON串
        String orderProdAuditJson = StringUtils.trimToEmpty(request.getParameter("orderProdAuditJson"));

        ResponseData data = new ResponseData();
        if (StringUtils.isBlank(orderProdAuditJson)) {
            data.setCode(-1);
            data.setMsg("参数为空");
            return data;
        }
        Map<String, Object> map = (Map<String, Object>) JsonUtils.jsonToObject(orderProdAuditJson, Map.class);
        if (CollectionUtils.isEmpty(map)) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<Map<String, Object>> lstTraceFile = (List<Map<String, Object>>) map.get("traceFilesJson");
        if (CollectionUtils.isEmpty(lstTraceFile)) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty((String) map.get("orderProdIdStr"));
        String content = StringUtils.trimToEmpty((String) map.get("content"));

        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        // 材料审核通过的ID集合
        List<Integer> passTraceFileIds = new ArrayList<>();
        // 材料审核不通过的ID集合
        List<Integer> failTraceFileIds = new ArrayList<>();
        String traceFileIdStr = null;
        int traceFileStatus = 0;
        for (Map<String, Object> item : lstTraceFile) {
            traceFileIdStr = StringUtils.trimToEmpty((String) item.get("pkidStr"));
            traceFileIdStr = SecurityUtils.rc4Decrypt(traceFileIdStr);
            traceFileStatus = NumberUtils.toInt(String.valueOf(item.get("auditStatusId")));

            /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
            if (traceFileStatus == 1054) {
                passTraceFileIds.add(NumberUtils.toInt(traceFileIdStr));
            } else if (traceFileStatus == 1053) {
                failTraceFileIds.add(NumberUtils.toInt(traceFileIdStr));
            }
        }

        if (!CollectionUtils.isEmpty(failTraceFileIds) && StringUtils.isBlank(content)) {
            data.setCode(-1);
            data.setMsg("审核不通过理由必填!");
            return data;
        }

        Integer userId = loginUser.getUcUser().getPkid();// 登录人ID
        int result = soOrderProdService.updateOrderProdAudit(orderProdId, passTraceFileIds, failTraceFileIds, content, userId);
        if (result <= 0) {
            data.setCode(-1);
            data.setMsg("审核失败！");
        }
        return data;
    }

    @RequestMapping({"/monitorList"})
    public ResponseData monitorList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 类型
        String type = StringUtils.trimToEmpty(request.getParameter("type"));
        // 组织名称
        String organizationName = StringUtils.trimToEmpty(request.getParameter("organizationName"));
        // 订单编号
        String orderNo = StringUtils.trimToEmpty(request.getParameter("orderNo"));
        // 产品名称
        String productName = StringUtils.trimToEmpty(request.getParameter("productName"));
        // 地区ID
        String cityIdStr = StringUtils.trimToEmpty(request.getParameter("cityIdStr"));
        cityIdStr = SecurityUtils.rc4Decrypt(cityIdStr);
        int cityId = NumberUtils.toInt(cityIdStr);
        // 业务员名称
        String realName = StringUtils.trimToEmpty(request.getParameter("realName"));
        // 下单时间
        String beginTime = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        String endTime = StringUtils.trimToEmpty(request.getParameter("endTime"));
        // 当前页
        String currentPage = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();
        condition.put("userId", loginUser.getUcUser().getPkid());// 当前登陆者
        if (NumberUtils.toInt(type) > 0) {
            condition.put("type", NumberUtils.toInt(type));
        }
        if (StringUtils.isNotBlank(organizationName)) {
            condition.put("organizationName", organizationName);
        }
        if (StringUtils.isNotBlank(orderNo)) {
            condition.put("orderNo", orderNo);
        }
        if (StringUtils.isNotBlank(productName)) {
            condition.put("productName", productName);
        }
        if (cityId > 0) {
            condition.put("cityId", cityId);
        }
        if (StringUtils.isNotBlank(realName)) {
            condition.put("realName", realName);
        }
        if (StringUtils.isNotBlank(beginTime)) {
            condition.put("beginTime", beginTime + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endTime)) {
            condition.put("endTime", endTime + " 23:59:59");
        }

        Pager<OrderProdMonitorList> pager = soOrderProdService.findOrderProdMonitorListByProperties(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        data.setData(pager);
        return data;
    }

    /**
     * 操作订单池列表（只有操作人员）
     * <p>
     * statusStr产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
     * 审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
     */
    @RequestMapping("/list")
    public ResponseData getOrderProdList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {

        //TODO 是否需要限制
        ResponseData data = new ResponseData();
        data.setCode(200);
        data.setMsg("操作成功");


        Map<String, Object> map = getQueryProdRowsParas(request);

        //查询当前人员的
        List<Integer> orgIds = ucUserService.getUserOrganizationIds(loginUser.getUcUser().getPkid());
        if (orgIds.size() > 0) {
            map.put("orgIds", orgIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        } else {
            map.put("orgIds", 0);
        }


        String currentPage = request.getParameter("currentPage");
        if (StringUtils.isBlank(currentPage)) {
            currentPage = "1";
        }

        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "10";
        }

        Pager<OrderProdRow> pager = soOrderProdService.pageOrderProdRowsByProperties(map, Integer.valueOf(currentPage), Integer.valueOf(pageSize));

        data.setData(pager);
        return data;
    }

    /**
     * @操作产品订单列表计数器 statusStr产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
     * 审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
     */
    @RequestMapping("count")
    public ResponseData getOrderProdCount(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();

        //查询当前人员的
        List<Integer> orgIds = ucUserService.getUserOrganizationIds(loginUser.getUcUser().getPkid());
        String orgIdsStr="0";
        if (orgIds.size() > 0) {
          orgIdsStr=orgIds.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        String statusStr = request.getParameter("statusStr");
        statusStr = StringUtils.trim(statusStr);
        if (StringUtils.isNotBlank(statusStr)
                && (statusStr.equalsIgnoreCase("00")
                || statusStr.equalsIgnoreCase("10")
                || statusStr.equalsIgnoreCase("24"))) {

            Integer count = soOrderProdService.getOrderProdCount(statusStr,orgIdsStr);

            data.setCode(200);
            data.setMsg("操作成功");
            data.setData(count);
            return data;
        }
        data.setCode(400);
        data.setMsg("请指定参数");

        return data;
    }

    /**
     * statusStr产品处理状态编码 0:未处理，1: 正在处理，2:正常结束, 8:异常结束
     * 审核状态码 0：未审核状态，1，审核中，2，审核通过，4，审核不通过
     */
    private Map<String, Object> getQueryProdRowsParas(HttpServletRequest request) {

        //公司名
        String companyNameStr = StringUtils.trimToEmpty(request.getParameter("companyName"));
        //业务人员名
        String userNameStr = StringUtils.trimToEmpty(request.getParameter("userName"));
        // 产品名称
        String prodName = StringUtils.trimToEmpty(request.getParameter("prodName"));
        // 订单编号
        String orderNo = StringUtils.trimToEmpty(request.getParameter("orderNo"));
        //产品操作状态
        //String processStatusIdStr = StringUtils.trimToEmpty(request.getParameter("processStatusIdStr"));
        // 订单审核状态
        //String auditStatusStr = StringUtils.trimToEmpty(request.getParameter("auditStatusIdStr"));

        //订单状态
        String statusStr = StringUtils.trimToEmpty(request.getParameter("statusStr"));

        // 地区ID
        String cityAreaIdStr = StringUtils.trimToEmpty(request.getParameter("cityIdStr"));
        //开始时间
        String beginTimeStr = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        //结束时间
        String endTimeStr = StringUtils.trimToEmpty(request.getParameter("endTime"));

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();

        //公司名称
        if (StringUtils.isNotBlank(companyNameStr)) {
            //TODO 目前不考虑公司名称过滤
            //condition.put("companyName", companyNameStr);
        }

        //业务人员名称
        if (StringUtils.isNoneBlank(userNameStr)) {
            condition.put("userName", "%" + userNameStr + "%");
        }
        //产品名称
        if (StringUtils.isNotBlank(prodName)) {
            condition.put("prodName", "%" + prodName + "%");
        }
        //产品编码
        if (StringUtils.isNotBlank(orderNo)) {
            condition.put("prodNo", "%" + orderNo+ "%");
        }

        //城市ID
        if (StringUtils.isNotBlank(cityAreaIdStr)) {
            //TODO 需要增加解密ID
            Integer cityAreaId = getIntegerIdFromEncryptStr(cityAreaIdStr);
            condition.put("cityId", cityAreaId);
        }

       /* //产品处理状态
        if (StringUtils.isNotBlank(processStatusIdStr)) {

            Integer processStatusId = getIntegerIdFromEncryptStr(processStatusIdStr);
            condition.put("processStatusId", processStatusId);
        }

        //审核状态
        if (StringUtils.isNotBlank(auditStatusStr)) {

            //auditStatusStr = SecurityUtils.rc4Decrypt(auditStatusStr);
            //Integer auditStatusId = Integer.valueOf(auditStatusStr);
            Integer auditStatusId = getIntegerIdFromEncryptStr(auditStatusStr);
            condition.put("auditStatusId", auditStatusId);
        }*/

        //产品状态和审核状态
        if (StringUtils.isNotBlank(statusStr) && statusStr.length() == 2) {
            condition.put("processStatusId", statusStr.charAt(0));
            condition.put("auditStatusId", statusStr.charAt(1));
        }


        //开始时间
        if (StringUtils.isNotBlank(beginTimeStr)) {
            condition.put("beginTime", DateUtils.strToDateTime(beginTimeStr));
        }
        //结束时间
        if (StringUtils.isNotBlank(endTimeStr)) {
            condition.put("endTime", DateUtils.strToDateTime(endTimeStr));
        }

        return condition;
    }

    private Integer getIntegerIdFromEncryptStr(String encryKeyStr) {
        String pkidStr = encryKeyStr;
        if (StringUtils.isNotBlank(pkidStr)) {
            //TODO 正式环境需要开启解密
            pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        }
        return NumberUtils.toInt(pkidStr, -1);
    }

    @RequestMapping("/orderauditlist")
    public ResponseData orderauditlist(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        HashMap<String, Object> map = new HashMap<>();
        //页码
        Integer currentPage = 1;
        if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
            currentPage = Integer.valueOf(request.getParameter("currentPage"));
        }
        //分页大小
        Integer pagerSize = 10;
        if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
            pagerSize = Integer.valueOf(request.getParameter("pageSize"));
        }
        //审核状态
        String auditStatusId = request.getParameter("auditStatusId");
        if (StringUtils.isNotBlank(auditStatusId)) {
            map.put("auditStatusId", auditStatusId);
        }
        //订单号
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotBlank(orderId)) {
            map.put("orderId", orderId);
        }
        //产品名称
        String productName = request.getParameter("productName");
        if (StringUtils.isNotBlank(productName)) {
            map.put("productName", productName);
        }
        //业务员
        String realName = request.getParameter("userName");
        if (StringUtils.isNotBlank(realName)) {
            map.put("userName", realName);
        }
        //组织
        String orgnizationName = request.getParameter("orgnizationName");
        if (StringUtils.isNotBlank(orgnizationName)) {
            map.put("orgnizationName", orgnizationName);
        }
        List<Integer> orgIds = ucUserService.getUserOrganizationIds(loginUser.getUcUser().getPkid());
        if (!CollectionUtils.isEmpty(orgIds)) {
            map.put("orgIds", orgIds);
        }
        Pager<OrderProdRow> pager = soOrderProdService.findOrderAuditListByProperties(map, currentPage, pagerSize);
        data.setData(pager);
        return data;
    }

    @RequestMapping("myorderlist")
    public ResponseData getMyOrderProdList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
//        ResponseData data = new ResponseData();
//        String contact  = request.getParameter("contact");
//        String phone = request.getParameter("phone");
//        String no = request.getParameter("no");
//        String productName = request.getParameter("productName");
//        String cityId = request.getParameter("cityId");
//        String startTime = request.getParameter("startTime");
//        String endTime = request.getParameter("endTime");
//        String audit = request.getParameter("audit");
//        String page = request.getParameter("currentPage");
//        String pagesize = request.getParameter("pageSize");
//        if (StringUtils.isBlank(page)) {
//            page = "1";
//        }
//        if (StringUtils.isBlank(pagesize)) {
//            pagesize = "10";
//        }
//
//        List<ParamType> orderParamTypeList = new ArrayList();
//
//        /*订单soOrder表条件开始*/
//        if (StringUtils.isNotEmpty(contact)) {
//            ParamType item = new ParamType();
//            item.setDbKey("account_name");
//            item.setDbValue(contact);
//            item.setSelectType("like");
//            orderParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(phone)) {
//            ParamType item = new ParamType();
//            item.setDbKey("account_mobile");
//            item.setDbValue(phone);
//            item.setSelectType("like");
//            orderParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(no)) {
//            ParamType item = new ParamType();
//            item.setDbKey("no");
//            item.setDbValue(no);
//            item.setSelectType("like");
//            orderParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(startTime)) {
//            ParamType item = new ParamType();
//            item.setDbKey("add_time");
//            item.setDbValue(startTime);
//            item.setSelectType("<=");
//            orderParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(endTime)) {
//            ParamType item = new ParamType();
//            item.setDbKey("add_time");
//            item.setDbValue(endTime);
//            item.setSelectType(">=");
//            orderParamTypeList.add(item);
//        }
//        List<Integer> soOrderIds = new ArrayList();
//        if(orderParamTypeList.size() > 0)
//        {
//            Pager<SoOrder> soOrderPager = soOrderService.findBySoOrders(orderParamTypeList, 0, 999999999);
//            if(soOrderPager.getList().size() > 0)
//            {
//                for (SoOrder item : soOrderPager.getList())
//                {
//                    soOrderIds.add(item.getPkid());
//                }
//            }
//        }
//        /*订单soOrder表条件结束*/
//
//        List<ParamType> orderProdParamTypeList = new ArrayList();
//        /*订单soOrderProd表条件开始*/
//        if (StringUtils.isNotEmpty(productName)) {
//            ParamType item = new ParamType();
//            item.setDbKey("product_name");
//            item.setDbValue(productName);
//            item.setSelectType("like");
//            orderProdParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(audit)) {
//            ParamType item = new ParamType();
//            item.setDbKey("audit_status_id");
//            item.setDbValue(audit);
//            item.setSelectType("=");
//            orderProdParamTypeList.add(item);
//        }
//        if (StringUtils.isNotEmpty(cityId)) {
//            ParamType item = new ParamType();
//            item.setDbKey("city_id");
//            item.setDbValue(SecurityUtils.rc4Decrypt(cityId));
//            item.setSelectType("=");
//            orderProdParamTypeList.add(item);
//        }
//        if(soOrderIds.size() > 0)
//        {
//            ParamType item = new ParamType();
//            item.setDbKey("order_id");
//            item.setDbValue(StringUtils.join(soOrderIds, ","));
//            item.setSelectType("in");
//            orderProdParamTypeList.add(item);
//        }
//        /*订单soOrderProd表条件结束*/
//
//        Integer userid = loginUser.getUcUser().getPkid();
//        Map<String, Object> myOrderMap = new HashMap();
//        myOrderMap.put("user_id", userid);
//        myOrderMap.put("type_id", 3063);
//        myOrderMap.put("status_id", 3141);
//        List<SoOrderProdUserMap> soOrderProdUserMapList = soOrderProdUserMapService.findByProperties(myOrderMap, 0, 1000);
//
//        List<Integer> soOrderProdIds = new ArrayList();
//        if(soOrderProdUserMapList.size() > 0)
//        {
//            for(SoOrderProdUserMap item : soOrderProdUserMapList)
//            {
//                soOrderProdIds.add(item.getOrderProdId());
//            }
//        }
//        Pager<SoOrderProd> soOrderProdPager = new Pager<SoOrderProd>(Integer.valueOf(page), Integer.valueOf(pagesize));
//        if(soOrderProdIds.size() > 0)
//        {
//            ParamType item = new ParamType();
//            item.setDbKey("pkid");
//            item.setDbValue(StringUtils.join(soOrderProdIds, ","));
//            item.setSelectType("in");
//            orderProdParamTypeList.add(item);
//            soOrderProdPager = soOrderProdService.findBySoOrderProds(orderProdParamTypeList, Integer.valueOf(page), Integer.valueOf(pagesize));
//        }
//
//        data.setCode(200);
//        data.setMsg("操作成功");
//        data.setData(soOrderProdPager);
//        return data;
        ResponseData data = new ResponseData();
        HashMap<String, Object> map = new HashMap<>();
        //页码
        Integer currentPage = 1;
        if (StringUtils.isNotBlank(request.getParameter("currentPage"))) {
            currentPage = Integer.valueOf(request.getParameter("currentPage"));
        }
        //分页大小
        Integer pagerSize = 10;
        if (StringUtils.isNotBlank(request.getParameter("pageSize"))) {
            pagerSize = Integer.valueOf(request.getParameter("pageSize"));
        }
        //审核状态
        String auditStatusId = request.getParameter("auditStatusId");
        if (StringUtils.isNotBlank(auditStatusId)) {
            map.put("auditStatusId", auditStatusId);
        }
        //业务人员对订单的状态  3141正在负责  3142曾经负责
        String statusId = request.getParameter("statusId");
        if (StringUtils.isNotBlank(statusId)) {
            map.put("statusId", statusId);
        }
        //公司名
        //联系人
        String accountName = request.getParameter("accountName");
        if (StringUtils.isNotBlank(accountName)) {
            map.put("accountName", accountName);
        }
        //联系电话
        String accountMobile = request.getParameter("accountMobile");
        if (StringUtils.isNotBlank(accountMobile)) {
            map.put("accountMobile", accountMobile);
        }
        //订单号
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotBlank(orderId)) {
            map.put("orderId", orderId);
        }
        //产品名称
        String productName = request.getParameter("productName");
        if (StringUtils.isNotBlank(productName)) {
            map.put("productName", productName);
        }
        //地区
        String cityId = request.getParameter("cityId");
        if (StringUtils.isNotBlank(cityId)) {
            map.put("cityId", cityId);
        }
        //开始时间
        String beginTime = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        if (StringUtils.isNotBlank(beginTime)) {
            map.put("beginTime", beginTime + " 00:00:00");
        }
        //结束时间
        String endTime = StringUtils.trimToEmpty(request.getParameter("endTime"));
        if (StringUtils.isNotBlank(endTime)) {
            map.put("endTime", endTime + " 23:59:59");
        }
        //权限orgId
        Integer userId = loginUser.getUcUser().getPkid();
        if (userId != null) {
            map.put("userId", userId);
        }
        Pager<OrderProdRow> pager = soOrderProdService.findMyOrderListByProperties(map, currentPage, pagerSize);
        data.setData(pager);
        return data;

    }
}