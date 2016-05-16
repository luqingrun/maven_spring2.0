package com.gongsibao.web.controllers.cms;


import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.sys.cms.entity.CmsTitleDesc;
import com.gongsibao.module.sys.cms.service.CmsTitleDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cms/titledesc")
public class CmsTitleDescController {

    @Autowired
    private CmsTitleDescService cmsTitleDescService;

    @RequestMapping(value = "/home")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsTitleDesc cmsTitleDesc) {
        ResponseData data = new ResponseData();
        CmsTitleDesc titleDesc = cmsTitleDescService.getIndex();
        data.setData(titleDesc);
        return data;
    }
}