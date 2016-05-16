package com.gongsibao.order.controllers.manager;


import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.socontract.entity.ContractList;
import com.gongsibao.module.order.socontract.service.SoContractService;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lianghongpeng on 2016/4/21.
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private SoContractService soContractService;

    @Autowired
    private SoOrderProdService soOrderProdService;

    @Autowired
    private UcUserService ucUserService;

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

    /***
     * 全部订单列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseData list(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        Integer userId = loginUser.getUcUser().getPkid();
        List<Integer> ids = ucUserService.getUserPkid(userId);
        map.put("ids", ids);
        setupParameters(map, request);
        Pager<OrderList> pager = soOrderService.pageOrderListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /***
     * 导出excel
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/list/export"})
    public ResponseData exportOrderList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        Integer userId = loginUser.getUcUser().getPkid();
        List<Integer> ids = ucUserService.getUserPkid(userId);
        map.put("ids", ids);
        setupParameters(map, request);
        map.put("currentUserId", loginUser.getUcUser().getPkid());
        String filePath = soOrderService.exportOrderList(map, Integer.valueOf(page));
        FileUtils.downLoacl(request, response, filePath, "测试导出csv.csv");
        FileUtils.removeLocal(new File(filePath));
        return data;
    }


    /**
     * 获取我的 合同 列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/contract/list", method = RequestMethod.GET)
    public ResponseData getContractList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        map.put("userId", loginUser.getUcUser().getPkid());
        addParameter(request, "auditStatusId", Integer.class, map);
        Pager<ContractList> pager = soContractService.pageContractListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 获取我的改价 列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/priceChange/list", method = RequestMethod.GET)
    public ResponseData getPriceChangeList(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        setupParameters(map, request);
        map.put("userId", loginUser.getUcUser().getPkid());
        map.put("isChangePrice", 1);
        Pager<OrderList> pager = soOrderService.pageOrderListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    /**
     * 获取订单详细
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseData getInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Integer pkidStr = DecryptPkIdStr(request.getParameter("pkidStr"));
        if (pkidStr == 0) {
            data.setMsg("参数错误");
            return data;
        }
        OrderList orderList = soOrderService.findOrderListById(pkidStr);
        data.setData(orderList);
        return data;
    }

    /**
     * 获取订单详细
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/orderProduct/list", method = RequestMethod.GET)
    public ResponseData getOrderProductList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Map<String, Object> map = new HashMap<>();
        if (null != request.getParameter("orderPkidStr")) {
            Integer orderPkidStr = DecryptPkIdStr(request.getParameter("orderPkidStr"));
            map.put("orderId", orderPkidStr);
        }
        Pager<OrderProdList> pager = soOrderProdService.pageOrderProdListByProperties(map, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    //装载参数
    private void setupParameters(Map<String, Object> map, HttpServletRequest request) {
        String no = request.getParameter("no");
        if (StringUtils.isBlank(no)) {
            //产品名称
            addParameter(request, "productName", String.class, map);
            //订单状态
            addParameter(request, "state", Integer.class, map);
            //订单类型，1订单，2合同
            addParameter(request, "type", Integer.class, map);
            //是否分期付款， 1是，2否
            addParameter(request, "isInstallment", Integer.class, map);
            //业务员姓名
            addParameter(request, "realName", String.class, map);
            //是否开发票
            addParameter(request, "isInvoice", Integer.class, map);
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
            //下单方式
            addParameter(request, "sourceType", Integer.class, map);
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

    /**
     * 订单池-订单分配
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/myOrder/assignApply")
    public ResponseData assignApply(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String[] orderIdStrArr = request.getParameter("orderPkidStr").split(",");

        List<Integer> orderIds = new ArrayList<>();
        for (String orderIdStr : orderIdStrArr) {
            int orderId = NumberUtils.toInt(orderIdStr);
            if (orderId == 0) {
                continue;
            }
            orderIds.add(orderId);
        }

        int userId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userIdStr")));
        if (userId == 0) {
            throw new IllegalArgumentException("userId[" + request.getParameter("userIdStr") + "]");
        }

        soOrderService.editAssignApply(orderIds, userId, user.getUcUser().getPkid());
        return data;
    }

}
