package com.lzc.security.config.authentication.social.qq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class QQAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    private RestTemplate restTemplate = new RestTemplate();

    private final static String CODE = "code";
    // 获取 Token 的 API
    private final static String accessTokenUri = "https://graph.qq.com/oauth2.0/token";
    // grant_type 由腾讯提供
    private final static String grantType = "authorization_code";
    // client_id 由腾讯提供
    static final String clientId = "101518082";
    // client_secret 由腾讯提供
    private final static String clientSecret = "2afe65a57fb3a9a39ff02259d27f1533";
    // redirect_uri 腾讯回调地址
    private final static String redirectUri = "http://lizhencheng.xyz/login/qq";
    // 获取 OpenID 的 API 地址
    private final static String openIdUri = "https://graph.qq.com/oauth2.0/me?access_token=";
    // 获取 token 的地址拼接
    private final static String TOKEN_ACCESS_API = "%s?grant_type=%s&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    public QQAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login/qq", "GET"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String code = request.getParameter(CODE);
        log.info("【code】" + code);
        String tokenAccessApi = String.format(TOKEN_ACCESS_API, accessTokenUri, grantType, clientId, clientSecret, code, redirectUri);
        try {
            QQToken qqToken = getToken(tokenAccessApi);
            String openid = getOpenId(qqToken.getAccessToken());
            QQAuthenticationToken authRequest = new QQAuthenticationToken(qqToken,openid);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new AuthenticationServiceException("QQ授权异常");
        }
    }

    private QQToken getToken(String tokenAccessApi) throws Exception{
        String token_result = restTemplate.getForObject(tokenAccessApi, String.class); // 返回结果 access_token=98E0766B8C353733E47A4EF6B8D4F130&expires_in=7776000&refresh_token=2ECA2A5B19B2B8C98EDF65C567900E87
        String []results = token_result.split("&");
        log.info("【get_token_result】" + token_result);
        QQToken qqToken = new QQToken();
        qqToken.setAccessToken(results[0].replace("access_token=",""));
        qqToken.setExpiresIn(Integer.valueOf(results[1].replace("expires_in=","")));
        qqToken.setRefreshToken(results[2].replace("refresh_token=",""));
        return qqToken;
    }

    private String getOpenId(String accessToken) throws Exception{
        String url = openIdUri + accessToken;
        String get_openid_result = restTemplate.getForObject(url, String.class);
        log.info("【QQ_OPEN_ID】 RESULT={}", get_openid_result);
        // RESULT=callback( {"client_id":"101518082","openid":"ACF3CFFDF17CB8F9A14AEEE595C0A1AB"} );
        String openid = StringUtils.substringBetween(get_openid_result, "\"openid\":\"", "\"}");
        return openid;
    }

    protected void setDetails(HttpServletRequest request, QQAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
