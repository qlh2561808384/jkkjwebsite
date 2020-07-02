package com.precisionmedcare.jkkjwebsite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.precisionmedcare.jkkjwebsite.mapper")
public class JkkjwebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(JkkjwebsiteApplication.class, args);
    }

}
