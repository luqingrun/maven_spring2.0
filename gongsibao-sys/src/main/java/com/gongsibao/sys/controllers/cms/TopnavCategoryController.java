package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsTopnavCategory;
import com.gongsibao.module.sys.cms.service.CmsTopnavCategoryService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.sys.controllers.cms.base.CmsBaseController;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cms/topnavcategory")
public class TopnavCategoryController extends CmsBaseController {

    @Autowired
    private CmsTopnavCategoryService cmsTopnavCategoryService;

    @RequestMapping(value = "/category")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsTopnavCategory cmsTopnavCategory) {
        ResponseData data = new ResponseData();
        cmsTopnavCategoryService.insert(cmsTopnavCategory);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsTopnavCategoryService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/get")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        CmsTopnavCategory category = cmsTopnavCategoryService.findById(getPkid(request));
        data.setMsg("操作成功");
        data.setData(category);
        return data;
    }

    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);

        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String url = StringUtils.trimToEmpty(request.getParameter("url"));

        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pidStr"))));

        CmsTopnavCategory cmsTopnavCategory = new CmsTopnavCategory();
        cmsTopnavCategory.setPkid(pkid);
        cmsTopnavCategory.setAddUser(user.getUcUser().getPkid());
        cmsTopnavCategory.setUpdUser(user.getUcUser().getPkid());
        cmsTopnavCategory.setPid(pid);
        cmsTopnavCategory.setName(name);
        cmsTopnavCategory.setUrl(url);

        if (pkid == 0) {
            cmsTopnavCategory.setStatus(CMSBase.STATUS_INIT);
            cmsTopnavCategory.setSort(0);
            cmsTopnavCategoryService.insert(cmsTopnavCategory);
        } else {
            cmsTopnavCategory.setPkid(pkid);
            cmsTopnavCategoryService.updateNameAndUrl(cmsTopnavCategory);
        }
        data.setMsg("操作成功");
        return data;
    }

    /**
     * 一级列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list1")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("pid", 0);

        List<CmsTopnavCategory> list = cmsTopnavCategoryService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    /**
     * 二级列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list2")
    public ResponseData secondList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pid"))));
        if (pid == 0) {
            throw new IllegalArgumentException("pid[" + request.getParameter("pid") + "]");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("pid", pid);

        List<CmsTopnavCategory> list = cmsTopnavCategoryService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    @RequestMapping("/up1")
    public ResponseData up1(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setData(editSort(getPkid(request), Boolean.TRUE, 0, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/down1")
    public ResponseData down1(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        data.setData(editSort(getPkid(request), Boolean.FALSE, 0, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/up2")
    public ResponseData up2(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pid"))));
        if (pid == 0) {
            throw new IllegalArgumentException("pid[" + request.getParameter("pid") + "]");
        }
        data.setData(editSort(getPkid(request), Boolean.TRUE, pid, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/down2")
    public ResponseData down2(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pid"))));
        if (pid == 0) {
            throw new IllegalArgumentException("pid[" + request.getParameter("pid") + "]");
        }
        data.setData(editSort(getPkid(request), Boolean.FALSE, pid, user.getUcUser().getPkid()));
        return data;
    }

    @RequestMapping("/publish1")
    public ResponseData publish1(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
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

        cmsTopnavCategoryService.editPublish(idList, 0, user.getUcUser().getPkid());
        return data;
    }

    @RequestMapping("/publish2")
    public ResponseData publish2(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("pid"))));
        if (pid == 0) {
            throw new IllegalArgumentException("pid[" + request.getParameter("pid") + "]");
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

        cmsTopnavCategoryService.editPublish(idList, pid, user.getUcUser().getPkid());
        return data;
    }

    private Boolean editSort(int pkid, Boolean isup, int pid, int userId) {
        return cmsTopnavCategoryService.editSort(pkid, isup, pid == 0, userId);
    }
}