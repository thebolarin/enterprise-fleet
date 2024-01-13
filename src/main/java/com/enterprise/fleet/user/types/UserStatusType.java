package com.enterprise.fleet.user.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusType {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    ;

    private final String UserStatusType;
}
