package com.gongsibao.{{module_name}}.controllers.{{pack_name}};


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.{{module_name}}.{{pack_name}}.entity.{{class_name}};
import com.gongsibao.module.{{module_name}}.{{pack_name}}.service.{{class_name}}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/{{pack_name}}")
public class {{class_name}}Controller {

    @Autowired
    private {{class_name}}Service {{java_name}}Service;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute {{class_name}} {{java_name}}) {
        ResponseData data = new ResponseData();
        {{java_name}}Service.insert({{java_name}});
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        {{java_name}}Service.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute {{class_name}} {{java_name}}) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        {{java_name}}.setPkid(pkid);
        {{java_name}}Service.update({{java_name}});
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
        Pager<{{class_name}}> pager = {{java_name}}Service.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        {{class_name}} {{java_name}} = {{java_name}}Service.findById(pkid);
        data.setData({{java_name}});
        return data;
    }

}
