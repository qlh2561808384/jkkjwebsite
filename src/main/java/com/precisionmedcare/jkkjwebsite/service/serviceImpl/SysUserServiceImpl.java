package com.precisionmedcare.jkkjwebsite.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.precisionmedcare.jkkjwebsite.components.BCryptPasswordEncoderUtil;
import com.precisionmedcare.jkkjwebsite.components.ErrorCode;
import com.precisionmedcare.jkkjwebsite.domain.NmnUser;
import com.precisionmedcare.jkkjwebsite.domain.NmnUserDetails;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserDetailMapper;
import com.precisionmedcare.jkkjwebsite.mapper.SysUserMapper;
import com.precisionmedcare.jkkjwebsite.service.SysUserDetailService;
import com.precisionmedcare.jkkjwebsite.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, NmnUser> implements SysUserService {

    private static final long USER_STATUS = 0;
    private static final long USER_STATUS_fail = 1;

    @Autowired
    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;

    @Autowired
    SysUserDetailMapper sysUserDetailMapper;
    @Autowired
    SysUserDetailService sysUserDetailService;

    @ApiOperation(value = "根据用户名获取用户信息")
    @Override
    public NmnUser getUserByUserName(String username) {
        LambdaQueryWrapper<NmnUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(NmnUser::getEmail, username);
        lambdaQueryWrapper.eq(NmnUser::getStatus, USER_STATUS);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean checkLogin(String username, String password) throws Exception{
        NmnUser nmnUser = this.getUserByUserName(username);
        if (nmnUser == null) {
            throw new Exception(ErrorCode.LOGIN_ACCOUNT_DOES_NOT_EXIST.toString());
        }else {
            String encodedPassword = nmnUser.getPassword();//加密的密码
            if (!bCryptPasswordEncoderUtil.matches(password, encodedPassword)) {//和加密后的密码进行比配
                throw new Exception(ErrorCode.LOGIN_WRONG_PASSWORD.toString());
            }else {
                return true;
            }
        }
    }

    @Override
    public boolean register(Map<String, Object> map) throws Exception {
        if (!map.isEmpty()) {
            NmnUser nmnUser = this.getUserByUserName(map.get("user").toString());
            if (nmnUser != null) {
                if (map.get("user").toString().equals(nmnUser.getEmail())) {
                    throw new Exception(ErrorCode.REGISTER_EMAIL_REGISTERED.toString());
                }
            }
            return this.save(map);//保存到数据库
        }else{
            throw new Exception(ErrorCode.REGISTER_USER_OBJECT_IS_EMPTY.toString());
        }
    }

    /**
     *
     * @param map
     * @return
     */
    public boolean save(Map<String, Object> map) {
        NmnUser nmnUser = new NmnUser();
        String username = map.get("user").toString();
        String password = map.get("pass").toString();
        nmnUser.setPassword(bCryptPasswordEncoderUtil.encode(password));   //对密码进行加密
        nmnUser.setEmail(username);
        nmnUser.setStatus(USER_STATUS);
        return this.save(nmnUser);
    }

    @Override
    public List<HashMap<String, Object>> queryAllUser(String keyword) {
        return this.baseMapper.queryAllUser(keyword);
    }

    @Override
    public boolean modifyUserAndUserDetails(Map<String, Object> map) {
        if (!map.isEmpty()) {
            boolean saveUser = modifyUser(map);
            boolean saveUserDetails = modifyUserDetails(map);
            return saveUser && saveUserDetails;
        }else {
            return false;
        }

    }

    private boolean modifyUserDetails(Map<String, Object> map) {
        LambdaUpdateWrapper<NmnUserDetails> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(NmnUserDetails::getEmail, map.get("detailsEmail").toString());
        lambdaUpdateWrapper.set(NmnUserDetails::getAddress, map.get("address").toString());
        lambdaUpdateWrapper.set(NmnUserDetails::getPhone, map.get("phone").toString());
        lambdaUpdateWrapper.set(NmnUserDetails::getIdcard, map.get("idcard").toString());
        lambdaUpdateWrapper.set(NmnUserDetails::getName, map.get("name").toString());
        lambdaUpdateWrapper.eq(NmnUserDetails::getId, map.get("userdetailsid").toString());
        return sysUserDetailService.saveUserDetails(lambdaUpdateWrapper);
    }

    private boolean modifyUser(Map<String, Object> map) {
        LambdaUpdateWrapper<NmnUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(NmnUser::getEmail, map.get("email").toString());
        lambdaUpdateWrapper.set(NmnUser::getNickname, map.get("nickname").toString());
        lambdaUpdateWrapper.eq(NmnUser::getId, map.get("userid").toString());
        return this.update(lambdaUpdateWrapper);
    }
}
