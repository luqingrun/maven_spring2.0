package com.gongsibao.module.product.prodproductcmstemplate.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodproductcmstemplate.dao.ProdProductCmsTemplateDao;
import com.gongsibao.module.product.prodproductcmstemplate.entity.ProdProductCmsTemplate;
import com.gongsibao.module.product.prodproductcmstemplate.entity.ProductCmsTemplateCity;
import com.gongsibao.module.product.prodproductcmstemplate.service.ProdProductCmsTemplateService;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.dao.ProdProductCmsTemplateBdDictMapDao;
import com.gongsibao.module.product.prodproductcmstemplatebddictmap.entity.ProdProductCmsTemplateBdDictMap;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("prodProductCmsTemplateService")
public class ProdProductCmsTemplateServiceImpl implements ProdProductCmsTemplateService {
    @Autowired
    private ProdProductCmsTemplateDao prodProductCmsTemplateDao;
    @Autowired
    private ProdProductCmsTemplateBdDictMapDao prodProductCmsTemplateBdDictMapDao;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private BdDictService bdDictService;

    @Override
    public ProdProductCmsTemplate findById(Integer pkid) {
        return prodProductCmsTemplateDao.findById(pkid);
    }

    @Override
    public int update(ProdProductCmsTemplate prodProductCmsTemplate) {
        return prodProductCmsTemplateDao.update(prodProductCmsTemplate);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductCmsTemplateDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProductCmsTemplate prodProductCmsTemplate) {
        return prodProductCmsTemplateDao.insert(prodProductCmsTemplate);
    }

