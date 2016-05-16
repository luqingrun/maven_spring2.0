package com.gongsibao.product.controllers.prodproductcmstemplate;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodproductcmstemplate.entity.ProdProductCmsTemplate;
import com.gongsibao.module.product.prodproductcmstemplate.service.ProdProductCmsTemplateService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prodproductcmstemplate")
public class ProdProductCmsTemplateController {

    @Autowired
    private ProdProductCmsTemplateService prodProductCmsTemplateService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCmsTemplate prodProductCmsTemplate) {
        ResponseData data = new ResponseData();
        prodProductCmsTemplateService.insert(prodProductCmsTemplate);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCmsTemplateService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdProductCmsTemplate prodProductCmsTemplate) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodProductCmsTemplate.setPkid(pkid);
        prodProductCmsTemplateService.update(prodProductCmsTemplate);
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
        Pager<ProdProductCmsTemplate> pager = prodProductCmsTemplateService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        ProdProductCmsTemplate prodProductCmsTemplate = prodProductCmsTemplateService.findById(pkid);
        data.setData(prodProductCmsTemplate);
        return data;
    }

    @RequestMapping("/addorupdatetemplate")
    public ResponseData addorupdatetemplate(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        /*InputStream inputStream = null;
        StringBuffer sb = new StringBuffer();
        try {
            byte[] bytes = new byte[1024];
            inputStream = request.getInputStream();
            int read = inputStream.read(bytes);
            while (read>0){
                sb.append(new String(bytes, "utf-8"));
                read = inputStream.read(bytes);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String,String> map = JsonUtils.jsonToMap(sb.toString(), String.class, String.class);
        System.out.printf(map.get("param"));*/

        /*Map<String, Object> mmpp = new HashMap<>();
        mmpp.put("prodIdStr", "1g~~");
        mmpp.put("id", "1Q~~");
        mmpp.put("name", "模板测试");
        mmpp.put("content", "<span style=\"font-family:Verdana, Arial, Helvetica, sans-serif;line-height:21.6000003814697px;background-color:#FFFFFF;\">对于多线程，无非是对于<span style=\"color:#E53333;\">顺序执行下任务的一种抽取和封装，将原来顺序执行的任务单独拿出来放到线程类的run方法中，通过线程类的start方法进行执行，对于多线程访问共同资源时，我们需要加锁，也就是只有某个线程在拥有锁的时候，才能够得到执行权利，一想，坏了，这如果在单核的情况下，本来就只有一个核来执行任务</span>，你倒好，弄<strong>个多线程，你让一个电脑内核还要不断的切换不同</strong>的线程执行任务，这得到锁，释放锁，切换的时<img src=\"/Scripts/kindeditor-4.1.10/attached/image/20160510/20160510175229_7262.jpg\" alt=\"\" />效率高吗，所以猜想只有在多核的条件下才能体现出多线程的效率高，在单核的情况下如果鼓捣个多线程那就是吃饱啦没事干</span>");

        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("cityIdStr", "1nR72ty9EVJS");
        Map<String, String> map2 = new HashMap<>();
        map2.put("cityIdStr", "1nR72ty9EVJR");
        list.add(map1);
        list.add(map2);
        mmpp.put("serviceCityList", list);
        String json = JsonUtils.objectToJson(mmpp);
        ProdProductCmsTemplate prodProductCmsTemplate = JsonUtils.jsonToObject(json, ProdProductCmsTemplate.class);*/

        if (request.getParameter("param") == null || request.getParameter("param").equals("")) {
            data.setMsg("参数不能为空");
            data.setCode(402);
            return data;
        }
        ProdProductCmsTemplate prodProductCmsTemplate = JsonUtils.jsonToObject(request.getParameter("param"), ProdProductCmsTemplate.class);
        if (prodProductCmsTemplate == null) {
            data.setMsg("Json参数格式错误");
            data.setCode(402);
            return data;
        }
        if (prodProductCmsTemplate.getProdIdStr() == null || prodProductCmsTemplate.getProdIdStr().equals("")) {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }
        prodProductCmsTemplate.setRemark("");
        prodProductCmsTemplate.setAddUserId(user.getUcUser().getPkid());
        prodProductCmsTemplate.setProductId(Integer.valueOf(SecurityUtils.rc4Decrypt(prodProductCmsTemplate.getProdIdStr())));
        if (prodProductCmsTemplate.getId() != null && !prodProductCmsTemplate.getId().equals(""))
            prodProductCmsTemplate.setPkid(Integer.valueOf(SecurityUtils.rc4Decrypt(prodProductCmsTemplate.getId())));

        prodProductCmsTemplate.getServiceCityList().forEach(x -> {
            x.setCityId(Integer.valueOf(SecurityUtils.rc4Decrypt(x.getCityIdStr())));
        });

        data = prodProductCmsTemplateService.addorupdatetemplate(prodProductCmsTemplate);
        return data;
    }


    @RequestMapping("/getcmstemplatebyprodid")
    public ResponseData getcmstemplatebyprodid(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        if (request.getParameter("prodIdStr") == null || request.getParameter("prodIdStr").equals("")) {
            data.setMsg("产品id不能为空");
            data.setCode(402);
            return data;
        }

        // 当前页
        String page = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = "1000";
        }

        Integer prodid = Integer.valueOf(SecurityUtils.rc4Decrypt(request.getParameter("prodIdStr")));

        Map<String, Object> params = new HashMap<>();
        params.put("product_id", prodid);
        data = prodProductCmsTemplateService.getcmstemplatebyprodid(params, Integer.valueOf(page), Integer.valueOf(pageSize));

        return data;
    }

}