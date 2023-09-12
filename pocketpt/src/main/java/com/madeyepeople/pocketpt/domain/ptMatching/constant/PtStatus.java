package com.madeyepeople.pocketpt.domain.ptMatching.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PtStatus {
    PENDING("pending"),
    ACTIVE("active"),
    EXPIRED("expired"),
    REJECTED("rejected");

    private final String value;

    public String getKey() {
        return name();
    }

    @JsonValue
    public String getJsonValue() {
        return this.value;
    }
}
