package com.gongsibao.order.controllers.soorderprodtrace;


import com.gongsibao.common.util.PropertiesReader;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorderprod.entity.SoOrderProd;
import com.gongsibao.module.order.soorderprod.service.SoOrderProdService;
import com.gongsibao.module.order.soorderprodaccount.entity.RequestOrderProd;
import com.gongsibao.module.order.soorderprodaccount.entity.RequestOrderProdAccount;
import com.gongsibao.module.order.soorderprodaccount.entity.SoOrderProdAccount;
import com.gongsibao.module.order.soorderprodaccount.service.SoOrderProdAccountService;
import com.gongsibao.module.order.soorderprodtrace.entity.SoOrderProdTrace;
import com.gongsibao.module.order.soorderprodtrace.service.SoOrderProdTraceService;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.order.soorderprodtracefile.service.SoOrderProdTraceFileService;
import com.gongsibao.module.order.soorderprodusermap.entity.SoOrderProdUserMap;
import com.gongsibao.module.order.soorderprodusermap.service.SoOrderProdUserMapService;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflowfile.service.ProdWorkflowFileService;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.module.uc.ucuser.entity.UcUser;
import com.gongsibao.module.uc.ucuser.service.UcUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/soorderprodtrace")
public class SoOrderProdTraceController {

    @Autowired
    private SoOrderProdTraceService soOrderProdTraceService;
    @Autowired
    private SoOrderProdService soOrderProdService;
    @Autowired
    private SoOrderProdUserMapService soOrderProdUserMapService;
    @Autowired
    private UcUserService ucUserService;
    @Autowired
    private SoOrderProdAccountService soOrderProdAccountService;
    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;
    @Autowired
    private ProdWorkflowFileService prodWorkflowFileService;
    @Autowired
    private SoOrderProdTraceFileService soOrderProdTraceFileService;

