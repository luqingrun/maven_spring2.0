package com.gongsibao.sys.controllers.bddict;


import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class BdDictController {

    @Autowired
    private BdDictService bdDictService;

    @RequestMapping({"/list"})
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        int type = 101;
        List<Integer> ids = new ArrayList<>();
        ids.add(101110115);
        ids.add(101120114);
        ids.add(101130823);
        // 测试
        Map<Integer, String> map = bdDictService.queryDictNames(type, ids);
        for(Integer key : map.keySet()) {
            System.out.println("key is:"+key);
            System.out.println("zhi is:"+map.get(key));
        }
        System.out.println("json is:"+ JsonUtils.objectToJson(map));
        // 测试
        List<Map<String, Object>> lst = bdDictService.queryParents(101, 101130823);
        System.out.println("lst size is:"+lst.size());
        for(Map<String, Object> m : lst){
            System.out.println("map pid is:"+m.get("pid"));
            System.out.println("map dictList is:"+((List<BdDict>)m.get("dictList")).size());
        }
        String json = JsonUtils.objectToJson(lst);
        System.out.println("json is:"+ json);

        // 测试
        List<BdDict> list1 = bdDictService.findByType(101);
        System.out.println("list1 size is:"+list1.size());

        List<BdDict> list2 = bdDictService.findTreeByType(101);
        System.out.println("list2 size is:"+list2.size());

        List<BdDict> list3 = bdDictService.findChildrenByParentId(101110100);
        System.out.println("list3 size is:"+list3.size());
        json = JsonUtils.objectToJson(list3);
        System.out.println("json is:"+ json);

        List<BdDict> list4 = bdDictService.findTreeChildrenByParentId(101110000);
        System.out.println("list4 size is:"+list4.size());
        json = JsonUtils.objectToJson(list4);
        //System.out.println("json is:"+ json);

        Pager<BdDict> pager = bdDictService.pageByProperties(null, Integer.valueOf(1));
        data.setData(pager);
        return data;
    }

    @RequestMapping("/getByType")
    public ResponseData findByType(HttpServletRequest request, HttpServletResponse response) {

        String type = StringUtils.trimToEmpty(request.getParameter("type"));

        List<BdDict> list = bdDictService.findOneLevelByType(NumberUtils.toInt(type));
        ResponseData data = new ResponseData();
        if(CollectionUtils.isEmpty(list)){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(list);
        }
        return data;
    }

    @RequestMapping("/getTreeByType")
    public ResponseData findTreeByType(HttpServletRequest request, HttpServletResponse response) {

        String type = StringUtils.trimToEmpty(request.getParameter("type"));

        List<BdDict> list = bdDictService.findTreeByType(NumberUtils.toInt(type));
        ResponseData data = new ResponseData();
        if(CollectionUtils.isEmpty(list)){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(list);
        }
        return data;
    }

    @RequestMapping("/getByPid")
    public ResponseData findChildrenByParentId(HttpServletRequest request, HttpServletResponse response) {
        // 字典ID加密字符串
        String pkidStr = StringUtils.trimToEmpty(request.getParameter("pkidStr"));

        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = NumberUtils.toInt(pkidStr, -1);

        List<BdDict> list = bdDictService.findChildrenByParentId(pkid);
        ResponseData data = new ResponseData();
        if(CollectionUtils.isEmpty(list)){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(list);
        }
        return data;
    }

    @RequestMapping("/getById")
    public ResponseData findTreeChildrenByParentId(HttpServletRequest request, HttpServletResponse response) {
        // 字典ID加密字符串
        String pkidStr = StringUtils.trimToEmpty(request.getParameter("pkidStr"));
        String type = StringUtils.trimToEmpty(request.getParameter("type"));

        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = NumberUtils.toInt(pkidStr, -1);

        BdDict dict = bdDictService.findTreeByParentId(NumberUtils.toInt(type, -1), pkid);
        ResponseData data = new ResponseData();
        if(dict == null){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(dict);
        }
        return data;
    }

    @RequestMapping("/getLeafById")
    public ResponseData queryLeafById(HttpServletRequest request, HttpServletResponse response) {
        // 字典ID加密字符串
        String pkidStr = StringUtils.trimToEmpty(request.getParameter("pkidStr"));
        String type = StringUtils.trimToEmpty(request.getParameter("type"));

        pkidStr = SecurityUtils.rc4Decrypt(pkidStr);
        Integer pkid = NumberUtils.toInt(pkidStr, -1);

        List<Map<String, Object>> list = bdDictService.queryParents(NumberUtils.toInt(type, -1), pkid);
        ResponseData data = new ResponseData();
        if(CollectionUtils.isEmpty(list)){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(list);
        }
        return data;
    }

//    @RequestMapping("/getLeafByIds")
//    public ResponseData queryLeafByIds(HttpServletRequest request, HttpServletResponse response) {
//        List<Integer> leafIds = new ArrayList<>();
//        leafIds.add(101110112);
//        leafIds.add(101110113);
//        leafIds.add(101350300);
//        leafIds.add(101350627);
//        leafIds.add(101350628);
//        List<BdDict> list = bdDictService.queryParentsByLeafIds(101, leafIds);
//        ResponseData data = new ResponseData();
//        if(CollectionUtils.isEmpty(list)){
//            data.setCode(-1);
//            data.setMsg("操作失败");
//        } else {
//            data.setData(list);
//        }
//        return data;
//    }

    @RequestMapping("/getNamesByType")
    public ResponseData queryNamesByType(HttpServletRequest request, HttpServletResponse response) {
        String type = StringUtils.trimToEmpty(request.getParameter("type"));
        String name = StringUtils.trimToEmpty(request.getParameter("name"));

        List<String> list = bdDictService.queryNames(NumberUtils.toInt(type, -1), name);
        ResponseData data = new ResponseData();
        if(CollectionUtils.isEmpty(list)){
            data.setCode(-1);
            data.setMsg("操作失败");
        } else {
            data.setData(list);
        }
        return data;
    }

    @RequestMapping("/addbatch")
    public ResponseData addbatch(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        String type = request.getParameter("type");
        String bddict = request.getParameter("value");
        List<BdDict> bdlist = new ArrayList();
        String[] bdString = bddict.split("、");
        int count = 0;
        Boolean isnext = true;
        for (String item : bdString)
        {
            count++;
            BdDict bd = new BdDict();
            bd.setPkid(Integer.valueOf(type + count));
            bd.setType(Integer.valueOf(type));
            bd.setPid(0);
            bd.setName(item);
            bd.setLevel(1);
            if(item.contains(" "))
            {
                String[] itemString = item.split(" ");
                bd.setPid(Integer.valueOf(itemString[0]));
                bd.setName(itemString[1]);
                BdDict temp = bdDictService.findById(Integer.valueOf(itemString[0]));
                if(temp == null || temp.equals(null))
                {
                    isnext = false;
                }
                else
                {
                    BdDict maxBd = bdDictService.getMaxPkidByType(Integer.valueOf(type));
                    bd.setPkid(maxBd.getPkid() + count);
                    bd.setLevel(temp.getLevel() + 1);
                }
            }
            bd.setSort(1000d);
            bdlist.add(bd);
        }
        if(isnext)
        {
            bdDictService.insertBatch(bdlist);
            data.setMsg("添加成功");
        }
        else
        {
            data.setMsg("有些子级没有父级");
        }
        return data;


    }

}