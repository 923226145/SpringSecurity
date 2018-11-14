package com.lzc.security.validate.code.sms;

import com.lzc.security.validate.code.ValidateCode;
import com.lzc.security.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<SmsCode> {

    private static final String SMS_CODE_PARAM_NAME = "mobile";

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    protected void send(ServletWebRequest request, SmsCode validateCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), SMS_CODE_PARAM_NAME);
        smsCodeSender.send(mobile, validateCode.getCode());
    }
}
