package com.liez.myConfig;

import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;

@Slf4j
public class MyProgressMonitor implements SftpProgressMonitor {

    private long transfered; // 记录已传输的数据总大小

    private long fileSize; // 记录文件总大小

    private double useTime = 0;

    private long lastTime = 0L;

    private final DecimalFormat df = new DecimalFormat("#0.00");



    public MyProgressMonitor(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("开始上传，上传文件为：{}，大小：{}，目标地址为：{}",src,fileSize,dest);
    }

    @Override
    public boolean count(long count) {
        transfered = transfered + count;
        long nowTime = System.currentTimeMillis();
        if(lastTime == 0){
            lastTime = System.currentTimeMillis();
        }
        if((nowTime-lastTime)/1000>=2){
            double insertTime = (nowTime-lastTime)/1000;
            useTime = useTime + Math.ceil(insertTime);
            log.info("文件已上传：{}秒,上传进度:{}%",Math.ceil(useTime),df.format((transfered*1.0/fileSize)*100));
            lastTime = nowTime;
        }
        return true;
    }

    @Override
    public void end() {
        log.info("上传结束");
    }
}
