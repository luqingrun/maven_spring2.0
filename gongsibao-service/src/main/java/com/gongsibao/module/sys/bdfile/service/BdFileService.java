package com.gongsibao.module.sys.bdfile.service;

import com.gongsibao.module.sys.bdfile.entity.BdFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BdFileService {

    BdFile findById(Integer pkid);

    int delete(Integer pkid);

    List<BdFile> findByIds(List<Integer> pkidList);

    Map<Integer, BdFile> findMapByIds(List<Integer> pkidList);

    /**
     * 单条插入
     * @param bdFile
     * @return
     */
    Integer insert(BdFile bdFile);

    /**
     * 批量插入
     * @param bdFiles
     * @return
     */
    int[] insertBatch(List<BdFile> bdFiles);

    /**
     * 删除 by  tab_name and form_id
     *
     * @param tabName
     * @param formId
     * @return
     */
    public Integer deleteByFormId(Integer formId, String tabName);

    /**
     * 查询 by  tab_name and form_id
     *
     * @param formId
     * @param tabName
     * @return
     */
    public List<BdFile> getListByFormId(Integer formId, String tabName);

    /**
     * 查询 by  tab_name and form_ids
     *
     * @param formIds
     * @param tabName
     * @return
     */
    public Map<Integer, List<BdFile>> getMapByFormIds(Collection<Integer> formIds, String tabName);
}