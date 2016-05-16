package com.gongsibao.quartz.job;

import org.apache.log4j.Logger;

/**
 * Created by luqingrun on 16/5/4.
 */
public class TestJob {

    private static Logger log = Logger.getLogger(TestJob.class);

    public void execute() throws InterruptedException {
        log.info("run start");
        Thread.sleep(1000*10L);
        log.info("run end");
    }
}
