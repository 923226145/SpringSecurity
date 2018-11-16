package com.lzc.security.config.authentication.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConnectionService {

    @Autowired
    private UserConnectionJpaRepository repository;

    public UserConnection save(UserConnection userConnection) {
        return repository.save(userConnection);
    }

    public UserConnection findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public UserConnection findByProviderUserId(String providerUserId) {
        return repository.findByProviderUserId(providerUserId);
    }
}
