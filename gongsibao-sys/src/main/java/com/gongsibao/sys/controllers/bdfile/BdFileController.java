package com.gongsibao.sys.controllers.bdfile;


import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import com.gongsibao.module.sys.bdfile.service.BdFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bdfile")
public class BdFileController {

    @Autowired
    private BdFileService bdFileService;

    @RequestMapping(value = "/add")
    public ResponseData add(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        BdFile bdFile = new BdFile();
        bdFile.setFormId(1);
        bdFile.setTabName("123");
        bdFile.setAddUserId(1);
        bdFile.setName("aaa");
        bdFile.setUrl("url");
        bdFileService.insert(bdFile);
        data.setMsg("操作成功");
        return data;
    }

    @RequestMapping(value = "/addBatch")
    public ResponseData addBatch(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();

        List<BdFile> list = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BdFile bdFile = new BdFile();
            bdFile.setFormId(2);
            bdFile.setTabName("123");
            bdFile.setAddUserId(1);
            bdFile.setName("aaa" + i);
            bdFile.setUrl("url" + i);
            list.add(bdFile);
        }
        bdFileService.insertBatch(list);
        data.setMsg("操作成功");
        return data;
    }


    @RequestMapping("/delete")
    public ResponseData delete(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        bdFileService.deleteByFormId(1, "123");
        data.setMsg("操作成功");
        return data;
    }


    @RequestMapping("/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response) {
        ResponseData data = new ResponseData();
        List<BdFile> listByFormId = bdFileService.getListByFormId(1, "123");
        System.out.println(listByFormId);

        Map<Integer, List<BdFile>> mapByFormIds = bdFileService.getMapByFormIds(new ArrayList<Integer>() {{
            add(1);
            add(2);
        }}, "123");

        data.setData(mapByFormIds);
        return data;
    }
}