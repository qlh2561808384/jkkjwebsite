package com.precisionmedcare.jkkjwebsite.components.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qlh
 *
 */
@Component
@Data
@ConfigurationProperties(prefix="wx.pay")
public class WeChatPayProperties {
	
	/**合作身份者ID */
	private String appid;
	/** 商户号 */
	private String mchId;
	/** 商户号密钥 */
	private String appsecret;
	/** API 密钥 商户后台配置的一个32位的key 微信商户平台-账户设置-安全设置-api安全 */
	private String key;
	/**是否使用沙箱*/
	private String useSandbox;
	/** 沙箱环境API 密钥  */
	private String sandboxKey;
	/** 回调地址 */
	private String notifyUrl;
}
