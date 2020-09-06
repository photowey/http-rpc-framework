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
package com.photowey.http.rpc.client.properties;

import com.photowey.http.rpc.core.enums.ClusterStrategyEnum;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Http Rpc
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@ConditionalOnExpression("!'${hrpc.client}'.isEmpty()")
@ConfigurationProperties(prefix = HRpcClientProperties.HRPC_CLIENT_REGISTRY_PREFIX, ignoreUnknownFields = true)
public class HRpcClientProperties {

    public static final String HRPC_CLIENT_REGISTRY_PREFIX = "hrpc.client";

    /**
     * the default http executor type
     */
    private ExecutorEnum executorType = ExecutorEnum.OK_HTTP;
    /**
     * the default strategy
     *
     * @since 1.1.0
     */
    private ClusterStrategyEnum clusterStrategy = ClusterStrategyEnum.POLLING;
    /**
     * the remote service info
     *
     * @since 1.1.0
     */
    private List<ServiceInfo> services;

    private int connectTimeout = 6;
    private int readTimeout = 60;
    private int writeTimeout = 60;

    public ExecutorEnum getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorEnum executorType) {
        this.executorType = executorType;
    }

    public ClusterStrategyEnum getClusterStrategy() {
        return clusterStrategy;
    }

    public void setClusterStrategy(ClusterStrategyEnum clusterStrategy) {
        this.clusterStrategy = clusterStrategy;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public List<ServiceInfo> getServices() {
        return services;
    }

    public void setServices(List<ServiceInfo> services) {
        this.services = services;
    }
}
