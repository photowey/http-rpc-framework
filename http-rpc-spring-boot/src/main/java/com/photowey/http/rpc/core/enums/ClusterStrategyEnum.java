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
package com.photowey.http.rpc.core.enums;

import com.photowey.http.rpc.core.util.HRpcUtils;

/**
 * the select server Strategy in Cluster mode
 *
 * @author WcJun
 * @date 2020/09/06
 * @since 1.1.0
 */
public enum ClusterStrategyEnum {
    /**
     * using a random algorithm
     */
    RANDOM("RANDOM", 1),
    /**
     * using a weight-random algorithm
     */
    WEIGHT_RANDOM("WEIGHT_RANDOM", 2),
    /**
     * using a polling algorithm
     */
    POLLING("POLLING", 3),
    /**
     * using a weight-polling algorithm
     */
    WEIGHT_POLLING("WEIGHT_POLLING", 4),
    /**
     * using a ip-hash algorithm
     */
    HASH("HASH", 5),
    /**
     * TODO
     * using a custom algorithm
     */
    CUSTOM("CUSTOM", 6);

    private String name;

    private int value;

    ClusterStrategyEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String toName() {
        return name;
    }

    public int toValue() {
        return value;
    }

    public static ClusterStrategyEnum fromName(String target) {
        if (HRpcUtils.isEmpty(target)) {
            return null;
        }
        for (ClusterStrategyEnum value : values()) {
            String toName = value.toName();
            if (target.toUpperCase().equals(toName)) {
                return value;
            }
        }

        return null;
    }

    public static ClusterStrategyEnum fromValue(int target) {
        for (ClusterStrategyEnum value : values()) {
            int toValue = value.toValue();
            if (target == toValue) {
                return value;
            }
        }

        return null;
    }
}
