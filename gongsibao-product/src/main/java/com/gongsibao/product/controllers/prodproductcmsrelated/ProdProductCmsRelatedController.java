package com.gongsibao.product.controllers.prodproductcmsrelated;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;
import com.gongsibao.module.product.prodproductcmsrelated.service.ProdProductCmsRelatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/prodproductcmsrelated")
public class ProdProductCmsRelatedController {

    @Autowired
    private ProdProductCmsRelatedService prodProductCmsRelatedService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCmsRelated prodProductCmsRelated) {
        ResponseData data = new ResponseData();
        prodProductCmsRelatedService.insert(prodProductCmsRelated);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCmsRelatedService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCmsRelated prodProductCmsRelated) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCmsRelated.setPkid(pkid);
        prodProductCmsRelatedService.update(prodProductCmsRelated);
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
        Pager<ProdProductCmsRelated> pager = prodProductCmsRelatedService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        ProdProductCmsRelated prodProductCmsRelated = prodProductCmsRelatedService.findById(pkid);
        data.setData(prodProductCmsRelated);
        return data;
    }


    @RequestMapping("/getprodnotrelated")
    public ResponseData getprodnotrelated(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String prodIdStr = request.getParameter("prodIdStr");
        String page = request.getParameter("currentPage");
        String pagesize = request.getParameter("pageSize");
        if (StringUtils.isEmpty(prodIdStr)) {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pagesize)) {
            pagesize = "10";
        }
        prodIdStr = SecurityUtils.rc4Decrypt(prodIdStr);
        Integer prodpkid = Integer.valueOf(prodIdStr);
        Pager<ProdProduct> prodProductCmsRelated = prodProductCmsRelatedService.getProdNotRelated(prodpkid, Integer.parseInt(page), Integer.parseInt(pagesize));
        data.setData(prodProductCmsRelated);
        return data;
    }

}