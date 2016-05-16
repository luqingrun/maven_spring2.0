package com.gongsibao.module.product.prodproductcms.service.impl;

import com.aliyun.oss.common.utils.DateUtil;
import com.gongsibao.common.util.NumberUtils;
import com.gongsibao.common.util.page.Pager;
import com.gongsibao.common.util.page.ResponseData;
import com.gongsibao.module.product.prodproduct.entity.ProdProduct;
import com.gongsibao.module.product.prodproduct.service.ProdProductService;
import com.gongsibao.module.product.prodproductcms.dao.ProdProductCmsDao;
import com.gongsibao.module.product.prodproductcms.entity.ProdProductCms;
import com.gongsibao.module.product.prodproductcms.service.ProdProductCmsService;
import com.gongsibao.module.product.prodproductcmsimage.dao.ProdProductCmsImageDao;
import com.gongsibao.module.product.prodproductcmsimage.entity.ProdProductCmsImage;
import com.gongsibao.module.product.prodproductcmsrelated.dao.ProdProductCmsRelatedDao;
import com.gongsibao.module.product.prodproductcmsrelated.entity.ProdProductCmsRelated;
import com.gongsibao.module.sys.bdfile.dao.BdFileDao;
import com.gongsibao.module.sys.bdfile.entity.BdFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;


@Service("prodProductCmsService")
public class ProdProductCmsServiceImpl implements ProdProductCmsService {
    @Autowired
    private ProdProductCmsDao prodProductCmsDao;
    @Autowired
    private ProdProductService prodProductService;
    @Autowired
    private BdFileDao bdFileDao;
    @Autowired
    private ProdProductCmsImageDao prodProductCmsImageDao;
    @Autowired
    private ProdProductCmsRelatedDao prodProductCmsRelatedDao;

    @Override
    public ProdProductCms findById(Integer pkid) {
        return prodProductCmsDao.findById(pkid);
    }

    @Override
    public int update(ProdProductCms prodProductCms) {
        return prodProductCmsDao.update(prodProductCms);
    }

    @Override
    public int delete(Integer pkid) {
        return prodProductCmsDao.delete(pkid);
    }

    @Override
    public Integer insert(ProdProductCms prodProductCms) {
        return prodProductCmsDao.insert(prodProductCms);
    }

    @Override
    public List<ProdProductCms> findByIds(List<Integer> pkidList) {
        return prodProductCmsDao.findByIds(pkidList);
    }

    @Override
    public Map<Integer, ProdProductCms> findMapByIds(List<Integer> pkidList) {
        List<ProdProductCms> list = findByIds(pkidList);
        Map<Integer, ProdProductCms> map = new HashMap<>();
        for (ProdProductCms prodProductCms : list) {
            map.put(prodProductCms.getPkid(), prodProductCms);
        }
        return map;
    }

    @Override
    public Pager<ProdProductCms> pageByProperties(Map<String, Object> map, int page) {
        int totalRows = prodProductCmsDao.countByProperties(map);
        Pager<ProdProductCms> pager = new Pager<>(totalRows, page);
        List<ProdProductCms> prodProductCmsList = prodProductCmsDao.findByProperties(map, pager.getStartRow(), pager.getPageSize());
        pager.setList(prodProductCmsList);
        return pager;
    }

    @Override
    public ProdProductCms findByProdId(Integer propkid) {

        ProdProductCms resdata = prodProductCmsDao.findByProId(propkid);

        if (resdata != null) {
            ProdProduct p = prodProductService.findById(resdata.getProductId());
            resdata.setProdIdStr(p.getPkidStr());
            resdata.setSort(p.getSort());
            //列表图(prod_product_cms1 产品列表显示图   prod_product_cms2 产品徽章图   prod_product_cms3 产品轮播图)
            List<BdFile> file = bdFileDao.getListByFormId(p.getPkid(), "prod_product_cms1");
            //徽章图
            List<BdFile> badgeimgfile = bdFileDao.getListByFormId(p.getPkid(), "prod_product_cms2");
            if (!file.isEmpty()) {
                resdata.setImgUrl(file.get(0).getUrl());
            }
            if (!badgeimgfile.isEmpty()) {
                resdata.setBadgeImgUrl(badgeimgfile.get(0).getUrl());
            }
            List<ProdProductCmsImage> cmsImageList = prodProductCmsImageDao.findByProId(p.getPkid());
            for (ProdProductCmsImage itemimg : cmsImageList) {
                BdFile f = bdFileDao.findById(itemimg.getFileId());
                if (f != null) {
                    itemimg.setImgurl(f.getUrl());
                }
            }
            resdata.setProductCmsImageList(cmsImageList);
            resdata.setProdProductCmsRelatedList(prodProductCmsRelatedDao.findByProId(p.getPkid()));
        }

        return resdata;
    }

