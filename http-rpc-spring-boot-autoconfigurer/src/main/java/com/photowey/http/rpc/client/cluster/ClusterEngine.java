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
package com.photowey.http.rpc.client.cluster;

import com.photowey.http.rpc.client.cluster.strategy.ClusterStrategySelector;
import com.photowey.http.rpc.core.enums.ClusterStrategyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the selector Engine in Cluster mode
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
@Component
public class ClusterEngine implements Cluster, BeanPostProcessor {

    private static final Map<ClusterStrategyEnum, ClusterStrategySelector>
            CLUSTER_STRATEGY_SELECTOR_CACHE = new ConcurrentHashMap<>(8);

    private static final Logger log = LoggerFactory.getLogger(ClusterEngine.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (bean instanceof ClusterStrategySelector) {
            ClusterStrategySelector strategySelector = (ClusterStrategySelector) bean;
            log.info("put cluster strategy:[{}] into cache", targetClass.getName());
            CLUSTER_STRATEGY_SELECTOR_CACHE.put(strategySelector.strategy(), strategySelector);
        }

        return bean;
    }

    @Override
    public ClusterStrategySelector determineClusterStrategy(ClusterStrategyEnum clusterStrategy) {
        return CLUSTER_STRATEGY_SELECTOR_CACHE.get(clusterStrategy);
    }
}
