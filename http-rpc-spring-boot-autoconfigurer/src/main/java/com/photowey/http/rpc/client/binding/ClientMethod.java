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
package com.photowey.http.rpc.client.binding;

import com.photowey.http.rpc.client.config.HRpcConfiguration;
import com.photowey.http.rpc.client.request.executor.RequestExecutor;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.model.RemoteInfo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ClientMethod
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class ClientMethod {

    private MethodSignature method;
    private RequestCommand command;

    HRpcConfiguration hrpcConfiguration;
    private RequestExecutor requestExecutor;

    public ClientMethod(Method target, Annotation httpAnnotation, HRpcConfiguration hrpcConfiguration) {
        this.hrpcConfiguration = hrpcConfiguration;
        this.requestExecutor = this.hrpcConfiguration.getRequestExecutor();
        RemoteInfo remoteInfo = this.hrpcConfiguration.parseHttpAnnotation(target, httpAnnotation);
        this.command = new RequestCommand(target, remoteInfo);
        this.method = new MethodSignature(target, this.command);
    }

    public Object execute() throws IOException {
        return this.requestExecutor.execute(this);
    }

    public MethodSignature getMethod() {
        return method;
    }

    public RequestCommand getCommand() {
        return command;
    }

    public HRpcConfiguration getHRpcConfiguration() {
        return hrpcConfiguration;
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
}