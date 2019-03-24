package com.bell.storage.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, ANALYST;

    @Override
    public String getAuthority() {
        return name();
    }
}
