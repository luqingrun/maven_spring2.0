package com.gongsibao.sys.controllers.bdauditlog;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdauditlog.entity.BdAuditLog;
import com.gongsibao.module.sys.bdauditlog.service.BdAuditLogService;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jko on 2016/4/21.
 */
@RestController
@RequestMapping("/api/bdAuditLog")
public class BdAuditLogController {

    @Autowired
    private BdAuditLogService bdAuditLogService;

    /**
     * 根据ID得到审核日志记录跳转
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
/*        String pkIdStr = request.getParameter("pkIdStr");
        String idStr = SecurityUtils.rc4Decrypt(pkIdStr);;
        Integer id = Integer.valueOf(idStr);*/
        BdAuditLog bdAuditLog = bdAuditLogService.findById(43);
        data.setData(bdAuditLog);
        data.setMsg("操作成功");
        return data;
    }

    /**
     * 根据参数得到列表信息跳转
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping({"/getList"})
    public ResponseData getList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        String formId = StringUtils.trimToEmpty(request.getParameter("formId"));
        String typeId = StringUtils.trimToEmpty(request.getParameter("typeId"));
        String statusfId = StringUtils.trimToEmpty(request.getParameter("statusfId"));
        String addUserId = StringUtils.trimToEmpty(request.getParameter("addUserId"));
        String level = StringUtils.trimToEmpty(request.getParameter("level"));
        Map<String, Object> properties = new HashMap<>();
        if(!StringUtils.isBlank(formId)){
            properties.put("from_id", formId);
        }
        if(!StringUtils.isBlank(typeId)){
            properties.put("type_id", typeId);
        }
        if(!StringUtils.isBlank(statusfId)){
            properties.put("status_id", statusfId);
        }
        if(!StringUtils.isBlank(addUserId)){
            properties.put("add_user_id", addUserId);
        }
        if(!StringUtils.isBlank(level)){
            properties.put("level", level);
        }
        List<BdAuditLog> list = bdAuditLogService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    /**
     * 单条插入跳转
     *
     * @param request
     * @param response
     * @param bdAuditLog
     * @return ResponseData
     */
    @RequestMapping(value = "/insert")
    public ResponseData insert(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BdAuditLog bdAuditLog, LoginUser user) {
        ResponseData data = new ResponseData();
        bdAuditLog.setFormId(2);
        bdAuditLog.setContent("审批内容");
        bdAuditLog.setAddUserId(1);
        bdAuditLog.setRemark("说明");
        bdAuditLog.setLevel(5);
        bdAuditLog.setStatusId(105);
        bdAuditLog.setTypeId(104);
        bdAuditLogService.insert(bdAuditLog);
        data.setMsg("操作成功");
        data.setData(bdAuditLog);
        return data;
    }

    /**
     * 批量插入跳转
     *
     * @param request
     * @param response
     * @param bdAuditLog
     * @return ResponseData
     */
    @RequestMapping(value = "/batchInsert")
    public ResponseData batchInsert(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BdAuditLog bdAuditLog) {
        ResponseData data = new ResponseData();
        List<BdAuditLog> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            BdAuditLog bdAuditLog1 = new BdAuditLog();
            bdAuditLog1.setFormId(1);
            bdAuditLog1.setContent("内容");
            bdAuditLog1.setAddUserId(2);
            bdAuditLog1.setRemark("2222");
            bdAuditLog1.setLevel(6);
            bdAuditLog1.setStatusId(9);
            bdAuditLog1.setTypeId(15);
            list.add(bdAuditLog1);
        }
        bdAuditLogService.insertBatch(list);
        data.setMsg("操作成功");
        data.setData(list);
        return data;
    }

    /**
     *  更新审核日志记录跳转
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping(value = "/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response,@ModelAttribute BdAuditLog bdAuditLog) {
        ResponseData data = new ResponseData();
/*        BdAuditLog bdAuditLog = new BdAuditLog();
        bdAuditLog.setPkid(2);
        bdAuditLog.setTypeId(105);
        bdAuditLog.setLevel(10);
        bdAuditLog.setStatusId(11);
        bdAuditLog.setRemark("修改说明");
        bdAuditLog.setAddUserId(3);
        bdAuditLog.setContent("修改内容");
        bdAuditLog.setFormId(12);*/
        bdAuditLogService.update(bdAuditLog);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        bdAuditLogService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update1")
    public ResponseData update1(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BdAuditLog bdAuditLog) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        bdAuditLog.setPkid(pkid);
        bdAuditLogService.update(bdAuditLog);
        data.setMsg("操作成功");
        return data;
    }

    /**
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isBlank(pageSize)){
            pageSize="0";
        }
        String formId = StringUtils.trimToEmpty(request.getParameter("formId"));
        String typeId = StringUtils.trimToEmpty(request.getParameter("typeId"));
        String statusId = StringUtils.trimToEmpty(request.getParameter("statusId"));
        String addUserId = StringUtils.trimToEmpty(request.getParameter("addUserId"));
        String level = StringUtils.trimToEmpty(request.getParameter("level"));
        Map<String, Object> properties = new HashMap<>();
        if(!StringUtils.isBlank(formId)){
            properties.put("from_id", formId);
        }
        if(!StringUtils.isBlank(typeId)){
            properties.put("type_id", typeId);
        }
        if(!StringUtils.isBlank(statusId)){
            properties.put("status_id", statusId);
        }
        if(!StringUtils.isBlank(addUserId)){
            properties.put("add_user_id", addUserId);
        }
        if(!StringUtils.isBlank(level)){
            properties.put("level", level);
        }
        //测试用例
/*        Map<String, Object> properties = new HashMap<>();
        properties.put("form_id", 2);
        properties.put("type_id", 104);*/
        Pager<BdAuditLog> pager = bdAuditLogService.pageByProperties(properties, NumberUtils.toInt(page) ,NumberUtils.toInt(pageSize));
        data.setData(pager);
        return data;
    }


}