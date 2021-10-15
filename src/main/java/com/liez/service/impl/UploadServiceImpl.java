package com.liez.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.liez.service.UploadService;
import com.liez.utils.UploadFileUtil;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public void uploadPathToLinuxByabsout() throws Exception{
        ChannelSftp sftpWin = UploadFileUtil.getConnect("liez", "117.136.38.240", 22, "zhe");
        ChannelSftp sftpLinux = UploadFileUtil.getConnect("root", "180.76.180.113", 22, "zhe981127!");
    }
}
