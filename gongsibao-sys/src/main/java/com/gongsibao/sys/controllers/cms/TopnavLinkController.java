package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.FileUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsTopnavLink;
import com.gongsibao.module.sys.cms.service.CmsTopnavLinkService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.sys.controllers.cms.base.CmsBaseController;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/topnavlink")
public class TopnavLinkController extends CmsBaseController {

    @Autowired
    private CmsTopnavLinkService cmsTopnavLinkService;

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsTopnavLinkService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));

        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String url = StringUtils.trimToEmpty(request.getParameter("url"));
        int recommend = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("recommend")));

        CmsTopnavLink cmsTopnavLink = new CmsTopnavLink();
        cmsTopnavLink.setPkid(pkid);
        cmsTopnavLink.setAddUser(user.getUcUser().getPkid());
        cmsTopnavLink.setUpdUser(user.getUcUser().getPkid());
        cmsTopnavLink.setCategoryId(categoryId);

        cmsTopnavLink.setName(name);
        cmsTopnavLink.setUrl(url);
        cmsTopnavLink.setRecommend(recommend);

        if (pkid == 0) {
            cmsTopnavLink.setSort(0);
            cmsTopnavLink.setStatus(CMSBase.STATUS_INIT);
            cmsTopnavLinkService.insert(cmsTopnavLink);
        } else {
            cmsTopnavLink.setPkid(pkid);
            cmsTopnavLinkService.updateInfo(cmsTopnavLink);
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

        List<CmsTopnavLink> list = cmsTopnavLinkService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        CmsTopnavLink cmsTopnavLink = cmsTopnavLinkService.findById(getPkid(request));
        data.setData(cmsTopnavLink);
        return data;
    }

    @RequestMapping("/up")
    public ResponseData up(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }
        data.setData(editSort(getPkid(request), categoryId, Boolean.TRUE, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/down")
    public ResponseData down(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }
        data.setData(editSort(getPkid(request), categoryId, Boolean.FALSE, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String ids = StringUtils.trimToEmpty(request.getParameter("linkIds"));
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArr) {
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if (id > 0) {
                idList.add(id);
            }
        }
        cmsTopnavLinkService.editPublish(idList, categoryId, user.getUcUser().getPkid());
        return data;
    }

    @RequestMapping("/download")
    public ResponseData download(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        String filePath = cmsTopnavLinkService.export(new HashMap<>());

        FileUtils.downLoacl(request, response, filePath, "测试到处csv.csv");
        FileUtils.removeLocal(new File(filePath));


        return data;
    }

    private Boolean editSort(int pkid, Integer categoryId, Boolean isup, int userId) {
        return cmsTopnavLinkService.editSort(pkid, categoryId, isup, userId);
    }
}