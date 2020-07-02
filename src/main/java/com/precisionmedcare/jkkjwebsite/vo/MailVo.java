package com.precisionmedcare.jkkjwebsite.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    @Value("注册验证码")
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

    public void sendEmail(JavaMailSender javaMailSender, String to, String text, Date sentDate) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(this.getSubject());
        simpleMailMessage.setFrom(this.getFrom());
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(text);
        simpleMailMessage.setSentDate(sentDate);
        javaMailSender.send(simpleMailMessage);
    }
}
