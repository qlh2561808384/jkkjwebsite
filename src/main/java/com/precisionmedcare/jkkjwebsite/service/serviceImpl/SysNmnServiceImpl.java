package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysNmnServiceImpl extends ServiceImpl<SysNmnMapper, NmnNmn> implements SysNmnService {

    private static final long STATUS = 0;
    private static final long STATUS_DISABLE = 0;
    @Override
    public boolean saveOrUpdateNmn(NmnNmn nmnNmn) {
        if (nmnNmn != null) {
            long id = nmnNmn.getId();
            if (id == 0) {
                return this.save(nmnNmn);
            }else {
                return this.saveOrUpdate(nmnNmn);
            }
        }else {
            return false;
        }
    }

    @Override
    public List<HashMap<String, Object>> queryNmn(String keyword) {
        return this.baseMapper.queryNmn(keyword);
    }
}
