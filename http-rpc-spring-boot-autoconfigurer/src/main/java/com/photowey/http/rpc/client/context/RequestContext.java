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
package com.photowey.http.rpc.client.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Request Context
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class RequestContext implements Attributes {

    /**
     * Query params
     */
    private Map<String, Object> queries = new HashMap<>();
    /**
     * Header params
     */
    private Map<String, Object> headers = new HashMap<>();

    // ==================================================================== ATTRIBUTE

    @Override
    public void setAttribute(String key, Object value) {
        this.queries.put(key, value);
    }

    @Override
    public void setAttributes(Map<String, Object> queries) {
        Set<Map.Entry<String, Object>> entries = queries.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            this.setAttribute(key, value);
        }
    }

    @Override
    public <T> T getAttribute(String key) {
        return (T) this.queries.get(key);
    }

    @Override
    public <T> T getAttribute(String key, Supplier<T> supplier) {
        if (this.queries.containsKey(key)) {
            return this.getAttribute(key);
        }
        T expect = supplier.get();

        this.queries.put(key, expect);

        return expect;
    }

    @Override
    public <T> Map<String, T> getAttributes(List<String> keys) {
        Map<String, T> attributes = new HashMap<>();
        for (String key : keys) {
            attributes.put(key, this.getAttribute(key));
        }

        return attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.queries;
    }

    // ==================================================================== HEADER

    public <T> void setHeader(String key, T value) {
        this.queries.put(key, value);
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    // ==================================================================== CLEAR

    public void clear() {
        // help GC
        this.queries.clear();
        this.headers.clear();
    }
}
