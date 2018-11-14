package com.lzc.security.validate.code.sms;



import com.lzc.security.validate.code.ValidateCode;
import com.lzc.security.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


@Component
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ValidateCode generator(ServletWebRequest httpServletRequest) {
        String code = RandomStringUtils.randomNumeric(6);
        return new SmsCode(httpServletRequest.getRequest().getParameter("mobile"),code, 60*5);
    }
}
