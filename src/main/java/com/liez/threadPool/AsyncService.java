package com.liez.threadPool;

/**
 * @author liez
 * @date 2022/6/13
 */
public interface AsyncService {
    /**
     * 执行异步任务
     * 可以根据需求，自己加参数拟定，我这里就做个测试演示
     */
    void executeAsync() throws InterruptedException;
}
