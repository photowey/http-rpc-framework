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
package com.photowey.http.rpc.client.request.okhttp;

import com.photowey.http.rpc.client.annotation.RequestExecutorMarker;
import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.client.binding.RequestCommand;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.request.trust.X509TrustManagerImpl;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcUtils;
import com.photowey.http.rpc.core.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OKHttp RequestExecutor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@RequestExecutorMarker(value = ExecutorEnum.OK_HTTP)
public class OkHttpRequestExecutor implements IOkHttpRequestExecutor {

    protected static final Logger log = LoggerFactory.getLogger(OkHttpRequestExecutor.class);

    protected final HRpcConfiguration hrpcConfiguration;

    public OkHttpRequestExecutor(HRpcConfiguration hrpcConfiguration) {
        this.hrpcConfiguration = hrpcConfiguration;
    }

    @Override
    public <T> T execute(ClientMethod method) throws RuntimeException, IOException {
        RequestCommand command = method.getCommand();
        MethodSignature methodSignature = method.getMethod();

        RequestMethod requestMethod = command.getRequestMethod();
        String url = methodSignature.getUrl();
        Map<String, String> headerValues = methodSignature.getHeaderValues();

        String bodyStr = "";

        OkHttpClient.Builder builder = this.determineClient(url);

        // hook
        this.preBuildClient(builder);

        OkHttpClient client = this.buildOkHttpClient(builder);
        Request request = null;
        switch (requestMethod) {
            case GET:
                request = this.populateGetRequest(url, headerValues);
                break;
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                Object requestBody = methodSignature.getRequestBody();
                if (HRpcUtils.isNotEmpty(requestBody)) {
                    bodyStr = JsonUtils.toJSONString(requestBody);
                }

                request = this.populateRequest(url, bodyStr, requestMethod, headerValues);
                break;
            default:
                break;
        }

        // hook
        this.preExecuteRequest(client, request);

        // The Response
        Response response = this.executeRequest(client, request);
        // Http Status
        HttpStatus status = HttpStatus.valueOf(response.code());
        if (HttpStatus.OK.equals(status)) {
            Class<?> returnType = methodSignature.getReturnType();
            if (log.isDebugEnabled()) {
                log.debug("the method:[{}]return type is:[{}]", command.getMethodName(), returnType.getSimpleName());
            }
            if (void.class.equals(returnType)) {
                return null;
            } else {
                String responseStr = response.body().string();
                if (log.isDebugEnabled()) {
                    log.debug("the okhttp url:[{}] response is:[{}]", url, responseStr);
                }
                if (Collection.class.isAssignableFrom(returnType)) {
                    List<?> multi = JsonUtils.toBeans(responseStr, returnType);
                    return (T) multi;
                }
                // handle The ResponseEntity
                // #issue: java.lang.IllegalArgumentException: HttpStatus must not be null
                if (ResponseEntity.class.equals(returnType)) {
                    String typeName = methodSignature.getTypeName();
                    Class<?> type = null;
                    try {
                        type = ClassUtils.forName(typeName, this.getClass().getClassLoader());
                    } catch (ClassNotFoundException e) {
                        throw new HRpcException("not found Class:[{}]", typeName, e);
                    }
                    Object body = JsonUtils.toBean(responseStr, type);
                    return (T) new ResponseEntity<>(body, status);
                } else {
                    return (T) JsonUtils.toBean(responseStr, returnType);
                }
            }
        } else {
            throw new HRpcException("execute the method:[{}], url:[{}] exception, status:[{}]", command.getMethodName(), url, status.value());
        }
    }

    protected void preExecuteRequest(OkHttpClient client, Request request) {
        // do some for sub-class if necessary
    }

    protected OkHttpClient buildOkHttpClient(OkHttpClient.Builder builder) {
        OkHttpClient client = builder.build();

        return client;
    }

