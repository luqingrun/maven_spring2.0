package com.gongsibao.module.sys.bddict.service;

import com.gongsibao.common.util.page.Pager;
import com.gongsibao.module.sys.bddict.entity.BdDict;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BdDictService {

    BdDict findById(Integer pkid);

    int update(BdDict bdDict);

    int delete(Integer pkid);

    Integer insert(BdDict bdDict);

    List<BdDict> findByIds(List<Integer> pkidList);

    Map<Integer, BdDict> findMapByIds(List<Integer> pkidList);

    Pager<BdDict> pageByProperties(Map<String, Object> properties, int page);

    /**
     * 查询某type下全部的字典数据
     * @param type
     * @return
     */
    List<BdDict> findByType(Integer type);

    /**
     * 查询某type下的字典数据(以tree结构展示)
     * @param type
     * @return
     */
    List<BdDict> findTreeByType(Integer type);

    /**
     * 查询某pkid下的全部子字典数据
     * @param pkid
     * @return
     */
    List<BdDict> findChildrenByParentId(Integer pkid);

    /**
     * 查询某pkid下的全部子字典数据(以tree结构展示)
     * @param pkid
     * @return
     */
    List<BdDict> findTreeChildrenByParentId(Integer pkid);

    /**
     * 查询某type 某pkid下的字典数据(以tree结构展示)
     * @param type
     * @param pkid
     * @return
     */
    BdDict findTreeByParentId(int type, Integer pkid);

    /**
     * 查询所有字典记录
     * @return
     */
    List<BdDict> findAll();

    /**
     * 查询某type下第一层的字典数据
     * @param type
     * @return
     */
    List<BdDict> findOneLevelByType(Integer type);

    /**
     * 查询某type下第一层的字典数据MAP形式
     * @param type
     * @return
     */
    Map<Integer, BdDict> findOneLevelMapByType(Integer type);

    /**
     * 查询某type下 以当前为叶子向上查询,查询当前叶子所在的集合以及向上每一层的集合
     * @param type
     * @param pkid
     * @return
     */
    List<Map<String, Object>> queryParents(int type, Integer pkid);

    /**
     * 查询某type下 以叶子ID集合向上查询,一直查询到根
     * @param type
     * @param leafIds
     * @return
     */
    List<BdDict> queryParentsByLeafIds(int type, Collection<Integer> leafIds);

    /**
     * 查询某type下 ids集合对应的名称字符串
     * @param type
     * @param ids
     * @return
     */
    Map<Integer, String> queryDictNames(int type, Collection<Integer> ids);

    /**
     * 查询某type下 某id对应的名称字符串
     * @param type
     * @param id
     * @return
     */
    String queryDictName(int type, Integer id);

    /**
     * 查询某type下 包含name的名称集合
     * @param type
     * @param name
     * @return
     */
    List<String> queryNames(int type, String name);


    void insertBatch(final List<BdDict> itemList);

    BdDict getMaxPkidByType(int type);

}