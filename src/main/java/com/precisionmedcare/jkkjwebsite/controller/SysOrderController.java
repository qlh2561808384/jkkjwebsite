package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.core.date.DateUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.precisionmedcare.jkkjwebsite.components.wx.CommonUtils;
import com.precisionmedcare.jkkjwebsite.components.wx.IpUtils;
import com.precisionmedcare.jkkjwebsite.components.wx.QRCodeUtil;
import com.precisionmedcare.jkkjwebsite.components.wx.WXPayUtil;
import com.precisionmedcare.jkkjwebsite.config.WeChatConfig;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Api(tags = "支付下单接口")
@RestController
@RequestMapping("order")
public class SysOrderController {

    @Autowired
    SysNmnService sysNmnService;

    @Autowired
    SysNmnOrderService sysNmnOrderService;

    @Autowired
    private WeChatConfig weChatConfig;

    @PostMapping("WxPay")
    public void saveAliPayOrder(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codeUrl = unifiedOperateMap(map, request, response);
        //生成支付二维码
        generateQrCode(codeUrl, response);
    }
    @PostMapping("AliPay")
    public String saveWxPayOrder(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return unifiedOperateMap(map, request, response);
    }
    /**
     * 微信支付回调
     * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
     * notify_url不能有参数，外网可以直接访问，不能有访问控制（比如必须要登录才能操作）。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
     * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
     * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，但微信不保证通知最终能成功。
     * （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
     * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
     */
    @RequestMapping("callback")
    public void orderCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {

        InputStream inputStream = request.getInputStream();

        //BufferedReader是包装设计模式，性能更搞
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        //1、将微信回调信息转为字符串
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();

        //2、将xml格式字符串格式转为map集合
        Map<String, String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callbackMap.toString());

        //3、转为有序的map
        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callbackMap);

        //4、判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())) {

            //5、判断回调信息是否成功
            if ("SUCCESS".equals(sortedMap.get("result_code"))) {

                //获取商户订单号
                //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                String outTradeNo = sortedMap.get("out_trade_no");
                System.out.println(outTradeNo);
                //6、数据库查找订单,如果存在则根据订单号更新该订单
                NmnNmnOrder nmnNmnOrder = sysNmnOrderService.findByOutTradeNo(outTradeNo);
                System.out.println(nmnNmnOrder);
                if (nmnNmnOrder != null && nmnNmnOrder.getState() == 0) {  //判断逻辑看业务场景
                    NmnNmnOrder NmnOrder = new NmnNmnOrder();
                    NmnOrder.setOutTradeNo(outTradeNo);
                    NmnOrder.setNotifyTime(DateUtil.now());
                    //修改支付状态，之前生成的订单支付状态是未支付，这里表面已经支付成功的订单
                    NmnOrder.setState(1);
                    //根据商户订单号更新订单
                    int rows = sysNmnOrderService.updateVideoOderByOutTradeNo(NmnOrder);
                    System.out.println(rows);
                    //7、通知微信订单处理成功
                    if (rows == 1) {
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }
        //7、通知微信订单处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");

    }

    /**
     * 提取用户信息表跟订单表公共部分
     *
     * @param map
     * @param request
     * @return
     */
    public NmnNmnOrderVo saveNmnOrderVo(Map<String, Object> map, HttpServletRequest request, String OutTradeNo) {
        String userAddress, userIdCard, userPhone, email, totalFee, payType;
        NmnNmnOrderVo nmnNmnOrderVo = new NmnNmnOrderVo();
        userAddress = map.get("userAddress").toString();
        userIdCard = map.get("userIdCard").toString();
        email = map.get("email").toString();
        userPhone = map.get("userPhone").toString();
        totalFee = map.get("totalFee").toString();
        payType = map.get("payType").toString();
        nmnNmnOrderVo.setIp(IpUtils.getIpAddr(request));
        nmnNmnOrderVo.setPhone(userPhone);
        nmnNmnOrderVo.setEmail(email);
        nmnNmnOrderVo.setIdcard(userIdCard);
        nmnNmnOrderVo.setAddress(userAddress);
        nmnNmnOrderVo.setOutTradeNo(OutTradeNo);
        nmnNmnOrderVo.setPayType(payType);
//        nmnNmnOrderVo.setGeneralTitle(generalTitle);
        nmnNmnOrderVo.setTotalAmount(totalFee);
        return nmnNmnOrderVo;
    }

    /**
     * @param productList
     * @param nmnNmnOrderVo
     */
    public void saveNmnOrder(List productList, NmnNmnOrderVo nmnNmnOrderVo) {
        String nmnName, nmnPrice, nmnNumber, nmnTotal, nmnId;
        Map<String, Object> productMap = new HashMap<>();
        for (Object product : productList) {
            productMap = (Map<String, Object>) product;
            nmnName = productMap.get("nmnName").toString();
            nmnPrice = productMap.get("nmnPrice").toString();
            nmnNumber = productMap.get("nmnNumber").toString();
            nmnTotal = productMap.get("nmnTotal").toString();
            nmnId = productMap.get("nmnId").toString();
            nmnNmnOrderVo.setNmnId(Long.parseLong(nmnId));
            nmnNmnOrderVo.setTotalFee(Long.parseLong(nmnTotal));
            nmnNmnOrderVo.setNmnTitle(nmnName);
//            sysNmnOrderService.saveNmnOrder(nmnNmnOrderVo);
        }
    }

    /**
     * @param codeUrl
     * @param response
     * @throws IOException
     */
    private void generateQrCode(String codeUrl, HttpServletResponse response) throws IOException {
        if (codeUrl == null) {
            throw new NullPointerException();
        }
        //3、通过google工具生成二维码供用户扫码支付
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            //使用工具类生成二维码
            QRCodeUtil.encode(codeUrl, stream);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }


    private String unifiedOperateMap(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderTime, userId, userName, codeUrl = "";
        List productList = new ArrayList();
        String OutTradeNo = CommonUtils.generateUUID();
        if (!map.isEmpty()) {
            orderTime = map.get("orderTime").toString();
            userId = map.get("userId").toString();
            userName = map.get("userName").toString();
            productList = (List) map.get("product");

            //1、根据用户id和商品id生成订单
            NmnNmnOrderVo nmnNmnOrderVo = saveNmnOrderVo(map, request, OutTradeNo);
            if ("".equals(userId)) {
                nmnNmnOrderVo.setUserId(0);
            } else {
                nmnNmnOrderVo.setUserId(Long.parseLong(userId));
            }
            //保存订单
            saveNmnOrder(productList, nmnNmnOrderVo);
            //登陆后支付 微信支付 保存订单同时返回codeUrl
            codeUrl = sysNmnOrderService.weChatPay(nmnNmnOrderVo);
        }
        return codeUrl;
    }
}
