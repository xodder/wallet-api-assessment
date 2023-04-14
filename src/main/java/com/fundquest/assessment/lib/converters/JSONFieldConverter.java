package com.fundquest.assessment.lib.converters;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JSONFieldConverter implements AttributeConverter<Object, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object value) {
        if (value == null) return null;

        try {
            return objectMapper.writeValueAsString(value);
        } catch (final JsonProcessingException e) {
            return null; // "JSON writing error";
        }
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        if (value == null) return null;

        try {
            return objectMapper.readValue(value, Object.class);
        } catch (final IOException e) {
            return null;
        }
    }
}
