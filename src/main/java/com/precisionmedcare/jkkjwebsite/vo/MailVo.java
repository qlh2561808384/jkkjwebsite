package com.precisionmedcare.jkkjwebsite.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Data
@Component
@ApiModel(description = "email实体类")
public class MailVo {
    @ApiModelProperty(value = "邮件id")
    private String id;
    @ApiModelProperty(value = "邮件发送人")
    @Value("${spring.mail.username}")
    private String from;
    @ApiModelProperty(value = "邮件接收人")
    private String to;
    @ApiModelProperty(value = "邮件主题")
    private String subject;
    @ApiModelProperty(value = "邮件内容")
    private String text;
    @ApiModelProperty(value = "发送时间")
    private Date sentDate;
    @ApiModelProperty(value = "抄送")
    private String cc;
    @ApiModelProperty(value = "密送")
    private String bcc;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "报错信息")
    private String error;
    @ApiModelProperty(value = "邮件附件")
    @JsonIgnore
    private MultipartFile[] multipartFiles;
    public void sendEmail(JavaMailSender javaMailSender) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(this.subject);
        simpleMailMessage.setFrom(this.from);
        simpleMailMessage.setTo(this.to);
        simpleMailMessage.setText(this.text);
        simpleMailMessage.setSentDate(this.sentDate);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendEmail(JavaMailSender javaMailSender, TemplateEngine templateEngine, Context context, String html) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(this.subject);
        helper.setFrom(this.from);
        helper.setTo(this.to);
        helper.setSentDate(this.sentDate);
        String process = templateEngine.process(html, context);
        helper.setText(process, true);
        javaMailSender.send(mimeMessage);
    }
}
