package com.lzc.security.validate.code.sms;

import com.lzc.security.validate.code.ValidateCode;

import java.time.LocalDateTime;

public class SmsCode extends ValidateCode {
    private String mobile;

    public SmsCode(String mobile, String code, int expireTime) {
        super(code, expireTime);
        this.mobile = mobile;
    }

    public SmsCode(String mobile, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
