/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Peng Wan
 */
public abstract class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    // do initialization for object mapper
    static {

    }

    public static boolean validate(String jsonData) {
        return new JsonValidator().validate(jsonData);
    }

    public static String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> T toObject(String data, Class<? extends T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (JsonParseException e) {
            log.error("read json parse error {}", data, e);
        } catch (JsonMappingException e) {
            log.error("read json mapper error {}", data, e);
        } catch (IOException e) {
            log.error("read json io error {}", data, e);
        }
        return null;
    }

    public static <T> T toObject(byte[] data, TypeReference<T> ref) {
        try {
            return objectMapper.readValue(data, ref);
        } catch (JsonParseException e) {
            log.error("read json parse error {}", data, e);
        } catch (JsonMappingException e) {
            log.error("read json mapper error {}", data, e);
        } catch (IOException e) {
            log.error("read json io error {}", data, e);
        }
        return null;
    }

    public static <K, V> Map<K, V> toMap(byte[] data) {
        return toObject(data, new TypeReference<Map<K, V>>() {
        });
    }

    public static String getField(String jsonData, String name) {
        if (jsonData == null) {
            return null;
        }
        JsonNode rootNode = toObject(jsonData, JsonNode.class);
        if (rootNode == null) {
            log.error("readValue() return is null.jsonData={}", jsonData);
            return null;
        }
        JsonNode path = rootNode.path(name);
        if (!path.isMissingNode()) {
            return path.toString();
        } else {
            log.error("path is missing node, name={}", name);
            return null;
        }
    }

    public static <T> T toObject(String jsonData, String field, Class<T> clazz) {
        return toObject(getField(jsonData, field), clazz);
    }

}
