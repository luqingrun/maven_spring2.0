package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsBottomnav;
import com.gongsibao.module.sys.cms.service.CmsBottomnavService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.sys.controllers.cms.base.CmsBaseController;
import com.gongsibao.util.constant.ConstantDic;
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
@RequestMapping("/cms/bottomnav")
public class BottomnavController extends CmsBaseController {

    @Autowired
    private CmsBottomnavService cmsBottomnavService;


    /**
     * 加载列表数据
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/list/cate")
    public ResponseData cateList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        data.setData(ConstantDic.BOTTOM_CATEGORY);
        return data;
    }

    /**
     * 加载列表数据
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/list/data")
    public ResponseData data(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("bottom_category", categoryId);
        List<CmsBottomnav> list = cmsBottomnavService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    /**
     * 编辑数据
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        CmsBottomnav cmsBottomnav = cmsBottomnavService.findById(pkid);
        data.setData(cmsBottomnav);
        return data;
    }



    /**
     * 新增或修改（编辑）操作
     *
     * @param request
     * @param response
     * @param cmsBottomnav
     * @return ResponseData
     */
    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsBottomnav cmsBottomnav, LoginUser user) {
        ResponseData data = new ResponseData();

        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        int pkid = getPkid(request);
        if (pkid == 0) {
            cmsBottomnav.setSort(0);
            cmsBottomnav.setAddUser(user.getUcUser().getPkid());
            cmsBottomnav.setUpdUser(user.getUcUser().getPkid());
            cmsBottomnav.setStatus(CMSBase.STATUS_INIT);
            cmsBottomnav.setSpider(0);
            cmsBottomnav.setBottomCategory(categoryId);
            cmsBottomnav.setImg("1");
            cmsBottomnavService.insert(cmsBottomnav);
        } else {
            cmsBottomnav.setPkid(pkid);
            cmsBottomnavService.updateInfo(cmsBottomnav);
        }
        data.setMsg("操作成功");
        return data;
    }


    /**
     * 删除操作
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsBottomnavService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }


    /**
     * 上移操作
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/up")
    public ResponseData up(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        Boolean bool = cmsBottomnavService.editSort(pkid, categoryId, Boolean.TRUE,user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    /**
     * 下移操作
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/down")
    public ResponseData down(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }
        Boolean bool = cmsBottomnavService.editSort(pkid, categoryId, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    /**发布操作
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response,LoginUser user){
        ResponseData data = new ResponseData();

        int categoryId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(StringUtils.trimToEmpty(request.getParameter("categoryId"))));
        if (categoryId == 0) {
            throw new IllegalArgumentException("categoryId [" + request.getParameter("categoryId") + "]");
        }

        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));
        List<Integer> list = new ArrayList<>();
        String [] idArr = ids.split(",");
        for(String idStr: idArr){
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if(id>0){
                list.add(id);
            }
        }
        Boolean bool = cmsBottomnavService.editPublish(list, categoryId, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }


}