    protected void preBuildClient(OkHttpClient.Builder builder) {
        // do some for sub-class if necessary
    }

    // ======================================================================= REQUEST

    protected Request populateGetRequest(String url) {
        return this.populateRequest(url, "", RequestMethod.GET, null);
    }

    protected Request populateGetRequest(String url, Map<String, String> headers) {
        return this.populateRequest(url, "", RequestMethod.GET, headers);
    }

    public Request populatePostRequest(String url, String bodyStr, Map<String, String> headers) {
        return this.populateRequest(url, bodyStr, RequestMethod.POST, headers);
    }

    public Request populatePuttRequest(String url, String bodyStr, Map<String, String> headers) {
        return this.populateRequest(url, bodyStr, RequestMethod.PUT, headers);
    }

    public Request populatePatchRequest(String url, String bodyStr, Map<String, String> headers) {
        return this.populateRequest(url, bodyStr, RequestMethod.PATCH, headers);
    }

    public Request populateDeleteRequest(String url, String bodyStr, Map<String, String> headers) {
        return this.populateRequest(url, bodyStr, RequestMethod.DELETE, headers);
    }

    public Request populateRequest(String url, String bodyStr, RequestMethod requestMethod, Map<String, String> headers) {
        RequestBody body = RequestBody.create(JSON_APPLICATION, bodyStr);
        Request.Builder builder = new Request.Builder().url(url);
        switch (requestMethod) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(body);
                break;
            case PUT:
                builder.put(body);
                break;
            case PATCH:
                builder.patch(body);
                break;
            case DELETE:
                builder.delete(body);
                break;
            default:
                break;
        }

        this.populateHeaders(builder, headers);

        return builder.build();
    }

    // ======================================================================= CLIENT

    public OkHttpClient.Builder determineClient(String url) throws RuntimeException {
        OkHttpClient.Builder builder = this.populateOkHttpClient(url);

        return builder;
    }

    public OkHttpClient.Builder populateOkHttpClient(final String url) {
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder()
                .hostnameVerifier(this.hrpcConfiguration.getHostnameVerifier())
                .connectTimeout(this.hrpcConfiguration.getHRpcClientProperties().getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(this.hrpcConfiguration.getHRpcClientProperties().getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(this.hrpcConfiguration.getHRpcClientProperties().getWriteTimeout(), TimeUnit.SECONDS);

        if (url.contains(HTTPS)) {
            SSLSocketFactory sslSocketFactory = this.createIgnoreVerifySSL();
            builder.sslSocketFactory(sslSocketFactory, this.hrpcConfiguration.getTrustManager());
        }

        return builder;
    }

    // ======================================================================= EXEC

    public Response executeRequest(OkHttpClient client, Request request) throws IOException {
        return client.newCall(request).execute();
    }

    // ======================================================================= HEADERS

    public void populateHeaders(Request.Builder builder, Map<String, String> headers) {
        if (!Objects.isNull(headers)) {
            Set<String> headerKeys = headers.keySet();
            for (String header : headerKeys) {
                builder.addHeader(header, headers.get(header));
            }
        }
    }

    // ======================================================================= SSL

    /**
     * Ignore ssl verify
     *
     * @return SSLSocketFactory
     * @throws NoSuchProviderException
     * @throws KeyManagementException
     * @throws KeyManagementException
     */
    public SSLSocketFactory createIgnoreVerifySSL() throws RuntimeException {
        SSLSocketFactory sslSocketFactory = null;
        try {
            TrustManager[] tm = {new X509TrustManagerImpl()};
            // SSLContext sslContext = SSLContext.getInstance(HTTPS_SSL_V3, HTTPS_POST_SUNJSSE);
            SSLContext sslContext = SSLContext.getInstance(HTTPS_TLS);
            sslContext.init(null, tm, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new HRpcException("handle the https SSLSocketFactory exception", e);
        }

        return sslSocketFactory;
    }
}
