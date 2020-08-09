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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The Attribute root Interface
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public interface Attributes {

    /**
     * SET
     *
     * @param key   KEY
     * @param value VALUE
     */
    <T> void setAttribute(String key, T value);

    /**
     * SET Multi
     *
     * @param params params
     */
    void setAttributes(Map<String, Object> params);

    /**
     * GET
     *
     * @param key KEY
     * @param <T>
     * @return VALUE
     */
    <T> T getAttribute(String key);

    /**
     * GET
     *
     * @param key
     * @param supplier
     * @param <T>
     * @return
     */
    <T> T getAttribute(String key, Supplier<T> supplier);

    /**
     * GET Multi
     *
     * @param keys KEY List
     * @param <T>
     * @return
     */
    <T> Map<String, T> getAttributes(List<String> keys);

    /**
     * GET ALL
     *
     * @return
     */
    Map<String, Object> getAttributes();

}
