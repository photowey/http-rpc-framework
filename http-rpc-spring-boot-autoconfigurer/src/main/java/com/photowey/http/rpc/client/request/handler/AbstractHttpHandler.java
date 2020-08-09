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
package com.photowey.http.rpc.client.request.handler;

import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.context.RequestContext;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractHttpHandler
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public abstract class AbstractHttpHandler implements HttpHandler {

    /**
     * The HRpcClient Interface
     */
    protected Class<?> targetProxy;

    private final Map<Method, ClientMethod> methodCache = new ConcurrentHashMap<>();

    protected HRpcConfiguration hrpcConfiguration;

    public AbstractHttpHandler(HRpcConfiguration hrpcConfiguration) {
        this.hrpcConfiguration = hrpcConfiguration;
    }

    // =============================================================================
    @Override
    public Object handleRequest(Class<?> targetProxy, Method target, Object[] args) throws HRpcException {
        Annotation annotation = this.handleAnnotation(target);
        ClientMethod clientMethod = this.cachedClientMethod(target, annotation);
        RequestContext context = this.hrpcConfiguration.getRequestContextFactory().createContext();
        MethodSignature methodSignature = this.hrpcConfiguration.handleAll(clientMethod.getMethod(), target);
        try {
            this.preInterceptAll(clientMethod, methodSignature, context);
            this.hrpcConfiguration.interceptAll(methodSignature, context);
            this.postInterceptAll(clientMethod, methodSignature, context);

            this.handleQuery(methodSignature, args);
            this.handlePath(methodSignature, args);
            this.handleBody(methodSignature, args);
            this.handleHeader(methodSignature, args);

            this.doRequest(clientMethod);

            Object response = clientMethod.execute();
            return response;
        } catch (HRpcException e) {
            throw e;
        } catch (Exception e) {
            throw new HRpcException("http executor invoke remote:[{}] exception", clientMethod.getCommand().getMethodName(), e);
        } finally {
            context.clear();
            methodSignature.clear();
        }
    }

    public abstract Annotation handleAnnotation(Method target);

    public void preInterceptAll(ClientMethod clientMethod, MethodSignature methodSignature, RequestContext context) {

    }

    public void postInterceptAll(ClientMethod clientMethod, MethodSignature methodSignature, RequestContext context) {

    }

    public void doRequest(ClientMethod clientMethod) throws HRpcException {
        // do something for sub-class
    }

    // ================================================================================================================= REQUEST PARAM

    public void handleQuery(MethodSignature methodSignature, Object[] args) {
        String url = methodSignature.getUrl();
        // 1.处理 普通的请求参数
        // 1.1.处理带 别名的参数
        Map<String, Integer> queries = methodSignature.getQueries();
        Set<Map.Entry<String, Integer>> queryEntries = queries.entrySet();
        for (Map.Entry<String, Integer> queryEntry : queryEntries) {
            String key = queryEntry.getKey();
            Integer paramIndex = queryEntry.getValue();
            url = url.replaceAll("\\{" + key + "}", String.valueOf(args[paramIndex]));
        }
        // 1.2.处理不带别名的
        StringBuilder builder = new StringBuilder(url);
        Map<String, Integer> noAliasParamIndex = methodSignature.getNoAliasParamIndex();
        Set<Map.Entry<String, Integer>> noAlisaEntries = noAliasParamIndex.entrySet();
        for (Map.Entry<String, Integer> noAlisaEntry : noAlisaEntries) {
            String key = noAlisaEntry.getKey();
            Integer paramIndex = noAlisaEntry.getValue();
            HRpcUtils.populateQuery(builder, key, String.valueOf(args[paramIndex]));
        }

        // 2.处理 MAP 对象 请求参数
        List<Integer> queryMapIndex = methodSignature.getQueryMapIndex();
        for (Integer index : queryMapIndex) {
            Map<String, Objects> mapParams = (Map<String, Objects>) args[index];
            Set<Map.Entry<String, Objects>> entries = mapParams.entrySet();
            for (Map.Entry<String, Objects> entry : entries) {
                String key = entry.getKey();
                String valueStr = String.valueOf(entry.getValue());
                HRpcUtils.populateQuery(builder, key, valueStr);
            }
        }

        methodSignature.setUrl(builder.toString());

    }

    // ================================================================================================================= PATH

    public void handlePath(MethodSignature methodSignature, Object[] args) {
        // 1.处理 普通的路径参数
        this.populatePaths(methodSignature, args);
        // 2.处理 没别名的路径参数
        this.populateNoAlisaPaths(methodSignature, args);
    }

    public void populatePaths(MethodSignature methodSignature, Object[] args) {
        Map<String, Integer> pathVariable = methodSignature.getPathVariable();
        Set<Map.Entry<String, Integer>> entries = pathVariable.entrySet();
        String url = methodSignature.getUrl();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer pathIndex = entry.getValue();
            url = url.replaceAll("\\{" + key + "}", String.valueOf(args[pathIndex]));
        }

        methodSignature.setUrl(url);
    }

    public void populateNoAlisaPaths(MethodSignature methodSignature, Object[] args) {
        Map<String, Integer> noAlisaPathIndex = methodSignature.getNoAlisaPathIndex();
        Set<Map.Entry<String, Integer>> entries = noAlisaPathIndex.entrySet();
        String url = methodSignature.getUrl();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer pathIndex = entry.getValue();
            url = url.replaceAll("\\{" + key + "}", String.valueOf(args[pathIndex]));
        }

        methodSignature.setUrl(url);
    }

    // ================================================================================================================= BODY

    public void handleBody(MethodSignature methodSignature, Object[] args) {
        Integer bodyIndex = methodSignature.getBodyIndex();
        if (null != bodyIndex) {
            Object requestBody = args[bodyIndex];
            methodSignature.setRequestBody(requestBody);
        }
    }

    // ================================================================================================================= HEADER

    public void handleHeader(MethodSignature methodSignature, Object[] args) {
        Map<String, String> headerValues = methodSignature.getHeaderValues();
        this.handleHeaders(methodSignature, args, headerValues);
        this.handleNoAlisaHeaders(methodSignature, args, headerValues);
    }

    public void handleHeaders(MethodSignature methodSignature, Object[] args, Map<String, String> headerValues) {
        Map<String, Integer> headers = methodSignature.getHeaders();
        Set<Map.Entry<String, Integer>> entries = headers.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer headerIndex = entry.getValue();
            headerValues.put(key, String.valueOf(args[headerIndex]));
        }
    }

    public void handleNoAlisaHeaders(MethodSignature methodSignature, Object[] args, Map<String, String> headerValues) {
        Map<String, Integer> noAlisaHeaderIndex = methodSignature.getNoAlisaHeaderIndex();
        Set<Map.Entry<String, Integer>> entries = noAlisaHeaderIndex.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            Integer headerIndex = entry.getValue();
            headerValues.put(key, String.valueOf(args[headerIndex]));
        }
    }

    public Class<?> getTargetProxy() {
        return targetProxy;
    }

    // =================================================================================================================

    protected ClientMethod cachedClientMethod(Method target, Annotation httpAnnotation) {
        return this.methodCache.computeIfAbsent(target, k -> new ClientMethod(target, httpAnnotation, hrpcConfiguration));
    }
}
