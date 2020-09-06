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

import java.io.Serializable;

/**
 * service deploy node info, contain ip & port
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public class ServiceRoute implements Serializable {

    private static final long serialVersionUID = -6406186738829820653L;
    /**
     * the service deploy machine's ip
     */
    private String ip;
    /**
     * the service expose's port
     */
    private int port;
    /**
     * the instance weight
     *
     * @see com.photowey.http.rpc.client.cluster.strategy.impl.WeightPollingClusterStrategySelector
     * @see com.photowey.http.rpc.client.cluster.strategy.impl.WeightRandomClusterStrategySelector
     */
    private int weight = 1;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
