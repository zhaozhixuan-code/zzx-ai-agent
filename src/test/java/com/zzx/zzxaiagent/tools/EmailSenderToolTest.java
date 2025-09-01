package com.zzx.zzxaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSenderToolTest {
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.from}")
    private String from;


    @Test
    void sendTextEmail() {
        EmailSenderTool emailSenderTool = new EmailSenderTool(sender, from);
        emailSenderTool.sendTextEmail("xxxxxxx@qq.com","ceshi");

    }
}