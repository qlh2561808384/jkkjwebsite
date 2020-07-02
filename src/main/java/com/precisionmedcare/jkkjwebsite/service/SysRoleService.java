package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnRole;

import java.util.List;

public interface SysRoleService extends IService<NmnRole> {
    List<String> getRolesByUserName(String username);
}
