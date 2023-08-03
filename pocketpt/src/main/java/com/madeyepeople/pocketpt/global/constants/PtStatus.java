package com.madeyepeople.pocketpt.global.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PtStatus {
    WAIT("wait"),
    ACTIVE("active");

    private final String key;

    PtStatus(String key) {
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
