package com.liez.myConfig;

import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Slf4j
public class MydownloadProgressMonitor implements SftpProgressMonitor {

    private long transfered; // 记录已传输的数据总大小

    private long fileSize; // 记录文件总大小

    private double useTime = 0;

    private long lastTime = 0L;

    private final DecimalFormat df = new DecimalFormat("#0.00");

    public MydownloadProgressMonitor(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("开始下载，下载文件为：{}，大小：{}，目标地址为：{}", src, fileSize, dest);
    }

    @Override
    public boolean count(long count) {
        transfered = transfered + count;
        long nowTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
        }
        if ((nowTime - lastTime) / 1000 >= 2) {
            double insertTime = (nowTime - lastTime) / 1000;
            useTime = useTime + Math.ceil(insertTime);
            log.info("文件已下载：{}秒,下载进度:{}%", Math.ceil(useTime), df.format((transfered * 1.0 / fileSize) * 100));
            lastTime = nowTime;
        }
        return true;
    }

    @Override
    public void end() {
        log.info("下载结束");
    }
}
