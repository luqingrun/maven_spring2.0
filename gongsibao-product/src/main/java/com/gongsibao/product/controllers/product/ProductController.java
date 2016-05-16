package com.gongsibao.product.controllers.product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/product")
public class ProductController {
    @RequestMapping("/test")
    public Map<String, Object> test(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        map.put("ID","1");
        map.put("product","产品名称");
        map.put("date", new Date());

        return map;
    }
}
