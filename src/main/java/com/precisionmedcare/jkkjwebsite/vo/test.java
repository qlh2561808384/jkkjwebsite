package com.precisionmedcare.jkkjwebsite.vo;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class test {
    public static void main(String[] args) {
        byte[] byteImg = getByteImg("http://p3.so.qhmsg.com/t014d3503dd2d67d728.jpg");
        System.out.println(byteImg);
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
