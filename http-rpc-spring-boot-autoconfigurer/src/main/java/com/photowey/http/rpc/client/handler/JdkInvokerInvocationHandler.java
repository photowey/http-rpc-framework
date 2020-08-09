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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * JdkInvokerInvocationHandler
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class JdkInvokerInvocationHandler extends AbstractInvocationHandler implements InvocationHandler {

    public JdkInvokerInvocationHandler(List<HttpHandler> httpHandlers) {
        super(httpHandlers);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.doIntercept(proxy, method, args);
    }
}
