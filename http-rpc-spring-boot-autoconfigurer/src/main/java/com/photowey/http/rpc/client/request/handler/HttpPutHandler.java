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
package com.photowey.http.rpc.client.request.handler;

import com.photowey.http.rpc.client.annotation.HttpHandlerMarker;
import com.photowey.http.rpc.client.binding.ClientMethod;
import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.core.annotation.HttpPut;
import com.photowey.http.rpc.core.exception.HRpcException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * HttpPostHandler
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@HttpHandlerMarker(value = HttpPut.class)
public class HttpPutHandler extends AbstractHttpHandler {

    public HttpPutHandler(HRpcConfiguration hRpcConfiguration) {
        super(hRpcConfiguration);
    }

    @Override
    public boolean supports(Method target) {
        return target.isAnnotationPresent(HttpPut.class);
    }

    @Override
    public Annotation handleAnnotation(Method target) {
        HttpPut httpPut = target.getAnnotation(HttpPut.class);
        return httpPut;
    }
}
