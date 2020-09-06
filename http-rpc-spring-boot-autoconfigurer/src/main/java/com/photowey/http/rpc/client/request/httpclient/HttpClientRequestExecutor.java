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
package com.photowey.http.rpc.client.request.httpclient;

import com.photowey.http.rpc.client.annotation.RequestExecutorMarker;
import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.binding.MethodSignature;
import com.photowey.http.rpc.client.binding.RequestCommand;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.properties.HRpcClientProperties;
import com.photowey.http.rpc.client.request.trust.X509TrustManagerImpl;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcUtils;
import com.photowey.http.rpc.core.util.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Apache Http Client RequestExecutor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@RequestExecutorMarker(value = ExecutorEnum.APACHE_HTTP_CLIENT)
public class HttpClientRequestExecutor implements IHttpClientRequestExecutor {

    private static final Logger log = LoggerFactory.getLogger(HttpClientRequestExecutor.class);

    private final HRpcConfiguration hrpcConfiguration;

    public HttpClientRequestExecutor(HRpcConfiguration hrpcConfiguration) {
        this.hrpcConfiguration = hrpcConfiguration;
    }

    @Override
    public <T> T execute(ClientMethod method) throws RuntimeException, IOException {
        RequestCommand command = method.getCommand();
        MethodSignature methodSignature = method.getMethod();

        RequestMethod requestMethod = command.getRequestMethod();
        String url = methodSignature.getUrl();

        HRpcClientProperties properties = hrpcConfiguration.getHRpcClientProperties();
        try {
            HttpClient httpClient = this.populateHttpClient(url, properties);
            HttpEntityEnclosingRequestBase request = this.populateRequest(methodSignature, requestMethod);
            this.requestEnhance(request);
            HttpResponse response = httpClient.execute(request);
            int core = response.getStatusLine().getStatusCode();
            if (REQUEST_OK == core) {
                Class<?> returnType = methodSignature.getReturnType();
                if (log.isDebugEnabled()) {
                    log.debug("httpclient:: the method:[{}]return type is:[{}]", command.getMethodName(), returnType.getSimpleName());
                }
                if (void.class.equals(returnType)) {
                    return null;
                } else {
                    String responseStr = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
                    if (log.isDebugEnabled()) {
                        log.debug("the okhttp url:[{}] response is:[{}]", url, responseStr);
                    }
                    if (Collection.class.isAssignableFrom(returnType)) {
                        List<?> multi = JsonUtils.toBeans(responseStr, returnType);
                        return (T) multi;
                    }
                    if (ResponseEntity.class.equals(returnType)) {
                        String typeName = methodSignature.getTypeName();
                        Class<?> type = null;
                        try {
                            type = ClassUtils.forName(typeName, this.getClass().getClassLoader());
                        } catch (ClassNotFoundException e) {
                            throw new HRpcException("not found Class:[{}]", typeName, e);
                        }
                        Object body = JsonUtils.toBean(responseStr, type);
                        HttpStatus status = HttpStatus.valueOf(core);
                        return (T) new ResponseEntity<>(body, status);
                    } else {
                        return (T) JsonUtils.toBean(responseStr, returnType);
                    }
                }
            }
        } catch (Exception e) {
            throw new HRpcException("handle the remote invoke with Apache http client exception");
        }


        throw new HRpcException("current not support Apache http client executor");
    }

    // ======================================================================================

    private HttpClient populateHttpClient(String url, HRpcClientProperties properties) throws KeyManagementException,
            NoSuchAlgorithmException, NoSuchProviderException {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = this.populateRegistryBuilder(url);
        this.preRegistryBuilder(registryBuilder);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = this.buildRegistry(registryBuilder);


        RequestConfig.Builder builder = populateConfigBuilder(properties);
        this.preBuildConfigBuilder(builder);
        RequestConfig defaultRequestConfig = this.buildRequestConfig(builder);

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        HttpClientBuilder httpClientBuilder = this.populateHttpClientBuilder(defaultRequestConfig, connManager);
        this.preBuildHttpClientBuilder(httpClientBuilder);
        HttpClient httpclient = this.buildHttpClientBuilder(httpClientBuilder);

        return httpclient;
    }

