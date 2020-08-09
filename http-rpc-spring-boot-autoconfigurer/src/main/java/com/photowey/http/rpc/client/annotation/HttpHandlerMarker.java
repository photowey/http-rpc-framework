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
package com.photowey.http.rpc.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Mark the modified class is HTTP Handler
 *
 * @author WcJun
 * @date 2020/08/08
 * @see com.photowey.http.rpc.client.request.handler.HttpHandler
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
@Inherited
public @interface HttpHandlerMarker {

    /**
     * the target Http Method Annotation
     *
     * @return
     * @see com.photowey.http.rpc.core.annotation.HttpDelete
     * @see com.photowey.http.rpc.core.annotation.HttpGet
     * @see com.photowey.http.rpc.core.annotation.HttpPatch
     * @see com.photowey.http.rpc.core.annotation.HttpPost
     * @see com.photowey.http.rpc.core.annotation.HttpPut
     */
    Class value();
}
