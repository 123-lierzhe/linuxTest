package com.liez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@EnableScheduling
@SpringBootApplication
@ServletComponentScan(basePackages="com.liez.web")
public class LinuxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinuxTestApplication.class, args);
    }

}
