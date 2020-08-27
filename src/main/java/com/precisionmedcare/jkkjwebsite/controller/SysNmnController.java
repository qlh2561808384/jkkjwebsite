package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.core.util.RandomUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "商品接口")
@RequestMapping("nmn")
@RestController
public class SysNmnController extends ApiController {

    @Autowired
    SysNmnService sysNmnService;
    @Value("${upload.path}")
    private String uploadPath;

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public R upload(HttpServletRequest request, @RequestParam Map<String, String> map) {
        String msg = "";
        MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象
        if (!map.isEmpty()) {
            String imgName = map.get("imgName");
            int i = imgName.lastIndexOf("/");
            String substring = imgName.substring(i + 1);
            File file = new File(uploadPath + File.separator + substring);
            if (file.exists()) {
                file.delete();
            }
        }
        for (MultipartFile file : files.values()) {
            if (file.isEmpty()) {
                // 设置错误状态码
                msg = "failed,file not empty";
                return failed(msg);
            }
            // 拿到文件名
            String newName = null;
            String openfilename = file.getOriginalFilename();
            String filename = openfilename.replace(" ", "_");
            int one = filename.lastIndexOf(".");
            if (-1 == one) {
                msg = "failed,File format error";
                return failed(msg);
            } else {
                newName = filename.replace(filename.substring(0, one), RandomUtil.randomString(10));
            }
            // 存放上传图片的文件夹
            String fileDirPath = new String(uploadPath + File.separator);
            File fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                // 递归生成文件夹
                fileDir.mkdirs();
            }
            // 输出文件夹绝对路径  -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径
//            System.out.println(fileDir.getAbsolutePath());
            try {
                // 构建真实的文件路径
                File newFile = new File(fileDir.getAbsolutePath() + File.separator + newName);
//                System.out.println(newFile.getAbsolutePath());
                // 上传图片到 -》 “绝对路径”
                file.transferTo(newFile);
                msg = "http://pmdcare.cn:82/" + newName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success(msg);
    }

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
