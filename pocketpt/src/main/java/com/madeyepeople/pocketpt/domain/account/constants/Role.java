package com.madeyepeople.pocketpt.domain.account.constants;

public enum Role {

    ADMIN("admin"),
    TRAINER("trainer"),
    TRAINEE("trainee");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
