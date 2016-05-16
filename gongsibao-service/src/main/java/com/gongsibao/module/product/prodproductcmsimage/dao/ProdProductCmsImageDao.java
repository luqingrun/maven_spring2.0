package com.gongsibao.module.product.prodproductcmsimage.dao;

import com.gongsibao.common.db.BaseDao;
import com.gongsibao.common.util.StringUtils;
import com.gongsibao.common.util.json.JsonUtils;
import com.gongsibao.module.product.prodproductcmsimage.entity.ProdProductCmsImage;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("prodProductCmsImageDao")
public class ProdProductCmsImageDao extends BaseDao<ProdProductCmsImage> {

    public static String INSERT_COLUMNS = " `product_id`, `file_id`, `tag`, `add_user_id`, `sort`, `remark`, `add_time` ";


    public static String ALL_COLUMNS = "`pkid`, " + INSERT_COLUMNS;

    @Override
    public Integer insert(ProdProductCmsImage prodProductCmsImage) {
        insertObject(prodProductCmsImage);
        return getLastInsertId();
    }

    @Override
    protected void insertObject(ProdProductCmsImage prodProductCmsImage) {
        getJdbcTemplate().update("insert into `prod_product_cms_image`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ? )",
                prodProductCmsImage.getProductId(),
                prodProductCmsImage.getFileId(),
                prodProductCmsImage.getTag(),
                prodProductCmsImage.getAddUserId(),
                prodProductCmsImage.getSort(),
                prodProductCmsImage.getRemark(),
                prodProductCmsImage.getAddTime()
        );
    }

    @Override
    public int update(ProdProductCmsImage prodProductCmsImage) {
        //TODO,需要自己决定如何实现
        throw new java.lang.UnsupportedOperationException();
        //String sql = "update `prod_product_cms_image` set pkid = pkid, `product_id` = :productId, `file_id` = :fileId, `tag` = :tag, `add_user_id` = :addUserId, `sort` = :sort, `remark` = :remark, `add_time` = :addTime where pkid = :pkid";
        //return getNamedParameterJdbcTemplate().update(sql, JsonUtils.jsonToObject(JsonUtils.objectToJson(prodProductCmsImage),Map.class));
    }

    @Override
    public int delete(Integer id) {
        //TODO,需要自己决定如何实现
        //return getJdbcTemplate().update("delete from `prod_product_cms_image` where pkid = "+id);
        throw new java.lang.UnsupportedOperationException();
    }

    @Override
    public ProdProductCmsImage findById(Integer pkid) {
        return getJdbcTemplate().queryForObject("select " + ALL_COLUMNS + " from `prod_product_cms_image` where pkid = " + pkid, getRowMapper());
    }

    @Override
    public List<ProdProductCmsImage> findByIds(List<Integer> pkidList) {
        Map<String, Object> map = new HashMap<>();
        map.put("pkidList", pkidList);
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_image` where pkid IN (:pkidList) ", map, getRowMapper());
    }


    @Override
    public RowMapper<ProdProductCmsImage> getRowMapper() {
        RowMapper<ProdProductCmsImage> rowMapper = (rs, i) -> {
            ProdProductCmsImage prodProductCmsImage = new ProdProductCmsImage();
            prodProductCmsImage.setPkid(rs.getInt("pkid"));
            prodProductCmsImage.setProductId(rs.getInt("product_id"));
            prodProductCmsImage.setFileId(rs.getInt("file_id"));
            prodProductCmsImage.setTag(rs.getString("tag"));
            prodProductCmsImage.setAddUserId(rs.getInt("add_user_id"));
            prodProductCmsImage.setSort(rs.getDouble("sort"));
            prodProductCmsImage.setRemark(rs.getString("remark"));
            prodProductCmsImage.setAddTime(rs.getTimestamp("add_time"));
            return prodProductCmsImage;
        };
        return rowMapper;
    }

    @Override
    public List<ProdProductCmsImage> findByProperties(Map<String, Object> properties, int start, int pageSize) {
        StringBuffer sql = new StringBuffer("select " + ALL_COLUMNS + " from `prod_product_cms_image` where 1=1 ");
        buildSQL(properties, sql);
        sql.append(" limit ").append(start).append(", ").append(pageSize);
        return getNamedParameterJdbcTemplate().query(sql.toString(), properties, getRowMapper());
    }

    @Override
    public int countByProperties(Map<String, Object> properties) {
        StringBuffer sql = new StringBuffer("select count(*) from `prod_product_cms_image` where 1=1 ");
        buildSQL(properties, sql);
        return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), properties, Integer.class);
    }

    /*根据产品id获取，该产品所有的轮播图*/
    public List<ProdProductCmsImage> findByProId(Integer propkid) {
        return getNamedParameterJdbcTemplate().query("select " + ALL_COLUMNS + " from `prod_product_cms_image` where product_id = " + propkid, getRowMapper());
    }

    /*根据产品id删除该产品所有的轮播图*/
    public int deletebyprodid(Integer prodid) {
        //删除该轮播图对应的file信息
        getJdbcTemplate().update("delete from `bd_file` where pkid in (select file_id from `prod_product_cms_image` where product_id = " + prodid + ")");
        return getJdbcTemplate().update("delete from `prod_product_cms_image` where product_id = " + prodid);
    }

    /*批量添加*/
    public void insertBatch(final List<ProdProductCmsImage> itemList) {
        String sql = "insert into `prod_product_cms_image`(" + INSERT_COLUMNS + ") values (?, ?, ?, ?, ?, ?, ? )";
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, itemList.get(i).getProductId());
                ps.setObject(2, itemList.get(i).getFileId());
                ps.setObject(3, itemList.get(i).getTag());
                ps.setObject(4, itemList.get(i).getAddUserId());
                ps.setObject(5, itemList.get(i).getSort());
                ps.setObject(6, itemList.get(i).getRemark());
                ps.setObject(7, itemList.get(i).getAddTime());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });
    }
}