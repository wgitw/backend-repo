package com.madeyepeople.pocketpt.domain.historicalData.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ScopeEnumConverter implements AttributeConverter<Scope, String> {

    @Override
    public String convertToDatabaseColumn(Scope attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public Scope convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Scope.valueOf(dbData.toUpperCase());
    }
}
