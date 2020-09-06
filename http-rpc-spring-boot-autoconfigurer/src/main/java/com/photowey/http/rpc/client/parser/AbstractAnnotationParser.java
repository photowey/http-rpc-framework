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
package com.photowey.http.rpc.client.parser;

import com.photowey.http.rpc.client.annotation.HRpcClient;
import com.photowey.http.rpc.client.cluster.ClusterEngine;
import com.photowey.http.rpc.client.cluster.strategy.ClusterStrategySelector;
import com.photowey.http.rpc.client.properties.HRpcClientProperties;
import com.photowey.http.rpc.client.properties.ServiceInfo;
import com.photowey.http.rpc.client.properties.ServiceRoute;
import com.photowey.http.rpc.core.constant.HRpcConstants;
import com.photowey.http.rpc.core.enums.ClusterStrategyEnum;
import com.photowey.http.rpc.core.enums.HostTypeEnum;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.HRpcUtils;
import com.photowey.http.rpc.core.util.StringFormatUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.List;

/**
 * The annotation parser
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public abstract class AbstractAnnotationParser implements ApplicationContextAware {

    public ApplicationContext applicationContext;

    public String determineHost(String host, HostTypeEnum hostType, Method target, HRpcClientProperties properties) {
        if (HostTypeEnum.DYNAMIC.equals(hostType)) {
            HRpcClient hRpcClient = target.getDeclaringClass().getAnnotation(HRpcClient.class);
            String targetService = hRpcClient.value();
            List<ServiceInfo> services = properties.getServices();
            ServiceInfo serviceInfo = this.checkEmpty(targetService, services);
            List<ServiceRoute> routes = serviceInfo.getRoutes();
            ClusterEngine clusterEngine = this.applicationContext.getBean(ClusterEngine.class);
            ClusterStrategyEnum clusterStrategy = properties.getClusterStrategy();
            ClusterStrategySelector clusterStrategySelector = clusterEngine.determineClusterStrategy(clusterStrategy);
            ServiceRoute targetRoute = clusterStrategySelector.select(routes);
            host = StringFormatUtils.format(HRpcConstants.TEMPLATE_HOST, targetRoute.getIp(), targetRoute.getPort());
        }

        return host;
    }

    private ServiceInfo checkEmpty(String targetService, List<ServiceInfo> services) {
        if (HRpcUtils.isEmpty(services)) {
            throw new HRpcException(StringFormatUtils.format(
                    HRpcConstants.MISS_CONFIG_MESSAGE_TEMPLATE, "HttpDelete"));
        }
        ServiceInfo serviceInfo = services.stream().filter(service -> service.getService().equals(targetService)).findAny().orElse(null);
        if (HRpcUtils.isEmpty(serviceInfo)) {
            throw new HRpcException(StringFormatUtils.format(
                    HRpcConstants.MISS_SERVICE_CONFIG_MESSAGE_TEMPLATE, "HttpDelete", targetService));
        }

        return serviceInfo;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
