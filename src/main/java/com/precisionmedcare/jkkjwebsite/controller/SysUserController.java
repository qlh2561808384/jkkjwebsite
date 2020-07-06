package com.precisionmedcare.jkkjwebsite.controller;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.precisionmedcare.jkkjwebsite.components.CustomException;
import com.precisionmedcare.jkkjwebsite.service.SysUserDetailService;
import com.precisionmedcare.jkkjwebsite.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


@Api(tags = "用户控制器")
@RestController
@RequestMapping("user")
public class SysUserController extends ApiController {
    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserDetailService sysUserDetailService;

    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R Register(@RequestBody Map<String, Object> map) {
        try {
            return success(sysUserService.register(map));
        } catch (Exception e) {
            return CustomException.exception(e);
        }
    }

    @ApiOperation(value = "保存用户详细信息")
    @PostMapping("saveUserDetails")
    public R saveUserDetails(@RequestBody Map<String,Object> map) {
        return sysUserDetailService.saveUserDetail(map);
    }

}
