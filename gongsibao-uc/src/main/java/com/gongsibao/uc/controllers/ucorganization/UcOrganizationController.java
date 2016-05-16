package com.gongsibao.uc.controllers.ucorganization;


import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.common.util.security.SecurityUtils;
import com.gongsibao.module.uc.ucorganization.entity.ORG;
import com.gongsibao.module.uc.ucorganization.entity.UcOrganization;
import com.gongsibao.module.uc.ucorganization.service.UcOrganizationService;
import com.gongsibao.module.uc.ucuser.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ucorganization")
public class UcOrganizationController {

    @Autowired
    private UcOrganizationService ucOrganizationService;

    /**
     * 组织机构详情查询
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/info")
    public ResponseData info(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orgPkidStr")));

        UcOrganization ucOrganization = ucOrganizationService.findOrgDetail(pkid);
        data.setData(ucOrganization);
        return data;
    }


    /**
     * 组织机构树查询
     *
     * @param request
     * @param response
     * @param user
     * @return
     */
    @RequestMapping("/tree")
    public ResponseData tree(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        Integer orgPkid = user.getUcOrganizationList().get(0).getPkid();
        UcOrganization ucOrganization = ucOrganizationService.findTreeById(orgPkid, true);
        data.setData(ucOrganization);
        return data;
    }

    /**
     * 查所有底层机构
     *
     * @return
     */
    @RequestMapping("/floor")
    public ResponseData bottomList() {
        ResponseData data = new ResponseData();
        data.setData(ucOrganizationService.findByLevel(ORG.LEVEL_BOTTOM));
        return data;
    }

    @RequestMapping("/list")
    public ResponseData list(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int currentPage = NumberUtils.toInt(request.getParameter("currentPage"));
        int pageSize = NumberUtils.toInt(request.getParameter("pageSize"));
        int orgPkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orgPkidStr")));
        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String shortName = StringUtils.trimToEmpty(request.getParameter("shortName"));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("currentUserId", user.getUcUser().getPkid());
        if (orgPkid > 0) {
            paramMap.put("orgPkid", orgPkid);
        }

        if (StringUtils.isNotBlank(name)) {
            paramMap.put("name", name);
        }

        if (StringUtils.isNotBlank(shortName)) {
            paramMap.put("shortName", shortName);
        }

        Pager<UcOrganization> pager = ucOrganizationService.findOrgList(paramMap, currentPage, pageSize);
        data.setData(pager);

        return data;
    }

    @RequestMapping("/oplist")
    public ResponseData oplist(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();

        int userId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("userIdStr")));

        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);
        Map<Integer, List<UcOrganization>> mapByUserIds = ucOrganizationService.findMapByUserIds(userIds);
        List<UcOrganization> ucOrganizations = mapByUserIds.get(userId);
        List<UcOrganization> byLevel = ucOrganizationService.findByLevel(6);
        List<UcOrganization> res = new ArrayList<>();
        if (ucOrganizations!=null) {

            for (UcOrganization o : ucOrganizations) {
                if (byLevel.contains(o)) {
                    res.add(o);
                }
            }
        }
        if (byLevel!=null) {

            for (UcOrganization o : byLevel) {
                if (!res.contains(o)) {
                    res.add(o);
                }
            }
        }
        data.setData(res);

        return data;
    }

    @RequestMapping("/myList")
    public ResponseData userOrgList(HttpServletRequest request, HttpServletResponse response, LoginUser user) {
        ResponseData data = new ResponseData();
        List<UcOrganization> userOrgList = ucOrganizationService.getUserOrgList(user.getUcUser().getPkid());
        data.setData(userOrgList);
        return data;
    }

    /**
     * 查所有底层机构
     *
     * @return
     */
    @RequestMapping("/del")
    public ResponseData del(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();
        data.setCode(-1);
        int pkid = NumberUtils.toInt(request.getParameter("orgPkidStr"));

        int rs = ucOrganizationService.delete(pkid, user.getUcUser().getPkid());
        if (rs > 0) {
            data.setCode(200);
            data.setMsg("删除成功");
        } else if (rs == -1) {
            data.setCode(-1);
            data.setMsg("您无权删除该组织机构");
        } else if (rs == -2) {
            data.setCode(-2);
            data.setMsg("存在配置了此组织的用户，无法删除！\n" +
                    "您可以在用户设置中查询配置此了组织的用户");
        } else if (rs == 0) {
            data.setCode(0);
            data.setMsg("删除失败");
        }
        return data;
    }

    /**
     * 查所有底层机构
     *
     * @return
     */
    @RequestMapping("/save")
    public ResponseData save(HttpServletRequest request, LoginUser user) {
        ResponseData data = new ResponseData();

//        | ** orgPkidStr **  |  true    |  String     | 订单ID(加密) |
//        | ** name **  |  true    |  String     | 组织机构名称 |
//        | ** shortName **  |  true    |  String     | 简称 |
//        | ** pidStr **  |  true    |  String     | 上级机构ID(加密) |
//        | ** leaderIdStr **  |  false    |  String     | 负责人ID(加密) |
//        | ** cityIdStr **  |  false    |  String     | 所在地区ID(加密) |
//        | ** categoryIds **  |  false    |  String     | 服务产品范围(加密), 多个以英文逗号分隔 |
//        | ** cityIds **  |  false    |  String     | 服务产品范围(加密), 多个以英文逗号分隔 |
        int pkid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("orgPkidStr")));
        int pid = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("pidStr")));
        String name = StringUtils.trimToEmpty(request.getParameter("name"));
        String shortName = StringUtils.trimToEmpty(request.getParameter("shortName"));
        int leaderId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("leaderIdStr")));
        int cityId = NumberUtils.toInt(SecurityUtils.rc4Decrypt(request.getParameter("cityIdStr")));

        String categoryIds = StringUtils.trimToEmpty(request.getParameter("categoryIds"));
        String cityIds = StringUtils.trimToEmpty(request.getParameter("cityIds"));

        if (pid == 0) {
            throw new IllegalArgumentException("pidStr[" + request.getParameter("pidStr") + "]");
        }

        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("empty name[" + request.getParameter("name") + "]");
        }

        if (StringUtils.isBlank(shortName)) {
            throw new IllegalArgumentException("empty name[" + request.getParameter("shortName") + "]");
        }

        UcOrganization organization = new UcOrganization() {{
            setPid(pid);
            setName(name);
            setCityId(cityId);
            setShortName(shortName);
            setLeaderId(leaderId);
            setCategoryIds(SecurityUtils.rc4DecryptBatch(categoryIds.split(",")));
            setCityIds(SecurityUtils.rc4DecryptBatch(cityIds.split(",")));

            setIsEnabled(1);
            setIsLeaf(1);
            setAddUserId(user.getUcUser().getPkid());
        }};
        organization.setPkid(pkid);

        int rs = ucOrganizationService.save(organization);
        if (rs != 1) {
            data.setCode(-1);
            data.setMsg("保存失败");
        }

        return data;
    }
}