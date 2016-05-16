package com.gongsibao.product.controllers.prodproductcms;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodproductcms.entity.ProdProductCms;
import com.gongsibao.module.product.prodproductcms.service.ProdProductCmsService;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/prodproductcms")
public class ProdProductCmsController {

    @Autowired
    private ProdProductCmsService prodProductCmsService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCms prodProductCms) {
        ResponseData data = new ResponseData();
        prodProductCmsService.insert(prodProductCms);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCmsService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCms prodProductCms) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCms.setPkid(pkid);
        prodProductCmsService.update(prodProductCms);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String page = request.getParameter("page");
        if (StringUtils.isBlank(page)) {
            page = "0";
        }
        Pager<ProdProductCms> pager = prodProductCmsService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        ProdProductCms prodProductCms = prodProductCmsService.findById(pkid);
        data.setData(prodProductCms);
        return data;
    }

    @RequestMapping("/decrypteny")
    public ResponseData DecryptEny(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String res;
        //1:加密 0：解密
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        res = type.equals("1") ? SecurityUtils.rc4Encrypt(Integer.parseInt(id)) : SecurityUtils.rc4Decrypt(id);
        HashMap<String, Object> resmap = new HashMap<>();
        resmap.put("id", res);
        data.setData(resmap);
        return data;
    }

    @RequestMapping("/getbyprodid")
    public ResponseData getbyprodid(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String prodpkidStr = request.getParameter("prodIdStr");
        if (StringUtils.isEmpty(prodpkidStr)) {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        prodpkidStr = SecurityUtils.rc4Decrypt(prodpkidStr);
        Integer prodpkid = Integer.valueOf(prodpkidStr);
        ProdProductCms prodProductCms = prodProductCmsService.findByProdId(prodpkid);
        data.setData(prodProductCms);
        return data;
    }

    @RequestMapping("/addorupdateprodcms")
    public ResponseData addorupdateprodcms(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        if (request.getParameter("param") == null || request.getParameter("param").equals("")) {
            data.setMsg("参数不能为空");
            data.setCode(402);
            return data;
        }
        ProdProductCms prodProductCms = JsonUtils.jsonToObject(request.getParameter("param"), ProdProductCms.class);
        if (prodProductCms == null) {
            data.setMsg("Json参数格式错误");
            data.setCode(402);
            return data;
        }
        prodProductCms.setRemark("");
        prodProductCms.setAddUserId(user.getUcUser().getPkid());
        prodProductCms.setProductId(Integer.valueOf(SecurityUtils.rc4Decrypt(prodProductCms.getProdIdStr())));
        prodProductCms.getProdProductCmsRelatedList().forEach(x -> {
            x.setRecommendProductId(Integer.valueOf(SecurityUtils.rc4Decrypt(x.getRecommendProductIdStr())));
        });
        String prodcmsid = prodProductCms.getId();
        if (StringUtils.isNotEmpty(prodcmsid) && StringUtils.isNotBlank(prodcmsid))
            prodProductCms.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(prodcmsid)));
        data = prodProductCmsService.addorupdateprodcms(prodProductCms);
        return data;
    }

}