package com.precisionmedcare.jkkjwebsite.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.vo.NmnNmnOrderVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface SysNmnOrderService extends IService<NmnNmnOrder> {
//    String saveOrder(NmnNmnOrderVo nmnNmnOrderVo) throws Exception;

    String weChatPay(NmnNmnOrderVo nmnNmnOrderVo) throws Exception;

    void saveNmnOrder(NmnNmnOrderVo nmnNmnOrderVo);

    NmnNmnOrder findByOutTradeNo(String outTradeNo);

    int updateVideoOderByOutTradeNo(NmnNmnOrder nmnOrder);

    String aliPay(NmnNmnOrderVo nmnNmnOrderVo);

    List<HashMap<String, Object>> queryOrder(String keyword,String userId);

    boolean send(Map<String, Object> map);

    HashMap<String, Object> getOneOrder(String orderId);
}
