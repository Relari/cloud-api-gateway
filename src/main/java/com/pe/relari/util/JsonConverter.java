package com.pe.relari.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonConverter {

    private static final ObjectMapper mapper;

    private JsonConverter() {}

    static {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public static String convertToJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            String json = mapper.writeValueAsString(object);
            log.debug("Data in JSON = {}", json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Error convert data from JSON", e);
            return null;
        }
    }

    public static <T> T readDataFromJson(
            String file, Class<T> className) {
        if (file == null) {
            return null;
        }
        try {
            T value = mapper.readValue(file, className);
            log.debug("Data in Class[{}] = {}", className.getSimpleName(), value.toString());
            return value;
        } catch (JsonProcessingException e) {
            log.error("Error reading JSON from data", e);
            return null;
        }
    }
}
