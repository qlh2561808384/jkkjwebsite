package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import java.util.Map;

public interface SysUserDetailService extends IService<NmnUserDetails> {
    R saveUserDetail(Map<String,Object> map);

    boolean addAndModifyUserDetails(NmnUserDetails nmnUserDetails);

    boolean saveUserDetails(LambdaUpdateWrapper<NmnUserDetails> lambdaUpdateWrapper);
}
