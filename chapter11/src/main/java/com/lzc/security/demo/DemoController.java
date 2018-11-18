package com.lzc.security.demo;

import com.lzc.security.dataobject.LoginUser;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/demo1")
    public Object admin(Principal principal) {
        return principal;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER') and principal.username.equals(#username)")
    @GetMapping("/test/{username}")
    public Object test(@PathVariable String username) {
        return "Hello test";
    }

    // 这里的returnObject就代表返回的对象
    @PostAuthorize("returnObject.username.equals(principal.username)")
    @GetMapping("/demo2")
    public Object demo2() {
        User user = new User("lzc","lzc",AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        return user;
    }
}
