package com.asiainfo.springboot.mail;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午6:04:59
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class MailController {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    JavaMailSender sender;

    @GetMapping("/simple")
    public String sendSimpleMail() throws Exception {
        
        logger.info("发送简单的文本邮件...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jaesonchen@163.com");
        message.setTo("95381487@qq.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        sender.send(message);
        return "success";
    }

    @GetMapping("/attchments")
    public String sendAttachmentsMail() throws Exception {
        
        logger.info("发送带附件的邮件...");
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("jaesonchen@163.com");
        helper.setTo("95381487@qq.com");
        helper.setSubject("主题：有附件");
        helper.setText("有附件的邮件");
        helper.addAttachment("logo.png", new ClassPathResource("static/logo.png"));
        sender.send(mimeMessage);
        return "success";
    }

    @GetMapping("/inline")
    public String sendInlineMail() throws Exception {
        
        logger.info("发送嵌入静态资源的邮件...");
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("jaesonchen@163.com");
        helper.setTo("95381487@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:logo\" /></body></html>", true);
        helper.addInline("logo", new ClassPathResource("static/pic.png"));
        sender.send(mimeMessage);
        return "success";
    }
}
