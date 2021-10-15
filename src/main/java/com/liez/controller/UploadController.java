package com.liez.controller;

import com.alibaba.fastjson.JSONObject;
import com.liez.utils.R;
import com.liez.utils.UploadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("system")
@RestController
@Slf4j
public class UploadController {

    /**
     * 简单上传文件(通过选择文件的方式上传)
     * @param file
     * @return
     */
    @PostMapping("uploadJarToLinux")
    public R uploadJarToLinux(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            UploadFileUtil.sftpUpload(bytes, fileName);
        }catch (Exception e){
            log.error("关闭文件流失败失败，失败原因：{}",e.toString());
            e.printStackTrace();
            return R.error();
        }
        return R.oK();
    }

    /**
     * 上传文件并显示进度（采用选择文件路径的方式上传）
     * @param
     * @return
     */
    @PostMapping("uploadPathToLinux")
    public R uploadPathToLinux(@RequestBody JSONObject params) {
        String src = params.get("src").toString();
        String dest = params.get("dest").toString();
        try {
            UploadFileUtil.sftpPathUpload(src, dest);
        }catch (Exception e){
            log.error("上传文件失败，失败原因：{}",e.toString());
            // TODO 关流
            e.printStackTrace();
            return R.error();
        }
        return R.oK();
    }
}
