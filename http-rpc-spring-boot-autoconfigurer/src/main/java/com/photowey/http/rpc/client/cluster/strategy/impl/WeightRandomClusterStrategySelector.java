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

import com.photowey.http.rpc.client.properties.ServiceRoute;
import com.photowey.http.rpc.core.enums.ClusterStrategyEnum;
import com.photowey.http.rpc.core.util.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * this selector using a weight-random algorithm
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
@Component
public class WeightRandomClusterStrategySelector extends AbstractClusterStrategySelector {

    @Override
    public ServiceRoute doSelect(List<ServiceRoute> routes) {
        List<ServiceRoute> reset = super.resetProviderServiceListByWeight(routes);
        int weightCount = reset.size();
        int seed = RandomUtils.nextInt(0, weightCount - 1);

        return reset.get(seed);
    }

    @Override
    public ClusterStrategyEnum strategy() {
        return ClusterStrategyEnum.WEIGHT_RANDOM;
    }
}
