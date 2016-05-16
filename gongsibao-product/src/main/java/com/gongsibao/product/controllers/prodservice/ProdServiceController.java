package com.gongsibao.product.controllers.prodservice;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.product.prodservice.service.ProdServiceService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/prodservice")
public class ProdServiceController {

    @Autowired
    private ProdServiceService prodServiceService;

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        if(pkidStr == null || pkidStr.equals(""))
        {
            data.setMsg("产品服务项id不能为空");
            data.setCode(402);
            return data;
        }
        Integer pkid = Integer.valueOf(SecurityUtils.rc4Decrypt(pkidStr));
        data.setData(prodServiceService.findById(pkid));
        return data;
    }

}