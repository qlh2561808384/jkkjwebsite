package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Api(tags = "商品接口")
@RequestMapping("nmn")
@RestController
public class SysNmnController extends ApiController {

    @Autowired
    SysNmnService sysNmnService;

    @ApiOperation("商品管理-新增and修改商品表")
    @ApiImplicitParams({@ApiImplicitParam(name = "nmnNmn", value = "商品信息实体类", dataType = "NmnNmn",paramType = "body")})
    @PostMapping("saveOrUpdateNmn")
    public R saveOrUpdateNmn(@RequestBody NmnNmn nmnNmn) {
        return success(sysNmnService.saveOrUpdateNmn(nmnNmn));
    }

    @ApiOperation(value = "商品管理-商品查询分页and条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "一页展示的数据量", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "用于查询的关键词（模糊查询）", dataType = "String"),
    })
    @GetMapping("queryNmn")
    public R queryNmn(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("keyword") String keyword) {
        PageHelper.startPage(page, limit);
        List<HashMap<String, Object>> nmn = sysNmnService.queryNmn(keyword);
        PageInfo pageInfo = new PageInfo(nmn);
        return success(JSONUtil.createObj()
                .putOnce("total", pageInfo.getTotal())
                .putOnce("data", pageInfo.getList()));
    }

    @ApiOperation(value = "商品管理-删除商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", dataType = "String")})
    @GetMapping("deleteNmn")
    public R deleteNmn(@RequestParam("id") String id) {
        return success(sysNmnService.deleteNmn(id));
    }

    @ApiOperation(value = "查询单个商品信息根据商品信息")
    @GetMapping("selectOneNmn")
    public R selectOneNmn(@RequestParam("nmnId") String nmnId) {
        return success(sysNmnService.getOneNmn(nmnId));
    }
}
