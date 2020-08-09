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
package com.photowey.http.rpc.client.factory.rpc;

import com.photowey.http.rpc.core.exception.HRpcException;

/**
 * the Rpc client HRpcFactory
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public interface HRpcFactory {

    /**
     * Create Proxy Object
     *
     * @param targetProxy the expect proxy type
     * @param targets     the proxy  targets
     * @param <T>
     * @return T
     * @throws HRpcException
     */
    <T> T createProxy(String targetProxy, Class<T>[] targets) throws HRpcException;
}
