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
package com.photowey.http.rpc.core.constant;

/**
 * HRpcConstants
 *
 * @author WcJun
 * @date 2020/08/08
 * @since 1.0.0
 */
public interface HRpcConstants {

    String TEMPLATE_URI = "{}://{}/{}";
    /**
     * host template
     * ip:port
     */
    String TEMPLATE_HOST = "{}:{}";

    String COMPILE_ARG_PREFIX = "arg";

    String MISS_CONFIG_MESSAGE_TEMPLATE = "the method's @{} annotation use the dynamic hostType, need config the services info";
    String MISS_SERVICE_CONFIG_MESSAGE_TEMPLATE = "the method's @{} annotation use the dynamic hostType, need config the services.service:[{]] info";
}
