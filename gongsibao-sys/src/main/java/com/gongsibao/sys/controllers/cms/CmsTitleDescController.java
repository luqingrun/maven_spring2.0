package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.sys.cms.entity.CmsTitleDesc;
import com.gongsibao.module.sys.cms.service.CmsTitleDescService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import com.gongsibao.sys.controllers.cms.base.CmsBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cms/titledesc")
public class CmsTitleDescController extends CmsBaseController {

    @Autowired
    private CmsTitleDescService cmsTitleDescService;

    @RequestMapping("/edit")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CmsTitleDesc cmsTitleDesc, LoginUser user) {
        ResponseData data = new ResponseData();
        Integer pkid = getPkid(request);

        cmsTitleDesc.setUpdUser(user.getUcUser().getPkid());
        cmsTitleDesc.setAddUser(user.getUcUser().getPkid());
        if (pkid == 0) {
            cmsTitleDescService.insert(cmsTitleDesc);
        } else {
            cmsTitleDesc.setPkid(pkid);
            cmsTitleDescService.update(cmsTitleDesc);
        }
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        CmsTitleDesc cmsTitleDesc = cmsTitleDescService.get();
        data.setData(cmsTitleDesc);
        return data;
    }
}