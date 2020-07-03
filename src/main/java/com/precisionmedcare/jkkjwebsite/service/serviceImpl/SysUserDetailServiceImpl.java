package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserDetailMapper;
import com.precisionmedcare.jkkjwebsite.service.SysUserDetailService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserDetailServiceImpl extends ServiceImpl<SysUserDetailMapper, NmnUserDetails> implements SysUserDetailService {
    @Autowired
    SysUserDetailMapper sysUserDetailMapper;

    @Override
    public void saveUserDetail(NmnNmnOrderVo nmnNmnOrderVo) {
        NmnUserDetails nmnUserDetails = new NmnUserDetails();
        nmnUserDetails.setEmail(nmnNmnOrderVo.getEmail());
        nmnUserDetails.setIdcard(nmnNmnOrderVo.getIdcard());
        nmnUserDetails.setPhone(nmnNmnOrderVo.getPhone());
        nmnUserDetails.setUserId(nmnNmnOrderVo.getUserId());
        nmnUserDetails.setAddress(nmnNmnOrderVo.getAddress());
        this.save(nmnUserDetails);
    }
}
