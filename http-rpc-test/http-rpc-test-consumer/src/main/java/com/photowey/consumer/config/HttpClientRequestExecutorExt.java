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
import com.photowey.http.rpc.client.request.httpclient.HttpClientRequestExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.springframework.stereotype.Component;

/**
 * this is a sample, How to extension the IHttpClientRequestExecutor
 * HttpClientRequestExecutor Ext
 *
 * @author WcJun
 * @date 2020/08/22
 * @since 1.0.0
 */
@Slf4j
@Component
public class HttpClientRequestExecutorExt extends HttpClientRequestExecutor {

    public HttpClientRequestExecutorExt(HRpcConfiguration hrpcConfiguration) {
        super(hrpcConfiguration);
    }

    @Override
    public void requestEnhance(HttpEntityEnclosingRequestBase requestBase) {
        super.requestEnhance(requestBase);
        log.info("execute the requestEnhance() in sub-class:[{}]", this.getClass().getSimpleName());
    }
}
