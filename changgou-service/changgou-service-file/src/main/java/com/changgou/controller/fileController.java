package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description
 * @Author tangKai
 * @Date 16:44 2019/12/26
 **/
@RestController
@CrossOrigin
public class fileController {



    /**
     * @Description 文件上传
     * @Author tangKai
     * @Date 16:52 2019/12/26
     * @Param [file]
     * @Return java.lang.String
     **/
    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 封装一个FastDFSFile
        FastDFSFile fastDFSFile = new FastDFSFile(
            // 文件名称
            file.getOriginalFilename(),
            // 文件字节数组
            file.getBytes(),
            // 文件拓展名
            StringUtils.getFilenameExtension(file.getOriginalFilename())
        );
        // 文件上传
        String[] uploads = FastDFSClient.upload(fastDFSFile);
        // 组装文件上传地址
        return FastDFSClient.getTrackerServerURL() + "/" + uploads[0] + "/" + uploads[1];
    }


}
