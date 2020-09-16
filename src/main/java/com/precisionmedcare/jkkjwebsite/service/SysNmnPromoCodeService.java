package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnPromoCode;

public interface SysNmnPromoCodeService extends IService<NmnPromoCode> {
    NmnPromoCode checkCode(String code);

    NmnPromoCode selectPromoCodeById(Integer promoCodeId);

    void updateUsageCountByPromoCodeId(Integer promoCodeId, String usageCount);
}
