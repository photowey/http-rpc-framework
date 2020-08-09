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
package com.photowey.http.rpc.client.register;

import com.photowey.http.rpc.client.factory.rpc.HRpcFactory;
import com.photowey.http.rpc.client.factory.rpc.ProxyHRpcFactory;
import com.photowey.http.rpc.core.exception.HRpcException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * the HRpcClient FactoryBean
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class HRpcClientFactoryBean<T> implements FactoryBean<T>, InitializingBean, ApplicationContextAware {

    private String service;
    private String version;
    private String contextId;
    private Class<?> type;
    /**
     * jdk or cglib
     */
    private String targetProxy;

    private ApplicationContext applicationContext;

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public T getObject() throws Exception {
        try {
            return this.getTarget();
        } catch (Exception e) {
            throw new HRpcException(e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(this.service, "service name must be set");
        Assert.hasText(this.version, "version must be set");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    // ===================================================================

    private <T> T getTarget() {
        return this.createProxy();
    }

    private <T> T createProxy() {
        HRpcFactory HRpcFactory = this.applicationContext.getBean(ProxyHRpcFactory.class);
        return (T) HRpcFactory.createProxy(this.targetProxy, new Class[]{this.type});
    }
    // ===================================================================

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getTargetProxy() {
        return targetProxy;
    }

    public void setTargetProxy(String targetProxy) {
        this.targetProxy = targetProxy;
    }

    // ===================================================================
}
