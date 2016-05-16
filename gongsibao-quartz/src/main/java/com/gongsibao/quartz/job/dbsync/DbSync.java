package com.gongsibao.quartz.job.dbsync;

/**
 * Created by cxq on 16/5/12.
 */
public interface DbSync {
    void CreatedSycn();
    void UpdatedSycn();
    void DeletedSync();
}
