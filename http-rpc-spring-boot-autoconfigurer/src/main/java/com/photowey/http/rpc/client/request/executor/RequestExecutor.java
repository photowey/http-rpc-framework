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
package com.photowey.http.rpc.client.request.executor;

import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.core.exception.HRpcException;
import okhttp3.MediaType;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

/**
 * Request Executor
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public interface RequestExecutor {

    MediaType JSON_APPLICATION = MediaType.parse("application/json; charset=utf-8");
    String HTTPS = "https";
    String HTTPS_SSL_V3 = "SSLv3";
    String HTTPS_POST_SUNJSSE = "SunJSSE";

    /**
     * execute the HTTP request
     *
     * @param method HPpc Client-Method-Object
     * @param <T>
     * @return T Type
     * @throws HRpcException
     * @throws IOException
     */
    <T> T execute(ClientMethod method) throws HRpcException, IOException;
}
