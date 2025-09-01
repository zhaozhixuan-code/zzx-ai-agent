package com.zzx.zzxaiagent.tools;


import cn.hutool.core.util.StrUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 邮件发送工具类，基于HuTool工具类实现
 */
public class EmailSenderTool {

    private final JavaMailSender sender;
    private final String from;
    private final String subject = "邮件标题";



    public EmailSenderTool(JavaMailSender sender, String from) {
        this.sender = sender;
        this.from = from;
    }


    /**
     * 发送简单文本邮件
     *
     * @param content 邮件内容
     * @param to      收件人邮箱地址，多个邮箱用逗号分隔
     * @return 是否发送成功
     */
    @Tool(description = "Used to send mail")
    public String sendTextEmail(
            @ToolParam(description = "the address for receiving emails") String to,
            @ToolParam(description = "the content of the email") String content) {
        // 参数校验
        if (StrUtil.isEmpty(content)) {
            return "The content of the email cannot be empty";
        }
        if (StrUtil.isEmpty(to)) {
            return "The recipient cannot be empty";
        }
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            // 设置主题
            mail.setSubject(subject);
            // 设置内容
            mail.setText(content);
            mail.setTo(to);
            mail.setFrom(from);
            sender.send(mail);
            return "send mail success ";
        } catch (Exception e) {
            return "send mail error " + e.getMessage();
        }


    }


}
