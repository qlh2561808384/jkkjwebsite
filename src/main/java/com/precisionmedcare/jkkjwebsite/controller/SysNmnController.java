package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "商品控制器")
@RequestMapping("nmn")
@RestController
public class SysNmnController extends ApiController {

    @Autowired
    SysNmnService sysNmnService;

    @PostMapping("saveOrUpdateNmn")
    public R saveOrUpdateNmn(@RequestBody NmnNmn nmnNmn) {
        return success(sysNmnService.saveOrUpdateNmn(nmnNmn));
    }
    @ApiOperation(value = "商品管理-商品查询分页and条件查询")
    @GetMapping("queryNmn")
    public R queryNmn(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("keyword") String keyword){
        PageHelper.startPage(page, limit);
        List<HashMap<String, Object>> nmn = sysNmnService.queryNmn(keyword);
        PageInfo pageInfo = new PageInfo(nmn);
        return success(JSONUtil.createObj()
                .putOnce("total", pageInfo.getTotal())
                .putOnce("data", pageInfo.getList()));
    }
}
