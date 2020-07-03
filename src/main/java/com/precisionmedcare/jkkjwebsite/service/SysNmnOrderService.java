package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;


public interface SysNmnOrderService extends IService<NmnNmnOrder> {
//    String saveOrder(NmnNmnOrderVo nmnNmnOrderVo) throws Exception;

    String weChatPay(NmnNmnOrderVo nmnNmnOrderVo) throws Exception;

    void saveNmnWeChatOrder(NmnNmnOrderVo nmnNmnOrderVo);

    NmnNmnOrder findByOutTradeNo(String outTradeNo);

    int updateVideoOderByOutTradeNo(NmnNmnOrder nmnOrder);
}
