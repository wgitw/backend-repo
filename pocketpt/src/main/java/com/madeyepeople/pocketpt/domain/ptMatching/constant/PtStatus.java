package com.madeyepeople.pocketpt.domain.ptMatching.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PtStatus {
    PENDING("pending"),
    ACTIVE("active");

    private final String key;

    @JsonValue
    public String getJsonValue() {
        return this.key;
    }
}
