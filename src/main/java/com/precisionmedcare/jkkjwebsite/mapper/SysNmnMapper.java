package com.precisionmedcare.jkkjwebsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.precisionmedcare.jkkjwebsite.domain.NmnNmn;
import com.precisionmedcare.jkkjwebsite.providers.BackStageApiProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.HashMap;
import java.util.List;

public interface SysNmnMapper extends BaseMapper<NmnNmn> {
    @SelectProvider(type = BackStageApiProvider.class,method = "queryNmn")
    List<HashMap<String, Object>> queryNmn(@Param("keyword") String keyword);
}
