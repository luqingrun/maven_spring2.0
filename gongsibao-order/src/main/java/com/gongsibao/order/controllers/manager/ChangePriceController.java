package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.AuditNode;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.soorderprod.entity.OrderProdList;
import com.gongsibao.module.order.soorderproditem.entity.SoOrderProdItem;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by wp on 16-4-27.
 */
@RestController
@RequestMapping("/api/order")
public class ChangePriceController {

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    private ResponseData auditList(HttpServletRequest request, LoginUser loginUser, Integer... auditTypes){
        ResponseData data = new ResponseData();
        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 0);

        String productName = StringUtils.trimToEmpty(request.getParameter("productName"));
        String no = StringUtils.trimToEmpty(request.getParameter("no"));
        String accountName = StringUtils.trimToEmpty(request.getParameter("accountName"));
        String accountMobile = StringUtils.trimToEmpty(request.getParameter("accountMobile"));
        String beginTime = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        String endTime = StringUtils.trimToEmpty(request.getParameter("endTime"));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("typeId", 1042);
        paramMap.put("auditUserId",loginUser.getUcUser().getPkid());

        if(ArrayUtils.isNotEmpty(auditTypes)){
            paramMap.put("statusIds", Arrays.asList(auditTypes));
        }

        if(StringUtils.isNotBlank(productName)){
            paramMap.put("productName", productName);
        }

        if(StringUtils.isNotBlank(no)){
            paramMap.put("no",no);
        }

        if(StringUtils.isNotBlank(accountName)){
            paramMap.put("accountName", accountName);
        }

        if(StringUtils.isNotBlank(accountMobile)){
            paramMap.put("accountMobile",accountMobile);
        }

        if(StringUtils.isNotBlank(beginTime)){
            paramMap.put("beginTime", beginTime + " 00:00:00");
        }

        if(StringUtils.isNotBlank(endTime)){
            paramMap.put("endTime", endTime + " 23:59:59");
        }

        Pager<OrderList> pager = soOrderService.listAuditChangePrice(paramMap,currentPage,pageSize);
        data.setData(pager);

        return data;
    }

    /**
     * 待审核
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changPrice/toaudit/list")
    public ResponseData toauditList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.TO_AUDIT);
    }

    /**
     * 审核通过
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changPrice/pass/list")
    public ResponseData passList(HttpServletRequest request, LoginUser user) {

        return auditList(request, user, AuditStatusUtils.AUDIT_PASS);
    }

    /**
     * 审核驳回
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changPrice/reject/list")
    public ResponseData rejectList(HttpServletRequest request,  LoginUser user) {
        return auditList(request, user, AuditStatusUtils.AUDIT_REJECT);
    }

    /**
     * 全部审核
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changPrice/all/list")
    public ResponseData auditAllList(HttpServletRequest request,  LoginUser user) {
        return auditList(request, user,
                AuditStatusUtils.TO_AUDIT,
                AuditStatusUtils.AUDIT_PASS,
                AuditStatusUtils.AUDIT_REJECT);
    }

    /**
     * 各审核状态数量
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changPrice/audit/nums")
    public ResponseData auditNums(HttpServletRequest request,  LoginUser user) {
        ResponseData data = new ResponseData();
        Map<String, Integer> auditNums = soOrderService.getAuditNums(user.getUcUser().getPkid(), 1042);
        data.setData(auditNums);
        return data;
    }

    /**
     * 改价订单-浮层
     * @param request
     * @return
     */
    @RequestMapping("/changePrice/info")
    public ResponseData changePriceInfo(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("orderPkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        List<SoOrderProdItem> soOrderProdItems = soOrderService.listChangePriceInfo(pkid);
        data.setData(soOrderProdItems);

        return data;
    }

    /**
     * 改价订单-审核通过
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changePrice/audit/pass")
    public ResponseData auditPass(HttpServletRequest request, LoginUser user){
        ResponseData data = new ResponseData();

        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soOrderService.editChangePricePass(pkid, user.getUcUser().getPkid(), remark);
            if (rs == -1) {
                data.setMsg("订单不存在");

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
     * 改价订单-审核不通过
     * @param request
     * @param user
     * @return
     */
    @RequestMapping("/changePrice/audit/reject")
    public ResponseData auditReject(HttpServletRequest request, LoginUser user){
        ResponseData data = new ResponseData();
        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }

        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soOrderService.editChangePriceReject(pkid, user.getUcUser().getPkid(), remark);
            if (rs == -1) {
                data.setMsg("订单不存在");

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

    @RequestMapping("/changePrice/audit/process")
    public ResponseData process(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<List<AuditNode>> list = bdAuditLogService.getAuditProcess(pkid, 1042);
        data.setData(list);
        return data;
    }
}
