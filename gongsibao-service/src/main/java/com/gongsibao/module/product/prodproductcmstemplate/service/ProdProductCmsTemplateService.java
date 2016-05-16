package com.gongsibao.module.product.prodproductcmstemplate.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.product.prodproductcmstemplate.entity.ProdProductCmsTemplate;

import java.util.List;
import java.util.Map;

public interface ProdProductCmsTemplateService {

    ProdProductCmsTemplate findById(Integer pkid);

    int update(ProdProductCmsTemplate prodProductCmsTemplate);

    int delete(Integer pkid);

    Integer insert(ProdProductCmsTemplate prodProductCmsTemplate);

    List<ProdProductCmsTemplate> findByIds(List<Integer> pkidList);

    Map<Integer, ProdProductCmsTemplate> findMapByIds(List<Integer> pkidList);

    Pager<ProdProductCmsTemplate> pageByProperties(Map<String, Object> properties, int page);
    /*保存或修改模板信息*/
    ResponseData addorupdatetemplate(ProdProductCmsTemplate prodProductCmsTemplate);
    /*根据产品id获取模板信息列表*/
    ResponseData getcmstemplatebyprodid (Map<String, Object> properties, int page, int pagesize);
}