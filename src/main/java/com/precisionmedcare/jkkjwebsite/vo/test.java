package com.precisionmedcare.jkkjwebsite.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class test {
    public static void main(String[] args) {
//        byte[] byteImg = getByteImg("http://p3.so.qhmsg.com/t014d3503dd2d67d728.jpg");
//        System.out.println(byteImg);
        //当前时间
       /* Date date = DateUtil.date();
        String now = DateUtil.now();
        String s = RandomUtil.randomString(18);
        String format = DateUtil.format(date, DatePattern.PURE_DATETIME_PATTERN);
        System.out.println(format + s);*/
        String str = "http://pmdcare.cn:82/1.jpg";
        String substring = "";
        int i1 = str.lastIndexOf("/");
        if (-1 == i1) {
            System.out.println("没有找到/");
        }else {
            substring = str.substring(i1 + 1);
        }
        System.out.println(i1);
        System.out.println(substring);
    }
    /**
     * <h1> ClassName: ImgConverter </h1>
     * <h1> Function: ImageIO 图片文件转换文件流 </h1>
     * @version 创建日期：2019年12月12日 下午5:38:59
     * @author lst
     */
    public static byte[] getByteImg(String imgURL) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgURL));
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "jpg", imOut);
            return bs.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(bs != null) {
                    bs.close();
                }
                if(imOut != null) {
                    imOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
