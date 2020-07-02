package com.precisionmedcare.jkkjwebsite.config.authentication;

import org.springframework.security.access.AccessDeniedException;

public class MyaccessDeniedException extends AccessDeniedException {


    public MyaccessDeniedException(String msg) {
        super(msg);
    }
}
