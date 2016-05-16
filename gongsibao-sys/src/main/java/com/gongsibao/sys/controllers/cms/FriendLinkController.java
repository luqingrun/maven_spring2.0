package com.gongsibao.sys.controllers.cms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsFriendLink;
import com.gongsibao.module.sys.cms.service.CmsFriendLinkService;
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
@RequestMapping("/cms/friendlink")
public class FriendLinkController extends CmsBaseController {

    @Autowired
    private CmsFriendLinkService cmsFriendLinkService;

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        cmsFriendLinkService.delete(getPkid(request), user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/edit")
    public ResponseData edit(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        int pkid = getPkid(request);
        int type = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("type")));
        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String url = StringUtils.trimToEmpty(request.getParameter("url"));
        int spider = NumberUtils.toInt(StringUtils.trimToEmpty(request.getParameter("spider")));
        String img = StringUtils.trimToEmpty(request.getParameter("img"));
        CmsFriendLink cmsFriendLink = new CmsFriendLink();
        cmsFriendLink.setUrl(url);
        cmsFriendLink.setType(type);
        cmsFriendLink.setName(name);
        cmsFriendLink.setSpider(spider);
        cmsFriendLink.setAddUser(user.getUcUser().getPkid());
        cmsFriendLink.setUpdUser(user.getUcUser().getPkid());
        cmsFriendLink.setSort(0);
        cmsFriendLink.setStatus(CMSBase.STATUS_INIT);
        if (CmsFriendLink.TYPE_WORD == type) {
            cmsFriendLink.setImg("");
        } else {
            if (StringUtils.isEmpty(url)) {
                data.setData(Boolean.FALSE);
                data.setMsg("请先上传图片");
                return data;
            }
            cmsFriendLink.setImg(img);
        }
        if (pkid > 0) {
            cmsFriendLink.setPkid(pkid);
            cmsFriendLinkService.updateInfo(cmsFriendLink);
        } else {
            cmsFriendLinkService.insert(cmsFriendLink);
        }
        data.setData(Boolean.TRUE);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/list/word")
    public ResponseData wordsList(HttpServletRequest request, HttpServletResponse response) {
        return list(CmsFriendLink.TYPE_WORD);
    }

    @RequestMapping("/list/img")
    public ResponseData imgList(HttpServletRequest request, HttpServletResponse response) {
        return list(CmsFriendLink.TYPE_IMAGE);
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        CmsFriendLink cmsFriendLink = cmsFriendLinkService.findById(getPkid(request));
        data.setData(cmsFriendLink);
        return data;
    }

    @RequestMapping("/up/img")
    public ResponseData upImg(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        Boolean bool = cmsFriendLinkService.editSort(getPkid(request), CmsFriendLink.TYPE_IMAGE, Boolean.TRUE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/down/img")
    public ResponseData downImg(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        Boolean bool = cmsFriendLinkService.editSort(getPkid(request), CmsFriendLink.TYPE_IMAGE, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/up/word")
    public ResponseData upWord(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        Boolean bool = cmsFriendLinkService.editSort(getPkid(request), CmsFriendLink.TYPE_WORD, Boolean.TRUE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/down/word")
    public ResponseData downWord(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        Boolean bool = cmsFriendLinkService.editSort(getPkid(request), CmsFriendLink.TYPE_WORD, Boolean.FALSE, user.getUcUser().getPkid());
        data.setData(bool);
        return data;
    }

    @RequestMapping("/publish/word")
    public ResponseData publishWord(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        List<Integer> ids = getPublishIds(request);
        cmsFriendLinkService.editPublish(ids, CmsFriendLink.TYPE_WORD, user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/publish/img")
    public ResponseData publishImg(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        List<Integer> ids = getPublishIds(request);
        cmsFriendLinkService.editPublish(ids, CmsFriendLink.TYPE_IMAGE, user.getUcUser().getPkid());
        data.setMsg("操作成功");
        return data;
    }

    private ResponseData list(int type) {
        ResponseData data = new ResponseData();

        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("type", type);
        List<CmsFriendLink> list = cmsFriendLinkService.listByProperties(properties);
        data.setData(list);
        return data;
    }

    private List<Integer> getPublishIds(HttpServletRequest request) {
        String ids = StringUtils.trimToEmpty(request.getParameter("ids"));
        String[] idArr = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String idStr : idArr) {
            int id = NumberUtils.toInt(SecurityUtils.rc4Decrypt(idStr));
            if (id > 0) {
                idList.add(id);
            }
        }

        return idList;
    }

}