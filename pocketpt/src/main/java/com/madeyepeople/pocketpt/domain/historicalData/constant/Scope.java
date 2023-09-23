package com.madeyepeople.pocketpt.domain.historicalData.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Scope {
    PUBLIC("public"),
    PRIVATE("private");

    private final String value;

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }

    @JsonValue
    public String getJsonValue() {
        return this.value;
    }
}