    @RequestMapping("/updatestatus")
    public ResponseData updatestatus(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String processStatusId = request.getParameter("processStatusId");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(processStatusId == null || processStatusId.equals(""))
        {
            data.setMsg("订单产品状态id不能为空");
            data.setCode(402);
            return data;
        }
        Integer isSendSms = 0;
        if(request.getParameter("isSendSms") != null)
        {
            isSendSms = Integer.valueOf(request.getParameter("isSendSms"));
        }
        SoOrderProd soOrderProd = new SoOrderProd();
        soOrderProd.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));
        soOrderProd = soOrderProdService.findById(soOrderProd.getPkid());
        soOrderProd.setProcessStatusId(Integer.valueOf(SecurityUtils.rc4Decrypt(processStatusId)));

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3151);
        soOrderProdTrace.setInfo("我是更新状态");
        soOrderProdTrace.setIsSendSms(isSendSms);

        List<ProdWorkflowNode> prodWorkflowNodeList = prodWorkflowNodeService.queryWorkflowNodeListByOrderProdId(soOrderProd.getPkid());
        if(prodWorkflowNodeList == null)
        {
            data.setMsg("未找到流程方案");
            data.setCode(402);
            return data;
        }
        ProdWorkflowNode prodWorkflowNode = prodWorkflowNodeList.get(prodWorkflowNodeList.size() - 1);
        if(soOrderProd.getProcessStatusId() == prodWorkflowNode.getPkid())
        {
            List<ProdWorkflowFile> prodWorkflowFileList = prodWorkflowFileService.queryWorkflowFileListByOrderProdId(soOrderProd.getPkid());
            List<Integer> ismustProdWorkflowFilelist = new ArrayList();
            for(ProdWorkflowFile prodWorkflowFile : prodWorkflowFileList)
            {
                if(prodWorkflowFile.getIsMust() == 1)
                {
                    ismustProdWorkflowFilelist.add(prodWorkflowFile.getPkid());
                }
            }
            Map<String, Object> soOrderProdTraceMap = new HashMap();
            soOrderProdTraceMap.put("order_prod_id", soOrderProdTrace.getOrderProdId());
            soOrderProdTraceMap.put("type_id", 3153);
            List<SoOrderProdTrace> soOrderProdTraceList = soOrderProdTraceService.findByProperties(soOrderProdTraceMap, 0, 1000);
            List<Integer> soOrderProdTraceIds = new ArrayList();
            if(soOrderProdTraceList.size() == 0)
            {
                data.setMsg("必须产品没有上传完");
                data.setCode(200);
                return data;
            }
            for(SoOrderProdTrace item : soOrderProdTraceList)
            {
                soOrderProdTraceIds.add(item.getPkid());
            }
            List<Integer> soOrderProdTraceFileList = soOrderProdTraceFileService.findProdWorkflowFileIdByOrderProdTraceId(soOrderProdTraceIds);
            Integer ismustnum = 0;
            for(Integer itemint : ismustProdWorkflowFilelist)
            {
                for(Integer item : soOrderProdTraceFileList)
                {
                    if(itemint == item)
                    {
                        ismustnum++;
                        continue;
                    }
                }
            }
            if(ismustnum != ismustProdWorkflowFilelist.size())
            {
                data.setMsg("必须产品没有上传完");
                data.setCode(200);
                return data;
            }

            soOrderProd.setAuditStatusId(1052);
            Boolean internal_check = Boolean.valueOf(PropertiesReader.getValue("project","internal_check"));
            if(!internal_check)
            {
                //if(user.is_inner == 1)
                //{
                //      soOrderProd.setAuditStatusId(1054);
                //}
            }
        }

        data.setData(soOrderProdTraceService.updateStatus(soOrderProd, soOrderProdTrace));

        return  data;
    }

    @RequestMapping("/addremark")
    public ResponseData addremark(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String info = request.getParameter("info");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(info == null || info.equals(""))
        {
            data.setMsg("备注内容不能为空");
            data.setCode(402);
            return data;
        }
        SoOrderProd soOrderProd = soOrderProdService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3152);
        soOrderProdTrace.setInfo(info);
        soOrderProdTrace.setIsSendSms(0);

        data.setData(soOrderProdTraceService.findById(soOrderProdTraceService.insert(soOrderProdTrace)));

        return  data;
    }

    @RequestMapping("/addfile")
    public ResponseData addfile(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String prodWorkflowFileId = request.getParameter("prodWorkflowFileId");
        String prodWorkflowFileName = request.getParameter("prodWorkflowFileName");
        String fileId = request.getParameter("fileId");
        String info = request.getParameter("info");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(prodWorkflowFileId == null || prodWorkflowFileId.equals(""))
        {
            data.setMsg("上传文件类型id不能为空");
            data.setCode(402);
            return data;
        }
        if(prodWorkflowFileName == null || prodWorkflowFileName.equals(""))
        {
            data.setMsg("上传文件名称不能为空");
            data.setCode(402);
            return data;
        }
        if(fileId == null || fileId.equals(""))
        {
            data.setMsg("上传文件id不能为空");
            data.setCode(402);
            return data;
        }
        if(info == null || info.equals(""))
        {
            data.setMsg("备注信息不能为空");
            data.setCode(402);
            return data;
        }
        SoOrderProd soOrderProd = soOrderProdService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3153);
        soOrderProdTrace.setInfo(info);
        soOrderProdTrace.setIsSendSms(0);

        SoOrderProdTraceFile soOrderProdTraceFile = new SoOrderProdTraceFile();
        soOrderProdTraceFile.setProdWorkflowFileId(Integer.valueOf(SecurityUtils.rc4Decrypt(prodWorkflowFileId)));
        soOrderProdTraceFile.setProdWorkflowFileName(prodWorkflowFileName);
        soOrderProdTraceFile.setFileId(Integer.valueOf(SecurityUtils.rc4Decrypt(fileId)));
        soOrderProdTraceFile.setAuditStatusId(1051);
        soOrderProdTraceFile.setIsNew(1);
        soOrderProdTraceFile.setAddUserId(0);
        soOrderProdTraceFile.setRemark("");

        data.setData(soOrderProdTraceService.addfile(soOrderProdTrace,soOrderProdTraceFile));

        return  data;
    }

    @RequestMapping("/tipcustomers")
    public ResponseData tipcustomers(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String info = request.getParameter("info");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(info == null || info.equals(""))
        {
            data.setMsg("提醒内容不能为空");
            data.setCode(402);
            return data;
        }
        Integer isSendSms = 0;
        if(request.getParameter("isSendSms") != null)
        {
            isSendSms = Integer.valueOf(request.getParameter("isSendSms"));
        }
        SoOrderProd soOrderProd = soOrderProdService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3154);
        soOrderProdTrace.setInfo(info);
        soOrderProdTrace.setIsSendSms(isSendSms);

        if(isSendSms == 1)
        {
            //发短信
        }
        data.setData(soOrderProdTraceService.findById(soOrderProdTraceService.insert(soOrderProdTrace)));

        return  data;
    }

    @RequestMapping("/sendexpress")
    public ResponseData sendexpress(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String expressContent = request.getParameter("expressContent");
        String expressTo = request.getParameter("expressTo");
        String expressCompanyName = request.getParameter("expressCompanyName");
        String expressNo = request.getParameter("expressNo");
        String info = request.getParameter("info");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(expressContent == null || expressContent.equals(""))
        {
            data.setMsg("快递清单不能为空");
            data.setCode(402);
            return data;
        }
        if(expressTo == null || expressTo.equals(""))
        {
            data.setMsg("收件人不能为空");
            data.setCode(402);
            return data;
        }
        if(expressCompanyName == null || expressCompanyName.equals(""))
        {
            data.setMsg("快递公司不能为空");
            data.setCode(402);
            return data;
        }
        if(expressNo == null || expressNo.equals(""))
        {
            data.setMsg("快递单号不能为空");
            data.setCode(402);
            return data;
        }
        if(info == null || info.equals(""))
        {
            data.setMsg("补充说明不能为空");
            data.setCode(402);
            return data;
        }
        Integer isSendSms = 0;
        if(request.getParameter("isSendSms") != null)
        {
            isSendSms = Integer.valueOf(request.getParameter("isSendSms"));
        }
        SoOrderProd soOrderProd = soOrderProdService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        SoOrderProdTrace soOrderProdTrace = new SoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3155);
        soOrderProdTrace.setInfo(info);
        soOrderProdTrace.setIsSendSms(isSendSms);
        soOrderProdTrace.setOperatorId(0);
        soOrderProdTrace.setRemark("");
        soOrderProdTrace.setExpressContent(expressContent);
        soOrderProdTrace.setExpressTo(expressTo);
        soOrderProdTrace.setExpressCompanyName(expressCompanyName);
        soOrderProdTrace.setExpressNo(expressNo);
        soOrderProdTrace.setProcessdDays(0);
        soOrderProdTrace.setTimeoutDays(0);

        data.setData(soOrderProdTraceService.findById(soOrderProdTraceService.insert(soOrderProdTrace)));

        return  data;
    }

    @RequestMapping("/markcomplaints")
    public ResponseData markcomplaints(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        String info = request.getParameter("info");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(info == null || info.equals(""))
        {
            data.setMsg("投诉说明不能为空");
            data.setCode(402);
            return data;
        }
        Integer isSendSms = 0;
        if(request.getParameter("isSendSms") != null)
        {
            isSendSms = Integer.valueOf(request.getParameter("isSendSms"));
        }
        Integer isFocus = 0;
        if(request.getParameter("isFocus") != null)
        {
            isFocus = Integer.valueOf(request.getParameter("isFocus"));
        }
        SoOrderProd soOrderProd = soOrderProdService.findById(Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3157);
        soOrderProdTrace.setInfo(info);
        soOrderProdTrace.setIsSendSms(0);
        if(isSendSms != 0)
        {
            Map<String, Object> userWhere = new HashMap();
            userWhere.put("order_prod_id", soOrderProd.getPkid());
            userWhere.put("type_id", 3061);
            List<SoOrderProdUserMap> soOrderProdUserMapList = soOrderProdUserMapService.findByProperties(userWhere, 0, 1000);
            if(soOrderProdUserMapList.size() > 0)
            {
                List<Integer> userIds = new ArrayList();
                for (SoOrderProdUserMap item : soOrderProdUserMapList)
                {
                    userIds.add(item.getUserId());
                }
                List<UcUser> ucusers = ucUserService.findByIds(userIds);
                //给这些业务员们发短信（还未实现）
            }
        }
        if(isFocus != 0)
        {
            SoOrderProdUserMap soOrderProdUserMap = new SoOrderProdUserMap();
            soOrderProdUserMap.setUserId(0);
            soOrderProdUserMap.setOrderProdId(soOrderProd.getPkid());
            soOrderProdUserMap.setTypeId(3062);
            soOrderProdUserMap.setStatusId(3141);
            soOrderProdUserMapService.insert(soOrderProdUserMap);
        }
        soOrderProdService.updateIsComplaint(soOrderProdService.findById(soOrderProdTrace.getOrderProdId()));

        data.setData(soOrderProdTraceService.findById(soOrderProdTraceService.insert(soOrderProdTrace)));

        return  data;
    }

    @RequestMapping("/getrecordinfo")
    public ResponseData getrecordinfo(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        String orderProdId = request.getParameter("orderProdId");
        if(orderProdId == null || orderProdId.equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        String page = request.getParameter("currentPage");
        String pagesize = request.getParameter("pageSize");
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pagesize)) {
            pagesize = "10";
        }
        Map<String, Object> accountWhere = new HashMap();
        accountWhere.put("order_prod_id",Integer.valueOf(SecurityUtils.rc4Decrypt(orderProdId)));

        data.setData(soOrderProdAccountService.pageByProperties(accountWhere, Integer.valueOf(page), Integer.valueOf(pagesize)));

        return data;
    }

    @RequestMapping("/editrecordinfo")
    public ResponseData editrecordinfo(HttpServletRequest request, HttpServletResponse response, LoginUser user)
    {
        ResponseData data = new ResponseData();
        if(request.getParameter("param") == null || request.getParameter("param").equals(""))
        {
            data.setMsg("参数不能为空");
            data.setCode(402);
            return data;
        }
        RequestOrderProd list = JsonUtils.jsonToObject(request.getParameter("param"),RequestOrderProd.class);

        if(list == null)
        {
            data.setMsg("参数不正确");
            data.setCode(402);
            return data;
        }
        if(list.getOrderProdId() == null || list.getOrderProdId().equals(""))
        {
            data.setMsg("订单产品id不能为空");
            data.setCode(402);
            return data;
        }
        if(list.getRecordInfoList().size() == 0)
        {
            data.setMsg("账号密码信息不能为空");
            data.setCode(402);
            return data;
        }
        Integer orderProdId = Integer.valueOf(SecurityUtils.rc4Decrypt(list.getOrderProdId()));

        SoOrderProd soOrderProd = soOrderProdService.findById(orderProdId);

        SoOrderProdTrace soOrderProdTrace = newSoOrderProdTrace();
        soOrderProdTrace.setOrderProdId(soOrderProd.getPkid());
        soOrderProdTrace.setOrderProdStatusId(soOrderProd.getProcessStatusId());
        soOrderProdTrace.setTypeId(3156);
        soOrderProdTrace.setInfo("我是账号密码哎");
        soOrderProdTrace.setIsSendSms(0);

        List<SoOrderProdAccount> sopalist = new ArrayList();
        for (RequestOrderProdAccount item : list.getRecordInfoList()) {
            SoOrderProdAccount sopaitem = new SoOrderProdAccount();
            if(item.getId() != null)
            {
                sopaitem.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(item.getId())));
            }
            sopaitem.setOrderProdId(orderProdId);
            sopaitem.setAccount(item.getAccount());
            sopaitem.setPasswd(item.getPasswd());
            sopaitem.setAddUserId(0);
            sopaitem.setRemark(item.getRemark());
            sopalist.add(sopaitem);
        }

        data.setData(soOrderProdTraceService.saveAccount(soOrderProdTrace, sopalist));

        return data;
    }

    private SoOrderProdTrace newSoOrderProdTrace()
    {
        SoOrderProdTrace soOrderProdTrace = new SoOrderProdTrace();
        soOrderProdTrace.setOperatorId(0);
        soOrderProdTrace.setRemark("");
        soOrderProdTrace.setExpressContent("");
        soOrderProdTrace.setExpressTo("");
        soOrderProdTrace.setExpressCompanyName("");
        soOrderProdTrace.setExpressNo("");
        soOrderProdTrace.setProcessdDays(0);
        soOrderProdTrace.setTimeoutDays(0);
        return soOrderProdTrace;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
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
        if (orderProdId > 0) {
            condition.put("order_prod_id", orderProdId);
        }

        Pager<SoOrderProdTrace> pager = soOrderProdTraceService.pageByProperties(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        if(pager != null) {
            data.setData(pager);
        }
        return data;
    }

}