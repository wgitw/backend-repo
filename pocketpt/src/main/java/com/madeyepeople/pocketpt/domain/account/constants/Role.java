package com.madeyepeople.pocketpt.domain.account.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ADMIN("admin"),
    TRAINER("trainer"),
    TRAINEE("trainee");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @JsonValue
    public String getJsonValue() {
        return this.key;
    }
}
