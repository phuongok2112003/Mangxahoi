package com.example.Mangxahoi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EbsConvertUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }
}
