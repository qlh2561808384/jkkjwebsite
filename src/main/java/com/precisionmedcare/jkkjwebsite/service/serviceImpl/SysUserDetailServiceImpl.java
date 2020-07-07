package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.components.ErrorCode;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserDetailMapper;
import com.precisionmedcare.jkkjwebsite.service.SysUserDetailService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserDetailServiceImpl extends ServiceImpl<SysUserDetailMapper, NmnUserDetails> implements SysUserDetailService {
    @Autowired
    SysUserDetailMapper sysUserDetailMapper;

    @Override
    public R saveUserDetail(Map<String,Object> map) {
        List<NmnUserDetails> userDetails = this.baseMapper.selectByMap(map);
        if (!userDetails.isEmpty()) {
            return R.failed(ErrorCode.USER_DETAIL_ALREADY_EXISTS);
        }else {
            NmnUserDetails nmnUserDetails = new NmnUserDetails();
            nmnUserDetails.setEmail(userDetails.get(0).getEmail());
            nmnUserDetails.setIdcard(userDetails.get(0).getIdcard());
            nmnUserDetails.setPhone(userDetails.get(0).getPhone());
            nmnUserDetails.setUserId(userDetails.get(0).getUserId());
            nmnUserDetails.setAddress(userDetails.get(0).getAddress());
            return R.ok(this.save(nmnUserDetails));
        }
    }

    @Override
    public boolean addAndModifyUserDetails(NmnUserDetails nmnUserDetails) {
        if (nmnUserDetails != null) {
            long id = nmnUserDetails.getId();
            if (id == 0) {
                return this.save(nmnUserDetails);
            }else {
                return this.saveOrUpdate(nmnUserDetails);
            }
        }else {
            return false;
        }
    }

    @Override
    public boolean saveUserDetails(LambdaUpdateWrapper<NmnUserDetails> lambdaUpdateWrapper) {
        return this.update(lambdaUpdateWrapper);
    }
}
