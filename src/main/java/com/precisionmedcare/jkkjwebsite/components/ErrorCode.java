package com.precisionmedcare.jkkjwebsite.components;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

public enum ErrorCode implements IErrorCode {
    FAILED(-1L, "操作失败"),
    SUCCESS(0L, "执行成功"),
    LOGIN_ACCOUNT_DOES_NOT_EXIST(10000, "登录失败:账号不存在，请重新尝试！"),
    LOGIN_WRONG_PASSWORD(10001, "登录失败:密码不正确！"),
    REGISTER_EMAIL_REGISTERED(10002, "该邮箱已经被注册，不能重复。"),
    REGISTER_USER_OBJECT_IS_EMPTY(10003, "错误消息：用户对象为空！"),
    EMAIL_REDIS_ERROR(10004, "redis异常"),
    USER_DETAIL_ALREADY_EXISTS(10005, "用户详细信息已存在"),
    ;

    private final long code;
    private final String msg;

    private ErrorCode(final long code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCode fromCode(long code) {
        ErrorCode[] ecs = values();
        ErrorCode[] var3 = ecs;
        int var4 = ecs.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ErrorCode ec = var3[var5];
            if (ec.getCode() == code) {
                return ec;
            }
        }

        return SUCCESS;
    }

    public long getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public String toString() {
        return String.format("{\"code\": %s, \"msg\":\"%s\"}", this.code, this.msg);
    }
}
