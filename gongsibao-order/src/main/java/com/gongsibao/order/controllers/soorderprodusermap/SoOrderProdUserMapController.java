package com.gongsibao.order.controllers.soorderprodusermap;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.sys.sms.entity.SmsResponse;
import com.gongsibao.module.sys.sms.service.SmsService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/soorderprodusermap")
public class SoOrderProdUserMapController {

    @Autowired
    private SoOrderProdUserMapService soOrderProdUserMapService;
    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private UcUserService ucUserService;
//    @Autowired
//    private SmsService smsService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdUserMap soOrderProdUserMap) {
        ResponseData data = new ResponseData();
        soOrderProdUserMapService.insert(soOrderProdUserMap);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdUserMap soOrderProdUserMap) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdUserMap.setPkid(pkid);
        soOrderProdUserMapService.update(soOrderProdUserMap);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/status")
    public ResponseData updateStatus(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        // 订单项和用户关系ID加密字符串
        String pkidStr = StringUtils.trimToEmpty(request.getParameter("pkidStr"));

        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = NumberUtils.toInt(pkidStr, -1);

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        SoOrderProd orderProd = soOrderProdService.findById(orderProdId);
        if(orderProd == null) {
            data.setCode(-1);
            data.setMsg("产品订单不存在");
            return data;
        }
        /** 105 审核状态：1051 待审核、1052 审核中、1053 驳回审核、1054 审核通过 */
        if(orderProd.getAuditStatusId().compareTo(1054) != 0) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("status_id", 3141);
            condition.put("type_id", 3063);
            condition.put("order_prod_id", orderProdId);
            int count = soOrderProdUserMapService.countByProperties(condition);
            if(count <= 1) {
                data.setCode(-1);
                data.setMsg("产品订单至少需要一个人负责");
                return data;
            }
        }

        // 负责人列表(操作列我已完成操作) 正在负责改成曾经负责 3141正在负责、3142曾经负责
        int result = soOrderProdUserMapService.updateStatus(pkid, 3142, 3141);
        if (result <= 0) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);
        // 当前页
        String currentPage = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();
        condition.put("userId", loginUser.getUcUser().getPkid());// 登陆人ID
        condition.put("type_id", 3063);
        if (orderProdId > 0) {
            condition.put("order_prod_id", orderProdId);
        }

        Pager<SoOrderProdUserMap> pager = soOrderProdUserMapService.pageByProperties(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        if(pager != null) {
            data.setData(pager);
        }
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        SoOrderProdUserMap soOrderProdUserMap = soOrderProdUserMapService.findById(pkid);
        data.setData(soOrderProdUserMap);
        return data;
    }

    @RequestMapping("/sendMsg")
    public ResponseData sendMsg(HttpServletRequest request, HttpServletResponse response) {
        // 负责人ID加密串
        String userIdStr = StringUtils.trimToEmpty(request.getParameter("userIdStr"));
        // 是否发送短信
        String isSendSms = StringUtils.trimToEmpty(request.getParameter("isSendSms"));

        userIdStr = SecurityUtils.rc4Decrypt(userIdStr);
        Integer userId = NumberUtils.toInt(userIdStr);

        // 发送站内信 TODO

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        if(NumberUtils.toInt(isSendSms) == 1) {
            // 根据userId查询手机号 以及短信内容  TODO
            UcUser user = ucUserService.findById(userId);
            if(user == null) {
                data.setCode(-1);
                data.setMsg("用户为空");
            } else {
                // Integer appId, String mobilePhone, String content
                SmsResponse result = null;//smsService.send(1, user.getMobilePhone(), "");
                if(result == null || !result.getIsSuccessful()) {
                    data.setCode(-1);
                    data.setMsg("操作失败");
                }
            }
        }
        return data;
    }

    @RequestMapping({"/follow"})
    public ResponseData follow(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        if(orderProdId <= 0) {
            data.setCode(-1);
            data.setMsg("产品订单ID错误");
            return data;
        }

        SoOrderProdUserMap soOrderProdUserMap = new SoOrderProdUserMap();
        soOrderProdUserMap.setOrderProdId(orderProdId);
        soOrderProdUserMap.setUserId(loginUser.getUcUser().getPkid());
        // 关系类型序号，type=306，3061业务、3062客服（关注）、3063操作
        soOrderProdUserMap.setTypeId(3062);
        soOrderProdUserMap.setStatusId(0);
        soOrderProdUserMap.setAddTime(new Date());

        Integer id = soOrderProdUserMapService.insert(soOrderProdUserMap);
        if(id == null || id <= 0) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    @RequestMapping({"/cancelFollow"})
    public ResponseData cancelFollow(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        if(orderProdId <= 0) {
            data.setCode(-1);
            data.setMsg("产品订单ID错误");
            return data;
        }

        // 关系类型序号，type=306，3061业务、3062客服（关注）、3063操作
        int result = soOrderProdUserMapService.deleteInfo(loginUser.getUcUser().getPkid(), orderProdId, 3062);
        if(result <= 0) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    @RequestMapping({"/addResponsibility"})
    public ResponseData addResponsibility(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        // 获取所要添加的用户ID加密串集合
        String userIdStrs = StringUtils.trimToEmpty(request.getParameter("userIdStrs"));
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        if(orderProdId <= 0) {
            data.setCode(-1);
            data.setMsg("产品订单ID错误");
            return data;
        }
        if(StringUtils.isBlank(userIdStrs)) {
            data.setCode(-1);
            data.setMsg("用户ID为空");
            return data;
        }

        String[] idStrs = StringUtils.split(userIdStrs, ",");

        int userId = 0;
        String useSIdStr = null;
        List<SoOrderProdUserMap> list = new ArrayList<>();
        for(int i=0;i<idStrs.length;i++) {
            useSIdStr = SecurityUtils.rc4Decrypt(idStrs[i]);
            userId = NumberUtils.toInt(useSIdStr);
            if(userId <= 0) {
                continue;
            }
            SoOrderProdUserMap soOrderProdUserMap = new SoOrderProdUserMap();
            soOrderProdUserMap.setOrderProdId(orderProdId);
            soOrderProdUserMap.setUserId(userId);
            // 关系类型序号，type=306，3061业务、3062客服（关注）、3063操作
            soOrderProdUserMap.setTypeId(3063);
            // 订单项和用户关系状态，type=314，3141正在负责、3142曾经负责
            soOrderProdUserMap.setStatusId(3141);
            soOrderProdUserMap.setAddTime(new Date());
            list.add(soOrderProdUserMap);
        }
        soOrderProdUserMapService.insertBatch(list);
        return data;
    }

}