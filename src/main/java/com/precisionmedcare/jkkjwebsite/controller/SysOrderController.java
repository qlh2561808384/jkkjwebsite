package com.precisionmedcare.jkkjwebsite.controller;

import com.precisionmedcare.jkkjwebsite.components.wx.IpUtils;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "支付下单接口")
@RestController
@RequestMapping("order")
public class SysOrderController {

    @Autowired
    SysNmnService sysNmnService;

    @Autowired
    SysNmnOrderService sysNmnOrderService;

    @GetMapping("buy")
    public void saveOrder(@RequestParam("userid") String userid, @RequestParam("nmnid") String nmnid, HttpServletRequest request, HttpServletResponse response) {
        //1、根据用户id和商品id生成订单
        NmnNmnOrderVo nmnNmnOrderVo = new NmnNmnOrderVo();
        nmnNmnOrderVo.setUserId(Long.parseLong(userid));
        nmnNmnOrderVo.setNmnId(Long.parseLong(nmnid));
        nmnNmnOrderVo.setIp(IpUtils.getIpAddr(request));
        //2、保存订单同时返回codeUrl
        String codeUrl = sysNmnOrderService.saveOrder(nmnNmnOrderVo);


    }

}
