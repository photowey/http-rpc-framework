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

/**
 * HRpcStatus
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public enum HRpcStatus {

    /**
     * the request execute success
     */
    SUCCESS("SUCCESS", 1),

    /**
     * the request execute failure
     */
    FAILURE("FAILURE", 0),
    /**
     * the service not available
     */
    NO_AVAILABLE("NO_AVAILABLE", -1);

    private String name;

    private int value;

    HRpcStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String toName() {
        return name;
    }

    public int toValue() {
        return value;
    }

    public static HRpcStatus fromName(String target) {
        for (HRpcStatus value : values()) {
            String toName = value.toName();
            if (target.equals(toName)) {
                return value;
            }
        }

        return null;
    }

    public static HRpcStatus fromValue(int target) {
        for (HRpcStatus value : values()) {
            int toValue = value.toValue();
            if (target == toValue) {
                return value;
            }
        }

        return null;
    }
}
