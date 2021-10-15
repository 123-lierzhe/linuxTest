package com.liez.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@Slf4j
public class SendMsgUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessage(String beginLog, String endLog,String subject,String text,String from,String to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(":{},时间是:{}", beginLog, sdf.format(new Date()));
        long startTime = System.currentTimeMillis();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(subject+"====" + sdf.format(new Date()));
        simpleMailMessage.setText(text);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        javaMailSender.send(simpleMailMessage);
        long endTime = System.currentTimeMillis();
        log.info(":{},时间是:{},耗时:{}秒", endLog, sdf.format(new Date()), (endTime - startTime) / 1000);
    }


}
