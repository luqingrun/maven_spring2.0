package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lianghongpeng on 2016/4/21.
 */
@RestController
@RequestMapping("/api/orderProd/")
public class OrderProductController {

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private SoOrderService soOrderService;

    /**
     * 订单产品 开始操作
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/operation/begin", method = RequestMethod.POST)
    public ResponseData postOperationBegin(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String orderProdPkidStr = request.getParameter("orderProdPkidStr");
        String organizationPkidStr = request.getParameter("organizationPkidStr");
        Integer orderProdPkid = Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdPkidStr));
        Integer organizationPkid = Integer.valueOf(SecurityUtils.rc4Decrypt(organizationPkidStr));
        soOrderProdService.updateBeginOperation(orderProdPkid, organizationPkid);
        return data;
    }

    /**
     * 订单产品 退回操作
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "operation/return", method = RequestMethod.POST)
    public ResponseData postOperationReturn(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("orderProdPkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        int userId = loginUser.getUcUser().getPkid();
        soOrderProdService.updateReturnProduct(pkid, userId);
        return data;
    }

    /**
     * 获取订单池列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/pool/list", method = RequestMethod.GET)
    public ResponseData getPoolList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        Pager<OrderProdList> pager = soOrderProdService.pageOrderProdListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 订单池-订单分配
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/pool/assignApply")
    public ResponseData assignApply(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int orderId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (orderId == 0) {
            throw new IllegalArgumentException("orderId[" + request.getParameter("orderPkidStr") + "]");
        }
        int userId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userIdStr")));
        if (userId == 0) {
            throw new IllegalArgumentException("userId[" + request.getParameter("userIdStr") + "]");
        }

        soOrderService.editAssignApply(orderId, userId, user.getUcUser().getPkid());
        return data;
    }

    /**
     * 订单明细列表
     *
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/detail/list", method = RequestMethod.GET)
    public ResponseData getOrderProductList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Integer orderPkid = DecryptPkIdStr(request.getParameter("orderPkidStr"));
        if (orderPkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<OrderProdList> orderProdList = soOrderProdService.findOrderProdListByOrderId(orderPkid);
        data.setData(orderProdList);
        return data;
    }

    //装载参数
    private void setupParameters(Map<String, Object> map, HttpServletRequest request) {
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
        String productName = request.getParameter("productName");
        addParameter("productName", productName, map);
        //城市id
        String cityId = request.getParameter("cityId");
        addParameter("cityId", cityId, map);
        //订单状态
        String state = request.getParameter("state");
        addParameter("state", state, map);
        //业务员姓名
        String realName = request.getParameter("realName");
        addParameter("realName", realName, map);
        //下单人姓名
        String accountName = request.getParameter("accountName");
        addParameter("accountName", accountName, map);
        //下单人电话
        String accountMobile = request.getParameter("accountMobile");
        addParameter("accountMobile", accountMobile, map);
        //下单时间-开始
        String beginTime = request.getParameter("beginTime");
        if (!StringUtils.isBlank(beginTime)) {
            addParameter("beginTime", beginTime, map);
        }
        //下单时间 结束
        String endTime = request.getParameter("endTime");
        if (!StringUtils.isBlank(endTime)) {
            addParameter("endTime", endTime, map);
        }
    }

    //对参数进行非空判断，然后添加至map中
    private void addParameter(String parameterName, String parameter, Map<String, Object> map) {
        if (!StringUtils.isBlank(parameter)) {
            map.put(parameterName, parameter);
        }
    }

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
}
