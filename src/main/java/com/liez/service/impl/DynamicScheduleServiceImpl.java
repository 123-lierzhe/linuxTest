package com.liez.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liez.dao.ScheduledDao;
import com.liez.job.SendEmailJob;
import com.liez.service.DynamicScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class DynamicScheduleServiceImpl implements DynamicScheduleService {

    @Autowired
    private ScheduledDao scheduledDao;

    @Override
    public void insertJob(JSONObject cronJSON) throws SchedulerException {
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
    }

    @Override
    public void insertDifferentServiceJob(JSONObject params) throws SchedulerException, ClassNotFoundException {
        //获得调度器
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        //通过反射获得类的class文件
        Class<?> className = Class.forName(params.getString("className"));
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        //创建job实例来绑定需要执行的类
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) className).withIdentity(params.getString("jobName"), params.getString("jobGroupName")).build();
        //构建tigger对应用于提示执行定时
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(params.getString("triggerName"), params.getString("triggerGroupName"))
                .usingJobData("data", params.getString("data"))
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(params.getString("cron")))
                .build();
        //执行
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        System.out.println("=======================scheduler start=====================");

    }

    @Override
    public void scanTableLoadScheduled() throws SchedulerException, ClassNotFoundException {
        //先判断程序是否是重启，如果是重启
        int historRows = scheduledDao.getJobHistoryRows("rowsId");
        int currentRows = scheduledDao.getJobCurrentRows();
        log.info("未监测到新的定时任务");
        if (historRows != currentRows) {
            log.info("监测到新的定时任务，开始导入Quarzt");
            int betterRows = currentRows - historRows;
            List<String> betterRowsDetailId = scheduledDao.getBetterRowsDetailId(betterRows);
            //通过id查询对应的定时详细信息
            List<Map<String, Object>> detailList = scheduledDao.getDetailById(betterRowsDetailId);
            if (detailList != null && detailList.size() > 0) {
                for (Map<String, Object> dataMap : detailList) {
                    //状态为0，停止该定时任务
                    if (!(Boolean) dataMap.get("state")) {

                    }
                    //状态为1
                    if ((Boolean) dataMap.get("state")) {
                        //若改变类型为A则则增加定时任务
                        if ("A".equals(dataMap.get("changeType"))) {
                            log.info("添加一个定时任务，jobName为：{}",dataMap.get("beanName")+dataMap.get("id").toString());
                            insertScheduled(dataMap);
                            scheduledDao.updateRows(currentRows);
                            log.info("添加一个定时任务，jobName为：{}结束",dataMap.get("beanName")+dataMap.get("id").toString());
                        }
                        //若改变类型为M则更新对应的定时任务
                        if ("M".equals(dataMap.get("changeType"))) {
                        }
                        //若改变类型为S则暂停对应的定时任务
                        if ("S".equals(dataMap.get("changeType"))) {
                            String jobNameParam = dataMap.get("beanName")+dataMap.get("id").toString();
                            log.info("暂停一个定时任务，jobName为：{}",jobNameParam);
                            stopScheduled(jobNameParam);
                            scheduledDao.updateRows(currentRows);
                            log.info("暂停一个定时任务，jobName为：{}结束",jobNameParam);
                        }
                        //若改变类型为B则结束暂停对应的定时任务
                        if ("B".equals(dataMap.get("changeType"))) {
                            String jobNameParam = dataMap.get("beanName")+dataMap.get("id").toString();
                            log.info("取消暂停一个定时任务，jobName为：{}",jobNameParam);
                            notStopDcheduled(jobNameParam);
                            scheduledDao.updateRows(currentRows);
                            log.info("取消暂停一个定时任务，jobName为：{}结束",jobNameParam);
                        }
                    }
                }
            }
        }
    }

    //上边方法简化版，直接查详情表，不存在暂停定时任务，只存在开启和关闭定时任务
    @Override
    public void scanTableLoadScheduled2() throws SchedulerException, ClassNotFoundException {
        //获得所有的jobName
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        //数据库中存在的定时
        List<Map<String,Object>> scheduledList = scheduledDao.getAllUseingScheduled();
        Boolean dataSourceHasScheduledflag = false;
        //停止数据库中不存在的定时，启动之前没启动的定时
        for (JobKey jobKey : jobKeys) {
            String jobName = jobKey.getName();
            Iterator<Map<String, Object>> iterator = scheduledList.iterator();
            while(iterator.hasNext()){
                Map<String, Object> next = iterator.next();
                String dataJobName = next.get("beanName") + "" + next.get("id");
                //数据库中存在该定时,
                if(jobName.equals(dataJobName)){
                    dataSourceHasScheduledflag = true;
                    iterator.remove();
                }
            }
            //如果数据库中不存在，停止该定时
            if(!dataSourceHasScheduledflag){
                deleteScheduled(jobKey);
            }

            dataSourceHasScheduledflag = false;
        }

        //启动数据库中新加的定时
        for (Map<String, Object> scheduledMap : scheduledList) {
            insertScheduled(scheduledMap);

        }
    }

    /**
     * 暂停定时任务
     * @param jobKeyParam
     * @throws SchedulerException
     */
    private void stopScheduled(String jobKeyParam) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobKeyParam);
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.pauseJob(jobKey);
    }

    /**
     * 停止暂停定时任务
     * @param jobKeyParam
     * @throws SchedulerException
     */
    private void notStopDcheduled(String jobKeyParam) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobKeyParam);
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.resumeJob(jobKey);
    }


    /**
     * 新增定时任务
     * @param requestMap
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    private void insertScheduled(Map<String, Object> requestMap) throws ClassNotFoundException, SchedulerException {
        log.info("开始添加一个定时任务");
        String classPathName = requestMap.get("packageName") + "." + requestMap.get("beanName");
        String jobName = requestMap.get("beanName") + "" + requestMap.get("id");
        String triggerName = requestMap.get("beanName") + "" + requestMap.get("id");
        String jobGroupName = requestMap.get("packageName").toString();
        String triggerGroupName = requestMap.get("packageName").toString();
        String cron = requestMap.get("cron").toString();
        //通过反射获得对应的jobclass类
        Class<?> jobClass = Class.forName(classPathName);
        //获得调度器
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        //创建对应的实例绑定对应的job类
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClass).withIdentity(jobName, jobGroupName).build();
        //创建trigger实例，用于匹配cron表达式
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        //执行
        scheduler.scheduleJob(jobDetail,trigger);
        log.info("=======================scheduler start=====================");
        scheduler.start();
        log.info("添加一个定时任务成功");

    }
    /**
     * 删除定时任务
     * @param
     * @throws ClassNotFoundException
     * @throws SchedulerException
     */
    private void deleteScheduled(JobKey jobKey) throws ClassNotFoundException, SchedulerException {
        log.info("开始 移除定时任务");
        //获得调度器
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        String jobName = jobKey.getName();
        String jobNameGroup = jobKey.getGroup();

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName,jobNameGroup);
        //停止触发器
        scheduler.resumeTrigger(triggerKey);
        //移除触发器
        scheduler.unscheduleJob(triggerKey);
        //移除任务
        scheduler.deleteJob(jobKey);
        log.info("移除定时任务结束");

    }

}
