package com.precisionmedcare.jkkjwebsite.components;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;

public class CustomException {

    public static R<String> exception(Exception e){
        JSONObject jsonObject = JSONUtil.parseObj(e.getMessage());
        long code = Long.parseLong(jsonObject.get("code").toString());
        String msg = jsonObject.get("msg").toString();
        return R.failed(new IErrorCode() {
            @Override
            public long getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        });
    }

}
