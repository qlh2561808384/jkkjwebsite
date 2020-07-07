package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;

import java.util.HashMap;
import java.util.List;

public interface SysNmnService extends IService<NmnNmn> {
    boolean saveOrUpdateNmn(NmnNmn nmnNmn);

    List<HashMap<String, Object>> queryNmn(String keyword);
}
