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
package com.photowey.http.rpc.client.handler;

import com.photowey.http.rpc.client.request.handler.HttpHandler;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.util.StringFormatUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AbstractInvocationHandler
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public abstract class AbstractInvocationHandler {

    protected static final String TO_STRING = "toString";
    protected static final String HASH_CODE = "hashCode";
    protected static final String EQUALS = "equals";

    protected List<HttpHandler> httpHandlers;

    public AbstractInvocationHandler(List<HttpHandler> httpHandlers) {
        this.httpHandlers = httpHandlers;
    }

    public Object doIntercept(Object proxy, Method method, Object[] args) throws Throwable {
        for (HttpHandler httpHandler : this.httpHandlers) {
            if (httpHandler.supports(method)) {
                return httpHandler.handleRequest(proxy.getClass(), method, args);
            }
        }
        throw new HRpcException(StringFormatUtils.format("this method:[{}] not modified by annotation, such as @HttpGet~", method.getName()));
    }
}
