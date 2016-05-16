package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
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
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退单审核模块
 * Created by jko on 2016/4/23.
 */
@RestController
@RequestMapping("/api/refundAudit")
public class RefundAuditController {

    @Autowired
    private SoRefundService soRefundService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @Autowired
    private SoOrderService soOrderService;


    /**
     * 审核列表公共方法
     * @param request
     * @param loginUser
     * @param auditTypes
     * @return ResponseData
     */
    private ResponseData auditList(HttpServletRequest request, LoginUser loginUser, Integer... auditTypes) {
        ResponseData data = new ResponseData();

        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 0);
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
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("typeId", 1046);
        paramMap.put("auditUserId", loginUser.getUcUser().getPkid());
        if (ArrayUtils.isNotEmpty(auditTypes)) {
            paramMap.put("statusIds", Arrays.asList(auditTypes));
        }
        if (StringUtils.isNotBlank(no)) {
            paramMap.put("no", no);
        }
        if (StringUtils.isNotBlank(prodName)) {
            paramMap.put("prodName", prodName);
        }
        if (StringUtils.isNotBlank(payStatusId)) {
            paramMap.put("payStatusId", payStatusId);
        }
        if (StringUtils.isNotBlank(processStatusId)) {
            paramMap.put("processStatusId", processStatusId);
        }
        if (StringUtils.isNotBlank(isInstallment)) {
            paramMap.put("isInstallment", isInstallment);
        }
        if (StringUtils.isNotBlank(isInvoice)) {
            paramMap.put("isInvoice", isInvoice);
        }
        if (StringUtils.isNotBlank(accountName)) {
            paramMap.put("accountName", accountName);
        }
        if (StringUtils.isNotBlank(accountMobile)) {
            paramMap.put("accountMobile", accountMobile);
        }
        if (StringUtils.isNotBlank(refundStatusId)) {
            paramMap.put("refundStatusId", refundStatusId);
        }
        if (StringUtils.isNotBlank(beginTime)) {
            paramMap.put("beginTime", beginTime + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endTime)) {
            paramMap.put("endTime", endTime + " 23:59:59");
        }
        Pager<SoRefund> pager = soRefundService.pageRefundAuditByProperties(paramMap, currentPage, pageSize);
        data.setData(pager);
        return data;
    }


    /**
     * 待审核
     * @param request
     * @param user
     * @return ResponseData
     */
    @RequestMapping("/getToAuditList")
    public ResponseData getToAuditList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.TO_AUDIT);
    }

    /**
     * 审核通过
     * @param request
     * @param user
     * @return ResponseData
     */
    @RequestMapping("/getPassList")
    public ResponseData getPassList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.TO_AUDIT);
    }

    /**
     * 审核不通过
     * @param request
     * @param user
     * @return ResponseData
     */
    @RequestMapping("/getRejectList")
    public ResponseData getRejectList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.AUDIT_REJECT);
    }

    /**
     * 全部审核
     * @param request
     * @param user
     * @return ResponseData
     */
    @RequestMapping("/getAuditAllList")
    public ResponseData getAuditAllList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user,
                AuditStatusUtils.TO_AUDIT,
                AuditStatusUtils.AUDIT_PASS,
                AuditStatusUtils.AUDIT_REJECT);
    }

    /**
     * 获取审核状态
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getRefundStatus")
    public ResponseData getRefundStatus(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        Map<String, Integer> auditNums = soOrderService.getAuditNums(user.getUcUser().getPkid(), 1046);
        data.setData(auditNums);
        return data;
    }

    /**
     * 获取订单基本信息
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
        //通过传入ID查询记录
        int pkId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));
        /*SoRefund soRefund = soRefundService.findById(1);*/ //测试用例
        SoRefund soRefund = soRefundService.findById(pkId);
        data.setData(soRefund);
        return data;
    }

    /**
     * 获取退单产品信息列表 （so_order_prod 中将is_refund设置为退单）
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getRefundProdList")
    public ResponseData getRefundProdList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int orderId = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("pkidStr")));
        //测试用例 orderId=2  isRefund=1
        List<SoOrderProd> orderProdList = soOrderProdService.getApplyRefundByOrderId(2, 1);
        data.setData(orderProdList);
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
    /* Map<String, Object> properties = new HashMap<>();
        properties.put("form_id", 2);
        properties.put("type_id", 104);*/
        Pager<BdAuditLog> pager = bdAuditLogService.pageByProperties(properties, NumberUtils.toInt(page), NumberUtils.toInt(pageSize));
        data.setData(pager);
        data.setMsg("操作成功");
        return data;
    }


    /**
     * 获取审核流程
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/process")
    public ResponseData getAuditProcess(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pkidStr")));
        if (pkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<List<AuditNode>> list = bdAuditLogService.getAuditProcess(pkid, 1046);
        data.setData(list);
        return data;
    }

    /**
     * 退单审核通过 1054
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/refundAudit/pass")
    public ResponseData refundAuditPass(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String json = StringUtils.trimToEmpty(request.getParameter("json"));
        Map<String, Object> map = (Map<String, Object>) JsonUtils.jsonToObject(json, Map.class);
        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soRefundService.editRefundPass(pkid, user.getUcUser().getPkid(), remark,map);
            if (rs == -1) {
                data.setMsg("退款单不存在");

            } else if (rs == -2) {
                data.setMsg("审核任务不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.setMsg("审核失败");
        }

        data.setCode(200);
        return data;
    }

    /**
     * 退单审核不通过  1053
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/refundAudit/reject")
    public ResponseData refundAuditNoPass(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        String json = StringUtils.trimToEmpty(request.getParameter("json"));
        Map<String, Object> map = (Map<String, Object>) JsonUtils.jsonToObject(json, Map.class);
        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soRefundService.editRefundReject(pkid, user.getUcUser().getPkid(), remark,map);
            if (rs == -1) {
                data.setMsg("退款单不存在");

            } else if (rs == -2) {
                data.setMsg("审核任务不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.setMsg("审核失败");
        }
        data.setCode(200);
        return data;
    }

}
