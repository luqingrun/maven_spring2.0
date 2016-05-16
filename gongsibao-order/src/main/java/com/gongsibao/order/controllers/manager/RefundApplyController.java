package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.sorefund.service.SoRefundService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 退单申请模块
 * Created by jko on 2016/4/23.
 */
@RestController
@RequestMapping("/api/refundApply")
public class RefundApplyController {

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private SoRefundService soRefundService;

    @Autowired
    private SoOrderService soOrderService;

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
     * 获取退款产品列表（全部）
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/getProdList")
    public ResponseData getProdList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pkidStr"))));
        Integer isRefund = 0;
        List<SoOrderProd> orderProdList = soOrderProdService.getApplyRefundByOrderId(pkid, isRefund);
        data.setData(orderProdList);
        return data;

    }

    /**
     * 确认操作
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/addRefund")
    public ResponseData addRefund(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String json = StringUtils.trimToEmpty(request.getParameter("json"));
        if (StringUtils.isBlank(json)) {
            data.setCode(-1);
            data.setMsg("参数为空");
            return data;
        }
        Map<String, Object> map = (Map<String, Object>) JsonUtils.jsonToObject(json, Map.class);
        if (CollectionUtils.isEmpty(map)) {
            data.setCode(-1);
            data.setMsg("参数为空");
            return data;
        }
        int num = soRefundService.insertRefundApply(map, user.getUcUser().getAddUserId());
        data.setData(num);
        return data;
    }

}

