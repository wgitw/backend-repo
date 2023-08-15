package com.madeyepeople.pocketpt.domain.account.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CareerTypeEnumConverter implements AttributeConverter<CareerType, String> {

    @Override
    public String convertToDatabaseColumn(CareerType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public CareerType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return CareerType.valueOf(dbData.toUpperCase());
    }
}