    // ======================================================================================

    public void requestEnhance(HttpEntityEnclosingRequestBase requestBase) {

    }

    public HttpEntityEnclosingRequestBase populateRequest(MethodSignature methodSignature, RequestMethod requestMethod) {
        HttpEntityEnclosingRequestBase requestBase = null;
        String url = methodSignature.getUrl();
        Map<String, String> headerValues = methodSignature.getHeaderValues();
        switch (requestMethod) {
            case GET:
                requestBase = new HttpGetExt(url);
                break;
            case POST:
                requestBase = new HttpPost(url);
                break;
            case PUT:
                requestBase = new HttpPut(url);
                break;
            case PATCH:
                requestBase = new HttpPatch(url);
                break;
            case DELETE:
                requestBase = new HttpDeleteExt(url);
                break;
            default:
                throw new HRpcException("not support the Http Method:[{}]", requestMethod.name());
        }

        // populate header id necessary
        if (HRpcUtils.isNotEmpty(headerValues)) {
            Set<Map.Entry<String, String>> entries = headerValues.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                requestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }

        Object requestBody = methodSignature.getRequestBody();
        if (HRpcUtils.isNotEmpty(requestBody)) {
            String bodyStr = JsonUtils.toJSONString(requestBody);
            StringEntity strEntity = new StringEntity(bodyStr, ContentType.APPLICATION_JSON);
            requestBase.setEntity(strEntity);
        }

        return requestBase;
    }

    // ======================================================================================

    public RegistryBuilder<ConnectionSocketFactory> populateRegistryBuilder(String url)
            throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.<ConnectionSocketFactory>create();
        if (url.contains(HTTPS)) {
            SSLContext sslcontext = this.createIgnoreVerifySSL();
            builder.register(HTTPS, new SSLConnectionSocketFactory(sslcontext));
        } else {
            builder.register(HTTP, PlainConnectionSocketFactory.INSTANCE);
        }

        return builder;
    }

    public void preRegistryBuilder(RegistryBuilder<ConnectionSocketFactory> registryBuilder) {

    }

    public Registry<ConnectionSocketFactory> buildRegistry(RegistryBuilder<ConnectionSocketFactory> builder) {
        Registry<ConnectionSocketFactory> registry = builder.build();
        return registry;
    }

    // ======================================================================================

    public HttpClient buildHttpClientBuilder(HttpClientBuilder httpClientBuilder) {
        HttpClient httpClient = httpClientBuilder.build();

        return httpClient;
    }

    public void preBuildHttpClientBuilder(HttpClientBuilder httpClientBuilder) {

    }

    public HttpClientBuilder populateHttpClientBuilder(RequestConfig defaultRequestConfig, PoolingHttpClientConnectionManager connManager) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(connManager);

        return httpClientBuilder;
    }

    // ======================================================================================

    public RequestConfig buildRequestConfig(RequestConfig.Builder builder) {
        RequestConfig requestConfig = builder.build();

        return requestConfig;
    }

    public void preBuildConfigBuilder(RequestConfig.Builder builder) {

    }

    private RequestConfig.Builder populateConfigBuilder(HRpcClientProperties properties) {
        return RequestConfig.custom()
                .setSocketTimeout(properties.getReadTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setConnectionRequestTimeout(-1);
    }

    // ======================================================================================

    private SSLContext createIgnoreVerifySSL() throws NoSuchProviderException,
            NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] tm = {new X509TrustManagerImpl()};
        SSLContext sslContext = SSLContext.getInstance(HTTPS_SSL_V3, HTTPS_POST_SUNJSSE);
        sslContext.init(null, tm, new SecureRandom());

        return sslContext;
    }
}
