package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsCooperation;
import com.gongsibao.module.sys.cms.service.CmsCooperationService;
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
@RequestMapping("/cms/cooperation")
public class CooperationController  extends CmsBaseController {

    @Autowired
    private CmsCooperationService cmsCooperationService;


    /**
     * 加载list数据
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/list/data")
    public ResponseData data(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        List<CmsCooperation> list = cmsCooperationService.listByProperties(properties);
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
        CmsCooperation cmsCooperation = cmsCooperationService.findById(pkid);
        data.setData(cmsCooperation);
        return data;
    }

    /**
     * 新增或修改（编辑）操作
     *
     * @param request
     * @param response
     * @param cmsCooperation
     * @return ResponseData
     */
    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsCooperation cmsCooperation, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        if (pkid == 0) {
            cmsCooperation.setSort(0);
            cmsCooperation.setStatus(CMSBase.STATUS_INIT);
            cmsCooperation.setUpdUser(user.getUcUser().getPkid());
            cmsCooperation.setAddUser(user.getUcUser().getPkid());
            cmsCooperationService.insert(cmsCooperation);
        } else {
            cmsCooperation.setPkid(pkid);
            cmsCooperationService.update(cmsCooperation);
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
        int pkid = getPkid(request);
        cmsCooperationService.delete(pkid, user.getUcUser().getPkid());
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
        Boolean bool = cmsCooperationService.editSort(pkid, Boolean.TRUE, user.getUcUser().getPkid());
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
        Boolean bool = cmsCooperationService.editSort(pkid, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    /**
     * 发布操作
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response,LoginUser user){
        ResponseData data = new ResponseData();
        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));
        List<Integer> list = new ArrayList<>();
        String [] idArr = ids.split(",");
        for(String idStr: idArr){
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if(id>0){
                list.add(id);
            }
        }
        Boolean bool = cmsCooperationService.editPublish(list, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

}