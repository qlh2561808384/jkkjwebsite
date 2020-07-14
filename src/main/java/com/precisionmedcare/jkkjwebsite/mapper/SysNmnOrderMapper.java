package com.precisionmedcare.jkkjwebsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;
import com.precisionmedcare.jkkjwebsite.providers.BackStageApiProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.HashMap;
import java.util.List;

public interface SysNmnOrderMapper extends BaseMapper<NmnNmnOrder> {

    @SelectProvider(type = BackStageApiProvider.class, method = "queryOrder")
    List<HashMap<String, Object>> queryOrder(@Param("keyword") String keyword, @Param("userId") String userId);

    @SelectProvider(type = BackStageApiProvider.class, method = "selectOneOrderById")
    HashMap<String, Object> selectOneOrderById(@Param("orderId") String orderId);
}
