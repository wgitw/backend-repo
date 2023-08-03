package com.madeyepeople.pocketpt.domain.ptMatching.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// TODO: 현재 Enum마다 converter 필요. AbstractEnumCodeAttributeConverter로 추상화 필요
@Converter(autoApply = true)
public class PtStatusEnumConverter implements AttributeConverter<PtStatus, String> {

    @Override
    public String convertToDatabaseColumn(PtStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public PtStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return PtStatus.valueOf(dbData.toUpperCase());
    }
}
