package com.precisionmedcare.jkkjwebsite.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.precisionmedcare.jkkjwebsite.components.EmailCodeUtil;
import com.precisionmedcare.jkkjwebsite.components.ErrorCode;
import com.precisionmedcare.jkkjwebsite.service.redis.RedisService;
import com.precisionmedcare.jkkjwebsite.vo.MailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@Api(tags = "邮箱控制器")
@RestController
@RequestMapping("email")
public class SysEmailController extends ApiController {

    private static final long EXPIRE_TIME = 60;

    @Autowired
    MailVo mailVo;
    //获取邮件发送类
    @Autowired
    JavaMailSender javaMailSender;
    //获取redis模板类
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisService redisService;

    @ApiOperation("获取邮箱验证码")
    @GetMapping("getEmailCode")
    public R getEmailCode(@Param("email") String email) {
        String code = EmailCodeUtil.generateCode();
        mailVo.sendEmail(javaMailSender, email, code, new Date());
        boolean flag = redisService.setEx(email, code, EXPIRE_TIME);
        if(flag){
            return success(true);
        }else {
            return R.failed(ErrorCode.EMAIL_REDIS_ERROR);
        }
    }

    @ApiOperation("校验邮箱验证码")
    @GetMapping("verifyEmailCode")
    public R verifyEmailCode(@Param("code") String code, @Param("email") String email) {
        Object redisCode = null;
        if (redisService.exists(email)) {
            redisCode = redisService.get(email);
        }
        if (!StringUtils.isEmpty(redisCode) && code.equals(redisCode)) {
            return success(true);
        }else {
            return success(false);
        }
    }
}
