package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsRecommendCategory;
import com.gongsibao.module.sys.cms.service.CmsRecommendCategoryService;
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
@RequestMapping("/cms/recommendcategory")
public class RecommendCategoryController extends CmsBaseController {

    @Autowired
    private CmsRecommendCategoryService cmsRecommendCategoryService;

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsRecommendCategoryService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/edit")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);

        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String description = StringUtils.trimToEmpty(request.getParameter("description"));
        String img = StringUtils.trimToEmpty(request.getParameter("img"));
        String bgImg = StringUtils.trimToEmpty(request.getParameter("bgImg"));

        CmsRecommendCategory cmsRecommendCategory = new CmsRecommendCategory();
        cmsRecommendCategory.setPkid(pkid);
        cmsRecommendCategory.setName(name);
        cmsRecommendCategory.setDescription(description);
        cmsRecommendCategory.setImg(img);
        cmsRecommendCategory.setBgImg(bgImg);
        cmsRecommendCategory.setAddUser(user.getUcUser().getPkid());
        cmsRecommendCategory.setUpdUser(user.getUcUser().getPkid());

        if (pkid == 0) {
            cmsRecommendCategory.setSort(0);
            cmsRecommendCategory.setStatus(CMSBase.STATUS_INIT);
            cmsRecommendCategoryService.insert(cmsRecommendCategory);
        } else {
            cmsRecommendCategoryService.updateInfo(cmsRecommendCategory);
        }

        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);

        List<CmsRecommendCategory> list = cmsRecommendCategoryService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        int pkid = getPkid(request);

        CmsRecommendCategory cmsRecommendCategory = cmsRecommendCategoryService.findById(pkid);
        data.setData(cmsRecommendCategory);
        return data;
    }

    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));
        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArr) {
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if (id > 0) {
                idList.add(id);
            }
        }

        cmsRecommendCategoryService.editPublish(idList, user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/up")
    public ResponseData up(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);

        Boolean bool = cmsRecommendCategoryService.editSort(pkid, Boolean.TRUE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/down")
    public ResponseData down(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);

        Boolean bool = cmsRecommendCategoryService.editSort(pkid, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }
}