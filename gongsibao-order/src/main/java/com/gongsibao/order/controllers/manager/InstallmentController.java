package com.gongsibao.order.controllers.manager;

import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorder.entity.AuditNode;
import com.gongsibao.module.order.soorder.entity.OrderList;
import com.gongsibao.module.order.soorder.entity.SoOrder;
import com.gongsibao.module.order.soorder.service.SoOrderService;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.util.AuditStatusUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 分期
 * Created by wk on 2016/4/23.
 */
@RestController
@RequestMapping("/api")
public class InstallmentController {

    @Autowired
    private SoOrderService soOrderService;

    @Autowired
    private BdAuditLogService bdAuditLogService;

    @RequestMapping("/myOrder/installment/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 0);

        Map<String, Object> paramMap = getRequestParams(request);
        paramMap.put("isInstallment", 1);
        Pager<OrderList> pager = soOrderService.listInstallment(paramMap, currentPage, pageSize);
        data.setData(pager);

        return data;
    }

    @RequestMapping("/myOrder/installment/export")
    public ResponseData export(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        Map<String, Object> paramMap = getRequestParams(request);
        paramMap.put("isInstallment", 1);
        paramMap.put("currentUserId", user.getUcUser().getPkid());
        String filePath = soOrderService.exportInstallment(paramMap);
        FileUtils.downLoacl(request, response, filePath, "我的订单-分期订单列表.csv");
        FileUtils.removeLocal(new File(filePath));

        return data;
    }

    @RequestMapping("/myOrder/installment/apply")
    public ResponseData apply(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }

        String[] itemArr = StringUtils.trimToEmpty(request.getParameter("installmentItem")).split(",");

        List<Integer> moneys = new ArrayList<>();
        for (String item : itemArr) {
            int itemMoney = NumberUtils.toInt(item);
            if (itemMoney > 0) {
                moneys.add(itemMoney);
            }
        }

        if (CollectionUtils.isEmpty(moneys)) {
            data.setCode(-1);
            data.setMsg("请填写分期款项");
            return data;
        }

        if (moneys.size() == 1) {
            data.setCode(-1);
            data.setMsg("分期申请至少填写两期");
            return data;
        }

