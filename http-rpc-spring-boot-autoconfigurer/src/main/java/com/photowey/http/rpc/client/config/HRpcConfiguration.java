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
package com.photowey.http.rpc.client.config;

import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.client.context.RequestContext;
import com.photowey.http.rpc.client.context.RequestContextFactory;
import com.photowey.http.rpc.client.interceptor.RequestInterceptor;
import com.photowey.http.rpc.client.parameter.ParameterProcessor;
import com.photowey.http.rpc.client.parser.AnnotationParser;
import com.photowey.http.rpc.client.properties.HRpcClientProperties;
import com.photowey.http.rpc.client.request.executor.RequestExecutor;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.model.RemoteInfo;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.OrderComparator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HRpc Client Configuration
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Configuration
public class HRpcConfiguration {

    @Autowired
    private HRpcClientProperties hrpcClientProperties;

    // =================================================================================================================

    @Autowired
    private RequestContextFactory requestContextFactory;
    @Autowired
    private HostnameVerifier hostnameVerifier;
    @Autowired
    private X509TrustManager trustManager;

    @Autowired
    private List<ParameterProcessor> parameterProcessorChains;
    @Autowired
    private List<RequestInterceptor> interceptorChains;
    @Autowired
    private List<AnnotationParser> annotationParsers;

    // =================================================================================================================

    private Map<String, RequestExecutor> requestExecutors = new ConcurrentHashMap<>();

    public HRpcClientProperties getHRpcClientProperties() {
        return hrpcClientProperties;
    }

    // =================================================================================================================

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    // =================================================================================================================

    public RequestContextFactory getRequestContextFactory() {
        return requestContextFactory;
    }

    // =================================================================================================================

    public List<ParameterProcessor> getParameterProcessorChains() {
        return parameterProcessorChains;
    }

    // =================================================================================================================

    public Map<String, RequestExecutor> getRequestExecutors() {
        return requestExecutors;
    }

    public void registerRequestExecutor(String key, RequestExecutor requestExecutor) {
        this.requestExecutors.put(key, requestExecutor);
    }

    public RequestExecutor getRequestExecutor(String key) {
        return requestExecutors.get(key);
    }

    public RequestExecutor getRequestExecutor() {
        ExecutorEnum executorType = this.hrpcClientProperties.getExecutorType();
        return this.getRequestExecutor(executorType.name());
    }

    // =================================================================================================================

    public List<RequestInterceptor> getInterceptorChains() {
        return interceptorChains;
    }

    // =================================================================================================================

    public MethodSignature handleAll(MethodSignature methodSignature, Method method) {
        Parameter[] parameters = method.getParameters();
        List<Parameter> annotationSets = new ArrayList<>(Arrays.asList(parameters));
        for (int i = 0, size = annotationSets.size(); i < size; i++) {
            Parameter parameter = parameters[i];
            Annotation[] annotations = parameter.getAnnotations();
            if (HRpcUtils.isEmpty(annotations)) {
                throw new HRpcException("the parameter:[{}] must be modified by annotation, such as @RequestParam", parameter.getName());
            }
            this.handleAll(parameter, i, methodSignature);
        }

        return methodSignature;
    }

    // =================================================================================================================

    private void handleAll(Parameter parameter, Integer paramIndex, MethodSignature methodSignature) {
        OrderComparator.sort(this.parameterProcessorChains);
        for (ParameterProcessor parameterProcessorChain : this.parameterProcessorChains) {
            parameterProcessorChain.handleParameter(parameter, paramIndex, methodSignature);
        }
    }

    // =================================================================================================================

    public void interceptAll(MethodSignature methodSignature, RequestContext context) {
        List<RequestInterceptor> interceptorChains = this.getInterceptorChains();
        OrderComparator.sort(this.interceptorChains);
        for (RequestInterceptor interceptor : interceptorChains) {
            interceptor.intercept(context);
        }
        // 处理附加的属性
        this.handleQueries(methodSignature, context);
        // 处理附加的 header
        this.handleHeaders(methodSignature, context);
    }

    // =================================================================================================================

    private void handleHeaders(MethodSignature methodSignature, RequestContext context) {
        Map<String, Object> headers = context.getHeaders();
        Set<Map.Entry<String, Object>> entries = headers.entrySet();
        Map<String, String> headerValues = methodSignature.getHeaderValues();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String valueStr = String.valueOf(entry.getValue());
            headerValues.put(key, valueStr);
        }
    }

    private void handleQueries(MethodSignature methodSignature, RequestContext context) {
        Map<String, Object> queries = context.getAttributes();
        Set<Map.Entry<String, Object>> entries = queries.entrySet();
        String url = methodSignature.getUrl();
        StringBuilder builder = new StringBuilder(url);
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String valueStr = String.valueOf(entry.getValue());
            HRpcUtils.populateQuery(builder, key, valueStr);
        }

        methodSignature.setUrl(builder.toString());
    }

    // =================================================================================================================

    public <T extends Annotation> RemoteInfo parseHttpAnnotation(Method target, T httpAnnotation) {
        for (AnnotationParser annotationParser : this.annotationParsers) {
            if (annotationParser.supports(target)) {
                return annotationParser.parse(httpAnnotation, target, this.hrpcClientProperties);
            }
        }

        throw new HRpcException("not found the http annotation:[{}]'s parser!", httpAnnotation.getClass().getName());
    }
}
