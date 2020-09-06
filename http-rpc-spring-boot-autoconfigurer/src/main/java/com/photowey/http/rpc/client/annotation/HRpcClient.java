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

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * HRpcClient
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HRpcClient {

    // from @FeignClient

    /**
     * The name of the service with optional protocol prefix. Synonym for {@link #name()
     * name}. A name must be specified for all clients, whether or not a url is provided.
     * Can be specified as property key, eg: ${propertyKey}.
     *
     * @return the name of the service with optional protocol prefix
     */
    @AliasFor("name")
    String value() default "";

    /**
     * client version
     *
     * @return the target client version
     */
    String version() default "1.0.0";

    /**
     * The service id with optional protocol prefix. Synonym for {@link #value() value}.
     *
     * @return the service id with optional protocol prefix
     * @deprecated use {@link #name() name} instead
     */
    @Deprecated
    String serviceId() default "";

    /**
     * This will be used as the bean name instead of name if present, but will not be used as a service id.
     *
     * @return bean name instead of name if present
     */
    String contextId() default "";

    /**
     * @return The service id with optional protocol prefix. Synonym for {@link #value() value}.
     */
    @AliasFor("value")
    String name() default "";

    /**
     * the expect proxy type
     * now: jdk | cglib
     *
     * @return the expect proxy type
     */
    String targetProxy() default "cglib";

    /**
     * List of classes annotated with @HRpcClient. If not empty, disables classpath scanning
     *
     * @return list of HRpcClient classes
     */
    Class<?>[] clients() default {};

    /**
     * @return whether to mark the rpc proxy as a primary bean. Defaults to true.
     */
    boolean primary() default true;
}
