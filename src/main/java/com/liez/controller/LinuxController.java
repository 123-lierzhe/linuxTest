package com.liez.controller;

import com.alibaba.fastjson.JSONObject;
import com.liez.service.AliyunService;
import com.liez.service.TrainService;
import com.liez.service.WhetherService;
import com.liez.utils.GetIpUtil;
import com.liez.utils.HttpClientUtils;
import com.liez.utils.R;
import com.liez.utils.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("system")
public class LinuxController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AliyunService aliyunService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private SendMsgUtil sendMsgUtil;
    @Autowired
    private WhetherService whetherService;

    //测试
    @PostMapping("sayHello")
    public R sayHello(@RequestParam(value = "content", required = true) String content) {
        log.info(":{}is foolish,ha ha ha",content);
        return R.oK();
    }

    //获得请求的IP
    @GetMapping("getIp")
    public R sayHello(HttpServletRequest request) {
        String ip = GetIpUtil.getIpAddr(request);
        log.info("当前IP为：{}的用户正在发起请求",ip);
        return R.oK().data("您的IP",ip);
    }

    //发送邮件1
//    @Scheduled(cron = "0 18 18 * * ?")
    public void sendEmail() {
        log.info("提醒定时填写jire的定时任务执行开始,时间是:{}", sdf.format(new Date()));
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("定时打卡提醒====" + sdf.format(new Date()));
        simpleMailMessage.setText("请填写jire");
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setFrom("1643057053@qq.com");
        simpleMailMessage.setTo("15128202550@163.com");
        javaMailSender.send(simpleMailMessage);
        log.info("提醒定时填写jire的定时任务执行结束,时间是:{}", sdf.format(new Date()));
    }

    //发送邮件2
//    @Scheduled(cron = "0 30 18 * * ?")
    public void sendEmail2() {
        log.info("提醒定时打卡的邮件执行开始,时间是:{}", sdf.format(new Date()));
        long startTime = System.currentTimeMillis();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("定时打卡提醒====" + sdf.format(new Date()));
        simpleMailMessage.setText("请打卡");
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setFrom("1643057053@qq.com");
        simpleMailMessage.setTo("15128202550@163.com");
        javaMailSender.send(simpleMailMessage);
        long endTime = System.currentTimeMillis();
        log.info("提醒定时打卡的邮件执行结束,时间是:{},耗时:{}秒", sdf.format(new Date()), (endTime - startTime) / 1000);
    }

    //定时发送短信
