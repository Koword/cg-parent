package com.changgou.util;

import com.changgou.file.FastDFSFile;
import java.io.IOException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

/**
 * @Description
 * @Author tangKai
 * @Date 14:39 2019/12/26
 **/
public class FastDFSClient {


    /**
     * @Description 初始化tracker信息
     * @Author tangKai
     * @Date 14:40 2019/12/26
     * @Param  * @param null
     * @Return
     **/
    static {
        try {
            // 获取tracker的配置文件fdfs_client.conf的位置
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            // 加在tracker配置信息
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description 文件上传
     * @Author tangKai
     * @Date 14:44 2019/12/26
     * @Param [file]
     * @Return java.lang.String[]
     **/
    public static String[] upload(FastDFSFile fastDFSFile) {

        try {
            // 提供附件上传需要的信息
            byte[] file_buff = fastDFSFile.getContent();
            String file_ext_name = fastDFSFile.getExt();
            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0] = new NameValuePair(fastDFSFile.getAuthor());
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient对象创建TrackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取
            StorageClient storageClient = getStorageClient();
            // 4.执行附件上传
            String[] uploadResult = storageClient
                .upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
            return uploadResult;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description 获取服务器地址
     * @Author tangKai
     * @Date 15:20 2019/12/26
     * @Param * @param null
     * @Return
     **/
    public static String getTrackerServerURL() {

        try {
            // 获取跟踪服务器的客户端
            TrackerServer trackerServer = getTrackerServer();
            // 获取跟踪服务器地址
            String hostAddress = trackerServer.getInetSocketAddress().getAddress().getHostAddress(); // 服务器地址
            int port = ClientGlobal.getG_tracker_http_port();
            return "http://" + hostAddress + ":" + port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 附件下载
     * @Author tangKai
     * @Date 16:13 2019/12/26
     * @Param [group_name, remote_filename]
     * @Return byte[]
     **/
    public static byte[] downloadFile(String group_name, String remote_filename) {
        try {
            // 创建存储服务器的客户端
            StorageClient storageClient = getStorageClient();
            // 附件下载
            return storageClient.download_file(group_name, remote_filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description 文件删除
     * @Author tangKai
     * @Date 16:16 2019/12/26
     * @Param [group_name, remote_filename]
     * @Return void
     **/
    public static void deleteFile(String group_name, String remote_filename) {
        try {
            StorageClient storageClient = getStorageClient();
            // 执行文件删除
            storageClient.delete_file(group_name, remote_filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description 根据文件组名和文件存储路径获取Storage服务的IP、端口信息
     * @Author tangKai
     * @Date 16:20 2019/12/26
     * @Param [group_name, remote_filename]
     * @Return org.csource.fastdfs.FileInfo
     **/
    public static FileInfo getFileInfo(String group_name, String remote_filename) {
        try {
            StorageClient storageClient = getStorageClient();
            // 返回附件信息
            return storageClient.get_file_info(group_name, remote_filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * @Description 获取存储服务器
     * @Author tangKai
     * @Date 16:42 2019/12/26
     * @Param [group_name]
     * @Return org.csource.fastdfs.StorageServer
     **/
    public static StorageServer getStorageServer(String group_name) {
        try {
            TrackerServer trackerServer = getTrackerServer();
            TrackerClient trackerClient = new TrackerClient();
            trackerClient.getStoreStorage(trackerServer, group_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //------------------------------------方法抽取 Start-------------------------------//
    private static TrackerServer getTrackerServer() throws IOException {
        /**
         * @Description 获取追踪服务器
         * @Author tangKai
         * @Date 15:46 2019/12/26
         * @Param []
         * @Return org.csource.fastdfs.TrackerServer
         **/
        // 1.创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        // 2.通过TrackerClient 对象获取TrackerServer信息
        return trackerClient.getConnection();
    }

    private static StorageClient getStorageClient() throws IOException {
        /**
         * @Description 获取存储服务器客户端
         * @Author tangKai
         * @Date 15:46 2019/12/26
         * @Param []
         * @Return org.csource.fastdfs.StorageClient
         **/
        TrackerServer trackerServer = getTrackerServer();
        // 3.获取StorageClient对象
        return new StorageClient(trackerServer, null);
    }

    //------------------------------------方法抽取 End-------------------------------//
}
