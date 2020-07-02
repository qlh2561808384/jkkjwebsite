package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import org.springframework.stereotype.Service;

@Service
public class SysNmnServiceImpl extends ServiceImpl<SysNmnMapper, NmnNmn> implements SysNmnService {
}
