package com.changgou.file;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @Description
 * @Author tangKai
 * @Date 14:32 2019/12/26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FastDFSFile implements Serializable {

    //文件名字
    @NotNull
    private String name;
    //文件内容
    @NotNull
    private byte[] content;
    //文件扩展名
    @NotNull
    private String ext;
    //文件MD5摘要值
    private String md5;
    //文件创建作者
    private String author;


    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }
}