        SoOrder order = new SoOrder();
        order.setPkid(pkid);
        order.setIsInstallment(1);
        order.setInstallmentMode(StringUtils.join(moneys, "|"));
        order.setInstallmentAuditStatusId(AuditStatusUtils.TO_AUDIT);
        int flag = soOrderService.addInstallment(order, user.getUcUser().getPkid());
        data.setCode(-1);
        if (flag == -1) {
            data.setMsg("订单不存在");
        } else if (flag == -2) {
            data.setMsg("合同订单不允许分期");
        } else if (flag == 0) {
            data.setMsg("分期失败");
        } else {
            data.setCode(200);
        }
        return data;
    }

    @RequestMapping("/auditOrder/installment/process")
    public ResponseData process(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        List<List<AuditNode>> list = bdAuditLogService.getAuditProcess(pkid, 1047);
        data.setData(list);
        return data;
    }

    @RequestMapping("/auditOrder/installment/toaudit/list")
    public ResponseData toauditList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.TO_AUDIT);
    }

    @RequestMapping("/auditOrder/installment/pass/list")
    public ResponseData passList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.AUDIT_PASS);
    }

    @RequestMapping("/auditOrder/installment/reject/list")
    public ResponseData rejectList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user, AuditStatusUtils.AUDIT_REJECT);
    }

    @RequestMapping("/auditOrder/installment/all/list")
    public ResponseData auditAllList(HttpServletRequest request, LoginUser user) {
        return auditList(request, user,
                AuditStatusUtils.TO_AUDIT,
                AuditStatusUtils.AUDIT_PASS,
                AuditStatusUtils.AUDIT_REJECT);
    }

    @RequestMapping("/auditOrder/installment/toaudit/export")
    public ResponseData exportToauditList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        return exportAuditList(request, response, user, "审核订单-待审核列表", AuditStatusUtils.TO_AUDIT);
    }

    @RequestMapping("/auditOrder/installment/pass/export")
    public ResponseData exportPassList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        return exportAuditList(request, response, user, "审核订单-审核通过列表", AuditStatusUtils.AUDIT_PASS);
    }

    @RequestMapping("/auditOrder/installment/reject/export")
    public ResponseData exportRejectList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        return exportAuditList(request, response, user, "审核订单-审核驳回列表", AuditStatusUtils.AUDIT_REJECT);
    }

    @RequestMapping("/auditOrder/installment/all/export")
    public ResponseData exportAuditAllList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        return exportAuditList(request, response, user, "审核订单-全部列表",
                AuditStatusUtils.TO_AUDIT,
                AuditStatusUtils.AUDIT_PASS,
                AuditStatusUtils.AUDIT_REJECT);
    }


    @RequestMapping("/auditOrder/installment/audit/nums")
    public ResponseData auditNums(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        Map<String, Integer> auditNums = soOrderService.getAuditNums(user.getUcUser().getPkid(), 1047);
        data.setData(auditNums);
        return data;
    }

    /**
     * 查询分期审核列表
     *
     * @param request
     * @param user
     * @return
     */
    private ResponseData auditList(HttpServletRequest request, LoginUser user, Integer... auditTypes) {
        ResponseData data = new ResponseData();
        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"), 1);
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"), 0);

        Map<String, Object> paramMap = getRequestParams(request);
        paramMap.put("typeId", 1047);
        paramMap.put("auditUserId", user.getUcUser().getPkid());

        if (ArrayUtils.isNotEmpty(auditTypes)) {
            paramMap.put("statusIds", Arrays.asList(auditTypes));
        }

        Pager<OrderList> pager = soOrderService.listAuditInstallment(paramMap, currentPage, pageSize);

        data.setData(pager);
        return data;
    }

    /**
     * 导出分期审核列表
     *
     * @param request
     * @param user
     * @return
     */
    private ResponseData exportAuditList(HttpServletRequest request, HttpServletResponse response, LoginUser user, String fileName, Integer... auditTypes) {
        ResponseData data = new ResponseData();

        Map<String, Object> paramMap = getRequestParams(request);
        paramMap.put("typeId", 1047);
        paramMap.put("auditUserId", user.getUcUser().getPkid());

        if (ArrayUtils.isNotEmpty(auditTypes)) {
            paramMap.put("statusIds", Arrays.asList(auditTypes));
        }

        String filePath = soOrderService.exportAuditInstallment(paramMap);

        FileUtils.downLoacl(request, response, filePath, fileName + ".csv");
        FileUtils.removeLocal(new File(filePath));
        return data;
    }


    // 审核
    @RequestMapping("/auditOrder/installment/audit/pass")
    public ResponseData auditPass(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();

        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }
        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soOrderService.editInstallmentPass(pkid, user.getUcUser().getPkid(), remark);
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

    @RequestMapping("/auditOrder/installment/audit/reject")
    public ResponseData auditReject(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setCode(-1);
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orderPkidStr")));
        if (pkid == 0) {
            data.setMsg("参数错误");
            return data;
        }

        String remark = StringUtils.trimToEmpty(request.getParameter("remark"));
        try {
            int rs = soOrderService.editInstallmentReject(pkid, user.getUcUser().getPkid(), remark);
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
     * 获取列表参数
     *
     * @param request
     * @return
     */
    private Map<String, Object> getRequestParams(HttpServletRequest request) {
        String no = StringUtils.trimToEmpty(request.getParameter("no"));
        String productName = StringUtils.trimToEmpty(request.getParameter("productName"));
        String accountName = StringUtils.trimToEmpty(request.getParameter("accountName"));
        String accountMobile = StringUtils.trimToEmpty(request.getParameter("accountMobile"));
        String beginTime = StringUtils.trimToEmpty(request.getParameter("beginTime"));
        String endTime = StringUtils.trimToEmpty(request.getParameter("endTime"));
        int installmentAuditStatusId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("installmentAuditStatusId")), -1);

        Map<String, Object> paramMap = new HashMap<>();

        // 订单编号
        if (StringUtils.isNotBlank(no)) {
            paramMap.put("no", no);
        }

        if (StringUtils.isNotBlank(productName)) {
            paramMap.put("productName", productName);
        }

        if (StringUtils.isNotBlank(accountName)) {
            paramMap.put("accountName", accountName);
        }
        if (StringUtils.isNotBlank(accountMobile)) {
            paramMap.put("accountMobile", accountMobile);
        }
        if (StringUtils.isNotBlank(beginTime)) {
            paramMap.put("beginTime", beginTime + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endTime)) {
            paramMap.put("endTime", endTime + " 23:59:59");
        }

        if (installmentAuditStatusId > -1) {
            paramMap.put("installmentAuditStatusId", installmentAuditStatusId);
        }
        return paramMap;
    }
}
