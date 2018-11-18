package com.lzc.security.authorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object o = authentication.getPrincipal();
        if (o instanceof UserDetails) {
            String username = ((UserDetails)o).getUsername();
            // 读取用户拥有的资源
            Set<String> urls = new HashSet<>();
            urls.add("/admin/**");
            // 判断用户是否拥有资源
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    return true;
                }
            }
        }
        return false;
    }
}
