package com.lzc.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }


//    使用上述配置后，我们可以将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID。
//    而CookiePath设置为根路径且配置了相关的正则表达式，可以达到同父域下的单点登录的效果，
//    在未涉及跨域的单点登录系统中，这是一个非常优雅的解决方案。
//    如果我们的当前域名是moe.cnkirito.moe，该正则会将Cookie设置在父域cnkirito.moe中，
//    如果有另一个相同父域的子域名blog.cnkirito.moe也会识别这个Cookie，
//    便可以很方便的实现同父域下的单点登录。
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }

}