//    @Scheduled(cron = "0 40 18 * * ?")
    @PostMapping("kk")
    public void sendMessage() {
        try {
//            ArrayList<Object> objects = new ArrayList<>();
//            log.info("LinuxController-提醒定时打卡的短信执行开始---------------------------------,时间是:{}", sdf.format(new Date()));
//            long startTime = System.currentTimeMillis();
//            String phone = "15128202550";
//            String code = String.valueOf(new Random().nextInt(666666));
//            aliyunService.sendMessage(phone, code);
            Map<String, Object> lastTrainCount = trainService.getLastTrainCount();
            System.out.println(lastTrainCount.toString());
//            long endTime = System.currentTimeMillis();
//            log.info("LinuxController-提醒定时打卡的邮件执行结束,时间是:{},耗时:{}", sdf.format(new Date()), (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("LinuxController-提醒定时打卡的邮件执行失败,时间是:{}", sdf.format(new Date()));
        }
    }

    //定时发送短信
    @Scheduled(cron = "0 50 8 * * ?")
    public void sendMessage2() {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            log.info("LinuxController-提醒定时打卡的短信执行开始---------------------------------,时间是:{}", sdf.format(new Date()));
            long startTime = System.currentTimeMillis();
            String phone = "15128202550";
            String code = String.valueOf(new Random().nextInt(666666));
            aliyunService.sendMessage(phone, code);
            long endTime = System.currentTimeMillis();
            log.info("LinuxController-提醒定时打卡的邮件执行结束,时间是:{},耗时:{}", sdf.format(new Date()), (endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("LinuxController-提醒定时打卡的邮件执行失败,时间是:{}", sdf.format(new Date()));
        }
    }

    //查
//    @PostMapping("selectTicket")
//    @Scheduled(cron = "19,39,59 * * * * ? ")
    public R selectTicket() {
        log.info("LinuxController-查询火车开始-----------------------------------------------,时间是:{}", sdf.format(new Date()));
        long startTime = System.currentTimeMillis();
        String url = "https://kyfw.12306.cn/otn/leftTicket/queryY?leftTicketDTO.train_date=2021-09-30&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=HDP&purpose_codes=ADULT";
        //获得查询报文
        String response = HttpClientUtils.doGet(url, null);
        JSONObject responseJson;
        try {
            //解析报文
            responseJson = JSONObject.parseObject(response);
        } catch (Exception e) {
            log.error("查询车票超时,爆错信息===============error");
            return R.error();
        }
        JSONObject dataJson = JSONObject.parseObject(String.valueOf(responseJson.get("data")));
        List<String> resultList = (List<String>) dataJson.get("result");

        //去除暂停发售的火车票
        Iterator<String> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            String result = iterator.next();
            if (result.contains("列车运行图调整")) {
                iterator.remove();
            }
        }

        //查询上次数据库中存储的火车列次数
        Map<String, Object> lastTrainCountMap = trainService.getLastTrainCount();
        //如果条数不一发送邮件
        if (resultList.size() != Integer.valueOf(String.valueOf(lastTrainCountMap.get("lastTrainCount")))) {
            if (resultList.size() > Integer.valueOf(String.valueOf(lastTrainCountMap.get("lastTrainCount")))) {
                sendMsgUtil.sendMessage("发送train开始", "发送train邮件结束", "train列次+1", "train列次+1", "1643057053@qq.com", "15128202550@163.com");
                String phone = "15128202550";
                String code = String.valueOf(new Random().nextInt(100000));
                try {
                    aliyunService.sendMessage(phone, code);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //列车次数如表
            trainService.insertTrainCount(resultList.size(), String.valueOf(lastTrainCountMap.get("id")));
        }

        //查询报文中上次有座个数
        int newHavingCount = 0;
        Map<String, Object> lastHavingSetMap = trainService.getHavintSet();
        for (String result : resultList) {
            if (result.contains("有")) {
                newHavingCount = newHavingCount + 1;
            }
        }
        if (newHavingCount > Integer.parseInt(String.valueOf(lastHavingSetMap.get("havingTrainsCount")))) {
            sendMsgUtil.sendMessage("发送train座位开始", "发送train座位邮件结束", "train座位+1", "train列次+1", "1643057053@qq.com", "15128202550@163.com");
            trainService.insertTrainSetCount(newHavingCount, String.valueOf(lastTrainCountMap.get("id")));
        }
        long endTime = System.currentTimeMillis();
        log.info("LinuxController-查询火车结束----------------------------------------------,时间是:{},耗时:{}", sdf.format(new Date()), (endTime - startTime));
        return R.oK();
    }

    @PostMapping("getWhether")
    public R getWhether(@RequestBody JSONObject params){
        String whether = whetherService.getWhether(params.getString("localName"));
        return R.oK().data("data",whether);
    }

//    public static void main(String[] args) throws IOException {
//        Map<String,String> param = new HashMap<>();
//        param.put("transNo","123");
//        param.put("type","1");
//        param.put("fromUser","1643057053@qq.com");
//        param.put("toUser","15128202550@163.com");
//        param.put("title","test@163.com");
//        param.put("content","test@163.com");
//        HttpClientUtils.doPost("http://mgateway.dev.cs/services/email/api/open/sendEmail",param,"UTF-8");
//    }

    public static void main(String[] args) {
        String param = HttpClientUtils.doGet("https://cd.jd.com/stocks?callback=jQuery295683&type=getstocks&skuIds=100012881852%2C100012881854%2C100012881836&area=1_2810_55547_0&_=1636380119654", null);
        String substring = param.substring(13);
        String substring1 = substring.substring(0, substring.length() - 1);
        JSONObject jsonObject = JSONObject.parseObject(substring1);        Map<String,Object> responseMap = (Map<String,Object>)jsonObject.get("100012881852");
        String stockState = responseMap.get("StockState").toString();
        System.out.println(stockState);
    }

    //发送邮件2
//    @Scheduled(cron = "0 30 18 * * ?")
    public void sendEmail23() {
        String param = HttpClientUtils.doGet("https://cd.jd.com/stocks?callback=jQuery295683&type=getstocks&skuIds=100012881852%2C100012881854%2C100012881836&area=1_2810_55547_0&_=1636380119654", null);
        String substring = param.substring(12);
        String substring1 = substring.substring(0, substring.length() - 1);
        JSONObject jsonObject = JSONObject.parseObject(substring1);
        Map<String,Object> responseMap = (Map<String,Object>)jsonObject.get("100012881852");
        String stockState = responseMap.get("StockState").toString();
        System.out.println(stockState);
    }

}
