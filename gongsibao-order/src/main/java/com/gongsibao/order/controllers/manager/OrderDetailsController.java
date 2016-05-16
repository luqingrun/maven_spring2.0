package com.gongsibao.order.controllers.manager;

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
import org.apache.commons.lang.IncompleteArgumentException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 退单详情功能模块
 * Created by jko on 2016/4/22.
 */
@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailsController {

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
    public ResponseData getRefundList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int orderId = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("orderId")));
        List<SoRefund> detailsList = soRefundService.findDetailsListByIds(orderId);
        data.setData(detailsList);
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
        int pkid = com.gongsibao.common.util.NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));
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
        ResponseData responseData = new ResponseData();
        int orderId = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("pkidStr")));
        int isRefund=1;
        List<SoOrderProd> list = soOrderProdService.getApplyRefundByOrderId(orderId, isRefund);
        responseData.setData(list);
        return responseData;
    }

    /**
     * 获取审核流程  (待确认)
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getAuditFlow")
    public ResponseData getAuditFlow(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = com.gongsibao.common.util.NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pkidStr")));
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
     * 获取审核日志列表 (已完成)
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
        int formId = com.gongsibao.common.util.NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkIdStr"))));
        if (formId == 0) {
            throw new IllegalArgumentException("formId [" + request.getParameter("formId") + "]");
        }

        int typeId = com.gongsibao.common.util.NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("typeId"))));
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
        Pager<BdAuditLog> pager = bdAuditLogService.pageByProperties(properties, com.gongsibao.common.util.NumberUtils.toInt(page), com.gongsibao.common.util.NumberUtils.toInt(pageSize));
        data.setData(pager);
        data.setMsg("操作成功");
        return data;
    }


}
