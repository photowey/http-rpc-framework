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

import com.photowey.http.rpc.client.context.DefaultRequestContextFactory;
import com.photowey.http.rpc.client.context.RequestContextFactory;
import com.photowey.http.rpc.client.interceptor.DefaultRequestInterceptor;
import com.photowey.http.rpc.client.interceptor.RequestInterceptor;
import com.photowey.http.rpc.client.properties.HRpcClientProperties;
import com.photowey.http.rpc.client.request.okhttp.IOkHttpRequestExecutor;
import com.photowey.http.rpc.client.request.okhttp.OkHttpRequestExecutor;
import com.photowey.http.rpc.client.request.trust.HostnameVerifierImpl;
import com.photowey.http.rpc.client.request.trust.X509TrustManagerImpl;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

/**
 * HRpc Configurer
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Configuration
@Import(HRpcConfiguration.class)
@ComponentScan(basePackages = "com.photowey.http.rpc.client")
@EnableConfigurationProperties(HRpcClientProperties.class)
public class HRpcConfigurer {

    @Bean
    @ConditionalOnMissingBean(RequestContextFactory.class)
    public RequestContextFactory requestContextFactory() {
        return new DefaultRequestContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean(HostnameVerifier.class)
    public HostnameVerifier hostnameVerifier() {
        return new HostnameVerifierImpl();
    }

    @Bean
    @ConditionalOnMissingBean(X509TrustManager.class)
    public X509TrustManager trustManager() {
        return new X509TrustManagerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RequestInterceptor.class)
    public RequestInterceptor requestInterceptor() {
        return new DefaultRequestInterceptor();
    }

    /**
     * custom define the IOkHtpRequestExecutor If necessary for Sub-Class
     *
     * @param hrpcConfiguration {@link HRpcConfiguration}
     * @return {@link IOkHttpRequestExecutor}
     */
    @Bean
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnMissingBean(IOkHttpRequestExecutor.class)
    public IOkHttpRequestExecutor okHtpRequestExecutor(HRpcConfiguration hrpcConfiguration) {
        return new OkHttpRequestExecutor(hrpcConfiguration);
    }
}