    @Override
    public List<ProdProductCmsTemplate> findByIds(List<Integer> pkidList) {
        return prodProductCmsTemplateDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProductCmsTemplate> findMapByIds(List<Integer> pkidList) {
        List<ProdProductCmsTemplate> list = findByIds(pkidList);
        Map<Integer, ProdProductCmsTemplate> map = new HashMap<>();
        for (ProdProductCmsTemplate prodProductCmsTemplate : list) {
            map.put(prodProductCmsTemplate.getPkid(), prodProductCmsTemplate);
        }
        return map;
    }

    @Override
    public Pager<ProdProductCmsTemplate> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductCmsTemplateDao.countByProperties(map);
        Pager<ProdProductCmsTemplate> pager = new Pager<>(totalRows, page);
        List<ProdProductCmsTemplate> prodProductCmsTemplateList = prodProductCmsTemplateDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductCmsTemplateList);
        return pager;
    }

    @Override
    public ResponseData addorupdatetemplate(ProdProductCmsTemplate prodProductCmsTemplate) {
        ResponseData resdata = new ResponseData();
        Integer templateid = 0;

        if (prodProductCmsTemplate.getPkid() == null || prodProductCmsTemplate.getPkid().equals(0)) {
            int countbypid = prodProductCmsTemplateDao.countByProdId(prodProductCmsTemplate.getProductId());
            ProdProduct p = prodProductService.findById(prodProductCmsTemplate.getProductId());
            //当添加的模板信息是第一个时，默认第一个是‘默认模板’
            prodProductCmsTemplate.setIsDefault(countbypid == 0 ? 1 : 0);
            prodProductCmsTemplate.setProductName(p.getName());
            prodProductCmsTemplate.setName("默认模板");
            templateid = prodProductCmsTemplateDao.insert(prodProductCmsTemplate);
        } else {
            ProdProductCmsTemplate templateobj = prodProductCmsTemplateDao.findById(prodProductCmsTemplate.getPkid());
            if (templateobj.getProductId().compareTo(prodProductCmsTemplate.getProductId()) != 0) {
                resdata.setCode(402);
                resdata.setMsg("该模板信息不属于该产品，禁止保存");
                return resdata;
            }
            prodProductCmsTemplate.setIsDefault(templateobj.getIsDefault());
            prodProductCmsTemplateDao.update(prodProductCmsTemplate);
            templateid = prodProductCmsTemplate.getPkid();
        }

        //获取该产品定过价的省市区
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("product_id", prodProductCmsTemplate.getProductId());
        List<BdDict> pricecitylist = prodProductService.findAllCity(propertiesMap);

        List<BdDict> rescitylist = new ArrayList<>();

        GetAllCityTree(pricecitylist, rescitylist);

        //只有非模板的模板才能添加区域map表
        if (prodProductCmsTemplate.getIsDefault().equals(0)) {
            //先删除所有该产品该模板所有的区域信息
            prodProductCmsTemplateBdDictMapDao.deletebyprodid(prodProductCmsTemplate.getProductId(), templateid);
            List<ProdProductCmsTemplateBdDictMap> tempcitylist = new ArrayList<>();
            for (ProductCmsTemplateCity tempdictm : prodProductCmsTemplate.getServiceCityList()) {
                if (rescitylist.stream().filter(c -> c.getPkid().equals(tempdictm.getCityId())).count() <= 0) {
                    BdDict city = bdDictService.findById(tempdictm.getCityId());
                    resdata.setCode(402);
                    resdata.setMsg("城市区域【" + city.getName() + "】还没有定价，禁止保存");
                    return resdata;
                }
                ProdProductCmsTemplateBdDictMap tempmap = new ProdProductCmsTemplateBdDictMap();
                tempmap.setAddUserId(prodProductCmsTemplate.getAddUserId());
                tempmap.setProductId(prodProductCmsTemplate.getProductId());
                tempmap.setCityId(tempdictm.getCityId());
                tempmap.setRemark("");
                tempmap.setTemplateId(templateid);
                tempcitylist.add(tempmap);
            }
            prodProductCmsTemplateBdDictMapDao.insertBatch(tempcitylist);
        }

        resdata.setMsg("操作成功");

        return resdata;
    }

    @Override
    public ResponseData getcmstemplatebyprodid(Map<String, Object> properties, int page, int pagesize) {
        ResponseData res = new ResponseData();
        int totalRows = prodProductCmsTemplateDao.countByProperties(properties);
        Pager<ProdProductCmsTemplate> pager = new Pager<>(totalRows, page, pagesize);
        List<ProdProductCmsTemplate> prodProductCmsTemplateList = prodProductCmsTemplateDao.findByProperties(properties, pager.getStartRow(), pager.getPageSize());
        Integer prodId = Integer.valueOf(properties.get("product_id").toString());
        //该产品所有模板下的所有城市区域
        List<ProdProductCmsTemplateBdDictMap> pccitymap = prodProductCmsTemplateBdDictMapDao.findByProdId(prodId);
        //获取该产品定过价的省市区
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("product_id", prodId);
        List<BdDict> citylist = prodProductService.findAllCity(propertiesMap);


        for (ProdProductCmsTemplate template : prodProductCmsTemplateList) {
            List<ProductCmsTemplateCity> rescitylist = new ArrayList<>();
            if (template.getIsDefault().equals(0))
                getProdCityTree(citylist, pccitymap, template, new ProductCmsTemplateCity(), rescitylist);
            template.setServiceCityList(rescitylist);
        }

        pager.setList(prodProductCmsTemplateList);
        res.setData(pager);
        res.setCode(200);
        return res;
    }

    //递归获取产品模板城市区域的树结构
    private void getProdCityTree(List<BdDict> citylist, List<ProdProductCmsTemplateBdDictMap> pccitymap, ProdProductCmsTemplate template, ProductCmsTemplateCity tempcity, List<ProductCmsTemplateCity> ressoncitylist) {
        for (BdDict city : citylist) {

            ProductCmsTemplateCity tempc = new ProductCmsTemplateCity();
            tempc.setCityId(city.getPkid());
            tempc.setCityName(city.getName());
            tempc.setChildren(new ArrayList<>());
            tempc.setIsHasTemplate(pccitymap.stream().filter(x -> x.getProductId().equals(template.getProductId())
                    && x.getTemplateId().equals(template.getPkid())
                    && x.getCityId().equals(city.getPkid())).count() > 0 ? 1 : 0);

            if (city.getPid().equals(0)) {
                ressoncitylist.add(tempc);
            } else {
                tempcity.getChildren().add(tempc);
            }

            getProdCityTree(city.getChildrenList(), pccitymap, template, tempc, ressoncitylist);

        }
    }

    //获取树结构的所有数据
    private void GetAllCityTree(List<BdDict> citylist, List<BdDict> reslist) {

        for (BdDict city : citylist) {
            reslist.add(city);
            GetAllCityTree(city.getChildrenList(), reslist);
        }
    }

}