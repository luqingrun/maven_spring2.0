package com.gongsibao.web.controllers.cms;


import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsTopnavCategory;
import com.gongsibao.module.sys.cms.service.CmsTopnavCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/topnav")
public class TopnavController {

    @Autowired
    private CmsTopnavCategoryService cmsTopnavCategoryService;

    @RequestMapping("/home")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", CMSBase.STATUS_PUBLISH);

        List<CmsTopnavCategory> list = cmsTopnavCategoryService.getIndexList(paramMap);
        data.setData(list);
        return data;
    }
}