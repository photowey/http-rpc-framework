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

import com.photowey.http.rpc.client.annotation.RequestExecutorMarker;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.request.executor.RequestExecutor;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * RequestExecutor BeanPostProcessor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Component
public class RequestExecutorBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass.isAnnotationPresent(RequestExecutorMarker.class)) {
            HRpcConfiguration hrpcConfiguration = this.beanFactory.getBean(HRpcConfiguration.class);
            RequestExecutorMarker marker = targetClass.getAnnotation(RequestExecutorMarker.class);
            ExecutorEnum value = marker.value();
            hrpcConfiguration.registerRequestExecutor(value.name(), (RequestExecutor) bean);
        }

        return bean;
    }
}
