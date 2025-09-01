package com.zzx.zzxaiagent.tools;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Slf4j
public class AliOssUtil {

    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;
    private final String bucketName;

    public AliOssUtil(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
    }

    /**
     * 文件上传
     *
     * @param localFilePath 本地文件路径（包含文件名）
     * @param objectName 要保存到oss中的名字（可以包含路径）
     * @return 文件的下载地址
     */
    public String upload(String localFilePath, String objectName) {
        // 验证本地文件是否存在
        File localFile = new File(localFilePath);
        if (!localFile.exists() || !localFile.isFile()) {
            log.error("本地文件不存在或不是有效的文件: {}", localFilePath);
            throw new IllegalArgumentException("本地文件不存在或不是有效的文件: " + localFilePath);
        }

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try (InputStream inputStream = new FileInputStream(localFile)) {
            // 创建PutObject请求
            ossClient.putObject(bucketName, objectName, inputStream);
            log.info("文件上传成功，本地路径: {}, OSS路径: {}", localFilePath, objectName);
        } catch (FileNotFoundException e) {
            log.error("文件未找到: {}", localFilePath, e);
            return null;
        } catch (Exception e) {
            handleException(e);
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 构建文件访问路径
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        String fileUrl = stringBuilder.toString();
        log.info("文件上传到: {}", fileUrl);

        return fileUrl;
    }

    /**
     * 处理OSS上传过程中的异常
     */
    private void handleException(Exception e) {
        if (e instanceof OSSException) {
            OSSException oe = (OSSException) e;
            log.error("OSSException - 错误消息: {}", oe.getErrorMessage());
            log.error("错误代码: {}", oe.getErrorCode());
            log.error("请求ID: {}", oe.getRequestId());
            log.error("Host ID: {}", oe.getHostId());
        } else if (e instanceof ClientException) {
            ClientException ce = (ClientException) e;
            log.error("ClientException - 错误消息: {}", ce.getMessage());
        } else {
            log.error("文件上传发生异常", e);
        }
    }
}
