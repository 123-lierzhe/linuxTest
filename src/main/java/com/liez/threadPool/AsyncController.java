package com.liez.threadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liez
 * @date 2022/6/19
 */
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("async")
    public void async() throws InterruptedException {
        asyncService.executeAsync();
    }
}
