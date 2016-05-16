package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soinvoice.entity.InvoiceList;
import com.gongsibao.module.order.soinvoice.entity.SoInvoice;
import com.gongsibao.module.order.soinvoice.entity.SoInvoiceAuditRequest;
import com.gongsibao.module.order.soinvoice.entity.SoInvoiceListRequest;
import com.gongsibao.module.order.soinvoice.service.SoInvoiceService;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderinvoicemap.entity.SoOrderInvoiceMap;
import com.gongsibao.module.order.soorderinvoicemap.service.SoOrderInvoiceMapService;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by lianghongpeng on 2016/4/21.
 */
@RestController
public class InvoiceController {

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoInvoiceService soInvoiceService;
    @Autowired
    private BdAuditLogService bdAuditLogService;

    /**
     * 解析 pkIdStr 2 pkId
     *
     * @param pkIdStr
     * @return
     */
    private Integer DecryptPkIdStr(String pkIdStr) {
        String str = SecurityUtils.rc4Decrypt(pkIdStr);
        Integer pkid = Integer.valueOf(str);
        return pkid;
    }

    /**
     * 获取 指定订单的指定发票
     */
    @RequestMapping(value = "/api/order/invoice/info", method = RequestMethod.GET)
    public ResponseData getInvoice(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Integer orderPkidStr = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        Integer invoicePkid = DecryptPkIdStr(request.getParameter("invoicePkidStr"));
        if (invoicePkid == 0 || orderPkidStr == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        SoOrder order = soOrderService.findById(orderPkidStr);
        if (order == null) {
            data.setCode(-1);
            data.setMsg("无效订单");
            return data;
        }
        SoInvoice invoice = soInvoiceService.findById(invoicePkid);
        data.setData(invoice);
        return data;
    }

    /**
     * 获取发票列表
     */
    @RequestMapping(value = "/api/order/invoice/list", method = RequestMethod.GET)
    public ResponseData getInvoiceList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        Pager<InvoiceList> pager = soInvoiceService.pageInvoiceListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 创建（申请）一张发票
     */
    @RequestMapping(value = "/api/order/invoice/create", method = RequestMethod.POST)
    public ResponseData postInvoice(@RequestBody SoInvoice soInvoice, HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        Integer orderPkid = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        if (orderPkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        SoOrder order = new SoOrder();
        order.setPkid(orderPkid);
        order.setIsInvoice(1);
        //发票类型 3081 普通发票、3082 增值税专用发票
        if (soInvoice.getTypeId() == 3081) {
            soInvoice.setVatTaxNo("");
            soInvoice.setVatAddress("");
            soInvoice.setVatPhone("");
            soInvoice.setVatBankName("");
            soInvoice.setVatBankNo("");
        }
        if (soInvoice.getTypeId() == 3082) {
            soInvoice.setReceiverName("");
            soInvoice.setReceiverMobilePhone("");
            soInvoice.setReceiverAddress("");
        }
        soInvoice.setAddTime(new Date());
        soInvoice.setAddUserId(loginUser.getUcUser().getPkid());
        soInvoice.setAuditStatusId(1051);
        soInvoice.setFileId(0);
        soInvoice.setRemark("");

        int flag = soOrderService.addInvoice(order, soInvoice, loginUser.getUcUser().getPkid());
        data.setCode(-1);
        if (flag == -1) {
            data.setMsg("订单不存在");
        } else if (flag == 0) {
            data.setMsg("发票申请失败");
        } else if (flag == -2) {
            data.setMsg("参数错误");
        } else if (flag == -3) {
            data.setMsg("金额超出限制");
        } else {
            List<SoInvoice> soInvoiceList = soInvoiceService.findByOrderId(orderPkid);
            if (null != soInvoiceList && soInvoiceList.size() > 0) {
                data.setData(soInvoiceList.get(0));
                data.setCode(200);
            }
        }
        return data;
    }

    /**
     * 审核指定的发票
     */
    @RequestMapping(value = "/api/order/invoice/audit", method = RequestMethod.POST)
    public ResponseData postInvoiceAudit(@RequestBody SoInvoiceAuditRequest invoiceAuditRequest, HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setCode(-1);
        Integer orderPkid = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        Integer invoicePkid = DecryptPkIdStr(request.getParameter("invoicePkidStr"));
        if (invoicePkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        try {
            int rs = soOrderService.editInvoice(orderPkid, invoicePkid, user.getUcUser().getPkid(), invoiceAuditRequest);
            if (rs == -1) {
                data.setMsg("订单不存在");
            } else if (rs == -2) {
                data.setMsg("审核任务不存在");
            } else {
                data.setCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.setMsg("审核失败");
        }
        return data;
    }

    /**
     * 获取 我的订单的发票列表
     */
    @RequestMapping(value = "/api/myOrder/invoice/list", method = RequestMethod.GET)
    public ResponseData getMyOrderInvoiceList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", loginUser.getUcUser().getPkid());
        setupParameters(map, request);
        Pager<InvoiceList> pager = soInvoiceService.pageInvoiceListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /***
     * 根据订单ID查询出全部发票信息
     */
    @RequestMapping(value = "/api/order/invoice/record", method = RequestMethod.GET)
    public ResponseData getOrderInvoicerecord(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Integer orderPkid = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        if (orderPkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderPkid);
        Pager<InvoiceList> pager = soInvoiceService.pageInvoiceListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 各审核状态数量
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/api/order/invoice/audit/nums", method = RequestMethod.GET)
    public ResponseData auditNums(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        Map<String, Integer> auditNums = soOrderService.getAuditNums(user.getUcUser().getPkid(), 1044);
        data.setData(auditNums);
        return data;
    }

    /**
     * 获取审核日志列表
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping(value = "/api/order/invoice/auditLogs", method = RequestMethod.GET)
    public ResponseData getAuditRecordList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "0";
        }
        int formId = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        if (formId == 0) {
            data.setMsg("参数错误");
            return data;
        }
        String statusId = StringUtils.trimToEmpty(request.getParameter("statusId"));
        String addUserId = StringUtils.trimToEmpty(request.getParameter("addUserId"));
        String level = StringUtils.trimToEmpty(request.getParameter("level"));
        Map<String, Object> properties = new HashMap<>();
        properties.put("form_id", formId);
        properties.put("type_id", 1044);
        if (!StringUtils.isBlank(statusId)) {
            properties.put("status_id", statusId);
        }
        if (!StringUtils.isBlank(addUserId)) {
            properties.put("add_user_id", addUserId);
        }
        if (!StringUtils.isBlank(level)) {
            properties.put("level", level);
        }
        Pager<BdAuditLog> pager = bdAuditLogService.pageByProperties(properties, NumberUtils.toInt(page), NumberUtils.toInt(pageSize));
        data.setData(pager);
        data.setMsg("操作成功");
        return data;
    }

    //装载参数
    private void setupParameters(Map<String, Object> map, HttpServletRequest request) {
        String no = request.getParameter("no");
        if (StringUtils.isBlank(no)) {
            //产品名称
            addParameter(request, "productName", String.class, map);
            //订单类型，1订单，2合同
            addParameter(request, "type", Integer.class, map);
            //发票类型 3081 普通发票、3082 增值税专用发票
            addParameter(request, "invoiceType", Integer.class, map);
            //发票公司 3071 汉唐信通（北京）咨询股份有限公司、3072 汉唐信通（北京）科技有限公司
            addParameter(request, "invoiceCompany", Integer.class, map);
            //下单人姓名
            addParameter(request, "accountName", String.class, map);
            //下单人电话
            addParameter(request, "accountMobile", String.class, map);
            //下单时间-开始
            String beginTime = request.getParameter("beginTime");
            if (!StringUtils.isBlank(beginTime)) {
                map.put("beginTime", beginTime + " 00:00:00");
            }
            //下单时间 结束
            String endTime = request.getParameter("endTime");
            if (!StringUtils.isBlank(endTime)) {
                map.put("endTime", endTime + " 23:59:59");
            }
            //审核状态 1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过
            addParameter(request, "auditStatus", Integer.class, map);
        } else {
            map.put("no", no);
        }
    }

    //对参数进行非空判断，然后添加至map中
    private void addParameter(HttpServletRequest request, String parameterName, Class c, Map<String, Object> map) {
        if (null != request.getParameter(parameterName)) {
            if (c == Integer.class) {
                map.put(parameterName, Integer.valueOf(request.getParameter(parameterName)));
            } else {
                map.put(parameterName, request.getParameter(parameterName));
            }
        }
    }
}
