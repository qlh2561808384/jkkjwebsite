package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.domain.NmnPromoCode;
import com.precisionmedcare.jkkjwebsite.mapper.SysNmnPromoCodeMapper;
import com.precisionmedcare.jkkjwebsite.service.SysNmnPromoCodeService;
import org.springframework.stereotype.Service;

/**
 * @Class: SysNmnPromoCodeImpl
 * @Description: SysNmnPromoCodeImpl$
 * @title: SysNmnPromoCodeImpl
 * @Author qlh
 * @Date: 2020/9/16 11:17
 * @Version 1.0
 */
@Service
public class SysNmnPromoCodeServiceImpl extends ServiceImpl<SysNmnPromoCodeMapper, NmnPromoCode> implements SysNmnPromoCodeService {

    private static final int GLOBAL_STATE = 0;
    @Override
    public NmnPromoCode checkCode(String code) {
        LambdaQueryWrapper<NmnPromoCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NmnPromoCode::getPromoCode, code);
        lambdaQueryWrapper.eq(NmnPromoCode::getState, GLOBAL_STATE);
        return this.baseMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public NmnPromoCode selectPromoCodeById(Integer promoCodeId) {
        LambdaQueryWrapper<NmnPromoCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NmnPromoCode::getId, promoCodeId);
        lambdaQueryWrapper.eq(NmnPromoCode::getState, GLOBAL_STATE);
        return this.baseMapper.selectOne(lambdaQueryWrapper);
    }
    @Override
    public void updateUsageCountByPromoCodeId(Integer promoCodeId, String usageCount) {
        LambdaUpdateWrapper<NmnPromoCode> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(NmnPromoCode::getId, promoCodeId);
        lambdaUpdateWrapper.eq(NmnPromoCode::getState, GLOBAL_STATE);
        lambdaUpdateWrapper.set(NmnPromoCode::getUsageCount, usageCount);
        this.update(lambdaUpdateWrapper);
    }
}
