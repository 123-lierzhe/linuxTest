package com.liez.utils;

import com.jcraft.jsch.*;
import com.liez.myConfig.MyUploadProgressMonitor;
import com.liez.myConfig.MydownloadProgressMonitor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

@Slf4j
public class UploadFileUtil {

    private static Session session = null;

    private static Channel channel = null;

    private static OutputStream outstream = null;

    /**
     * 利用JSch包实现SFTP上传文件
     *
     * @param bytes    文件字节流
     * @param fileName 文件名
     * @throws Exception
     */
    public static void sftpUpload(byte[] bytes, String fileName, String username, String ip, int port, String password, String linuxSaveFilePath) throws Exception {

        //获得连接
        ChannelSftp sftp = getConnect(username, ip, port, password);

        //进入服务器指定文件夹
        sftp.cd(linuxSaveFilePath);
        //列出服务器指定的文件列表
        Vector v = sftp.ls("*");
        for (int i = 0; i < v.size(); i++) {
            //删除之前上传的同名文件
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) v.get(i);
            if (fileName.equals(String.valueOf(lsEntry.getFilename()))) {
                log.info("正在删除已有文件");
                sftp.rm(fileName);
            }
        }

        //从本地上传一个文件到服务器
        outstream = sftp.put(fileName);
        outstream.write(bytes);

        //关闭连接
        closeConnect();
    }

    /**
     * 利用JSch包实现SFTP上传文件(通过路径，显示进度)
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void sftpPathUpload(String src, String dest, String username, String ip, int port, String password) throws Exception {

        ChannelSftp sftp = getConnect(username, ip, port, password);

        //获得文件大小
        File file = new File(src);
        long fileSize = file.length();

        //从本地上传一个文件到服务器
        sftp.put(src, dest, new MyUploadProgressMonitor(fileSize));

        //关闭连接
        closeConnect();
    }

    /**
     * 利用JSch包实现SFTP文件下载(通过路径，显示进度)
     *
     * @param
     * @param dest
     * @throws IOException
     */
    public static void downFromToLinux(String ip, String username, String password, String fromFilePath, int port, String dest, String localIp, String localUsername, String localPassword, int localPort,String fileName) throws Exception {
        //连接文件原服务器
        ChannelSftp localSftp = getConnect(localUsername, localIp, localPort, localPassword);

        ChannelSftp sftp = getConnect(username, ip, port, password);
        SftpATTRS stat = sftp.stat(fromFilePath);
        long fileSize = stat.getSize();
        FileOutputStream fileOutputStream = new FileOutputStream(dest);

        //连接本地服务器
//        ChannelSftp localSftp = getConnect(localUsername, localIp, localPort, localPassword);
        localSftp.cd(dest);
        Vector ls = sftp.ls("*");
        for (int i = 0; i < ls.size(); i++) {
            //删除之前上传的同名文件
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) ls.get(i);
            if (fileName.equals(String.valueOf(lsEntry.getFilename()))) {
                log.info("正在删除旧文件：{}",fileName);
                sftp.rm(fileName);
            }
        }

        sftp.get(fromFilePath, fileOutputStream, new MydownloadProgressMonitor(fileSize));


    }


    public static ChannelSftp getConnect(String username, String ip, int port, String password) throws Exception {

        JSch jsch = new JSch();

        //连接服务器
        session = jsch.getSession(username, ip, port);
        //如果服务器连接不上抛出异常
        if (session == null) {
            throw new Exception("连接服务器异常");
        }
        //设置主机登录密码
        session.setPassword(password);

        //设置第一次登陆的提示
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000);

        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect(1000);
        ChannelSftp sftp = (ChannelSftp) channel;
        return sftp;
    }

    public static void closeConnect() throws IOException {
        //关流
        if (outstream != null) {
            outstream.flush();
            outstream.close();
        }
        if (session != null) {
            session.disconnect();
        }
        if (channel != null) {
            channel.disconnect();
        }
    }


}
