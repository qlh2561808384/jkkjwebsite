package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnRole;
import com.precisionmedcare.jkkjwebsite.mapper.SysRoleMapper;
import com.precisionmedcare.jkkjwebsite.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, NmnRole> implements SysRoleService {

    @Override
    public List<String> getRolesByUserName(String username) {
        return this.baseMapper.getRoleByUserName(username);
    }
}
