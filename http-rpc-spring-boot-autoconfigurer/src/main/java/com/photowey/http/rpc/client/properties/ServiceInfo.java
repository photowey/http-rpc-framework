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

import com.photowey.http.rpc.client.annotation.HRpcClient;

import java.io.Serializable;
import java.util.List;

/**
 * service info
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public class ServiceInfo implements Serializable {

    private static final long serialVersionUID = 1816445615927207821L;
    /**
     * the service name OR id ->
     * <p>
     * {@link HRpcClient#contextId()}
     * || {@link HRpcClient#name()}
     * || {@link HRpcClient#value()}
     * || {@link HRpcClient#serviceId()}
     *
     * @see HRpcClient
     */
    private String service;
    /**
     * the node info
     * - ip
     * - port
     */
    private List<ServiceRoute> routes;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<ServiceRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<ServiceRoute> routes) {
        this.routes = routes;
    }
}
