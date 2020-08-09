/*
 * Copyright © 2020 photowey (photowey@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.photowey.http.rpc.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.photowey.http.rpc.core.exception.HRpcException;
import org.springframework.util.Assert;

import java.util.List;

/**
 * JSON  Utils
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class JsonUtils {

    private JsonUtils() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static ObjectMapper getInstance() {
        return ObjectMapperFactory.OBJECT_MAPPER;
    }

    public static <T> T toBean(String json, Class<T> type) {
        return toBean(json, type, getInstance());
    }

    public static <T> T toBean(String json, Class<T> type, ObjectMapper objectMapper) {
        Assert.hasText(json, "the target json content can't be blank");
        Assert.notNull(type, "the target type can't be null");
        Assert.notNull(objectMapper, "the target ObjectMapper can't be null");

        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new HRpcException("parse the json Str to Bean exception", e);
        }
    }

    public static <T> List<T> toBeans(String json, Class<T> type) {
        return toBeans(json, type, getInstance());
    }

    public static <T> List<T> toBeans(String json, Class<T> type, ObjectMapper objectMapper) {
        Assert.hasText(json, "the target json content can't be blank");
        Assert.notNull(type, "the target type can't be null");
        Assert.notNull(objectMapper, "the target ObjectMapper can't be null");
        try {
            List<T> beans = objectMapper.readValue(json, new TypeReference<List<T>>() {
            });
            return beans;
        } catch (Exception e) {
            throw new HRpcException("parse the json Str to Beans exception", e);
        }
    }

    public static <T> String toJSONString(T source) {
        return toJSONString(source, getInstance());
    }

    public static <T> String toJSONString(T source, ObjectMapper objectMapper) {
        Assert.notNull(source, "the target source can't be null");
        Assert.notNull(objectMapper, "the target ObjectMapper can't be null");
        try {
            return objectMapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new HRpcException("handle the Bean to json Str exception", e);
        }
    }

    // ================================================================ OBJECT_MAPPER

    private static class ObjectMapperFactory {
        public static ObjectMapper OBJECT_MAPPER = defaultJsonMapper();
    }

    private static ObjectMapper defaultJsonMapper() {
        return defaultJsonMapper(null);
    }

    private static ObjectMapper defaultJsonMapper(PropertyNamingStrategy strategy) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (HRpcUtils.isNotEmpty(strategy)) {
            objectMapper.setPropertyNamingStrategy(strategy);
        }

        return objectMapper;
    }

}
