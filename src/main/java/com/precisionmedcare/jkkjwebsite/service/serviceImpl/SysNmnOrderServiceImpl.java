package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnOrderMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnOrderService;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysNmnOrderServiceImpl extends ServiceImpl<SysNmnOrderMapper, NmnNmnOrder> implements SysNmnOrderService {

    @Autowired
    SysNmnMapper sysNmnMapper;
    @Autowired
    SysNmnOrderMapper sysNmnOrderMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public String saveOrder(NmnNmnOrderVo nmnNmnOrderVo) {
        //1、查找商品信息
        NmnNmn nmnNmn = sysNmnMapper.selectById(nmnNmnOrderVo.getNmnId());
        //2、查找用户信息
        NmnUser nmnUser = sysUserMapper.selectById(nmnNmnOrderVo.getUserId());
        //3、生成订单，插入数据库
        NmnNmnOrder nmnNmnOrder = new NmnNmnOrder();
        nmnNmnOrder.setTotalFee(nmnNmn.getPrice());

        return null;
    }
}
