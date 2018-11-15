package com.lzc.security;

import com.lzc.security.config.authentication.social.UserConnection;
import com.lzc.security.config.authentication.social.UserConnectionJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {

    @Autowired
    private UserConnectionJpaRepository repository;

    @Test
    public void contextLoads() {
        Date date = new Date(7776000);
        System.out.println(date.toString());
    }

}
