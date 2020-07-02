package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;

import java.util.Map;

public interface SysUserService extends IService<NmnUser> {
    NmnUser getUserByUserName(String username);

    boolean checkLogin(String username, String password) throws Exception;

    boolean register(Map<String, Object> map) throws Exception;
}
