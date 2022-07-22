package com.liez.threadPool;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author liez
 * @date 2022/6/13
 */
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService{

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    //将Service层的服务异步化，在executeAsync()方法上增加注解@Async("asyncServiceExecutor")，asyncServiceExecutor方法是前面ExecutorConfig.java中的方法名，表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建的
    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync() throws InterruptedException {
        logger.info("start executeAsync");

        System.out.println("异步线程要做的事情");

        System.out.println("可以在这里执行批量插入等耗时的事情");

        logger.info("end executeAsync");
    }
}
