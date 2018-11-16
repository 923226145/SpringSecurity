package com.lzc.security.config.authentication.social;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserConnection {

    @Id
    private String userId;

    private String providerId;

    private String providerUserId;

    private String displayName;

    private String profile_url;

    private String imageUrl;

    private String accessToken;

    private String secret;

    private String refreshToken;
}
