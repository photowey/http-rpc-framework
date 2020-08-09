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
package com.photowey.http.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * Mark the HTTP POST request
 *
 * @author WcJun
 * @date 2020/08/08
 * @see org.springframework.web.bind.annotation.PostMapping
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpPost {

    /**
     * protocol
     * https || http
     *
     * @return protocol
     */
    String protocol() default "https";

    /**
     * host = domain || ip:port
     *
     * @return
     */
    String host() default "localhost:8080";

    /**
     * URI
     *
     * @return
     */
    String uri() default "/";
}