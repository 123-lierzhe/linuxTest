package com.liez.job;

import com.liez.dao.TrainDao;
import com.liez.utils.SendMsgUtil;
import com.liez.utils.SpringContextJobUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class SendEmailJob implements Job {

    private static boolean flag = true;

    @Autowired
    private TrainDao trainDao;
    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String format = sdf.format(new Date());
            trainDao = (TrainDao) SpringContextJobUtil.getBean("trainDao");
            trainDao.insertData(format, true, new Date());
        }catch (Exception e){
            if(flag) {
                sendMsgUtil = (SendMsgUtil) SpringContextJobUtil.getBean("sendMsgUtil");
                sendMsgUtil.sendMessage("定时插入数据库error发送邮件开始", "定时插入数据库error发送邮件结束", "datasource is error", "datasource is error!!!!!!!!!!!!!!","1643057053@qq.com","15128202550@163.com");
                flag = false;
                e.printStackTrace();
            }
            log.error(e.toString());
        }

    }
}
