package com.example.banhangapi.api.globalEnum;

public enum ROLES {
    ROLE_ADMIN,
    ROLE_USER;

    public static ROLES fromInt(int index) {
        ROLES[] values = ROLES.values();
        if (index < 0 || index >= values.length) {
            throw new IllegalArgumentException("Invalid role index: " + index);
        }
        return values[index];
    }
}
