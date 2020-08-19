package com.precisionmedcare.jkkjwebsite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//表示这是一个配置类
@Configuration
public class MyWebConfigurer implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**").addResourceLocations("file:F:/upload/");
        boolean win = System.getProperty("os.name").toLowerCase().startsWith("win");
        if(win){
            registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadPath + "/");
        }else {
            registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadPath + "/");
        }
    }
}
