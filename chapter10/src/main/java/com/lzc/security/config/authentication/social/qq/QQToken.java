package com.lzc.security.config.authentication.social.qq;

import lombok.Data;

@Data
public class QQToken {
    private String accessToken;

    private Integer expiresIn;

    private String refreshToken;
}
