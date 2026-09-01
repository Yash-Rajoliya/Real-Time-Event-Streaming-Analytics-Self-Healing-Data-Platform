package com.platform.analytics.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * Thread-safe centralized JSON serialization/deserialization helper wrapping Jackson ObjectMapper.
 */
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Serializes an object to a JSON string.
     */
    public static String toJson(Object object) {
        if (object == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Serializes an object to a byte array.
     */
    public static byte[] toJsonBytes(Object object) {
        if (object == null) return new byte[0];
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize object to byte array", e);
        }
    }

    /**
     * Deserializes a JSON string into a target object class.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) return null;
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize JSON to " + clazz.getName(), e);
        }
    }

    /**
     * Deserializes a JSON string into complex generic structures using TypeReference.
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isBlank()) return null;
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize JSON with TypeReference", e);
        }
    }

    /**
     * Reads a JSON stream into a target object class.
     */
    public static <T> T fromInputStream(InputStream inputStream, Class<T> clazz) {
        Objects.requireNonNull(inputStream, "InputStream cannot be null");
        try {
            return OBJECT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse JSON from stream to " + clazz.getName(), e);
        }
    }

    /**
     * Converts a Java bean/object into a Map<String, String>.
     */
    public static Map<String, String> toMap(Object object) {
        if (object == null) return Map.of();
        return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, String>>() {});
    }

    /**
     * Exposes shared configured ObjectMapper instance.
     */
    public static ObjectMapper getMapper() {
        return OBJECT_MAPPER;
    }
}