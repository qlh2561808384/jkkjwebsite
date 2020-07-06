package com.precisionmedcare.jkkjwebsite.components.AliPay;

import com.precisionmedcare.jkkjwebsite.components.AliPay.sign.RSA;
import com.precisionmedcare.jkkjwebsite.config.AliPayConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *class name:AlipaySubmit
 *Function:The class Alipay use to submit request
 *Detail:Construct the HTML form of Alipay interface,get the data from remote HTTP
 *version:3.3
 *modify date:2012-08-13
 *instructions:
 *This code below is a sample demo for merchants to do test.Merchants can refer to the integration documents and write your own code to fit your website.Not necessarily to use this code.  
 *Alipay provide this code for you to study and research on Alipay interface, just for your reference.

 */

public class AlipaySubmit {
    
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
	 * The Alipay gateway provided to merchants
     */
	//沙箱网关The Alipay gateway of sandbox environment.
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipaydev.com/gateway.do?";
	//生产环境网关，如果商户用的生产环境请换成下面的正式网关
	//The Alipay gateway of production environment.(pls use the below line instead if you were in production environment)
    //private static final String ALIPAY_GATEWAY_NEW = "https://intlmapi.alipay.com/gateway.do?";
	
    /**
     * 生成签名结果
	 * Generate the sign
     * @param sPara 要签名的数组Parameters to sign
     * @return 签名结果字符串sign generated
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
	    //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	//Rearrange parameters in the data set alphabetically and connect rearranged parameters with & like "parametername=value"
		String prestr = AlipayCore.createLinkString(sPara); 
        String mysign = "";
        if(AliPayConfig.global_sign_type.equals("RSA") ) {
        	mysign = RSA.sign(prestr, AliPayConfig.global_merchant_private_key, AliPayConfig.charset);
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
	 * Generate a set of parameters need in the request of Alipay
     * @param sParaTemp 请求前的参数数组Pre-sign string
     * @return 要请求的参数数组 parameters need to be in the request
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
		//Remove the blank ,sign and sign_type
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
		//Generate the sign
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
		//Add the sign and sign_type into the sPara
        sPara.put("sign", mysign);
        sPara.put("sign_type", AliPayConfig.global_sign_type);

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
	 * Build the request,costruct in the format of HTML form
     * @param sParaTemp 请求参数数组the request params
     * @param strMethod 提交方式。两个值可选：post、get  request form.support two types:post and get
     * @param strButtonName 确认按钮显示文字The text of confirmation button
     * @return 提交表单HTML文本 the text of requested HTML form
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组pre-request params
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AliPayConfig.charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
		//Pls don't set name attribute for the submit button 
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
      
        return sbHtml.toString();
    }
    
 
    
    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * Used to anti-phishing，use interface "query_timestamp" to get the function to get the timestamp
     * note：If you get error when parsing XML from remote services,it is related to sever setting like whether the server supports SSL etc.
     * @return 时间戳字符串String of timestamp
     * @throws IOException
     * @throws DocumentException
     * @throws MalformedURLException
     */
	public static String query_timestamp() throws MalformedURLException,
            DocumentException, IOException {

        //构造访问query_timestamp接口的URL串
		//generate the URL to call the query_timestamp interface
        String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + AliPayConfig.partner + "&_input_charset" + AliPayConfig.charset;
        StringBuffer result = new StringBuffer();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());

        List<Node> nodeList = doc.selectNodes("//alipay/*");

        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
			//filter the info that don't need to be parsed
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
				//check success or not
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    result.append(node1.getText());
                }
            }
        }

        return result.toString();
    }
}
