package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsRecommendService;
import com.gongsibao.module.sys.cms.service.CmsRecommendServiceService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.sys.controllers.cms.base.CmsBaseController;
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
@RequestMapping("/cms/recommendservice")
public class RecommendServiceController extends CmsBaseController {

    @Autowired
    private CmsRecommendServiceService cmsRecommendServiceService;

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        data.setData(cmsRecommendServiceService.findById(getPkid(request)));
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsRecommendServiceService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/edit")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int pkid = getPkid(request);
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));

        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String description = StringUtils.trimToEmpty(request.getParameter("description"));
        String url = StringUtils.trimToEmpty(request.getParameter("url"));
//        String img = StringUtils.trimToEmpty(request.getParameter("img"));
        String price = StringUtils.trimToEmpty(request.getParameter("price"));
        int priceType = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("priceType")));

        CmsRecommendService cmsRecommendService = new CmsRecommendService();
        cmsRecommendService.setPkid(pkid);
        cmsRecommendService.setName(name);
        cmsRecommendService.setDescription(description);
        cmsRecommendService.setUrl(url);
        cmsRecommendService.setImg("");
        cmsRecommendService.setPrice(price);
        cmsRecommendService.setPriceType(priceType);
        cmsRecommendService.setCategoryId(categoryId);

        cmsRecommendService.setAddUser(user.getUcUser().getPkid());
        cmsRecommendService.setUpdUser(user.getUcUser().getPkid());

        if (pkid == 0) {
            cmsRecommendService.setSort(0);
            cmsRecommendService.setStatus(CMSBase.STATUS_INIT);
            cmsRecommendServiceService.insert(cmsRecommendService);
        } else {
            cmsRecommendServiceService.updateInfo(cmsRecommendService);
        }
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("category_id", categoryId);

        List<CmsRecommendService> list = cmsRecommendServiceService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    @RequestMapping("/up")
    public ResponseData up(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        Boolean bool = cmsRecommendServiceService.editSort(getPkid(request), categoryId, Boolean.TRUE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/down")
    public ResponseData down(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        Boolean bool = cmsRecommendServiceService.editSort(getPkid(request), categoryId, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));

        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArr) {
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if (id > 0) {
                idList.add(id);
            }
        }

        cmsRecommendServiceService.editPublish(idList, categoryId, user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }
}