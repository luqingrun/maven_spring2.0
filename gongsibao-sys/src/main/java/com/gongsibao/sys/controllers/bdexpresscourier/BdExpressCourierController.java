package com.gongsibao.sys.controllers.bdexpresscourier;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdexpresscourier.entity.BdExpressCourier;
import com.gongsibao.module.sys.bdexpresscourier.service.BdExpressCourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bdexpresscourier")
public class BdExpressCourierController {

    @Autowired
    private BdExpressCourierService bdExpressCourierService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BdExpressCourier bdExpressCourier) {
        ResponseData data = new ResponseData();
        bdExpressCourierService.insert(bdExpressCourier);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        bdExpressCourierService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BdExpressCourier bdExpressCourier) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        bdExpressCourier.setPkid(pkid);
        bdExpressCourierService.update(bdExpressCourier);
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
        Pager<BdExpressCourier> pager = bdExpressCourierService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        BdExpressCourier bdExpressCourier = bdExpressCourierService.findById(pkid);
        data.setData(bdExpressCourier);
        return data;
    }

    @RequestMapping("/getall")
    public ResponseData getall(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String isHot = request.getParameter("isHot");
        List<BdExpressCourier> list;
        if(StringUtils.isEmpty(isHot)) { 
            list = bdExpressCourierService.findAllBdExpressCourier();
        } else {
            HashMap<String, Object> propertiesMap = new HashMap<>();
            propertiesMap.put("is_hot", Integer.valueOf(isHot));
            list = bdExpressCourierService.pageByProperties(propertiesMap, 0).getList();
        }
        data.setData(list);
        return data;
    }
}