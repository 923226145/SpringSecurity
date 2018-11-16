package com.lzc.security.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LzcLogoutSuccessHandler implements LogoutSuccessHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("退出登录");
        // 根据需要返回html页面或者json


        httpServletResponse.sendRedirect("/login/index");


//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 0);
//        map.put("msg", "退出成功");
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
