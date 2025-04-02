package com.github.kmu_wink.seoul_in_culture.common.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAuthentication implements Authentication {

    private final User user;

    @Getter
    private final boolean authenticated = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of();
    }

    @Override
    public String getCredentials() {

        return null;
    }

    @Override
    public Object getDetails() {

        return null;
    }

    @Override
    public User getPrincipal() {

        return user;
    }

    @Override
    public String getName() {

        return user.getId();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {

        throw new UnsupportedOperationException();
    }
}
