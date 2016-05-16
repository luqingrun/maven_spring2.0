package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsGuestVoice;
import com.gongsibao.module.sys.cms.service.CmsGuestVoiceService;
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
@RequestMapping("/cms/guestvoice")
public class GuestVoiceController extends CmsBaseController {

    @Autowired
    private CmsGuestVoiceService cmsGuestVoiceService;


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
        List<CmsGuestVoice> list = cmsGuestVoiceService.listByProperties(properties);
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
        CmsGuestVoice cmsGuestVoice = cmsGuestVoiceService.findById(pkid);
        data.setData(cmsGuestVoice);
        return data;
    }

    /**
     * 新增或修改（编辑）操作
     *
     * @param request
     * @param response
     * @param cmsGuestVoice
     * @return ResponseData
     */
    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsGuestVoice cmsGuestVoice, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        cmsGuestVoice.setUpdUser(user.getUcUser().getPkid());
        cmsGuestVoice.setAddUser(user.getUcUser().getPkid());

        if (pkid == 0) {
            cmsGuestVoice.setSort(0);
            cmsGuestVoice.setStatus(CMSBase.STATUS_INIT);
            cmsGuestVoiceService.insert(cmsGuestVoice);
        } else {
            cmsGuestVoice.setPkid(pkid);
            cmsGuestVoiceService.update(cmsGuestVoice);
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
        cmsGuestVoiceService.delete(pkid, user.getUcUser().getPkid());
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
    public ResponseData up(HttpServletRequest request, HttpServletResponse response,LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        Boolean bool = cmsGuestVoiceService.editSort(pkid, Boolean.TRUE, user.getUcUser().getPkid());
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
    public ResponseData down(HttpServletRequest request, HttpServletResponse response,LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        Boolean bool = cmsGuestVoiceService.editSort(pkid, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }


    /**
     * 发布操作跳转
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/publish")
    public ResponseData publish(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
        ResponseData data = new ResponseData();
        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));
        List<Integer> idList = new ArrayList<>();
        String[] idArray = ids.split(",");
        for (String idStr : idArray) {
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if (id > 0) {
                idList.add(id);
            }
        }
        Boolean bool = cmsGuestVoiceService.editPublish(idList, loginUser.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }



}