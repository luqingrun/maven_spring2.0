package com.gongsibao.module.product.prodproductcmsimage.service.impl;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.product.prodproductcmsimage.dao.ProdProductCmsImageDao;
import com.gongsibao.module.product.prodproductcmsimage.entity.ProdProductCmsImage;
import com.gongsibao.module.product.prodproductcmsimage.service.ProdProductCmsImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("prodProductCmsImageService")
public class ProdProductCmsImageServiceImpl implements ProdProductCmsImageService {
    @Autowired
    private ProdProductCmsImageDao prodProductCmsImageDao;

    @Override
    public ProdProductCmsImage findById(Integer pkid) {
        return prodProductCmsImageDao.findById(pkid);
    }

    @Override
    public int update(ProdProductCmsImage prodProductCmsImage) {
        return prodProductCmsImageDao.update(prodProductCmsImage);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductCmsImageDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProductCmsImage prodProductCmsImage) {
        return prodProductCmsImageDao.insert(prodProductCmsImage);
    }

    @Override
    public List<ProdProductCmsImage> findByIds(List<Integer> pkidList) {
        return prodProductCmsImageDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProductCmsImage> findMapByIds(List<Integer> pkidList) {
        List<ProdProductCmsImage> list = findByIds(pkidList);
        Map<Integer, ProdProductCmsImage> map = new HashMap<>();
        for(ProdProductCmsImage prodProductCmsImage : list){
            map.put(prodProductCmsImage.getPkid(), prodProductCmsImage);
        }
        return map;
    }

    @Override
    public Pager<ProdProductCmsImage> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductCmsImageDao.countByProperties(map);
        Pager<ProdProductCmsImage> pager = new Pager<>(totalRows, page);
        List<ProdProductCmsImage> prodProductCmsImageList = prodProductCmsImageDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductCmsImageList);
        return pager;
    }

}