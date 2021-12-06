package com.liez.controller;

import com.alibaba.fastjson.JSONObject;
import com.liez.dao.TrainDao;
import com.liez.job.SendEmailJob;
import com.liez.service.DynamicScheduleService;
import com.liez.utils.R;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Autowired
    private DynamicScheduleService scheduleService;


    /**
     * 动态的添加定时任务
     *
     * @param cronJSON {cron，jobName,jobGroupName,triggerName,triggerGroupName}
     * @return
     * @throws SchedulerException
     */
    @PostMapping("insertJob")
    public R insertJob(@RequestBody JSONObject cronJSON) throws SchedulerException {
        try {
            scheduleService.insertJob(cronJSON);
            return R.oK();
        } catch (Exception e) {
            e.toString();
            return R.error();
        }

    }

    /**
     * 动态添加不同定时任务的执行时间
     *
     * @param params
     * @return
     */
    @PostMapping("insertDifferentServiceJob")
    public R insertDifferentServiceJob(@RequestBody JSONObject params) {
        try {
            scheduleService.insertDifferentServiceJob(params);
            return R.oK();
        } catch (Exception e) {
            e.toString();
            return R.error();
        }
    }

    /**
     * 定时扫描定时历史条数表，检测到由新日志插入时根据主键id扫描定时详情表(五分钟扫描一次)
     *
     * @return
     */
//    @Scheduled(cron = "0/10 * * * * ? ")
    public R scanTableLoadScheduled() {
        try {
            scheduleService.scanTableLoadScheduled();
            return R.oK();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 定时扫描定时历史条数表，检测到由新日志插入时根据主键id扫描定时详情表(五分钟扫描一次)
     *
     * @return
     */
    @Scheduled(cron = "0/10 * * * * ? ")
    public R scanTableLoadScheduled2() {
        try {
            scheduleService.scanTableLoadScheduled2();
            return R.oK();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }


    @PostMapping("saveData")
    public R saveData() {
        trainDao.insertData(UUID.randomUUID().toString(), true, new Date());
        return R.oK();
    }

    @PostMapping("selectData")
    public R selectData() {
        List<Map<String, Object>> mapList = trainDao.selectData();
        return R.oK().data("data", mapList);
    }
}
