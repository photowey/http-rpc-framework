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
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * JdkInvokerInvocationHandler
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class CglibInvokerInvocationHandler extends AbstractInvocationHandler implements MethodInterceptor {

    public CglibInvokerInvocationHandler(List<HttpHandler> httpHandlers) {
        super(httpHandlers);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(proxy, args);
        }
        if (TO_STRING.equals(methodName) && parameterTypes.length == 0) {
            return proxy.toString();
        }
        if (HASH_CODE.equals(methodName) && parameterTypes.length == 0) {
            return proxy.hashCode();
        }
        if (EQUALS.equals(methodName) && parameterTypes.length == 1) {
            return proxy.equals(args[0]);
        }

        return this.doIntercept(proxy, method, args);
    }
}
