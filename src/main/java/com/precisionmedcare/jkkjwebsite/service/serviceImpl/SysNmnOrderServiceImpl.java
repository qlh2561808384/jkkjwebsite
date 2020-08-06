package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.XmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.components.AliPay.AlipaySubmit;
import com.precisionmedcare.jkkjwebsite.components.wx.*;
import com.precisionmedcare.jkkjwebsite.config.AliPayConfig;
import com.precisionmedcare.jkkjwebsite.config.WeChatConfig;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnOrderMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.vo.GlobalAlipayVo;
import com.precisionmedcare.jkkjwebsite.vo.MailVo;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysNmnOrderServiceImpl extends ServiceImpl<SysNmnOrderMapper, NmnNmnOrder> implements SysNmnOrderService {

    private static final long ALL_STATUS = 0;
    private static final long ALL_STATUS_DISABLE = 1;
    private static final String WX_PAY_TYPE = "weChatPay";
    private static final String ALI_PAY_TYPE = "aliPay";
    private static final String EMAIL_MSG = "您购买的商品已发货/The item you purchased has been shipped";

    private static final String UNIFIEDORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String GETSIGNKEYURL = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
    private static final String SANDBOXUNIFIEDORDERURL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    private static final String ORDERQUERYURL = "https://api.mch.weixin.qq.com/pay/orderquery";
    @Autowired
    SysNmnMapper sysNmnMapper;
    @Autowired
    SysNmnOrderMapper sysNmnOrderMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private GlobalAlipayVo globalAlipayVo;
    @Autowired
    MailVo mailVo;
    //获取邮件发送类
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    WeChatPayProperties weChatPayProperties;

    @Autowired
    private RestTemplate restTemplate;

/*    @Override
    public String saveOrder(NmnNmnOrderVo nmnNmnOrderVo) throws Exception {
        String OutTradeNo = CommonUtils.generateUUID();
        //1、查找商品信息
        NmnNmn nmnNmn = sysNmnMapper.selectById(nmnNmnOrderVo.getNmnId());
        //2、查找用户信息
        NmnUser nmnUser = sysUserMapper.selectById(nmnNmnOrderVo.getUserId());
        //3、生成订单，插入数据库
        NmnNmnOrder nmnNmnOrder = new NmnNmnOrder();
        nmnNmnOrder.setTotalFee(nmnNmn.getPrice());
        nmnNmnOrder.setNmnImg(nmnNmn.getCoverImg());
        nmnNmnOrder.setNmnTitle(nmnNmn.getTitle());
        nmnNmnOrder.setCreateTime(DateUtil.now());
        nmnNmnOrder.setNmnId(nmnNmn.getId());
        nmnNmnOrder.setState(ALL_STATUS);
        nmnNmnOrder.setUserId(nmnUser.getId());
        nmnNmnOrder.setDel(ALL_STATUS);
        nmnNmnOrder.setIp(nmnNmnOrderVo.getIp());
        nmnNmnOrder.setOutTradeNo(OutTradeNo);
        sysNmnOrderMapper.insert(nmnNmnOrder);
        //4.获取codeUrl
        String codeUrl = unifiedOrder(nmnNmnOrder);
        return codeUrl;
    }*/

    @Override
    public void saveNmnOrder(NmnNmnOrderVo nmnNmnOrderVo) {
        //1、查找商品信息
        NmnNmn nmnNmn = sysNmnMapper.selectById(nmnNmnOrderVo.getNmnId());
        NmnUser nmnUser = null;
        //2、查找用户信息
        if (nmnNmnOrderVo.getUserId() != 0) {
            nmnUser = sysUserMapper.selectById(nmnNmnOrderVo.getUserId());
        }

        //3、生成订单，插入数据库
        NmnNmnOrder nmnNmnOrder = new NmnNmnOrder();
        nmnNmnOrder.setOutTradeNo(nmnNmnOrderVo.getOutTradeNo());
        nmnNmnOrder.setState(ALL_STATUS);
        nmnNmnOrder.setCreateTime(DateUtil.now());
        nmnNmnOrder.setTotalFee(nmnNmnOrderVo.getTotalFee());
        nmnNmnOrder.setNmnId(nmnNmn.getId());
        nmnNmnOrder.setNmnTitle(nmnNmn.getTitle());
        nmnNmnOrder.setNmnImg(nmnNmn.getCoverImg());
        if (nmnUser != null) {
            nmnNmnOrder.setUserId(nmnUser.getId());
        }
        nmnNmnOrder.setIp(nmnNmnOrderVo.getIp());
        nmnNmnOrder.setDel(ALL_STATUS);
        nmnNmnOrder.setStatus(ALL_STATUS);
        if(WX_PAY_TYPE.equals(nmnNmnOrderVo.getPayType())){
            nmnNmnOrder.setPaymentTypes(ALL_STATUS);
        } else if (ALI_PAY_TYPE.equals(nmnNmnOrderVo.getPayType())) {
            nmnNmnOrder.setPaymentTypes(ALL_STATUS_DISABLE);
        }
        nmnNmnOrder.setPhone(nmnNmnOrderVo.getPhone());
        nmnNmnOrder.setEmail(nmnNmnOrderVo.getEmail());
        nmnNmnOrder.setIdcard(nmnNmnOrderVo.getIdcard());
        nmnNmnOrder.setAddress(nmnNmnOrderVo.getAddress());
        nmnNmnOrder.setReceiverName(nmnNmnOrderVo.getReceiverName());
        nmnNmnOrder.setAmount(nmnNmnOrderVo.getAmount());
        sysNmnOrderMapper.insert(nmnNmnOrder);
    }

    @Override
    public String weChatPay(NmnNmnOrderVo nmnNmnOrderVo) throws Exception {
        //4.获取codeUrl
//        String codeUrl = unifiedOrder(nmnNmnOrderVo);
        String codeUrl = nativePay(nmnNmnOrderVo);
        return codeUrl;
    }

    public String nativePay(NmnNmnOrderVo nmnNmnOrderVo){

        /*配置微信支付基础信息参数*/
        Map<String, String> requestData = new HashMap<String, String>();
        requestData.put("appid", weChatPayProperties.getAppid());//公众账号ID
        requestData.put("mch_id", weChatPayProperties.getMchId());//商户号
        requestData.put("nonce_str", RandomUtil.randomString(15));//随机字符串 32位以内
        // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        requestData.put("spbill_create_ip", nmnNmnOrderVo.getIp());
        requestData.put("trade_type", "NATIVE");//交易类型 扫码支付
        /*配置微信支付自定义支付信息参数*/
        requestData.put("attach", "附加数据远洋返回");
        requestData.put("body", nmnNmnOrderVo.getNmnTitle());//商品简单描述
        requestData.put("out_trade_no", CommonUtils.generateUUID());//商户订单号
        requestData.put("total_fee", String.valueOf(nmnNmnOrderVo.getTotalAmount()));//标价金额 按照分进行计算
        requestData.put("notify_url", "http://localhost:8888/order/wxNotify");//通知地址 异步接收微信支付结果通知的回调地址必须外网访问 不能携带参数

        /*配置微信支付sign信息参数*/
        String sign = null;
        String payUrl = null;
        if(Boolean.valueOf(weChatPayProperties.getUseSandbox())){
            sign = WeChatUtil.generateSign(requestData,weChatPayProperties.getKey());//生成签名
            payUrl = UNIFIEDORDERURL;
        }else{
            sign = WeChatUtil.generateSign(requestData,weChatPayProperties.getSandboxKey());//生成签名
            payUrl = SANDBOXUNIFIEDORDERURL;
        }

        requestData.put("sign", sign);

        /*将map信息转换成String*/
        String mapToXmlStr = XmlUtil.mapToXmlStr(requestData, "xml");

        /*调用微信统一下单Api将mapToXmlStr作为参数*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> formEntity = new HttpEntity<>(mapToXmlStr, headers);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(payUrl, formEntity, String.class);

        //获取微信返回的信息
        String returnXmlString = postForEntity.getBody();
        Map<String, Object> xmlToMap = XmlUtil.xmlToMap(returnXmlString);
        String returnCode = (String)xmlToMap.get("return_code");
        if("SUCCESS".equals(returnCode)){
            String codeUrl = (String)xmlToMap.get("code_url");
            return codeUrl;
        }

        return "";
    }
    /**
     * 统一下单方法
     * @return
     */
    private String unifiedOrder(NmnNmnOrderVo nmnNmnOrderVo) throws Exception {
        //4.1、生成签名 按照开发文档需要按字典排序，所以用SortedMap
        SortedMap<String,String> params = new TreeMap<>();
        params.put("appid",weChatConfig.getAppId());         //公众账号ID
        params.put("mch_id", weChatConfig.getMchId());       //商户号
        params.put("nonce_str", CommonUtils.generateUUID()); //随机字符串
        params.put("body", nmnNmnOrderVo.getNmnTitle());       // 商品描述
        //国际微信
//        params.put("version","1.0");       // 版本号
//        params.put("detail","{\"goods_detail\": [{ \"wxpay_goods_id \":\"1001\"}] }");       // 详细描述

        params.put("out_trade_no",nmnNmnOrderVo.getOutTradeNo());//商户订单号,商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
        //国际微信
//        params.put("fee_type","USD");

//        params.put("total_fee", String.valueOf(nmnNmnOrderVo.getTotalAmount()));//标价金额	分
        params.put("total_fee", "999");//标价金额	分
        params.put("spbill_create_ip",nmnNmnOrderVo.getIp());
        params.put("notify_url",weChatConfig.getPayCallbackUrl());  //通知地址
        params.put("trade_type","NATIVE"); //交易类型 JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付


        //4.2、sign签名 具体规则:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=4_3
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign",sign);

        //4.3、map转xml （ WXPayUtil工具类）
        String payXml = WXPayUtil.mapToXml(params);

        //4.4、回调微信的统一下单接口(HttpUtil工具类）
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(),payXml,4000);
        if(null == orderStr) {
            return null;
        }
        //4.5、xml转map （WXPayUtil工具类）
        Map<String, String> unifiedOrderMap =  WXPayUtil.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());

        //4.6、获取最终code_url
        if(unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }

        return null;
    }

    @Override
    public NmnNmnOrder findByOutTradeNo(String outTradeNo) {
        LambdaQueryWrapper<NmnNmnOrder> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(NmnNmnOrder::getOutTradeNo, outTradeNo);
        lambdaQueryWrapper.eq(NmnNmnOrder::getDel, ALL_STATUS);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public int updateVideoOderByOutTradeNo(NmnNmnOrder nmnOrder) {
        LambdaUpdateWrapper<NmnNmnOrder> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(NmnNmnOrder::getState, nmnOrder.getState());
        lambdaUpdateWrapper.set(NmnNmnOrder::getNotifyTime, nmnOrder.getNotifyTime());
        lambdaUpdateWrapper.eq(NmnNmnOrder::getOutTradeNo, nmnOrder.getOutTradeNo());
        lambdaUpdateWrapper.eq(NmnNmnOrder::getState, ALL_STATUS);
        lambdaUpdateWrapper.eq(NmnNmnOrder::getDel, ALL_STATUS);
        boolean update = this.update(lambdaUpdateWrapper);
        if(update){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public String aliPay(NmnNmnOrderVo nmnNmnOrderVo) {
        //把请求参数打包成数组
        //package the request parameters
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", globalAlipayVo.getService());
        sParaTemp.put("partner",AliPayConfig.partner);
        sParaTemp.put("_input_charset", AliPayConfig.charset);
        sParaTemp.put("notify_url", AliPayConfig.notify_url);
        sParaTemp.put("return_url", AliPayConfig.return_url);
        sParaTemp.put("out_trade_no", new SimpleDateFormat().format(new Date()));
        sParaTemp.put("subject", nmnNmnOrderVo.getNmnTitle());
        sParaTemp.put("total_fee", String.valueOf(nmnNmnOrderVo.getTotalAmount() / 100));
        //sParaTemp.put("rmb_fee", rmb_fee);
        sParaTemp.put("body", "test");//body商品描述 可为空
        sParaTemp.put("currency", "USD");//currency 币种，不可空
        sParaTemp.put("product_code", globalAlipayVo.getProduct_code());//注意：必传，PC端是NEW_OVERSEAS_SELLER，移动端是NEW_WAP_OVERSEAS_SELLER Remarks:Mandatory.For PC: NEW_OVERSEAS_SELLER ;FOR WAP and APP: NEW_WAP_OVERSEAS_SELLER
        //sParaTemp.put("supplier", supplier);
        sParaTemp.put("timeout_rule", globalAlipayVo.getTimeout_rule());
        //split_fund_info = split_fund_info.replaceAll("\"", "'");
        //sParaTemp.put("split_fund_info", split_fund_info);

//        trade_information = trade_information.replaceAll("\"", "'");
        sParaTemp.put("trade_information", globalAlipayVo.getTrade_information().toString().replaceAll("\"", "'"));
        String response = AlipaySubmit.buildRequest(sParaTemp, "get", "OK");
        System.out.println(response);
        return response;
    }

    @Override
    public List<HashMap<String, Object>> queryOrder(String keyword,String userId) {
        return this.baseMapper.queryOrder(keyword, userId);
    }

    @Override
    public boolean send(Map<String, Object> map) {
        if(!map.isEmpty()){
            LambdaUpdateWrapper<NmnNmnOrder> nmnNmnOrderLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            nmnNmnOrderLambdaUpdateWrapper.set(NmnNmnOrder::getStatus, ALL_STATUS_DISABLE);
            nmnNmnOrderLambdaUpdateWrapper.eq(NmnNmnOrder::getId, map.get("nmnOrderId"));
            boolean update = this.update(nmnNmnOrderLambdaUpdateWrapper);
            if(update){
                NmnNmnOrder nmnNmnOrder = getNmnNmnOrder(map);
                mailVo.sendEmail(javaMailSender, nmnNmnOrder.getEmail(), EMAIL_MSG, new Date());
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    private NmnNmnOrder getNmnNmnOrder(Map<String, Object> map) {
        LambdaQueryWrapper<NmnNmnOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NmnNmnOrder::getId, map.get("nmnOrderId"));
        return this.baseMapper.selectOne(lambdaQueryWrapper);
    }
    @Override
    public HashMap<String, Object> getOneOrder(String orderId) {
        return this.baseMapper.selectOneOrderById(orderId);
    }

    @Override
    public boolean checkBuy(Map<String, Object> map) {
        if(!map.isEmpty()) {
            LambdaUpdateWrapper<NmnNmnOrder> nmnNmnOrderLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            nmnNmnOrderLambdaUpdateWrapper.set(NmnNmnOrder::getState, ALL_STATUS_DISABLE);
            nmnNmnOrderLambdaUpdateWrapper.eq(NmnNmnOrder::getOutTradeNo, map.get("outTradeNo"));
            return this.update(nmnNmnOrderLambdaUpdateWrapper);
        }else{
            return false;
        }
    }
}
