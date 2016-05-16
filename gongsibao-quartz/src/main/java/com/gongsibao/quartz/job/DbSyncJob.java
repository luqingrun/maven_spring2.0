package com.gongsibao.quartz.job;

import com.gongsibao.quartz.job.dbsync.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxq on 16/5/12.
 */
public class DbSyncJob {
    private static Logger log = Logger.getLogger(DbSyncJob.class);

    public void execute() throws InterruptedException{
        log.info("db sync run start.");

        List<DbSync> syncList = new ArrayList<>();
        syncList.add(new CitySync());
        syncList.add(new ProductTypeSync());
        syncList.add(new ProductSync());
        syncList.add(new PriceSync());
        syncList.add(new OrderSync());
        syncList.add(new PaySync());
        syncList.add(new UserSync());
        syncList.add(new AccountSync());

        syncList.forEach(m -> m.CreatedSycn());
        syncList.forEach(m -> m.UpdatedSycn());
        syncList.forEach(m -> m.DeletedSync());

        log.info("db sync run end.");
    }
}
