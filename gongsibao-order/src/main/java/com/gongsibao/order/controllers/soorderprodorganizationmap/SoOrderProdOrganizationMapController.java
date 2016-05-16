package com.gongsibao.order.controllers.soorderprodorganizationmap;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.order.soorderprodorganizationmap.entity.SoOrderProdOrganizationMap;
import com.gongsibao.module.order.soorderprodorganizationmap.service.SoOrderProdOrganizationMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/soorderprodorganizationmap")
public class SoOrderProdOrganizationMapController {

    @Autowired
    private SoOrderProdOrganizationMapService soOrderProdOrganizationMapService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        ResponseData data = new ResponseData();
        soOrderProdOrganizationMapService.insert(soOrderProdOrganizationMap);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdOrganizationMapService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SoOrderProdOrganizationMap soOrderProdOrganizationMap) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        soOrderProdOrganizationMap.setPkid(pkid);
        soOrderProdOrganizationMapService.update(soOrderProdOrganizationMap);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Pager<SoOrderProdOrganizationMap> pager = soOrderProdOrganizationMapService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        SoOrderProdOrganizationMap soOrderProdOrganizationMap = soOrderProdOrganizationMapService.findById(pkid);
        data.setData(soOrderProdOrganizationMap);
        return data;
    }

}