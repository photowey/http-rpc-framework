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
package com.photowey.http.rpc.client.factory;

import com.photowey.http.rpc.client.handler.CglibInvokerInvocationHandler;
import com.photowey.http.rpc.client.request.handler.HttpHandler;
import com.photowey.http.rpc.core.exception.HRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CglibProxyFactory
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Component
public class CglibProxyFactory implements ProxyFactory {

    private static final String TARGET_PROXY = "cglib";

    @Autowired
    protected List<HttpHandler> httpHandlers;

    @Override
    public boolean supports(String targetProxy) {
        return TARGET_PROXY.equals(targetProxy);
    }

    @Override
    public <T> T buildProxy(String targetProxy, Class<T>[] interfaces) throws HRpcException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaces[0]);
        enhancer.setCallback(new CglibInvokerInvocationHandler(this.httpHandlers));
        return (T) enhancer.create();
    }
}
