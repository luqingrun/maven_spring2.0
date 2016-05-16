package com.gongsibao.order.controllers.soorderprodtracefile;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorderprodtracefile.entity.SoOrderProdTraceFile;
import com.gongsibao.module.order.soorderprodtracefile.service.SoOrderProdTraceFileService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/soorderprodtracefile")
public class SoOrderProdTraceFileController {

    @Autowired
    private SoOrderProdTraceFileService soOrderProdTraceFileService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdTraceFile soOrderProdTraceFile) {
        ResponseData data = new ResponseData();
        soOrderProdTraceFileService.insert(soOrderProdTraceFile);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdTraceFileService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdTraceFile soOrderProdTraceFile) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdTraceFile.setPkid(pkid);
        soOrderProdTraceFileService.update(soOrderProdTraceFile);
        data.setMsg("操作成功");
        return data;
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
            condition.put("orderProdId", orderProdId);
        }

        Pager<SoOrderProdTraceFile> pager = soOrderProdTraceFileService.pageByProperties(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
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
        SoOrderProdTraceFile soOrderProdTraceFile = soOrderProdTraceFileService.findById(pkid);
        data.setData(soOrderProdTraceFile);
        return data;
    }

}