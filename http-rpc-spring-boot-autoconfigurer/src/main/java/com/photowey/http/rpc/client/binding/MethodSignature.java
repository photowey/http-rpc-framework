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
package com.photowey.http.rpc.client.binding;

import com.photowey.http.rpc.core.util.HRpcUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * RequestCommand
 * <p>
 * Such as:
 * String getUser(@RequestParam("uname") String user,
 * {@literal @}RequestParam Map<String, Object> params,
 * {@literal @}RequestParam Map<String, Object> params,
 * {@literal @}RequestParam("age") Integer age,
 * {@literal @}RequestParam Map<String Object> names,
 * {@literal @}RequestParam Long time,
 * {@literal @}RequestParam BigDecimal money,
 * {@literal @}RequestParam("current") Date current,
 * {@literal @}RequestBody ServiceInfo serviceInfo,
 * {@literal @}RequestHeader("token") String token,
 * {@literal @}RequestHeader String auth,
 * {@literal @}PathVariable("id") Long id,
 * {@literal @}PathVariable Long userId
 * );
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class MethodSignature {

    private final Class<?> returnType;

    private Map<String, Integer> queries = new TreeMap<>();
    private Map<String, String> aliasMap = new TreeMap<>();
    private List<Integer> queryMapIndex = new ArrayList<>();

    private Map<String, Integer> noAliasParamIndex = new HashMap<>();
    private Map<String, Integer> pathVariable = new HashMap<>();
    private Map<String, Integer> noAlisaPathIndex = new HashMap<>();

    private Map<String, Integer> headers = new HashMap<>();
    private Map<String, String> headerValues = new HashMap<>();
    private Map<String, Integer> noAlisaHeaderIndex = new HashMap<>();

    private Class<?> requestBodyType;
    private Object requestBody;
    private Integer bodyIndex;

    private String url;

    private String typeName;

    public MethodSignature(Method method, RequestCommand command) {
        this.returnType = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        if (null != genericReturnType) {
            // handle ResponseEntity
            // org.springframework.request.ResponseEntity<com.photowey.domain.HealthDTO>
            // com.photowey.domain.HealthDTO
            String typeName = genericReturnType.getTypeName();
            if (typeName.matches(".*<.*>$")) {
                this.typeName = HRpcUtils.extractBodyClazz(typeName);
            }
        }
        this.url = command.getUrl();
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Map<String, Integer> getQueries() {
        return queries;
    }

    public Map<String, String> getAliasMap() {
        return aliasMap;
    }

    public List<Integer> getQueryMapIndex() {
        return queryMapIndex;
    }

    public Map<String, Integer> getNoAliasParamIndex() {
        return noAliasParamIndex;
    }

    public Map<String, Integer> getPathVariable() {
        return pathVariable;
    }

    public Map<String, Integer> getNoAlisaPathIndex() {
        return noAlisaPathIndex;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }

    public Map<String, String> getHeaderValues() {
        return headerValues;
    }

    public Map<String, Integer> getNoAlisaHeaderIndex() {
        return noAlisaHeaderIndex;
    }

    public Class<?> getRequestBodyType() {
        return requestBodyType;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public Integer getBodyIndex() {
        return bodyIndex;
    }

    public String getUrl() {
        return url;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public void setRequestBodyType(Class<?> requestBodyType) {
        this.requestBodyType = requestBodyType;
    }

    public void setBodyIndex(Integer bodyIndex) {
        this.bodyIndex = bodyIndex;
    }

    public void clear() {
        this.queries.clear();
        this.aliasMap.clear();
        this.noAliasParamIndex.clear();
        this.pathVariable.clear();
        this.noAlisaPathIndex.clear();
        this.headers.clear();
        this.headerValues.clear();
        this.queryMapIndex.clear();
        this.noAlisaHeaderIndex.clear();
    }
}
