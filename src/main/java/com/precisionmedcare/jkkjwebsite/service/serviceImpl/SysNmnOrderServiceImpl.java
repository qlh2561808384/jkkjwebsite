package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.components.wx.CommonUtils;
import com.precisionmedcare.jkkjwebsite.components.wx.HttpUtils;
import com.precisionmedcare.jkkjwebsite.components.wx.WXPayUtil;
import com.precisionmedcare.jkkjwebsite.config.WeChatConfig;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnOrderMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class SysNmnOrderServiceImpl extends ServiceImpl<SysNmnOrderMapper, NmnNmnOrder> implements SysNmnOrderService {

    private static final long ALL_STATUS = 0;
    private static final long ALL_STATUS_DISABLE = 1;

    @Autowired
    SysNmnMapper sysNmnMapper;
    @Autowired
    SysNmnOrderMapper sysNmnOrderMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    private WeChatConfig weChatConfig;

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
    public void saveNmnWeChatOrder(NmnNmnOrderVo nmnNmnOrderVo) {
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
        nmnNmnOrder.setPaymentTypes(ALL_STATUS);
        nmnNmnOrder.setPhone(nmnNmnOrderVo.getPhone());
        nmnNmnOrder.setEmail(nmnNmnOrderVo.getEmail());
        nmnNmnOrder.setIdcard(nmnNmnOrderVo.getIdcard());
        nmnNmnOrder.setAddress(nmnNmnOrderVo.getAddress());
        sysNmnOrderMapper.insert(nmnNmnOrder);
    }

    @Override
    public String weChatPay(NmnNmnOrderVo nmnNmnOrderVo) throws Exception {
        //4.获取codeUrl
        String codeUrl = unifiedOrder(nmnNmnOrderVo);
        return codeUrl;
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
//        params.put("fee_type","USD");//商户订单号,商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一

        params.put("total_fee", String.valueOf(nmnNmnOrderVo.getTotalAmount()));//标价金额	分
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
}
