package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.vo.UserAndDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<NmnUser> {
    NmnUser getUserByUserName(String username);

    boolean checkLogin(String username, String password) throws Exception;

    boolean register(Map<String, Object> map) throws Exception;

    List<HashMap<String, Object>> queryAllUser(String keyword);

    boolean modifyUserAndUserDetails(Map<String, Object> map);

    UserAndDetails getOneUser(String userId);
}
