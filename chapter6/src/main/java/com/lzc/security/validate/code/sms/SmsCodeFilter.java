package com.lzc.security.validate.code.sms;

import com.lzc.security.validate.code.ValidateCode;
import com.lzc.security.validate.code.ValidateCodeProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private Set<String> urls = new HashSet<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        // 这里配置需要拦截的地址 ......
        urls.add("/authentication/mobile"); //

    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url : urls) {
            if (antPathMatcher.match(url, httpServletRequest.getRequestURI())) {
                action = true;
                break;
            }
        }
        if (action) {
            try {
                validate(httpServletRequest);
            } catch (SmsCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void validate(HttpServletRequest request) {
        SmsCode smsCode = (SmsCode)request.getSession().getAttribute(ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
        String smsCodeRequest = request.getParameter("smsCode");
        if (smsCodeRequest == null || smsCodeRequest.isEmpty()) {
            throw new SmsCodeException("短信验证码不能为空");
        }
        if (smsCode == null) {
            throw new SmsCodeException("短信验证码不存在");
        }
        if (smsCode.isExpired()) {
            request.getSession().removeAttribute(ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
            throw new SmsCodeException("短信验证码已过期");
        }
        if(!smsCodeRequest.equalsIgnoreCase(smsCode.getCode())) {
            throw new SmsCodeException("短信验证码错误");
        }
        if(!smsCode.getMobile().equals(request.getParameter("mobile"))) {
            throw new SmsCodeException("输入的手机号与发送短信验证码的手机号不一致");
        }
        request.getSession().removeAttribute(ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
    }
}
