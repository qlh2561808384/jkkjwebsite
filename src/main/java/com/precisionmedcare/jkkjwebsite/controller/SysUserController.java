package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.precisionmedcare.jkkjwebsite.components.CustomException;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import com.precisionmedcare.jkkjwebsite.service.SysUserDetailService;
import com.precisionmedcare.jkkjwebsite.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @ApiOperation(value = "保存用户详细信息（废弃）")
    @PostMapping("saveUserDetails")
    public R saveUserDetails(@RequestBody Map<String,Object> map) {
        return sysUserDetailService.saveUserDetail(map);
    }

    @ApiOperation(value = "用户管理-用户查询分页and条件查询")
    @GetMapping("queryAllUser")
    public R queryAllUser(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("keyword") String keyword){
        PageHelper.startPage(page, limit);
        List<HashMap<String, Object>> allUser = sysUserService.queryAllUser(keyword);
        PageInfo pageInfo = new PageInfo(allUser);
        return success(JSONUtil.createObj()
                .putOnce("total", pageInfo.getTotal())
                .putOnce("data", pageInfo.getList()));
    }

    @ApiOperation(value = "用户管理-修改用户表and详细信息表")
    @PostMapping("modifyUserAndUserDetails")
    public R modifyUserAndUserDetails(@RequestBody Map<String, Object> map) {
        boolean b = sysUserService.modifyUserAndUserDetails(map);
        return success(true);
    }

    @ApiOperation(value = "网站(个人信息)-新增and修改用户详细信息表接口")
    @PostMapping("addAndModifyUserDetails")
    public R addAndModifyUserDetails(@RequestBody NmnUserDetails nmnUserDetails) {
        return success(sysUserDetailService.addAndModifyUserDetails(nmnUserDetails));
    }
}
