package com.precisionmedcare.jkkjwebsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.precisionmedcare.jkkjwebsite.domain.NmnRole;
import com.precisionmedcare.jkkjwebsite.providers.BackStageApiProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<NmnRole> {
    @SelectProvider(type = BackStageApiProvider.class, method = "getRoleByUserName")
    List<String> getRoleByUserName(@Param("username") String username);
}
