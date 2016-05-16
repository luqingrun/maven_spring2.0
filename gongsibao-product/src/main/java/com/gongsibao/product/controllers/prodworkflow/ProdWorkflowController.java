package com.gongsibao.product.controllers.prodworkflow;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.product.prodworkflow.entity.ProdWorkflow;
import com.gongsibao.module.product.prodworkflow.service.ProdWorkflowService;
import com.gongsibao.module.product.prodworkflowbddictmap.entity.ProdWorkflowBdDictMap;
import com.gongsibao.module.product.prodworkflowfile.entity.ProdWorkflowFile;
import com.gongsibao.module.product.prodworkflownode.entity.ProdWorkflowNode;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/prodworkflow")
public class ProdWorkflowController {

    @Autowired
    private ProdWorkflowService prodWorkflowService;

    @RequestMapping(value = "/save")
    public ResponseData save(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) {
//        Map<String, Object> mmpp = new HashMap<>();
//        mmpp.put("workflowIdStr", "5");
//        mmpp.put("prodIdStr", "3");
//        mmpp.put("regionStr", "101110115,101120114");
//        mmpp.put("formName", "表单名称1");
//        List<Map<String, Object>> lst1 = new ArrayList<>();
//        Map<String, Object> m1 = new HashMap<>();
//        m1.put("name","节点名称1");
//        m1.put("weekdayCount",1);
//        m1.put("typeId",1);
//        m1.put("sort",1);
//        lst1.add(m1);
//        mmpp.put("stateConfigurationStr", lst1);
//        List<Map<String, Object>> lst2 = new ArrayList<>();
//        Map<String, Object> m2 = new HashMap<>();
//        m2.put("name","材料名称1");
//        m2.put("isMust",1);
//        m2.put("sort",1);
//        lst2.add(m2);
//        mmpp.put("materialDispositionStr", lst2);

        ResponseData data = new ResponseData();

        //String json = JsonUtils.objectToJson(mmpp);
        String json = StringUtils.trimToEmpty(request.getParameter("prodWorkfolwJson"));
        if(StringUtils.isBlank(json)) {
            data.setCode(-1);
            data.setMsg("参数为空");
            return data;
        }
        Map<String, Object> map = (Map<String, Object>) JsonUtils.jsonToObject(json, Map.class);
        if(CollectionUtils.isEmpty(map)) {
            data.setCode(-1);
            data.setMsg("参数错误");
            return data;
        }
        String workflowIdStr = StringUtils.trimToEmpty((String) map.get("workflowIdStr"));
        workflowIdStr = SecurityUtils.rc4Decrypt(workflowIdStr);
        Integer workflowId = NumberUtils.toInt(workflowIdStr, -1);

        String prodIdStr = StringUtils.trimToEmpty((String) map.get("prodIdStr"));
        prodIdStr = SecurityUtils.rc4Decrypt(prodIdStr);
        Integer prodId = NumberUtils.toInt(prodIdStr, -1);

//        String prodTypeIdStr = StringUtils.trimToEmpty((String) map.get("prodTypeId"));
//        Integer prodTypeId = NumberUtils.toInt(prodTypeIdStr);

//        String prodName = StringUtils.trimToEmpty((String) map.get("prodName"));
        String formName = StringUtils.trimToEmpty((String) map.get("formName"));
        String regionStr = StringUtils.trimToEmpty((String) map.get("regionStr"));

//        if(NumberUtils.toInt(prodTypeIdStr) <= 0) {
//            data.setCode(-1);
//            data.setMsg("产品分类为空");
//            return data;
//        }
//        if(StringUtils.isBlank(prodName)) {
//            data.setCode(-1);
//            data.setMsg("产品名称为空");
//            return data;
//        }
        if(StringUtils.isBlank(regionStr)) {
            data.setCode(-1);
            data.setMsg("影响地区为空");
            return data;
        }
        // 封装方案
        ProdWorkflow prodWorkflow = new ProdWorkflow();
        prodWorkflow.setPkid(workflowId);
        prodWorkflow.setProductId(prodId);
        prodWorkflow.setFormName(formName);
        prodWorkflow.setIsEnabled(1);
        prodWorkflow.setAddTime(new Date());
        prodWorkflow.setAddUserId(loginUser.getUcUser().getPkid());// 登录人ID
        prodWorkflow.setRemark("");
        // 封装方案关联影响地区
        ProdWorkflowBdDictMap prodWorkflowBdDictMap = null;
        List<ProdWorkflowBdDictMap> regionList = new ArrayList<>();
        for(String cityId : StringUtils.split(regionStr, ",")) {
            prodWorkflowBdDictMap = new ProdWorkflowBdDictMap();
            prodWorkflowBdDictMap.setCityId(NumberUtils.toInt(cityId));
            regionList.add(prodWorkflowBdDictMap);
        }
        prodWorkflow.setRegionList(regionList);

        // 封装方案关联状态
        List<Map<String, Object>> nodeMap = (List<Map<String, Object>>) map.get("stateConfigurationStr");
        if(!CollectionUtils.isEmpty(nodeMap)) {
            ProdWorkflowNode prodWorkflowNode = null;
            List<ProdWorkflowNode> prodWorkflowNodeList = new ArrayList<>();
            for(Map<String, Object> item : nodeMap){
                prodWorkflowNode = new ProdWorkflowNode();
                prodWorkflowNode.setName((String) item.get("name"));
                prodWorkflowNode.setWeekdayCount((Integer) item.get("weekdayCount"));
                prodWorkflowNode.setTypeId((Integer) item.get("typeId"));
                prodWorkflowNode.setSort(Double.parseDouble(String.valueOf(item.get("sort"))));
                prodWorkflowNodeList.add(prodWorkflowNode);
            }
            prodWorkflow.setProdWorkflowNodeList(prodWorkflowNodeList);
        }
        // 封装方案关联材料
        List<Map<String, Object>> fileMap = (List<Map<String, Object>>) map.get("materialDispositionStr");
        if(!CollectionUtils.isEmpty(nodeMap)) {
            ProdWorkflowFile prodWorkflowFile = null;
            List<ProdWorkflowFile> workflowFileList = new ArrayList<>();
            for(Map<String, Object> item : fileMap){
                prodWorkflowFile = new ProdWorkflowFile();
                prodWorkflowFile.setName((String) item.get("name"));
                prodWorkflowFile.setIsMust((Integer) item.get("isMust"));
                prodWorkflowFile.setSort(Double.parseDouble(String.valueOf(item.get("sort"))));
                workflowFileList.add(prodWorkflowFile);
            }
            prodWorkflow.setWorkflowFileList(workflowFileList);
        }

        try{
            int result = prodWorkflowService.saveItem(prodWorkflow);
            if(result <= 0) {
                data.setCode(-1);
                data.setMsg("操作失败");
            }
        }catch(Exception e){
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    @RequestMapping("/editEnabled")
    public ResponseData updateEnabled(HttpServletRequest request, HttpServletResponse response) {
        // 方案ID加密字符串
        String workflowIdStr = StringUtils.trimToEmpty(request.getParameter("workflowIdStr"));
        // 修改后状态
        String enabledStr = StringUtils.trimToEmpty(request.getParameter("isEnabled"));

        workflowIdStr = SecurityUtils.rc4Decrypt(workflowIdStr);
        Integer workflowId = NumberUtils.toInt(workflowIdStr, -1);
        int isEnabled = NumberUtils.toInt(enabledStr, -1);

        ResponseData data = new ResponseData();
        data.setMsg("操作成功");

        int result = prodWorkflowService.updateEnabled(workflowId, isEnabled);
        if (result <= 0) {
            data.setCode(-1);
            data.setMsg("操作失败");
        }
        return data;
    }

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        // 产品分类ID
        String prodTypeId = StringUtils.trimToEmpty(request.getParameter("prodTypeId"));
        // 产品名称
        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        // 产品编号
        String no = StringUtils.trimToEmpty(request.getParameter("no"));
        // 是否启用状态(0:不启用；1：启用)
        String isEnabled = StringUtils.trimToEmpty(request.getParameter("isEnabled"));
        // 地区ID
        String cityId = StringUtils.trimToEmpty(request.getParameter("cityId"));
        // 当前页
        String currentPage = StringUtils.trimToEmpty(request.getParameter("currentPage"));
        // 每页显示记录数量
        String pageSize = StringUtils.trimToEmpty(request.getParameter("pageSize"));

        // 封装查询条件
        Map<String, Object> condition = new HashMap<>();
        if (NumberUtils.toInt(prodTypeId) > 0) {
            condition.put("prodTypeId", prodTypeId);
        }
        if (StringUtils.isNotBlank(name)) {
            condition.put("name", name);
        }
        if (StringUtils.isNotBlank(no)) {
            condition.put("no", no);
        }
        if (NumberUtils.toInt(isEnabled, -1) > 0) {
            condition.put("is_enabled", NumberUtils.toInt(isEnabled, -1));
        }
        if (NumberUtils.toInt(cityId) > 0) {
            condition.put("cityId", NumberUtils.toInt(cityId));
        }

        Pager<ProdWorkflow> pager = prodWorkflowService.pageByProperties(condition, NumberUtils.toInt(currentPage), NumberUtils.toInt(pageSize));
        ResponseData data = new ResponseData();
        if(pager != null) {
            data.setData(pager);
        }
        return data;
    }

    @RequestMapping("/get")
    public ResponseData get(HttpServletRequest request, HttpServletResponse response) {
        // 方案ID加密字符串
        String workflowIdStr = StringUtils.trimToEmpty(request.getParameter("workflowIdStr"));

        workflowIdStr = SecurityUtils.rc4Decrypt(workflowIdStr);
        Integer workflowId = NumberUtils.toInt(workflowIdStr, -1);

        ProdWorkflow prodWorkflow = prodWorkflowService.findDetailById(workflowId);
        ResponseData data = new ResponseData();
        if(prodWorkflow == null){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(prodWorkflow);
        }
        return data;
    }

}