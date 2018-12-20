package com.lzc.security.authorize;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RbacService  {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
