package com.gongsibao.web.controllers.cms;

import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.sys.cms.base.entity.CMSBase;
import com.gongsibao.module.sys.cms.entity.CmsFriendLink;
import com.gongsibao.module.sys.cms.service.CmsFriendLinkService;
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
@RequestMapping("/cms/friendlink")
public class FriendLinkController {

    @Autowired
    CmsFriendLinkService cmsFriendLinkService;

    /**
     * 得到文字数据LIST
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/home")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("type", CmsFriendLink.TYPE_WORD);
        List<CmsFriendLink> list = cmsFriendLinkService.getIndexList(properties);
        data.setData(list);
        return data;
    }

    /**
     * 得到图片数据LIST
     *
     * @param request
     * @param response
     * @return ResponseData
     */
    @RequestMapping("/img")
    public ResponseData imgList(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        Map<String, Object> properties = new HashMap<>();
        properties.put("status", CMSBase.STATUS_SHOW);
        properties.put("type", CmsFriendLink.TYPE_IMAGE);
        List<CmsFriendLink> list = cmsFriendLinkService.getIndexList(properties);
        data.setData(list);
        return data;
    }

}
