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
package com.photowey.http.rpc.client.request.httpclient;

import com.photowey.http.rpc.client.annotation.RequestExecutorMarker;
import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.request.executor.RequestExecutor;
import com.photowey.http.rpc.core.enums.ExecutorEnum;
import com.photowey.http.rpc.core.exception.HRpcException;

import java.io.IOException;

/**
 * Apache Http Client RequestExecutor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@RequestExecutorMarker(value = ExecutorEnum.APACHE_HTTP_CLIENT)
public class HttpClientRequestExecutor implements RequestExecutor {

    private final HRpcConfiguration hrpcConfiguration;

    public HttpClientRequestExecutor(HRpcConfiguration hrpcConfiguration) {
        this.hrpcConfiguration = hrpcConfiguration;
    }

    @Override
    public <T> T execute(ClientMethod method) throws RuntimeException, IOException {
        throw new HRpcException("current not support Apache http client executor");
    }
}
