package com.gongsibao.quartz.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by luqingrun on 16/5/4.
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args)
    {
        System.out.println("Main start.");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/*.xml");
        //如果配置文件中将startQuertz bean的lazy-init设置为false 则不用实例化
        //context.getBean("startQuertz");
        System.out.print("Main end..");

    }

}
