package com.gongsibao.web.controllers.cms;

import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsRecommendPackage;
import com.gongsibao.module.sys.cms.service.CmsRecommendPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jko on 2016/4/11.
 */
@RestController
@RequestMapping("/cms/recommendpackage")
public class RecommendPackageController {

    @Autowired
    CmsRecommendPackageService cmsRecommendPackageService;

    /**
     * 得到列表数据List
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/home")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_PUBLISH);
        List<CmsRecommendPackage> list = cmsRecommendPackageService.getIndexList(properties);
        data.setData(list);
        return data;
    }


}
