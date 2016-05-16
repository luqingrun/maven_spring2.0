package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.order.sopay.entity.AuditFlow;
import com.gongsibao.module.order.sopay.entity.PayType;
import com.gongsibao.module.order.sopay.entity.SoPay;
import com.gongsibao.module.order.sopay.service.SoPayService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 付款
 * Created by wk on 2016/4/27.
 */
@RestController
@RequestMapping("/api/order")
public class PayController {

    @Autowired
    private SoPayService soPayService;

    @RequestMapping(value="/pay/list",method = RequestMethod.GET)
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        String orderPkIdStr = StringUtils.trimToEmpty(request.getParameter("orderPkIdStr"));
        int orderPkId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(orderPkIdStr));

        List<SoPay> soPays = soPayService.findByOrderId(orderPkId);
        data.setData(soPays);
        return data;
    }

    @RequestMapping(value="/pay",method = RequestMethod.POST)
    public ResponseData insertPay(HttpServletRequest request, HttpServletResponse response , LoginUser loginUser) {
        ResponseData data = new ResponseData();
        SoPay soPay = new SoPay();

        String payWayTypeId = StringUtils.trimToEmpty(request.getParameter("payWayTypeId"));
        if(StringUtils.isNotBlank(payWayTypeId)){
            soPay.setPayWayTypeId(NumberUtils.toInt(payWayTypeId));
        }
        String installmentTypeId = StringUtils.trimToEmpty(request.getParameter("installmentTypeId"));
        if(StringUtils.isNotBlank(installmentTypeId)){
            soPay.setOfflineInstallmentTypeId(NumberUtils.toInt(installmentTypeId));
        }
        String payerName = StringUtils.trimToEmpty(request.getParameter("payerName"));
        if(StringUtils.isNotBlank(payerName)){
            soPay.setOfflinePayerName(payerName);
        }
        String bankNo = StringUtils.trimToEmpty(request.getParameter("bankNo"));
        if(StringUtils.isNotBlank(bankNo)){
            soPay.setOfflineBankNo(bankNo);
        }
        String amount = StringUtils.trimToEmpty(request.getParameter("amount"));
        if(StringUtils.isNotBlank(amount)){
            soPay.setAmount(NumberUtils.toInt(amount));
        }
        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        if(StringUtils.isNotBlank(remark)){
            soPay.setOfflineRemark(remark);
        }

        String orderPkidStr = StringUtils.trimToEmpty(request.getParameter("orderPkidStr"));
        int orderPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(orderPkidStr));


        String vouchers = StringUtils.trimToEmpty(request.getParameter("vouchers"));

        String[] vouchersArray = vouchers.split("\\|");
        List<Integer> voucherList = new ArrayList<>();
        for (String voucher:vouchersArray) {
            voucherList.add(NumberUtils.toInt(SecurityUtils.rc4Decrypt(voucher)));
        }

        soPayService.insertPay(soPay,orderPkid,voucherList, loginUser.getUcUser().getPkid());
        return data;
    }

    @RequestMapping(value="pay/auditflow",method = RequestMethod.GET)
    public ResponseData auditflow(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        String payPkIdStr = StringUtils.trimToEmpty(request.getParameter("payPkidStr"));
        int payPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(payPkIdStr));
        List<AuditFlow> auditFlow = soPayService.getAuditFlow(payPkid);
        data.setData(auditFlow);
        return data;
    }

    @RequestMapping(value="pay/paytype",method = RequestMethod.GET)
    public ResponseData paytype(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        String orderPkidStr = StringUtils.trimToEmpty(request.getParameter("orderPkidStr"));
        int orderPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(orderPkidStr));

        List<PayType> payType = soPayService.getPayType(orderPkid);
        data.setData(payType);
        return data;
    }

    @RequestMapping(value="pay",method = RequestMethod.GET)
    public ResponseData findPayById(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        String payPkidStr = StringUtils.trimToEmpty(request.getParameter("payPkidStr"));
        int payPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(payPkidStr));

        SoPay soPay = soPayService.findById(payPkid);
        data.setData(soPay);
        return data;
    }

    @RequestMapping(value="pay/audit",method = RequestMethod.GET)
    public ResponseData audit(HttpServletRequest request, HttpServletResponse response,LoginUser loginUser) {
        ResponseData data = new ResponseData();

        String payPkidStr = StringUtils.trimToEmpty(request.getParameter("payPkidStr"));
        int payPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(payPkidStr));
        int auditStatusId = NumberUtils.toInt(request.getParameter("auditStatusId"));
        String remark = request.getParameter("remark");

        soPayService.updateAudit(payPkid,auditStatusId,remark,loginUser.getUcUser().getPkid());

        return data;
    }
}