package com.precisionmedcare.jkkjwebsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.providers.BackStageApiProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.HashMap;
import java.util.List;

public interface SysUserMapper extends BaseMapper<NmnUser> {
    @SelectProvider(type = BackStageApiProvider.class, method = "queryAllUser")
    List<HashMap<String, Object>> queryAllUser(@Param("keyword")String keyword);
}
