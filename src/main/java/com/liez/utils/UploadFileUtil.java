package com.liez.utils;

import com.jcraft.jsch.*;
import com.liez.myConfig.MyProgressMonitor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

@Slf4j
public class UploadFileUtil {

    private static final String ip = "180.76.180.113";

    private static final String username = "root";

    private static final String password = "zhe981127!";

    private static final int port = 22;

    private static final String linuxFilePath = "/jars";

    /**
     * 利用JSch包实现SFTP上传文件
     *
     * @param bytes    文件字节流
     * @param fileName 文件名
     * @throws Exception
     */
    public static void sftpUpload(byte[] bytes, String fileName) throws IOException {
        Session session = null;
        Channel channel = null;
        OutputStream outstream = null;

        JSch jsch = new JSch();

        try {
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

            //进入服务器指定文件夹
            sftp.cd(linuxFilePath);
            //列出服务器指定的文件列表
            Vector v = sftp.ls("*");
            for (int i = 0; i < v.size(); i++) {
                //删除之前上传的同名文件
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) v.get(i);
                if (fileName.equals(String.valueOf(lsEntry.getFilename()))) {
                    sftp.rm(fileName);
                }
            }

            //从本地上传一个文件到服务器
            outstream = sftp.put(fileName);
            outstream.write(bytes);
        }catch (Exception e){
            log.error("上传文件报错，原因：{}",e.toString());
            e.printStackTrace();
        }finally {
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

    /**
     * 利用JSch包实现SFTP上传文件(通过路径，显示进度)
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void sftpPathUpload(String src,String dest) throws Exception {
        Session session = null;
        Channel channel = null;
        OutputStream outstream = null;

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

        //获得文件大小
        File file = new File(src);
        long fileSize = file.length();

        //从本地上传一个文件到服务器
        sftp.put(src,dest,new MyProgressMonitor(fileSize));

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
