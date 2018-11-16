package com.lzc.security.config.authentication.social.qq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzc.security.config.authentication.social.UserConnection;
import com.lzc.security.config.authentication.social.UserConnectionService;
import com.lzc.security.dataobject.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Slf4j
public class QQAuthenticationProvider implements AuthenticationProvider {

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    // 获取 QQ 登录信息的 API 地址
    private final static String userInfoUri = "https://graph.qq.com/user/get_user_info";

    // 获取 QQ 用户信息的地址拼接
    private final static String USER_INFO_API = "%s?access_token=%s&oauth_consumer_key=%s&openid=%s";

    private UserConnectionService userConnectionService;

    public UserConnectionService getUserConnectionService() {
        return userConnectionService;
    }

    public void setUserConnectionService(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("进入QQAuthenticationProvider");
        QQAuthenticationToken qqAuthenticationToken = (QQAuthenticationToken)authentication;
        QQToken qqToken = (QQToken)authentication.getPrincipal();
        String openid = (String)authentication.getCredentials();

        List<GrantedAuthority> authorityLists = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER");
        LoginUser loginUser = null;

        // 首先判断该用户是否已经使用QQ授权过
        UserConnection userConnection = userConnectionService.findByProviderUserId(openid);
        if(userConnection != null && userConnection.getProviderId().equals("qq")) {
            // 已经使用qq账号登录过
            log.info("【用户已经使用QQ授权过】");
            // 通过userId去数据库里获取用户，这里省略通过数据获取用户的过程
            loginUser = new LoginUser(userConnection.getUserId(),new BCryptPasswordEncoder().encode("123456"),authorityLists);
        } else {
            // 通过openid去QQ获取用户信息
            String url = String.format(USER_INFO_API, userInfoUri, qqToken.getAccessToken(), QQAuthenticationFilter.clientId, openid);
            String result = restTemplate.getForObject(url, String.class);
            log.info("【QQ_URL_GET_USER_INFO" + result);
            try {
                QQUserInfo userInfo = objectMapper.readValue(result, QQUserInfo.class);
                userConnection = new UserConnection();
                userConnection.setProviderId("qq");
                userConnection.setAccessToken(qqToken.getAccessToken());
                userConnection.setRefreshToken(qqToken.getRefreshToken());
                userConnection.setProviderUserId(openid);
                userConnection.setImageUrl(userInfo.getFigureurl_1());
                userConnection.setUserId(userInfo.getNickname());
                userConnectionService.save(userConnection); // 将新用户信息保存到数据库
                // 这里省略了一步，应该把新用户注册到自己的用户表中
                log.info("【新用户注册】" + userInfo.getNickname());
                loginUser = new LoginUser(userConnection.getUserId(),new BCryptPasswordEncoder().encode("123456"),authorityLists);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        QQAuthenticationToken result = new QQAuthenticationToken(loginUser, authorityLists);
        result.setDetails(qqAuthenticationToken.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return QQAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
