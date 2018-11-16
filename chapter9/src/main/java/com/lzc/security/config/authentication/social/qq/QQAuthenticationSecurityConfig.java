package com.lzc.security.config.authentication.social.qq;

import com.lzc.security.config.MyAuthenticationFailHandler;
import com.lzc.security.config.MyAuthenticationSuccessHandler;
import com.lzc.security.config.authentication.social.UserConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class QQAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private MyAuthenticationFailHandler myAuthenticationFailHandler;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private UserConnectionService userConnectionService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        QQAuthenticationFilter qqAuthenticationFilter = new QQAuthenticationFilter();
        qqAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        qqAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        qqAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailHandler);

        QQAuthenticationProvider qqAuthenticationProvider = new QQAuthenticationProvider();
        qqAuthenticationProvider.setUserConnectionService(userConnectionService);

        http.authenticationProvider(qqAuthenticationProvider)
            .addFilterAfter(qqAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
