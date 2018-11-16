package com.lzc.security.demo;

import com.lzc.security.dataobject.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/index")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/info")
    public String info() {
        return "admin/info";
    }

    @GetMapping("/users")
    public Object users() {
        return sessionRegistry.getAllPrincipals();
    }

    @GetMapping("/removeUserSessionByUsername")
    public Object removeUserSessionByUsername(@RequestParam String username) {
        List<Object> users = sessionRegistry.getAllPrincipals(); // 获取session中所有的用户信息
        for (Object principal : users) {
            if (principal instanceof LoginUser) {
                final LoginUser loggedUser = (LoginUser) principal;
                if (username.equals(loggedUser.getUsername())) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false); // false代表不包含过期session
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow(); // 使session过期
                        }
                    }
                }
            }
        }
        return "操作成功";
    }

    // 查看所有用户session信息
    @GetMapping("/getAllUserSession")
    public Object getAllUserSession() {
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof LoginUser) {
                List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, true);
                // 有每个用户可以在多台电脑上登录，所以可能存在多个session
                if (null != sessionsInfo && sessionsInfo.size() > 0) {
                    for (SessionInformation sessionInformation : sessionsInfo) {
                        LoginUser loginUser = (LoginUser) principal;
                        System.out.println("用户名:" + loginUser.getUsername() + "," + "是否失效:" + sessionInformation.isExpired() + ", sessionid:" + sessionInformation.getSessionId());
                    }
                }
            }
        }
        return sessionRegistry.getAllPrincipals();
    }
}
