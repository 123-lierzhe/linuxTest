package com.liez.myConfig;

import com.liez.job.SendEmailJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 调度配置类
 */
//@Configuration
public class MyQuartz {
    public static void main(String[] args) throws SchedulerException, InterruptedException {

        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);

        //1.创建调度器
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        //2.创建job实例并于要执行类绑定
        JobDetail jobDetail = JobBuilder.newJob(SendEmailJob.class)
                .usingJobData("dateTest1","这是jobDetail测试的数据")
                .withIdentity("job1", "group1")
                .build();

        //3.构建tigger实例。设置执行频率(基于精准指定间隔进行任务调度)
        /*Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .startNow() //立即执行
                .usingJobData("dataTigger","这是测试tigger的数据")
                .startAt(startDate) //程序运行5s后开始执行Job
                .endAt(endDate) //执行Job 5s后结束执行
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2) //每隔两秒执行一次
                        .repeatForever())
                .build();//一直执行*/

        //3.构建tigger实例。设置执行频率(基于日历（cron）进行任务调度)
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("tigger2", "tiggerGroup2")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("11,23,45,59 * * * * ? "))
                .build();


        //4.执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("=======================scheduler start=====================");
        scheduler.start();

        //睡眠(一分钟后该定时任务停止结束)
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();
        System.out.println("=======================scheduler shutdown=====================");
    }
}
