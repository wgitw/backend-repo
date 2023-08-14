package com.madeyepeople.pocketpt.domain.account.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CareerType {
    COMPETITION("competition"),
    LICENSE("license"),
    EXPERIENCE("experience");

    private final String value;

    public String getKey() {
        return name();
    }

    @JsonValue
    public String getJsonValue() {
        return this.value;
    }
}
