package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.sopay.entity.PayAudit;
import com.gongsibao.module.order.sopay.service.SoPayService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lianghongpeng on 2016/4/21.
 */
@RestController
@RequestMapping("/api/myOrder")
public class MyOrderController {
    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private SoPayService soPayService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        map.put("userId", loginUser.getUcUser().getPkid());
        Pager<OrderList> pager = soOrderService.pageOrderListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 我的订单明细
     *
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/orderProd/list", method = RequestMethod.GET)
    public ResponseData getOrderProductList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        map.put("userId", loginUser.getUcUser().getPkid());
        Pager<OrderProdList> pager = soOrderProdService.pageOrderProdListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    //装载参数
    private void setupParameters(Map<String, Object> map, HttpServletRequest request) {
        String no = request.getParameter("no");
        if (!StringUtils.isBlank(no)) {
            map.put("no", no);
            return;
        }
        String orderNo = request.getParameter("orderNo");
        if (!StringUtils.isBlank(orderNo)) {
            map.put("orderNo", orderNo);
            return;
        }
        String orderProdNo = request.getParameter("orderProdNo");
        if (!StringUtils.isBlank(orderProdNo)) {
            map.put("orderProdNo", orderProdNo);
            return;
        }
        //产品名称
        addParameter(request, "productName", String.class, map);
        //城市id
        addParameter(request, "cityId", String.class, map);
        //是否退单
        addParameter(request, "isRefund", Integer.class, map);
        //订单状态
        addParameter(request, "state", Integer.class, map);
        //订单类型，1订单，2合同
        addParameter(request, "type", Integer.class, map);
        //是否分期付款， 1是，2否
        addParameter(request, "isInstallment", Integer.class, map);
        //是否开发票
        addParameter(request, "isInvoice", Integer.class, map);
        //下单人姓名
        addParameter(request, "accountName", String.class, map);
        //下单人电话
        addParameter(request, "accountMobile", String.class, map);
        //付款状态
        addParameter(request, "payStatusId", Integer.class, map);
        //付款审核状态
        addParameter(request, "payAuditStatusId", Integer.class, map);
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
        //下单方式
        addParameter(request, "sourceType", Integer.class, map);
        //订单操作状态
        addParameter(request, "processStatusId", Integer.class, map);
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


    @RequestMapping(value = "/list/pay", method = RequestMethod.GET)
    public ResponseData getPayAuditList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        map.put("userId", loginUser.getUcUser().getPkid());
        Pager<PayAudit> pager = soPayService.getPayAuditList(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/changePrice")
    public ResponseData getChangePriceByOrderId(HttpServletRequest request) {
        ResponseData data = new ResponseData();
        String pkIdStr = request.getParameter("orderPkidStr");
        Integer pkId = Integer.valueOf(SecurityUtils.rc4Decrypt(pkIdStr));
        SoOrder soOrder = soOrderService.findChangePriceById(pkId);
        data.setData(soOrder);

        return data;
    }
}
