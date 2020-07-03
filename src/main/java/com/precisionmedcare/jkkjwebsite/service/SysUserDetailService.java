package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;

public interface SysUserDetailService extends IService<NmnUserDetails> {
    void saveUserDetail(NmnNmnOrderVo nmnNmnOrderVo);
}
