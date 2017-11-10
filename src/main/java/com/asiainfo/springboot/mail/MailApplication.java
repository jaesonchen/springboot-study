package com.asiainfo.springboot.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月10日  上午11:19:23
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
@ComponentScan("com.asiainfo.springboot.mail")
@RestController
public class MailApplication {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    JavaMailSender sender;

    @RequestMapping("/simple")
    public void sendSimpleMail() throws Exception {
        
        logger.info("发送简单的文本邮件...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jaesonchen@163.com");
        message.setTo("chenzq3@asiainfo.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        sender.send(message);
    }

    @RequestMapping("/attchments")
    public void sendAttachmentsMail() throws Exception {
        
        logger.info("发送带附件的邮件...");
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("jaesonchen@163.com");
        helper.setTo("chenzq3@asiainfo.com");
        helper.setSubject("主题：有附件");
        helper.setText("有附件的邮件");
        FileSystemResource file = new FileSystemResource(new File("/static/logo.png"));
        helper.addAttachment("附件-1.jpg", file);
        sender.send(mimeMessage);
    }

    @RequestMapping("/inline")
    public void sendInlineMail() throws Exception {
        
        logger.info("发送嵌入静态资源的邮件...");
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("jaesonchen@163.com");
        helper.setTo("chenzq3@asiainfo.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:logo\" ></body></html>", true);
        FileSystemResource file = new FileSystemResource(new File("logo.jpg"));
        helper.addInline("logo", file);
        sender.send(mimeMessage);
    }
    
    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(new Object[] {MailApplication.class});
        app.setAdditionalProfiles(new String[] {"mail"});
        app.run(args);
    }
}
