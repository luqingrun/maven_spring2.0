package com.gongsibao.product.controllers.prodworkflownode;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.product.prodworkflownode.service.ProdWorkflowNodeService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/prodworkflownode")
public class ProdWorkflowNodeController {

    @Autowired
    private ProdWorkflowNodeService prodWorkflowNodeService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdWorkflowNode prodWorkflowNode) {
        ResponseData data = new ResponseData();
        //prodWorkflowNodeService.insert(prodWorkflowNode);

        List<ProdWorkflowNode> itemList = new ArrayList<>();
        for(int i=0;i<3;i++){
            ProdWorkflowNode info = new ProdWorkflowNode();
            info.setName("aa");
            info.setSort(1.2);
            info.setTypeId(i);
            info.setWeekdayCount(i);
            info.setWorkflowId(i);
            itemList.add(info);
        }

        prodWorkflowNodeService.insertBatch(itemList);

        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodWorkflowNodeService.delete(pkid);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping("/update")
    public ResponseData update(HttpServletRequest request, HttpServletResponse response, @ModelAttribute ProdWorkflowNode prodWorkflowNode) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        prodWorkflowNode.setPkid(pkid);
        prodWorkflowNodeService.update(prodWorkflowNode);
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
        Pager<ProdWorkflowNode> pager = prodWorkflowNodeService.pageByProperties(null, Integer.valueOf(page));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String pkidStr = request.getParameter("pkidStr");
        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = Integer.valueOf(pkidStr);
        ProdWorkflowNode prodWorkflowNode = prodWorkflowNodeService.findById(pkid);
        data.setData(prodWorkflowNode);
        return data;
    }

    @RequestMapping({"/selects"})
    public ResponseData queryListByOrderProdId(HttpServletRequest request, HttpServletResponse response) {
        // 产品订单ID加密串
        String orderProdIdStr = StringUtils.trimToEmpty(request.getParameter("orderProdIdStr"));
        orderProdIdStr = SecurityUtils.rc4Decrypt(orderProdIdStr);
        int orderProdId = NumberUtils.toInt(orderProdIdStr);

        List<ProdWorkflowNode> list = prodWorkflowNodeService.queryWorkflowNodeListByOrderProdId(orderProdId);
        ResponseData data = new ResponseData();
        data.setData(list);
        return data;
    }

}