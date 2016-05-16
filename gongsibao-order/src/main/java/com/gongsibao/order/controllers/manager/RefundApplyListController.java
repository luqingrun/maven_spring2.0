package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.AuditNode;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.sorefund.entity.SoRefund;
import com.gongsibao.module.order.sorefund.service.SoRefundService;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.lang.IncompleteArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退单列表查看模块
 * Created by jko on 2016/4/23.
 *
 */
@RestController
@RequestMapping("/api/refundApplyList")
public class RefundApplyListController {

    @Autowired
    private SoRefundService soRefundService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Autowired
    private SoOrderService soOrderService;

    /**
     * 获取退单记录列表
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getRefundList")
    public ResponseData getRefundList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
   /*     String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "0";
        }
        //参数pkIdStr是订单表ID
        int pkId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkIdStr"))));
        if (pkId == 0) {
            throw new IllegalArgumentException("pkId [" + request.getParameter("pkIdStr") + "]");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", pkId);*/
        //测试用例
/*        Map<String, Object> properties = new HashMap<>();
        properties.put("order_id", 5);*/
/*        Pager<SoOrder> pager = soRefundService.getRefundViewList(map, NumberUtils.toInt(page), NumberUtils.toInt(pageSize));*/

        Map<String, Object> map = new HashMap<>();

        String no = StringUtils.trimToEmpty(request.getParameter("no"));
        String prodName = StringUtils.trimToEmpty(request.getParameter("prodName"));
        String payStatusId = StringUtils.trimToEmpty(request.getParameter("payStatusId"));
        String processStatusId = StringUtils.trimToEmpty(request.getParameter("processStatusId"));
        String isInstallment = StringUtils.trimToEmpty(request.getParameter("isInstallment"));
        String isInvoice = StringUtils.trimToEmpty(request.getParameter("isInvoice"));
        String accountName = StringUtils.trimToEmpty(request.getParameter("accountName"));
        String accountMobile = StringUtils.trimToEmpty(request.getParameter("accountMobile"));
        String refundStatusId = StringUtils.trimToEmpty(request.getParameter("refundStatusId"));
        String beginTime = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        String endTime = StringUtils.trimToEmpty(request.getParameter("endTime"));
        if (!StringUtils.isBlank(no)) {
            map.put("so.no", no);
        }
        if (!StringUtils.isBlank(prodName)) {
            map.put("so.prod_name", prodName);
        }
        if (!StringUtils.isBlank(payStatusId)) {
            map.put("so.pay_status_id", payStatusId);
        }
        if (!StringUtils.isBlank(processStatusId)) {
            map.put("so.process_status_id", processStatusId);
        }
        if (!StringUtils.isBlank(isInstallment)) {
            map.put("so.is_installment", isInstallment);
        }
        if (!StringUtils.isBlank(isInvoice)) {
            map.put("so.is_invoice", isInvoice);
        }
        if (!StringUtils.isBlank(accountName)) {
            map.put("so.account_name", accountName);
        }
        if (!StringUtils.isBlank(refundStatusId)) {
            map.put("so.refund_status_id", refundStatusId);
        }
        if (!StringUtils.isBlank(accountMobile)) {
            map.put("so.account_mobile", accountMobile);
        }
        if (!StringUtils.isBlank(beginTime)) {
            map.put("so.begin_time", beginTime);
        }
        if (!StringUtils.isBlank(endTime)) {
            map.put("so.end_time", endTime);
        }
        //type_id 审核类型: 1046退单申请审核
        //status_id 审核状态: 1051 待审核
        //add_user_id  添加人序号
        Integer statusId = 1051;
        List<SoOrder> soOrderList = soRefundService.getRefundViewList(1046, 1051, 2, map); //测试用例
     /*   List<SoOrder> soOrderList = soRefundService.getRefundViewList(BdDict.TYPE_104_REFUND, statusId, user.getUcUser().getAddUserId(),map);*/
        data.setData(soOrderList);
        data.setMsg("操作成功");
        return data;
    }

    /**
     * 获取订单基本信息
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getOrderInfo")
    public ResponseData getOrderInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));
        SoOrder soOrder = soOrderService.findById(pkid);
        data.setData(soOrder);
        return data;
    }

    /**
     * 获取退单信息
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getRefundInfo")
    public ResponseData getRefund(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));
        SoRefund soRefund = soRefundService.findById(pkid);
        data.setData(soRefund);
        return data;
    }

    /**
     * 获取退单产品信息列表
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getRefundProdList")
    public ResponseData getRefundProdList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));//pkidStr是订单id的加密字符串
        int isRefund = 1;
        List<SoOrderProd> orderProdList = soOrderProdService.getApplyRefundByOrderId(pkid, isRefund);
        data.setData(orderProdList);
        return data;
    }

    /**
     * 获取审核流程
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getAuditFlow")
    public ResponseData getAuditFlow(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pkidStr")));//退单id 【so_refund】
        if (pkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<List<AuditNode>> list = bdAuditLogService.getAuditProcess(pkid, 1046);
        /*List<List<AuditNode>> list = bdAuditLogService.getAuditProcess(1, 1046);*///测试用例
        data.setData(list);
        return data;
    }

    /**
     * 获取审核日志列表
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getAuditRecordList")
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
        int formId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkIdStr"))));
        if (formId == 0) {
            throw new IllegalArgumentException("formId [" + request.getParameter("formId") + "]");
        }

        int typeId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("typeId"))));
        if (typeId == 0) {
            throw new IncompleteArgumentException("typeId [" + request.getParameter("typeId") + "]");
        }
        String statusId = StringUtils.trimToEmpty(request.getParameter("statusId"));
        String addUserId = StringUtils.trimToEmpty(request.getParameter("addUserId"));
        String level = StringUtils.trimToEmpty(request.getParameter("level"));
        Map<String, Object> properties = new HashMap<>();
        properties.put("form_id", 2);
        properties.put("type_id", 104);

        if (!StringUtils.isBlank(statusId)) {
            properties.put("status_id", statusId);
        }
        if (!StringUtils.isBlank(addUserId)) {
            properties.put("add_user_id", addUserId);
        }
        if (!StringUtils.isBlank(level)) {
            properties.put("level", level);
        }
        //测试用例
/*     Map<String, Object> properties = new HashMap<>();
        properties.put("form_id", 2);
        properties.put("type_id", 104);*/
        Pager<BdAuditLog> pager = bdAuditLogService.pageByProperties(properties, NumberUtils.toInt(page), NumberUtils.toInt(pageSize));
        data.setData(pager);
        data.setMsg("操作成功");
        return data;
    }


}
