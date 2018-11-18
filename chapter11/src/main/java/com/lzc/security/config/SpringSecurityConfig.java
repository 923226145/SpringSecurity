package com.lzc.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserService myUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authentication/*","/login") // 不需要登录就可以访问
                .permitAll()
                //.antMatchers("/user/**").hasAnyRole("USER") // 需要具有ROLE_USER角色才能访问
                //.antMatchers("/admin/**").access("hasAnyRole('ADMIN') and hasAnyRole('USER')") // 需要具有ROLE_ADMIN和ROLE_USER角色才能访问
                .anyRequest().authenticated()
                // .anyRequest().access("@rbacService.hasPermission(request, authentication)")
                .and()
                    .formLogin()
                    .loginPage("/authentication/login") // 设置登录页面
                    .loginProcessingUrl("/authentication/form")
                    .defaultSuccessUrl("/user/index") // 设置默认登录成功后跳转的页面
                ;
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
