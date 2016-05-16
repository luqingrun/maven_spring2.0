package com.gongsibao.sys.controllers.oss;

import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.oss.OSSFileUtils;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.Encodes;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wk on 2016/3/28.
 */
@RestController
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    BdFileService bdFileService;

    /**
     * 私有图片测试类
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/test")
    public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("oss_test");
        mv.addObject("img", request.getContextPath() + "/oss/img/?p=" + Encodes.urlEncode("/test/pic3.png"));
        mv.addObject("randomKey", RandomStringUtils.randomAlphanumeric(32));
        return mv;
    }

    /**
     * 文件上传
     *
     * @param file
     */
    @RequestMapping("/upload")
    public ResponseData getpic(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        ResponseData responseData = new ResponseData();
        Map<String, Object> map = new HashMap<String, Object>();
        responseData.setData(map);

        String folder = StringUtils.trimToEmpty(request.getParameter("folder"));
        try {
            String url = null;
            url = OSSFileUtils.uploadFile(folder, file);

            map.put(OSSFileUtils.FILE_RES_URL, url);
            map.put(OSSFileUtils.FILE_RES_NAME, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseData;
    }

    /**
     * 文件上传
     *
     * @param file
     */
    @RequestMapping("/uploadAndSave")
    public ResponseData getpic(@RequestParam("file") MultipartFile file, HttpServletRequest request, LoginUser user) {

        ResponseData responseData = new ResponseData();

        String tabName = StringUtils.trimToEmpty(request.getParameter("tabName"));
        String folder = StringUtils.trimToEmpty(request.getParameter("folder"));
        try {
            String url = OSSFileUtils.uploadFile(folder, file);
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(url)) {
                Integer pkid = bdFileService.insert(new BdFile() {{
                    setFormId(0);
                    setTabName(tabName);
                    setName("");
                    setUrl(url);
                    setAddUserId(user.getUcUser().getPkid());
                }});
                map.put("pkidStr", SecurityUtils.rc4Encrypt(pkid));
            }

            map.put(OSSFileUtils.FILE_RES_URL, url);
            map.put(OSSFileUtils.FILE_RES_NAME, file.getOriginalFilename());
            responseData.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseData;
    }

}