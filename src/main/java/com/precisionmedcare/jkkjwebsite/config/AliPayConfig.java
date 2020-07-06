package com.precisionmedcare.jkkjwebsite.config;

import org.springframework.context.annotation.Configuration;

import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class AliPayConfig {
    private static String neturl = "http://localhost:8888";
    public static String partner = "2088621949772380";
    public static String produce_partner = "";//海外生产环境APPID
    public static String global_merchant_private_key ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDigD0l83GAEPLIf166KaIVPvSZi4x+vutjWfYZmTFTv1SifhWd1b529CNFK4gGd9t+gTycHHbPMmgG8RcLo12rDeMIDeH8XZR7rjv2GQNhWCVybpKa3p4A1jEA7xASlUK5YMZ3w0qIcVxw3RhAVxt2sbjHDDFo0+poG5epX/ALnDE6bmsrJmF2uVytJ32nBU9A32a7N3zHLxAUaoMit2ziV6x+J5DCTDwNGjXpsXghEUSjMnioFA+8RvvLu5+xJWt4OEM9MdlCjpuCebQb8hXt1a3GDr2ppxepZK9KuE+c6pDa9PBwWtWUW9Ay+Abrn07XOay9k6Zp45+VRlBwc+5dAgMBAAECggEAc1b0TIgciyeWSm058rjStdj6O9ppL/3O5ifgf4aYLNpoodps7ZPzyqVl9IpHWWvdlR0AnUqNYdN8T9SjdKS7RSjsLKTNKHMK8rR2CksQgpidcRlHhJ/KfAiWqbATUjM56CaUylXjdXmspjigadsxvA6iVpIp8wS/AEbUw0Z8eZwZlgphVEQz7iPLWBc0yncG/ae6JGb+fqvbsSdU1VrrAEYQmC+lg6Fjj37cqK2ir80ZiCVtjs3t6QxMPf1TQxPbgDWYYT+Ynn2+Z7VRfh4bv0j04GmeWYF7e/8zMB99csZql9AwtiRNaXV1flcKS0jWBhkE6SQUKzFTaSgztqK8YQKBgQD8/2/6qxzJ/jzieXf319Kvnu2Ke8mnvuMyPykwtmeeMtrlYp8olqHkdAgilIs9JVgo6lTiQOumor4FmQcXxgcwokNES3dE/8CT5muAG+ygGJdsNzB5pvV2/y4fmJ1V7a0vN30o5EBf03zXSdvJ21uxAAWUkyGx1XfyXEzEcQLc+QKBgQDlME8C7p/U9S2pwSfBwIhNv5w1fVzTnakuDg7LRbyhnCgEATJnRV0ScDB1anjbtSz7X0ccPCDRjd6udTcIlieTgLue88EN65l8LDVoS/dNB1InMMIXph+BV08LmdBu3tK+7Bjs1TTnpv+FKpWzsOoIpIeF0dbHaV4t0Dd3lj1phQKBgFz7LBZ+SMhb7dh2OAcYhNASos6gjA5gc3OGDwdeIlrb6eMgCcqLAdjKZCsBWOMSFIKFDMlSfsjxStEX2ql8GCxhlkNFUQJE1GwG5T9iA+ca4pvc9K3hFJm1Uyg9mKV6Aoo15IPgz1/ohBtDg8l+GwbEMUjXgiyvfbPPBE+2EQ1RAoGBAOHc6+DN3YH3ZdrnWhDddvZ4IK+uLe72byXOANJelXkG9YCM1WlkuEZVfcwY86ZCNGQ+D0xKDWW21H9FUMkLgVEiCsSpH9ZiGs5+mtuAEeU9k76/lGfN9Tp4GpQqL3kz6qjw79dmfk7VCvfnccS2Us3VpyFAHNREiOXJTwBxdXApAoGALeZTHKOJy2fbNhrx4RVv6aPBMI/BJdpfJq031DESfn8wVssxHL4FHsIRLSEOgv+Q9zX+JXJDP/LgmhwM6B/xsgnmuTqczpNILCm61vhmymtdVh1TK6P5DCkHwQL1teTuX+LJMxWdhBV2BgUJ9TItb8VrF4CxJfMtBINggTFrDQw=";
    public static String global_alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDS92pDVyWNT7dzG9zH0opH44z9FayCZTX5iqGUxUjPi667IkyaqrsmDPqKsJp47lJ29lzs+Qv8zjPPdmnxjFteMrfpc4ui24gL1iZnchwX87Ox/+Xrm8HFmKlhmUO9n/QgTT+Nz1RGMEN1+HijvsoAhS0TS8XjSfzRkrwvK2pJQIDAQAB";
    public static String notify_url = neturl+"/alipay/notify_url";//网站支付完成之后调转哪个页面
    public static String return_url = neturl+"/alipay/return_url";//网站支付完成之后调转哪个页面
    public static String global_sign_type = "RSA";
    public static String charset = "utf-8";
    public static String global_gatewayUrl = "https://mapi.alipaydev.com/gateway.do";
    // 支付宝网关
    public static String log_path = "F:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
