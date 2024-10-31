package com.example.Mangxahoi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }

    public static <T> T toObject(String dto, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(dto, valueType);
    }
}
