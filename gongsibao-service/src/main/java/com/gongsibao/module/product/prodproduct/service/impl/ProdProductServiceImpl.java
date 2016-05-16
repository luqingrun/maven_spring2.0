package com.gongsibao.module.product.prodproduct.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodprice.dao.ProdPriceDao;
import com.gongsibao.module.product.prodprice.entity.ProdPrice;
import com.gongsibao.module.product.prodpriceaudit.dao.ProdPriceAuditDao;
import com.gongsibao.module.product.prodpriceaudit.entity.ProdPriceAudit;
import com.gongsibao.module.product.prodproduct.dao.ProdProductDao;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodservice.dao.ProdServiceDao;
import com.gongsibao.module.product.prodservice.entity.ProdService;
import com.gongsibao.module.sys.bdauditlog.entity.AuditStatuses;
import com.gongsibao.module.sys.bddict.entity.BdDict;
import com.gongsibao.module.sys.bddict.service.BdDictService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodProductService")
public class ProdProductServiceImpl implements ProdProductService {
    @Autowired
    private ProdProductDao prodProductDao;
    @Autowired
    private ProdServiceDao prodServiceDao;
    @Autowired
    private BdDictService bdDictService;
    @Autowired
    private ProdPriceAuditDao prodPriceAuditDao;
    @Autowired
    private  ProdPriceDao prodPriceDao;

    @Override
    public ProdProduct findById(Integer pkid) {
        return prodProductDao.findById(pkid);
    }

    @Override
    public int update(ProdProduct prodProduct) {
        return prodProductDao.update(prodProduct);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProduct prodProduct) {
        return prodProductDao.insert(prodProduct);
    }

    @Override
    public List<ProdProduct> findByIds(List<Integer> pkidList) {
        return prodProductDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProduct> findMapByIds(List<Integer> pkidList) {
        List<ProdProduct> list = findByIds(pkidList);
        Map<Integer, ProdProduct> map = new HashMap<>();
        for(ProdProduct prodProduct : list){
            map.put(prodProduct.getPkid(), prodProduct);
        }
        return map;
    }

    @Override
    public Pager<ProdProduct> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductDao.countByProperties(map);
        Pager<ProdProduct> pager = new Pager<>(totalRows, page);
        List<ProdProduct> prodProductList = prodProductDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductList);
        return pager;
    }

    public ProdProduct saveProduct(ProdProduct pp, List<ProdService> pslist)
    {
        Integer pid = prodProductDao.insert(pp);

        for (int i = 0 ; i < pslist.size() ; i++)
        {
            pslist.get(i).setProductId(pid);
        }
        prodServiceDao.insertBatch(pslist);

        Map pidMap = new HashMap() ;
        pidMap.put( "product_id", pid);

        ProdProduct response_pro = prodProductDao.findById(pid);
        List<ProdService> response_pro_ser = prodServiceDao.findByProperties(pidMap,0,1000);

        response_pro.setProdServiceList(response_pro_ser);

        return response_pro;
    }

    public ProdProduct updateProduct(ProdProduct pp, List<ProdService> pslist)
    {
        prodProductDao.update(pp);

        Integer pid = pp.getPkid();

        List<ProdService> addList = new ArrayList();
        List<ProdService> editList = new ArrayList();
        List<ProdService> dropList = new ArrayList();
        List<ProdService> oldList = new ArrayList();

        Map pidMap = new HashMap() ;
        pidMap.put( "product_id", pid);

        oldList = prodServiceDao.findByProperties(pidMap,0,1000);

        for(ProdService ps : pslist)
        {
            if(ps.getPkid() == null)
            {
                addList.add(ps);
            }
            else
            {
                editList.add(ps);
            }
        }
        for(ProdService ps : oldList)
        {
            Boolean isnothave = true;
            for(ProdService psItem : editList)
            {
                if(ps.getPkid().equals(psItem.getPkid()))
                {
                    isnothave = false;
                }
            }
            if(isnothave)
            {
                dropList.add(ps);
            }
        }

        prodServiceDao.insertBatch(addList);
        prodServiceDao.updateBatch(editList);
        prodServiceDao.deleteBatch(dropList);

        ProdProduct response_pro = prodProductDao.findById(pid);
        List<ProdService> response_pro_ser = prodServiceDao.findByProperties(pidMap,0,1000);

        response_pro.setProdServiceList(response_pro_ser);
        return response_pro;
    }

    public int updateEnabled(ProdProduct pp)
    {
        return prodProductDao.updateEnabled(pp);
    }

    public Pager<ProdProduct> pageByProducts(Map<String, Object> map, int page, int pagesize) {
        int totalRows = prodProductDao.countByProducts(map);
        Pager<ProdProduct> pager = new Pager<>(totalRows, page, pagesize);
        List<ProdProduct> prodProductList = prodProductDao.findByProducts(map, pager.getStartRow(), pager.getPageSize());
        List<ProdProduct> prodProducts = new ArrayList<ProdProduct>();
        for (ProdProduct prodProduct : prodProductList)
        {
            prodProduct.setTypeName(bdDictService.queryDictName(201,prodProduct.getTypeId()));
            prodProduct.setDealerTypeName(bdDictService.queryDictName(205,prodProduct.getDealerTypeId()));
            prodProducts.add(prodProduct);
        }
        pager.setList(prodProducts);
        return pager;
    }

    @Override
    public List<Integer> queryProductIds(Map<String, Object> condition) {
        return prodProductDao.queryProductIds(condition);
    }

    @Override
    public List<BdDict> findAllCity(Map<String, Object> properties) {
        properties.put("audit_status_id", AuditStatuses.Type_1054);
        int totalRows = prodPriceAuditDao.countByProperties(properties);
        Pager<ProdPriceAudit> pager = new Pager<>(totalRows, 0);
        //根据产品Id 及审核通过状态查询出所有ProdPriceAudit
        List<ProdPriceAudit> prodPriceAuditList = prodPriceAuditDao.findByProperties(properties, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodPriceAuditList);
        List<ProdPriceAudit> prodPriceAuditlist = pager.getList();
        ArrayList<Integer> allCity = new ArrayList<>();
        for (ProdPriceAudit prodPriceAudit : prodPriceAuditlist) {
            HashMap<String, Object>  propertiesMap2 = new HashMap<>();
            propertiesMap2.put("price_audit_id", prodPriceAudit.getPkid());
            propertiesMap2.put("is_on_sale", 1);
            //查询出所有上架的地区
            List<ProdPrice> prodPriceList = prodPriceDao.findByAuditIdAndSaleState(prodPriceAudit.getPkid(), 1);
            for (ProdPrice prodPrice : prodPriceList) {
                allCity.add(prodPrice.getCityId());
            }
        }
        List<BdDict> list = bdDictService.queryParentsByLeafIds(BdDict.TYPE_101, allCity);
        if(CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        return  list;
    };
}