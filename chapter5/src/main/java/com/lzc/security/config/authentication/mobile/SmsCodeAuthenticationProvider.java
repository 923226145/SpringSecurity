package com.lzc.security.config.authentication.mobile;

import com.lzc.security.config.MyUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private MyUserService myUserService;

    public MyUserService getMyUserService() {
        return myUserService;
    }

    public void setMyUserService(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken)authentication;
        UserDetails user = myUserService.loadUserByMobile((String)smsCodeAuthenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        result.setDetails(smsCodeAuthenticationToken.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
