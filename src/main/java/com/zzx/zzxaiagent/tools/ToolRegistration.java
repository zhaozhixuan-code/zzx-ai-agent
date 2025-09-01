package com.zzx.zzxaiagent.tools;

import jakarta.annotation.Resource;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 工具注册类
 */
@Configuration
public class ToolRegistration {

    // search-api 用于百度搜索
    @Value("${searchapi.api-key}")
    private String searchApiKey;

    // 邮件发送人
    @Value("${spring.mail.from}")
    private String emailFrom;

    @Autowired
    private JavaMailSender sender;

    // 阿里云 oss 配置
    @Value("${aliyvn.alioss.endpoint}")
    private String endpoint;
    @Value("${aliyvn.alioss.access-key-id}")
    private String accessKeyId;
    @Value("${aliyvn.alioss.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyvn.alioss.bucket-name}")
    private String bucketName;


    @Bean
    public ToolCallback[] allTools() {
        // 发送邮箱
        EmailSenderTool emailSenderTool = new EmailSenderTool(sender, emailFrom);
        // 操作文件
        FileOperationTool fileOperationTool = new FileOperationTool();
        // 生成PDF工具
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool(endpoint, accessKeyId, accessKeySecret, bucketName);
        // 下载文件
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        // 终端操作工具
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        // 终止工具
        TerminateTool terminateTool = new TerminateTool();
        // 联网搜索
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        // 网页抓取
        WebScrapingTool webScrapingTool = new WebScrapingTool();

        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGenerationTool,
                emailSenderTool,
                terminateTool
        );
    }
}
