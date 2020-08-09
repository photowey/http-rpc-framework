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
package com.photowey.consumer.config;

import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.request.okhttp.OkHttpRequestExecutor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Component;

/**
 * this is a sample, How to extension the IOkHttpRequestExecutor
 * OkHttpRequestExecutor Ext
 *
 * @author WcJun
 * @date 2020/08/10
 * @since 1.0.0
 */
@Slf4j
@Component
public class HttpClientRequestExecutorExt extends OkHttpRequestExecutor {

    public HttpClientRequestExecutorExt(HRpcConfiguration hrpcConfiguration) {
        super(hrpcConfiguration);
    }

    @Override
    protected void preBuildClient(OkHttpClient.Builder builder) {
        super.preBuildClient(builder);
        log.info("execute the preBuildClient() in sub-class:[{}]", this.getClass().getSimpleName());
    }

    @Override
    protected void preExecuteRequest(OkHttpClient client, Request request) {
        super.preExecuteRequest(client, request);
        log.info("execute the preExecuteRequest() in sub-class:[{}]", this.getClass().getSimpleName());
    }
}
