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

import com.photowey.http.rpc.core.annotation.*;
import com.photowey.http.rpc.core.exception.HRpcException;
import com.photowey.http.rpc.core.model.RemoteInfo;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * RequestCommand
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public class RequestCommand {
    /**
     * the method name
     */
    private final String methodName;
    /**
     * http://localhost:8080/api/v1
     */
    private final String remoteAddress;
    /**
     * the request URI
     * /hello/world
     * full-name = remoteAddress + appPath
     * <p>
     * http://localhost:8080/api/v1/hello/world
     */
    private final String appPath;
    private String url;
    /**
     * the HTTP Request Method
     *
     * @see RequestMethod
     */
    private RequestMethod requestMethod;

    private Annotation requestAnnotation;

    public RequestCommand(Method target, RemoteInfo remoteInfo) {
        this.remoteAddress = remoteInfo.buildURI();
        Class<?> targetProxy = target.getDeclaringClass();
        this.methodName = targetProxy.getName() + "." + target.getName();
        Annotation[] declaredAnnotations = target.getDeclaredAnnotations();
        this.appPath = remoteInfo.getUri();
        boolean findSucceed = true;
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation instanceof HttpGet) {
                this.requestMethod = RequestMethod.GET;
            } else if (declaredAnnotation instanceof HttpPost) {
                this.requestMethod = RequestMethod.POST;
            } else if (declaredAnnotation instanceof HttpPut) {
                this.requestMethod = RequestMethod.PUT;
            } else if (declaredAnnotation instanceof HttpPatch) {
                this.requestMethod = RequestMethod.PATCH;
            } else if (declaredAnnotation instanceof HttpDelete) {
                this.requestMethod = RequestMethod.DELETE;
            } else {
                findSucceed = false;
                continue;
            }

            this.requestAnnotation = declaredAnnotation;

            // http://localhost:8080/api/v1
            // /hello/world[/{userId}
            // http://localhost:8080/api/v1/hello/world[/{userId}
            this.url = remoteAddress + appPath;
            break;
        }

        if (!findSucceed) {
            throw new HRpcException("the method:[{}] not modified by one of @HttpGet, @HttpPost, @HttpPut, @HttpPatch or @HttpDelete", target.getName());
        }

    }

    public String getMethodName() {
        return methodName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getAppPath() {
        return appPath;
    }

    public String getUrl() {
        return url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public Annotation getRequestAnnotation() {
        return requestAnnotation;
    }
}
