package com.lzc.security.config.authentication.social;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConnectionJpaRepository extends JpaRepository<UserConnection, String> {

    UserConnection findByUserId(String userId);

    UserConnection findByProviderUserId(String providerUserId);
}
