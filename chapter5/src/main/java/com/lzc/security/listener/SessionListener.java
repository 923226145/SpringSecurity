package com.lzc.security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
@Component
@Slf4j
public class SessionListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        log.info("--attributeAdded--");
        HttpSession session=httpSessionBindingEvent.getSession();
        log.info("key----:"+httpSessionBindingEvent.getName());
        log.info("value---:"+httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        log.info("--attributeRemoved--");
        HttpSession session = httpSessionBindingEvent.getSession();
        log.info("key----:"+httpSessionBindingEvent.getName());
        log.info("value---:"+httpSessionBindingEvent.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        log.info("--attributeReplaced--");
        String oldName = httpSessionBindingEvent.getName();
        log.info("--old key--"+oldName);
        log.info("--old value--"+httpSessionBindingEvent.getValue());
        HttpSession session = httpSessionBindingEvent.getSession();
        log.info("new value---:"+session.getAttribute(oldName));
    }
}
