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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "需要注册的用户信息map", dataType = "Map<String, Object>",paramType = "body")})
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "一页展示的数据量", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "用于查询的关键词（模糊查询）", dataType = "String")
    })
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
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "需要修改用户表跟用户信息表的信息map", dataType = "Map<String, Object>",paramType = "body")})
    @PostMapping("modifyUserAndUserDetails")
    public R modifyUserAndUserDetails(@RequestBody Map<String, Object> map) {
        return success(sysUserService.modifyUserAndUserDetails(map));
    }

    @ApiOperation(value = "网站(个人信息)-新增and修改用户详细信息表接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "nmnUserDetails", value = "用户详细信息", dataType = "NmnUserDetails",paramType = "body")})
    @PostMapping("addAndModifyUserDetails")
    public R addAndModifyUserDetails(@RequestBody NmnUserDetails nmnUserDetails) {
        return success(sysUserDetailService.addAndModifyUserDetails(nmnUserDetails));
    }
}
