package com.liez.service;

import com.alibaba.fastjson.JSONObject;
import org.quartz.SchedulerException;

public interface DynamicScheduleService {

    void insertJob(JSONObject cronJSON) throws SchedulerException;

    void insertDifferentServiceJob(JSONObject params) throws SchedulerException, ClassNotFoundException;

    void scanTableLoadScheduled() throws SchedulerException, ClassNotFoundException;

    void scanTableLoadScheduled2() throws SchedulerException, ClassNotFoundException;

//    void scanTableLoadScheduled2();
}
