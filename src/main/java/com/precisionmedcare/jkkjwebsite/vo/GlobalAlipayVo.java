package com.precisionmedcare.jkkjwebsite.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "global.alipay")
public class GlobalAlipayVo {

    private static String out_trade_no;//商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复

    private String timeout_rule;//该笔订单允许的最晚付款时间

//    private String totle_amout;//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]

    private  String service ;//销售产品码，商家和支付宝签约的产品码，为固定值 FAST_INSTANT_TRADE_PAY

    private String product_code;

    private Map<String, String> trade_information;
}
