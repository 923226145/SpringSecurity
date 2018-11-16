package com.lzc.security.config;

import com.lzc.security.dataobject.LoginUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
          //通过用户名s去数据库里查找用户以及用户权限
          // 然后返回User对象，注意，这里的User对象是org.springframework.security.core.userdetails.User
         // return new User(s,new BCryptPasswordEncoder().encode("123456"),AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER"));

        List<GrantedAuthority> authorityLists = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER");
        LoginUser loginUser = new LoginUser(s,new BCryptPasswordEncoder().encode("123456"),authorityLists);
        loginUser.setNickName("成");
        return loginUser;
    }

    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        //  通过手机号mobile去数据库里查找用户以及用户权限
        List<GrantedAuthority> authorityLists = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER");
        LoginUser loginUser = new LoginUser(mobile,new BCryptPasswordEncoder().encode("123456"),authorityLists);
        loginUser.setNickName("成");
        return loginUser;
    }
}
