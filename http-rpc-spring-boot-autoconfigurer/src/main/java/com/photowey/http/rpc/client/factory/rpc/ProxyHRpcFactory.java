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
package com.photowey.http.rpc.client.factory.rpc;

import com.photowey.http.rpc.client.factory.CglibProxyFactory;
import com.photowey.http.rpc.client.factory.ProxyFactory;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ServiceLoader;

/**
 * ProxyRpcFactory
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Component
public class ProxyHRpcFactory implements HRpcFactory, BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public <T> T createProxy(String targetProxy, Class<T>[] targets) throws HRpcException {
        String[] beanNames = this.beanFactory.getBeanNamesForType(ProxyFactory.class);
        if (HRpcUtils.isNotEmpty(beanNames)) {
            for (String beanName : beanNames) {
                ProxyFactory targetProxyFactory = this.beanFactory.getBean(beanName, ProxyFactory.class);
                if (targetProxyFactory.supports(targetProxy)) {
                    return targetProxyFactory.buildProxy(targetProxy, targets);
                }
            }
        } else {
            CglibProxyFactory proxyFactory = this.getDefaultProxyFactory();
            return proxyFactory.buildProxy(targetProxy, targets);
        }

        return null;
    }

    private CglibProxyFactory getDefaultProxyFactory() {
        return this.beanFactory.getBean(CglibProxyFactory.class);
    }
}
