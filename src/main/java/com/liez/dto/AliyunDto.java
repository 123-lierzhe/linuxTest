package com.liez.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AliyunDto {

    @Value("${aliyun.sendMsg.product}")
    private String product;
    @Value("${aliyun.sendMsg.domain}")
    private String domain;
    @Value("${aliyun.sendMsg.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sendMsg.accessKeySecret}")
    private String accessKeySecret;

}
