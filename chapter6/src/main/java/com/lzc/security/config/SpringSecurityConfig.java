package com.lzc.security.config;

import com.lzc.security.config.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.lzc.security.validate.code.image.ImageCodeFilter;
import com.lzc.security.validate.code.sms.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserService myUserService;

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;

    @Autowired
    private ImageCodeFilter imageCodeFilter;

    @Autowired
    private SmsCodeFilter smsCodeFilter;

    // 注入短信登录的相关配置
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true);
        // create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class) // 将ImageCodeFilter过滤器设置在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/authentication/*","/login/*","/code/*") // 不需要登录就可以访问
                .permitAll()
                .antMatchers("/user/**").hasAnyRole("USER") // 需要具有ROLE_USER角色才能访问
                .antMatchers("/admin/**").hasAnyRole("ADMIN") // 需要具有ROLE_ADMIN角色才能访问
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/authentication/login") // 访问需要登录才能访问的页面，如果未登录，会跳转到该地址来
                    .loginProcessingUrl("/authentication/form")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailHandler)
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(24*60*60) // 过期秒数
                    .userDetailsService(myUserService)

                ;
        http.apply(smsCodeAuthenticationSecurityConfig);
    }

    // 密码加密方式
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // 重写方法，自定义用户
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("lzc").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN","USER");
//        auth.inMemoryAuthentication().withUser("zhangsan").password(new BCryptPasswordEncoder().encode("123456")).roles("USER");
        auth.userDetailsService(myUserService); // 注入MyUserService，这样SpringSecurity会调用里面的loadUserByUsername(String s)
    }
}