    @Override
    public ResponseData addorupdateprodcms(ProdProductCms prodProductCms) {
        ResponseData res = new ResponseData();
        Integer resid = 0;

        ProdProductCms resdata = prodProductCmsDao.findByProId(prodProductCms.getProductId());
        if ((prodProductCms.getPkid() == null || prodProductCms.getPkid().equals(0)) && resdata != null) {
            res.setMsg("该产品已经存在扩展信息，禁止新建");
            res.setCode(400);
            return res;
        }

        //修改时，不能修改别的产品的扩展信息
        if(prodProductCms.getPkid() != null && !prodProductCms.getPkid().equals(0)){
            ProdProductCms procmsdata = prodProductCmsDao.findById(prodProductCms.getPkid());
            if (procmsdata.getProductId().compareTo(prodProductCms.getProductId()) != 0) {
                res.setMsg("该产品扩展信息不属于该产品，禁止操作");
                res.setCode(400);
                return res;
            }
        }

        if (prodProductCms.getPkid() == null || prodProductCms.getPkid().equals(0)) {
            resid = prodProductCmsDao.insert(prodProductCms);
        } else {
            prodProductCmsDao.update(prodProductCms);
            resid = prodProductCms.getPkid();
        }
        ProdProduct pobj = prodProductService.findById(prodProductCms.getProductId());
        //设置产品列表图片
        List<BdFile> file = bdFileDao.getListByFormId(prodProductCms.getProductId(), "prod_product_cms1");
        if (!file.isEmpty()) {
            BdFile f = file.get(0);
            f.setUrl(prodProductCms.getImgUrl());
            bdFileDao.updateurl(f);
        } else {
            BdFile f = new BdFile();
            f.setUrl(prodProductCms.getImgUrl());
            f.setAddUserId(prodProductCms.getAddUserId());
            f.setTabName("prod_product_cms1");
            f.setFormId(prodProductCms.getProductId());
            bdFileDao.insert(f);
        }

        //设置产品徽章图片
        List<BdFile> badgefile = bdFileDao.getListByFormId(prodProductCms.getProductId(), "prod_product_cms2");
        if (!badgefile.isEmpty()) {
            BdFile f = badgefile.get(0);
            f.setUrl(prodProductCms.getBadgeImgUrl());
            bdFileDao.updateurl(f);
        } else {
            BdFile f = new BdFile();
            f.setUrl(prodProductCms.getBadgeImgUrl());
            f.setAddUserId(prodProductCms.getAddUserId());
            f.setTabName("prod_product_cms2");
            f.setFormId(prodProductCms.getProductId());
            bdFileDao.insert(f);
        }

        //设置轮播图(prod_product_cms1 产品列表显示图   prod_product_cms2 产品徽章图   prod_product_cms3 产品轮播图)
        //先删除所有的该产品的轮播图
        prodProductCmsImageDao.deletebyprodid(prodProductCms.getProductId());
        List<ProdProductCmsImage> CmsImageList = new ArrayList<>();
        for (ProdProductCmsImage itemimg : prodProductCms.getProductCmsImageList()) {
            Integer imgsort = prodProductCms.getProductCmsImageList().indexOf(itemimg);
            itemimg.setSort(Double.valueOf(imgsort.toString()));
            itemimg.setAddUserId(prodProductCms.getAddUserId());
            itemimg.setProductId(prodProductCms.getProductId());
            itemimg.setRemark("");
            //图片
            BdFile f = new BdFile();
            f.setUrl(itemimg.getImgurl());
            f.setTabName("prod_product_cms3");
            f.setFormId(0);
            f.setAddUserId(prodProductCms.getAddUserId());
            Integer fid = bdFileDao.insert(f);
            itemimg.setFileId(fid);
            CmsImageList.add(itemimg);
        }
        //在新建本次提交的数据
        prodProductCmsImageDao.insertBatch(CmsImageList);

        //设置相关产品（推荐产品）
        prodProductCmsRelatedDao.deletebyprodid(prodProductCms.getProductId());
        List<ProdProductCmsRelated> cmsRelatedList = new ArrayList<>();
        for (ProdProductCmsRelated itemRelated : prodProductCms.getProdProductCmsRelatedList()) {
            itemRelated.setSort(0d);
            ProdProduct precommobj = prodProductService.findById(itemRelated.getRecommendProductId());
            itemRelated.setAddUserId(prodProductCms.getAddUserId());
            itemRelated.setProductId(prodProductCms.getProductId());
            itemRelated.setRecommendProductId(itemRelated.getRecommendProductId());
            itemRelated.setProductName(pobj.getName());
            itemRelated.setRecommendProductName(precommobj.getName());
            itemRelated.setRecommendProductId(itemRelated.getRecommendProductId());
            itemRelated.setRemark("");
            cmsRelatedList.add(itemRelated);
        }
        prodProductCmsRelatedDao.insertBatch(cmsRelatedList);

        res.setMsg("操作成功");
        return res;
    }
}