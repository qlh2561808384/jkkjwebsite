package com.precisionmedcare.jkkjwebsite.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.precisionmedcare.jkkjwebsite.components.wx.*;
import com.precisionmedcare.jkkjwebsite.config.WeChatConfig;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import com.precisionmedcare.jkkjwebsite.vo.MailVo;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Api(tags = "支付下单接口")
@RestController
@RequestMapping("order")
public class SysOrderController extends ApiController {

    @Autowired
    SysNmnService sysNmnService;

    @Autowired
    SysNmnOrderService sysNmnOrderService;
    //获取邮件发送类
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    MailVo mailVo;
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    WeChatPayProperties weChatPayProperties;
    private static final String EMAIL_MSG = "您购买的商品已发货/The item you purchased has been shipped";
    private static final String MANAGE_EMAIL = "trhealth12@gmail.com";

    @PostMapping("WxPay")
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "微信支付订单信息", dataType = "Map<String, Object>",paramType = "body")})
    public R saveAliPayOrder(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return success(unifiedOperateMap(map, request, response));
        //生成支付二维码
//        generateQrCode(codeUrl, response);
    }

    @PostMapping("AliPay")
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "支付宝支付订单信息", dataType = "Map<String, Object>",paramType = "body")})
    public R saveWxPayOrder(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return success(unifiedOperateMap(map, request, response));
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
/*    @RequestMapping("callback")
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

    }*/
    /**
     * 支付回调
     * @throws Exception
     */
    @RequestMapping("/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, String> parseNotifyParameter = parseNotifyParameter(request);
        String sign = WeChatUtil.generateSign(parseNotifyParameter,weChatPayProperties.getKey());//生成签名
        if(sign.equals(parseNotifyParameter.get("sign"))){
            //支付成功
            //判断回调信息是否成功
            if ("SUCCESS".equals(parseNotifyParameter.get("result_code"))) {
                //获取商户订单号
                //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                String outTradeNo = parseNotifyParameter.get("out_trade_no");
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
        }else {
            //7、通知微信订单处理失败
            response.setContentType("text/xml");
            response.getWriter().println("fail");
        }
    }
    /**
     * 从request的inputStream中获取参数
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String, String> parseNotifyParameter(HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, length);
        }
        outSteam.close();
        inputStream.close();

        // 获取微信调用我们notify_url的返回信息
        String resultXml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> notifyMap = WeChatUtil.xmlToMap(resultXml);
        return notifyMap;
    }
    /**
     * 提取用户信息表跟订单表公共部分
     *
     * @param map
     * @param request
     * @return
     */
    public NmnNmnOrderVo saveNmnOrderVo(Map<String, Object> map, HttpServletRequest request) {
        String userAddress, userIdCard, userPhone, email, totalFee, payType,receiverName,code,orderNote,userId;
        //用户id=0
        String ymdhms = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_PATTERN);
        String randomString ;
        //用户id!=0
        NmnNmnOrderVo nmnNmnOrderVo = new NmnNmnOrderVo();
        userAddress = map.get("userAddress").toString();
        userIdCard = map.get("userIdCard").toString();
        email = map.get("email").toString();
        userPhone = map.get("userPhone").toString();
        totalFee = map.get("totalFee").toString();
        receiverName = map.get("userName").toString();
        payType = map.get("payType").toString();
        code = map.get("code").toString();
        orderNote = map.get("orderNote").toString();
        userId = map.get("userId").toString();
        if ("".equals(userId)) {
            nmnNmnOrderVo.setUserId(0);
            randomString = RandomUtil.randomString(18);
            nmnNmnOrderVo.setOutTradeNo(ymdhms + randomString);
        } else {
            nmnNmnOrderVo.setUserId(Long.parseLong(userId));
            int length = userId.length();
            randomString = RandomUtil.randomString(18 - length);
            nmnNmnOrderVo.setOutTradeNo(ymdhms + randomString);
        }
        nmnNmnOrderVo.setIp(IpUtils.getIpAddr(request));
        nmnNmnOrderVo.setPhone(userPhone);
        nmnNmnOrderVo.setEmail(email);
        nmnNmnOrderVo.setIdcard(userIdCard);
        nmnNmnOrderVo.setAddress(userAddress);
        nmnNmnOrderVo.setPayType(payType);
//        nmnNmnOrderVo.setGeneralTitle(generalTitle);
        nmnNmnOrderVo.setTotalAmount(Double.parseDouble(totalFee));
        nmnNmnOrderVo.setReceiverName(receiverName);
        nmnNmnOrderVo.setCode(code);
        nmnNmnOrderVo.setOrderNote(orderNote);
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
            nmnName = productMap.get("title").toString();
            nmnPrice = productMap.get("price").toString();
            nmnNumber = productMap.get("num").toString();
            nmnTotal = productMap.get("total").toString();
            nmnId = productMap.get("id").toString();
            nmnNmnOrderVo.setNmnId(Long.parseLong(nmnId));
            nmnNmnOrderVo.setTotalFee(Double.parseDouble(nmnTotal));
            nmnNmnOrderVo.setNmnTitle(nmnName);
            nmnNmnOrderVo.setAmount(nmnNumber);
            sysNmnOrderService.saveNmnOrder(nmnNmnOrderVo);
        }
        sendEmailToManage(nmnNmnOrderVo.getOutTradeNo(), nmnNmnOrderVo.getEmail());
    }

    private void sendEmailToManage(String outTradeNo, String email) {
        mailVo.setTo(MANAGE_EMAIL);
        mailVo.setSubject("有订单生成了，请到后台进行确认/An order has been generated, please go to the background to confirm");
//            mailVo.setText("订单编号：" + nmnNmnOrder.getOutTradeNo() + "。\n订单收货邮箱：" + nmnNmnOrder.getEmail()+"\n\n");
        Context context = new Context();
        Map<String, Object> map = new HashMap<>();
        map.put("订单编号/Order number", outTradeNo);
        map.put("订单收货邮箱/Order receiving mailbox", email);
        context.setVariable("map", map);
        try {
            mailVo.sendEmail(javaMailSender, templateEngine, context, "order.html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    private R unifiedOperateMap(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderTime, payType, codeUrl = "";
        List productList = new ArrayList();
//        String OutTradeNo = CommonUtils.generateUUID();
        if (!map.isEmpty()) {
            orderTime = map.get("orderTime").toString();
            payType = map.get("payType").toString();
            productList = (List) map.get("product");

            //1、根据用户id和商品id生成订单
            NmnNmnOrderVo nmnNmnOrderVo = saveNmnOrderVo(map, request);
            //保存订单
            saveNmnOrder(productList, nmnNmnOrderVo);
            //登陆后支付 微信支付 保存订单同时返回codeUrl
           /* if ("weChatPay".equals(payType)) {
                codeUrl = sysNmnOrderService.weChatPay(nmnNmnOrderVo);
            }else {
                codeUrl = sysNmnOrderService.aliPay(nmnNmnOrderVo);
            }*/
            return success(nmnNmnOrderVo.getOutTradeNo());
        }else {
            return success(false);
        }
    }

    @ApiOperation(value = "订单管理-订单查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "一页展示的数据量", dataType = "int"),
            @ApiImplicitParam(name = "keyword", value = "用于查询的关键词（模糊查询）（订单管理页面 关键词查询）", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id（该条件是用户管理界面根据用户id查询该用户下面的订单）（订单管理界面 userId为空）", dataType = "String")
    })
    @GetMapping("queryOrder")
    public R queryOrder(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("keyword") String keyword, @RequestParam("userId") String userId) {
        PageHelper.startPage(page, limit);
        List<HashMap<String, Object>> nmn = sysNmnOrderService.queryOrder(keyword,userId);
        PageInfo pageInfo = new PageInfo(nmn);
        return success(JSONUtil.createObj()
                .putOnce("total", pageInfo.getTotal())
                .putOnce("data", pageInfo.getList()));
    }

    @ApiOperation(value = "订单管理-发货")
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "map（订单id）", dataType = "Map<String, Object>",paramType = "body")})
    @PostMapping("send")
    public R send(@RequestBody Map<String, Object> map) {
        return success(sysNmnOrderService.send(map));
    }

    @ApiOperation(value = "查询单个订单根据订单id")
    @GetMapping("selectOneOrder")
    public R selectOneOrder(@RequestParam("orderId") String orderId) {
        return success(sysNmnOrderService.getOneOrder(orderId));
    }

    @ApiOperation(value = "订单管理-支付确认")
    @ApiImplicitParams({@ApiImplicitParam(name = "map", value = "map（订单号）", dataType = "Map<String, Object>",paramType = "body")})
    @PostMapping("checkBuy")
    public R checkBuy(@RequestBody Map<String, Object> map) {
        return success(sysNmnOrderService.checkBuy(map));
    }

}
