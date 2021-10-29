package com.liez.controller;

import com.alibaba.fastjson.JSONObject;
import com.liez.dao.TrainDao;
import com.liez.job.SendEmailJob;
import com.liez.utils.R;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("system/dynamicSchedule")
@RestController
public class DynamicScheduleController {

    @Autowired
    private TrainDao trainDao;


    /**
     * 动态的添加定时任务
     *
     * @param cronJSON {cron，jobName,jobGroupName,triggerName,triggerGroupName}
     * @return
     * @throws SchedulerException
     */
    @PostMapping("insertJob")
    public R insertJob(@RequestBody JSONObject cronJSON) throws SchedulerException {
        //1.创建调度器
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        String triggerName = cronJSON.getString("triggerName");
        String triggerGroupName = cronJSON.getString("triggerGroupName");
        Scheduler scheduler = schedulerFactory.getScheduler();
        //2.创建job实例并于要执行类绑定
        JobDetail jobDetail = JobBuilder.newJob(SendEmailJob.class).withIdentity(cronJSON.getString("jobName"), cronJSON.getString("jobGroupName")).build();
        //3.构建tigger实例。设置执行频率
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
                .usingJobData("cron", cronJSON.getString("cron"))
                .startNow() //立即执行
                .withSchedule(CronScheduleBuilder.cronSchedule(cronJSON.getString("cron")))
                .build();
        //4.执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("=======================scheduler start=====================");
        scheduler.start();
        return R.oK();
    }

    @PostMapping("saveData")
    public R saveData(){
        trainDao.insertData(UUID.randomUUID().toString(),true,new Date());
        return R.oK();
    }
    @PostMapping("selectData")
    public R selectData(){
        List<Map<String,Object>> mapList = trainDao.selectData();
        return R.oK().data("data",mapList);
    }
}
