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
package com.photowey.http.rpc.client.cluster.strategy.impl;

import com.photowey.http.rpc.client.cluster.strategy.ClusterStrategySelector;
import com.photowey.http.rpc.client.properties.ServiceRoute;
import com.photowey.http.rpc.core.enums.ClusterStrategyEnum;
import com.photowey.http.rpc.core.enums.HRpcStatus;
import com.photowey.http.rpc.core.exception.RpcServiceNotAvailableException;
import com.photowey.http.rpc.core.util.HRpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AbstractClusterStrategySelector
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public abstract class AbstractClusterStrategySelector implements ClusterStrategySelector {

    protected static final Logger log = LoggerFactory.getLogger(AbstractClusterStrategySelector.class);

    @Override
    public boolean supports(ClusterStrategyEnum strategy) {
        return this.strategy().equals(strategy);
    }

    /**
     * select a target service for invoke
     *
     * @param routes the service list
     * @return the invoke target service
     */
    @Override
    public ServiceRoute select(List<ServiceRoute> routes) throws RpcServiceNotAvailableException {
        if (null == routes) {
            // the checkAvailable will be throw {@link RpcServiceNotAvailableException}
            // not NullPointerException
            routes = Collections.emptyList();
        }
        this.checkAvailable(routes);
        int serviceCount = routes.size();
        if (1 == serviceCount) {
            return routes.get(0);
        }
        return this.doSelect(routes);
    }

    /**
     * check the service list can be used
     *
     * @param routes the service list
     */
    protected void checkAvailable(List<ServiceRoute> routes) {
        if (HRpcUtils.isEmpty(routes)) {
            throw new RpcServiceNotAvailableException(HRpcStatus.NO_AVAILABLE.toValue(), "in current service list, have not any service can be used!");
        }
    }

    /**
     * executing decision options based on different selector algorithms
     *
     * @param routes the service list
     * @return the invoke target service
     */
    public abstract ServiceRoute doSelect(List<ServiceRoute> routes);

    /**
     * reset the service list by itself weight
     *
     * @param routes the service list
     * @return the reset service list
     */
    protected List<ServiceRoute> resetProviderServiceListByWeight(List<ServiceRoute> routes) {
        List<ServiceRoute> reset = new ArrayList<>();
        for (ServiceRoute current : routes) {
            int weight = current.getWeight();
            for (int i = 0; i < weight; i++) {
                reset.add(current);
            }
        }

        return reset;
    }
}
